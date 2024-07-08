package com.example.websocket;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionStage;

public class WebSocketHandler extends TextWebSocketHandler {

    private final Map<String, String> messageMap = new HashMap<>();
    private final String fastApiUrl = "ws://localhost:8000/ws";
    private WebSocket fastApiWebSocket;

    public WebSocketHandler() {
        loadMessages();
        connectToFastApi();
    }

    private void loadMessages() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource("messages.txt").getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    messageMap.put(parts[1].trim().toLowerCase(), parts[0].trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void connectToFastApi() {
        HttpClient client = HttpClient.newHttpClient();
        client.newWebSocketBuilder()
                .buildAsync(URI.create(fastApiUrl), new FastApiWebSocketListener())
                .thenAccept(webSocket -> fastApiWebSocket = webSocket);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload().toLowerCase();
        String id = messageMap.getOrDefault(payload, "0");
        
        if (fastApiWebSocket != null) {
            fastApiWebSocket.sendText(id, true);
            FastApiWebSocketListener.setSession(session);
        } else {
            session.sendMessage(new TextMessage("Error: FastAPI connection not established"));
        }
    }

    private static class FastApiWebSocketListener implements WebSocket.Listener {
        private static WebSocketSession session;

        public static void setSession(WebSocketSession webSocketSession) {
            session = webSocketSession;
        }

        @Override
        public void onOpen(WebSocket webSocket) {
            webSocket.request(1);
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            if (session != null && session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(data.toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            webSocket.request(1);
            return null;
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            error.printStackTrace();
        }
    }
}