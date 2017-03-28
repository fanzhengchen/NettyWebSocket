package com.fzc;

import okhttp3.*;
import okio.ByteString;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * Created by mark on 17-3-28.
 */

public class NettyWebSocketUnitTests {

    private static final Logger logger = LoggerFactory.getLogger(NettyWebSocketUnitTests.class);
    private WebSocket mWebSocket;

    @Test
    public void testWebSocketHandShake() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        webSocketHandShake();
        webSocketHandShake();
        latch.await();
    }


    public void webSocketHandShake() throws Exception {

        Request request = new Request.Builder()
                .url("ws://127.0.0.1:20000/webSocket")
                .build();

        OkHttpClient client = new OkHttpClient.Builder()
                .build();


        client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                logger.debug("websocket on open \n{}\n {}", webSocket, response);
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
