package com.pingao.server;

import com.pingao.Main;
import com.pingao.enums.MiMeType;
import com.pingao.utils.FileUtils;
import com.pingao.utils.HtmlUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;


public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestHandler.class);

    private MarkDownServer server;

    public HttpRequestHandler(MarkDownServer server) {
        this.server = server;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {
        String uri = request.uri();
        LOGGER.info("Request uri {}", uri);
        if (uri.startsWith("/index")) {
            index(ctx, request);
        } else if (uri.startsWith("/ws")) {
            ctx.fireChannelRead(request.retain());
        } else if (uri.startsWith("/js") || uri.startsWith("/css")) {
            transferStaticFile(ctx, request);
        } else if (uri.startsWith("/image")) {
            image(ctx, request);
        } else {
            commonResponse(ctx, request, FileUtils.getBytes("How do you do"), MiMeType.PLAIN);
        }
    }

    private void index(ChannelHandlerContext ctx, FullHttpRequest request) {
        String html = FileUtils.readAsString(Main.ROOT_PATH + "/webapp/index.html");
        int index = html.indexOf("${theme}");
        String htmlWithTheme = html.substring(0, index) + server.getTheme() +
                               html.substring(index + "${theme}".length());
        commonResponse(ctx, request, FileUtils.getBytes(htmlWithTheme), MiMeType.HTML);
    }

    private void image(ChannelHandlerContext ctx, FullHttpRequest request) {
        Map<String, String> params = HtmlUtils.getParameters(request);
        String path = params.get("path");
        commonResponse(ctx, request, FileUtils.readAllBytes(path), HtmlUtils.getMiMeTypeByPath(path));
    }

    private void transferStaticFile(ChannelHandlerContext ctx, FullHttpRequest request) {
        String path = Main.ROOT_PATH + "/webapp" + HtmlUtils.getRequestPath(request.uri());
        commonResponse(ctx, request, FileUtils.readAllBytes(path), HtmlUtils.getMiMeTypeByPath(path));
    }

    private void commonResponse(ChannelHandlerContext ctx, FullHttpRequest request, byte[] bytes, MiMeType type) {
        if (HttpUtil.is100ContinueExpected(request)) {
            send100Continue(ctx);
        }

        FullHttpResponse response =
            new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(bytes));

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, type.getType());
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        boolean keepAlive = HttpUtil.isKeepAlive(request);
        if (keepAlive) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        ctx.write(response);
        ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(
            HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
