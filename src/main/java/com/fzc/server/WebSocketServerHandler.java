package com.fzc.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledDirectByteBuf;
import io.netty.buffer.UnpooledUnsafeDirectByteBuf;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by mark on 17-3-28.
 */
@Component
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketServerHandler.class);
    private static final String WEB_SOCKET_LOCATION = "webSocket";

    private WebSocketServerHandshaker handShaker;

    private final ChannelGroup channelGroup;


    public WebSocketServerHandler(ChannelGroup channelGroup) {
        super();
        this.channelGroup = channelGroup;
        logger.debug("channel group \n{}\n{}", channelGroup, channelGroup.name());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            handleHttpFullRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
        logger.debug("channel read {}\n{}\n{}\n", msg, ctx, this);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        if (channelGroup != null) {
            channelGroup.add(ctx.channel());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        if (channelGroup != null) {
            channelGroup.remove(ctx.channel());
        }
    }

    private void handleHttpFullRequest(ChannelHandlerContext ctx, FullHttpRequest httpRequest) {

        if (!httpRequest.decoderResult().isSuccess()) {
            sendHttpResponse(ctx, httpRequest, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        if (httpRequest.method() != HttpMethod.GET) {
            sendHttpResponse(ctx, httpRequest, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                getWebSocketLocation(httpRequest), null, true, 5 << 20
        );

        handShaker = wsFactory.newHandshaker(httpRequest);

        if (handShaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handShaker.handshake(ctx.channel(), httpRequest);
        }


    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame webSocketFrame) {
        logger.debug("handle web socket frame");
        if (webSocketFrame instanceof PingWebSocketFrame) {
            ByteBuf byteBuf = Unpooled.copiedBuffer("pong webSocket frame yutou".getBytes());
            PongWebSocketFrame pongWebSocketFrame = new PongWebSocketFrame(byteBuf);
            ctx.write(pongWebSocketFrame);
        } else if (webSocketFrame instanceof CloseWebSocketFrame) {
            handShaker.close(ctx.channel(), (CloseWebSocketFrame) webSocketFrame.retain());
        } else if (webSocketFrame instanceof TextWebSocketFrame) {

            TextWebSocketFrame frame = (TextWebSocketFrame) webSocketFrame;
//            logger.debug("text web socket frame :" + frame.text());
//
//            ctx.channel().write(frame.retain());

            logger.debug("text web socket frame " + frame.text());
            broadcastMessage(ctx, frame);


        } else if (webSocketFrame instanceof BinaryWebSocketFrame) {

        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response) {

        if (response.status().code() != 200) {
            ByteBuf byteBuf = Unpooled.copiedBuffer(response.status().toString(), CharsetUtil.UTF_8);
            response.content().writeBytes(byteBuf);
            byteBuf.release();
            HttpUtil.setContentLength(response, response.content().readableBytes());
        }

        ChannelFuture future = ctx.channel().writeAndFlush(response);

        if (!HttpUtil.isKeepAlive(response) || response.status().code() != 200) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static String getWebSocketLocation(HttpRequest request) {
        String location = request.headers().get(HttpHeaderNames.HOST) + "/" + WEB_SOCKET_LOCATION;
        return "ws://" + location;
    }

    private void broadcastMessage(ChannelHandlerContext ctx, WebSocketFrame frame) {
        final Channel channel = ctx.channel();

        logger.debug("broadcast message {}\n{}\n{}\n", channelGroup, channel.id(), channelGroup.size());
        for (Channel ch : channelGroup) {

            logger.debug("channel id {}", ch);
//            if (ch.equals(channel)) {
            logger.debug("channel write {}", ch);
            ch.writeAndFlush(frame.retain());

//            }
        }

    }
}
