package com.fzc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by mark on 17-3-28.
 */
@Configuration
public class ServerConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ServerConfiguration.class);


    @Value("${boss.thread.count}")
    private int bossThreadCount;

    @Value("${work.thread.count}")
    private int workThreadCount;

    @Bean(name = "bossEventGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup provideBossEventGroup() {
        logger.debug("boss event group {}", bossThreadCount);
        return new NioEventLoopGroup(bossThreadCount);
    }

    @Bean(name = "workEventGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup provideWorkEventGroup() {
        logger.debug("work event group {} ", workThreadCount);
        return new NioEventLoopGroup(workThreadCount);
    }

    @Bean(name = "channelGroup")
    public ChannelGroup provideChannelGroup() {
        return new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    }

//    @Bean(name = "loggingHandler")
//    public LoggingHandler provideLoggingHandler() {
//        return new LoggingHandler(LogLevel.DEBUG);
//    }

//    @Bean(name = "webSocketServerInitializer")
//    public WebSocketServerInitializer provideWebSocketServerInitializer() {
//        return new WebSocketServerInitializer();
//    }

    @Bean(name = "serverBootStrap")
    public ServerBootstrap provideServerBootStrap() throws Exception {
        return new ServerBootstrap();
    }

    @Bean(name = "webSocketServer")
    public WebSocketServer provideWebSocketServer() {
        return new WebSocketServer();
    }

}
