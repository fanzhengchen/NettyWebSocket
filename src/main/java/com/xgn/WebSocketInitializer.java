package com.xgn;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;

/**
 * Created by mark on 7/6/17.
 */
public class WebSocketInitializer extends ChannelInitializer<SocketChannel> {

    private static final String WEBSOCKET_PATH = "/websocket";

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();


        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(1 << 16));
        pipeline.addLast(new WebSocketServerCompressionHandler());
        pipeline.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH));
        pipeline.addLast(new WebSocketShakeHandler());
        pipeline.addLast(new WebSocketFrameHandler());
    }
}
