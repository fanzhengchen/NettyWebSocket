package com.fzc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * Created by mark on 17-3-28.
 */
@Component
public class WebSocketServer implements IServer {

    @Value("${webSocket.port}")
    private int serverPort;

    @Autowired
    @Qualifier("serverBootStrap")
    private ServerBootstrap bootstrap;

    @Autowired
    @Qualifier("bossEventGroup")
    private NioEventLoopGroup bossGroup;

    @Autowired
    @Qualifier("workEventGroup")
    private NioEventLoopGroup workGroup;

    @Autowired
    @Qualifier("channelGroup")
    private ChannelGroup channelGroup;

    private Channel channel;


    public WebSocketServer() {
    }

    public void start() throws Exception {
        start(serverPort);
    }

    @Override
    public void start(int serverPort) throws Exception {

        bootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new WebSocketServerInitializer(channelGroup));

        channel = bootstrap.bind(serverPort)
                .sync()
                .channel()
                .closeFuture()
                .sync()
                .channel();
    }

    @PreDestroy
    private void stop() {
        if (channel != null) {
            channel.close();
            channel.parent().close();
        }
    }
}
