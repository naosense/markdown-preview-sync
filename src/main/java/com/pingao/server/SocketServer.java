package com.pingao.server;

import com.pingao.utils.Base64Utils;
import com.pingao.utils.FileUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;


/**
 * Created by pingao on 2018/8/5.
 */
public class SocketServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketServer.class);
    private static final String SEP = "\r\n\r\n";
    private static final ByteBuf EOF = Unpooled.copiedBuffer(FileUtils.getBytes("$_"));

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
            LOGGER.info("Server received: " + string);
            String[] data = string.split(SEP);
            if ("start".equals(data[0])) {
                server = MarkDownServer.getInstance();
                server.setTheme(data[2]);
                server.start(Integer.parseInt(data[1]));
            } else if ("sync".equals(data[0])) {
                server.broadcast("sync", data[1], Base64Utils.decode2String(data[2]), Integer.parseInt(data[3]));
            } else if ("close".equals(data[0])) {
                server.broadcast("close", data[1], "", 1);
            } else if ("stop".equals(data[0])) {
                server.destroy();
                System.exit(0);
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx,
                                    Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
