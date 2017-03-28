package com.fzc.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;


/**
 * Created by mark on 17-3-28.
 */
public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {

    private final static int CONTENT_LENGTH = 1 << 16;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(CONTENT_LENGTH));
        pipeline.addLast(new WebSocketServerHandler());
    }
}
