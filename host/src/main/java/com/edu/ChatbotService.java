package com.edu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
public class ChatbotService {

    private static final Logger logger = LoggerFactory.getLogger(ChatbotService.class);
    private final ChatClient chatClient;
    private final MemoryService memoryService;
    private final ToolCallbackProvider mcpToolProvider;


    public ChatbotService(@Qualifier("openAi") ChatClient chatClient,
                          MemoryService memoryService,
                          ToolCallbackProvider mcpToolProvider) {
        this.chatClient = chatClient;
        this.memoryService = memoryService;
        this.mcpToolProvider = mcpToolProvider;
    }

    void chat(String sessionId, String userInput) {
        logger.info("Request: " + userInput);

        try {
            // Guarda el missatge de l’usuari a la memòria
            memoryService.addUserMessage(sessionId, userInput);

            // Obtenim tota la conversa prèvia
            List<Message> history = memoryService.getHistory(sessionId);
            // Preparem el Prompt
            ChatClient.ChatClientRequestSpec prompt = chatClient.prompt();
            history.forEach(message -> prompt.messages(message));
            prompt
                    .user(userInput)
                    .toolContext(Map.of("progressToken", "token-" + new Random().nextInt())) // (5)
                    .toolCallbacks(mcpToolProvider);
            String response = prompt.call().content();
            System.out.println(response);
            /*
            System.out.println(chatClient
                    .prompt()
                    .user(userInput)
                    .toolContext(Map.of("progressToken", "token-" + new Random().nextInt())) // (5)
                    .toolCallbacks(mcpToolProvider)

                    .call()
                    .content());
            */
        } catch (Exception ex) {
            Throwable root = ex;
            while (root.getCause() != null) {
                root = root.getCause();
            }
            System.err.println("Error calling model: " + root.getClass().getName() + " - " + root.getMessage());
            throw ex; // rethrow so you still see the 500 until we add better handling
        }
    }

}
