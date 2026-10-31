package com.example.springbootapp.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiChatController {

    private final ChatClient chatClient;

    public AiChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @PostMapping("/chat")
    public Map<String, String> chat(@RequestBody Map<String, String> request) {
        String question = request.getOrDefault("question", "").trim();
        if (question.isBlank()) {
            return Map.of("answer", "Please enter a question first.");
        }

        String answer = chatClient.prompt()
                .system("You are a stock portfolio assistant. Use the available database-backed MCP tools to answer questions about stocks from the MySQL database. " +
                        "When listing stocks, include actual values from the data. " +
                        "When asked for top prices, use the stock price ordering tool. " +
                        "Do not invent data.")
                .user(question)
                .call()
                .content();

        return Map.of("answer", answer);
    }
}

