package org.example;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/messages")
    public ChatMessage addUser(
            ChatMessage message,
            SimpMessageHeaderAccessor headerAccessor) {

        // Store username in this WebSocket session
        headerAccessor.getSessionAttributes()
                .put("username", message.getSender());

        return new ChatMessage(
                message.getSender(),
                message.getSender() + " joined the chat",
                "JOIN"
        );
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {

        return message;
    }
}