package com.amarogamedev.plantai.service;

import com.amarogamedev.plantai.tools.PlantTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
public class LLMService {

    private final ChatClient chatClient;

    public LLMService(ChatClient.Builder builder, PlantTools plantTools, ChatMemory chatMemory) {
        this.chatClient = builder
                .defaultSystem("""
                            You are a helpful AI assistant specialized in plant care and plant management.
                        
                            Your responsibilities:
                            - Help users manage their plant collection.
                            - Provide advice about watering, care routines, and plant information.
                            - Use tools whenever database information is required.
                        
                            Rules:
                            - Do not invent plant data. Use tools when information is missing.
                            - Ask for missing required fields before creating or updating plants.
                            - Only create, update, or delete plants when explicitly requested.
                            - Keep responses concise and natural.
                        
                            When interacting with tools:
                            - Prefer retrieving existing data instead of guessing.
                            - Confirm destructive actions before deleting data.
                        """)
                .defaultTools(plantTools)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    public String ask(String prompt) {
        return chatClient.prompt()
                .user(prompt)
                //in this prototype default-user is fixed, in a real application it should be the authenticated user id
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, "default-user"))
                .call()
                .content();
    }
}