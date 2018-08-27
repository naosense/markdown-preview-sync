package com.pingao.server;

import com.pingao.utils.FileUtils;
import com.pingao.utils.JSoupUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.util.CharsetUtil;
import org.pmw.tinylog.Logger;

import java.net.InetSocketAddress;


/**
 * Created by pingao on 2018/8/5.
 */
public class SocketServer {
    private static final String SEP = "__%#mpsync&@__";
    private static final ByteBuf EOF = Unpooled.copiedBuffer(FileUtils.getBytes("\0"));

    private MarkDownServer server;

    public void start() throws Exception {
        final SocketServerHandler serverHandler = new SocketServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(23789))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, EOF));
                        ch.pipeline().addLast(serverHandler);
                    }
                });

            ChannelFuture f = b.bind().sync();
            Logger.info("Socket server is running on {} ...", f.channel().localAddress());
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    @ChannelHandler.Sharable
    private class SocketServerHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            ByteBuf in = (ByteBuf) msg;
            String string = in.toString(CharsetUtil.UTF_8);
            in.release();
            Logger.info("Server received: " + string);
            String[] data = string.split(SEP);
            switch (data[0]) {
                case "start":
                    server = MarkDownServer.getInstance();
                    server.setTheme(data[2]);
                    server.start(Integer.parseInt(data[1]));
                    break;
                case "open":
                    JSoupUtils.removeCache(data[1]);
                    break;
                case "sync":
                    server.broadcast("sync", data[1], data[2], Integer.parseInt(data[3]));
                    break;
                case "close":
                    server.broadcast("close", data[1], "", 1);
                    break;
                case "stop":
                    server.destroy();
                    System.exit(0);
                    break;
                default:
                    Logger.info("Command {} is unknown", data[0]);
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            Logger.error("Error occurs cause", cause);
            ctx.close();
        }
    }
}
