package com.edu.providers.ollama;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OllamaChatClientConfig {

    @Bean
    ChatClient chatClient(){
        ChatModel chatModel = OllamaChatModel.builder()
                .ollamaApi(OllamaApi.builder()
                        .baseUrl("http://localhost:11434")
                        .build())
            .defaultOptions(OllamaChatOptions.builder()
                    .model(OllamaModel.LLAMA3_1)
                    .temperature(0.4)
                    .build())
            .build();
        return ChatClient
                .builder(chatModel)
                .build();
    }

}
