package com.edu.providers.wingman;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiConfig {

    private final AiChatClientConfigurationProperties aiChatClientConfigurationProperties;


    public OpenAiConfig(AiChatClientConfigurationProperties aiChatClientConfigurationProperties) {
        this.aiChatClientConfigurationProperties = aiChatClientConfigurationProperties;
    }

    @Bean(name = "openAi")
    public ChatClient getChatClient() {
        var openAiApi = OpenAiApi.builder()
                .apiKey(aiChatClientConfigurationProperties.getApiKey())
                .baseUrl(aiChatClientConfigurationProperties.getEndpoint())
                .completionsPath(aiChatClientConfigurationProperties.getCompletionsPath())
                .build();
        var openAIChatOptions = OpenAiChatOptions.builder()
                .model(aiChatClientConfigurationProperties.getModel())
                .temperature(aiChatClientConfigurationProperties.getTemperature())
                .maxTokens(aiChatClientConfigurationProperties.getMaxTokens())
                .frequencyPenalty(aiChatClientConfigurationProperties.getFrequencyPenalty())
                .presencePenalty(aiChatClientConfigurationProperties.getPresencePenalty())
                .topP(aiChatClientConfigurationProperties.getTopP())
                .build();
        var chatModel = OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(openAIChatOptions)
                .build();
        return ChatClient
                .builder(chatModel)
                .defaultOptions(openAIChatOptions)
                .build();
    }

}
