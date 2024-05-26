package com.example.aerobankapp;

import com.example.aerobankapp.dto.ScheduledPaymentDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

public class MyWebSocketHandler extends TextWebSocketHandler
{
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final ObjectMapper objectMapper;
    private Logger LOGGER = LoggerFactory.getLogger(MyWebSocketHandler.class);

    public MyWebSocketHandler(){
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("WebSocket connection established: " + session.getId());
        sessions.add(session);
        LOGGER.info("Current sessions: " + sessions.size());


        // Send keep-alive messages every 30 seconds
        Timer timer = new Timer(true);

        // Send keep-alive messages every 30 seconds
        // Send keep-alive messages every 30 seconds
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (session.isOpen()) {
                        String keepAliveMessage = "{\"type\":\"keep-alive\"}";
                        session.sendMessage(new TextMessage(keepAliveMessage));
                    } else {
                        timer.cancel();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    timer.cancel();
                }
            }
        }, 30000, 30000);
        // Store the timer in session attributes so it can be cancelled when the connection is closed
        session.getAttributes().put("keepAliveTimer", timer);
    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("Received: " + payload);
        String responseMessage = "{\"type\":\"message\",\"content\":\"Hello from server\"}";
        session.sendMessage(new TextMessage(responseMessage));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("WebSocket connection closed: " + session.getId());
        Timer timer = (Timer) session.getAttributes().get("keepAliveTimer");
        if (timer != null) {
            timer.cancel();
        }
        sessions.remove(session);
        LOGGER.info("Current sessions after removal: " + sessions.size());
    }

    public void broadcastUpdatedData(List<ScheduledPaymentDTO> data) {
        try {
            String jsonData = objectMapper.writeValueAsString(data);
            LOGGER.info("Socket Json Data: " + jsonData);
            if(!sessions.isEmpty()){
                for (WebSocketSession session : sessions) {
                    LOGGER.info("In Session: " + session.getId());
                    LOGGER.info("Session Attr: " + session.getAttributes());
                    if (session.isOpen()) {
                        LOGGER.info("Session is Open: " + session.isOpen());
                        session.sendMessage(new TextMessage("{\"type\":\"update\", \"data\":" + jsonData + "}"));
                    }else {
                        LOGGER.info("Session is Closed: " + session.getId());
                    }
                }
            }else{
                LOGGER.info("Sessions array is empty.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
