package com.edu;

import org.springframework.stereotype.Service;
import org.springframework.ai.chat.messages.*;

import java.util.*;

@Service
public class MemoryService {

    private final Map<String, List<Message>> memories = new HashMap<>();

    public void addUserMessage(String sessionId, String message) {
        memories.computeIfAbsent(sessionId, k -> new ArrayList<>())
                .add(new UserMessage(message));
    }

    public void addAssistantMessage(String sessionId, String message) {
        memories.computeIfAbsent(sessionId, k -> new ArrayList<>())
                .add(new AssistantMessage(message));
    }

    public List<Message> getHistory(String sessionId) {
        return memories.getOrDefault(sessionId, new ArrayList<>());
    }
}
