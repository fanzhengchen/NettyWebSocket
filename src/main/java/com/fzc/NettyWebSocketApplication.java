package com.fzc;

import com.fzc.server.WebSocketServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class NettyWebSocketApplication {

    @Value("${webSocket.port}")
    private static int serverPort;

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(NettyWebSocketApplication.class, args);

        try {
            WebSocketServer webSocketServer = ctx.getBean("webSocketServer", WebSocketServer.class);
            webSocketServer.start(serverPort);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
