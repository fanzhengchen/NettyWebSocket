package com.fzc;

import okhttp3.*;
import okhttp3.internal.ws.RealWebSocket;
import okio.ByteString;
import okio.Okio;
import okio.Pipe;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by mark on 17-3-28.
 */

public class NettyWebSocketUnitTests {

    private static final Logger logger = LoggerFactory.getLogger(NettyWebSocketUnitTests.class);
    private RealWebSocket mWebSocket;

    @Test
    public void testWebSocketHandShake() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
//        webSocketHandShake();
//        webSocketHandShake();
//        latch.await();
    }


    public void webSocketHandShake() throws Exception {

        Request request = new Request.Builder()
                .url("ws://172.16.14.115:20000/webSocket")
                .build();

        OkHttpClient client = new OkHttpClient.Builder()
                .pingInterval(5, TimeUnit.SECONDS)
                .build();


        client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                mWebSocket = (RealWebSocket)webSocket;
                System.out.println("on open ");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                System.out.println("on message " + text);
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
                System.out.println("on message " + bytes);
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                System.out.println("on closing");
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                System.out.println("on close");
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                System.out.println("on failure");
            }
        });

//        mWebSocket.onReadMessage();


    }


    static class WebSocketClient extends RealWebSocket.Streams {

        private String name;

        public WebSocketClient(boolean client, Pipe source, Pipe sink) throws IOException {
            super(client, Okio.buffer(source.source()), Okio.buffer(sink.sink()));
        }

        @Override
        public void close() throws IOException {

        }
    }
}
