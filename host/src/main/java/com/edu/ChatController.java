package com.edu;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatbotService chatbotService;

    public ChatController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping("/chat")
    ResponseEntity<Void> chat(@RequestBody ChatRequest chatRequest) {

        chatbotService.chat(chatRequest.sessionId, chatRequest.question());
        return ResponseEntity.ok(null);
    }

    @GetMapping("/test")
    String checkWeather() {
        chatbotService.chat("test", "What weather do we have today in Barcelona? And next days? Will it rain?");
        return "OK";
    }

    record ChatRequest(
            String sessionId,
            String question) {}

}
