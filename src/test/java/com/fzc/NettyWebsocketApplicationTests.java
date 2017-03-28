package com.fzc;

import com.fzc.server.WebSocketServer;
import okhttp3.*;
import okio.ByteString;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NettyWebsocketApplicationTests {

    private static final int SERVER_PORT = 11111;
    @Autowired
    @Qualifier("webSocketServer")
    private WebSocketServer webSocketServer;

    WebSocket mWebSocket;

    @Before
    public void prepare() throws Exception {
        System.out.println("before");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    webSocketServer.start(SERVER_PORT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        System.out.println("after");
    }

    @Test
    public void testOkWebSocket() {
        String url = String.format("ws://172.16.14.115:%d/webSocket", SERVER_PORT);
        Request request = new Request.Builder()
                .url(url)
                .build();

        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        System.out.println("test ok web socket");
        client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
            }
        });
    }


}
