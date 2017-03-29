package com.fzc.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by mark on 17-3-28.
 */
public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {

    private final static int CONTENT_LENGTH = 1 << 16;
    private final static Logger logger = LoggerFactory.getLogger(WebSocketServerInitializer.class);

    private final ChannelGroup channelGroup;

    public WebSocketServerInitializer() {
        super();
        channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    }

    public WebSocketServerInitializer(ChannelGroup channels) {
        super();
        channelGroup = channels;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
//        super.initChannel(socketChannel);
        ChannelPipeline pipeline = socketChannel.pipeline();


        logger.debug("pipeline {}\n initializer {}", socketChannel, this);


        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(CONTENT_LENGTH));
        pipeline.addLast(new WebSocketServerHandler(channelGroup));
    }
}
