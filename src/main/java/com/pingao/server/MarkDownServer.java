package com.pingao.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pingao.model.MarkDownUnit;
import com.pingao.model.WebSocketMsg;
import com.pingao.utils.HtmlUtils;
import com.pingao.utils.JsoupUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.ImmediateEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;


public class MarkDownServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(MarkDownServer.class);

    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    private static MarkDownServer INSTANCE;

    private final ChannelGroup channelGroup =
        new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
    private final EventLoopGroup group = new NioEventLoopGroup();
    private Channel channel;

    private boolean isRunning;

    private String theme;

    public static MarkDownServer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MarkDownServer();
        }
        return INSTANCE;
    }

    private MarkDownServer() {
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public ChannelFuture start(int port) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(group)
                 .channel(NioServerSocketChannel.class)
                 .childHandler(new MarkDownServerInitializer(this));
        ChannelFuture future = bootstrap.bind(new InetSocketAddress(port));
        future.syncUninterruptibly();
        channel = future.channel();
        isRunning = true;
        LOGGER.info("Markdown server is running on {} ...", channel.localAddress());
        return future;
    }

    public void destroy() {
        channel.close();
        channelGroup.close();
        group.shutdownGracefully();
        LOGGER.info("Markdown server shutdown success");
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void addChannel(Channel channel) {
        channelGroup.add(channel);
    }

    public void broadcast(String command, String path, String content, int bottom) {
        String html = HtmlUtils.markdown2Html(HtmlUtils.plugBottomMarker(content, bottom));
        List<MarkDownUnit> units = JsoupUtils.diff(path, html);
        WebSocketMsg msg = new WebSocketMsg(command, path, units);
        String json = GSON.toJson(msg);
        channelGroup.writeAndFlush(new TextWebSocketFrame(json));
        LOGGER.info("Broadcast bottom {} msg {} success", bottom, msg);
    }

    public static void main(String[] args) {
        final MarkDownServer endpoint = new MarkDownServer();
        endpoint.setTheme("github");
        endpoint.start(7788);
        Runtime.getRuntime().addShutdownHook(new Thread(endpoint::destroy));
    }
}
