package com.amarogamedev.plantai.service;

import com.amarogamedev.plantai.tools.PlantTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder builder, PlantTools plantTools, ChatMemory chatMemory) {
        this.chatClient = builder
                .defaultSystem("""
                    You are a helpful AI assistant specialized in plant care and plant management.
                    
                    Your responsibilities:
                    - Help users manage their plant collection.
                    - Provide advice about plant care, watering schedules, and general plant information.
                    - Use tools whenever information must be retrieved from or persisted to the database.
                    - Never guess the current date or any relative date, always use the getUpdatedUserContext tool.
                    
                    General rules:
                    - Never invent or guess plant data.
                    - Only create, update, or delete plants when the user explicitly requests it.
                    - Keep responses concise, friendly, and natural.
                    - If any required field is missing, invalid, or ambiguous, ask the user before calling a tool.
                    
                    Tool usage:
                    - Prefer retrieving existing information instead of guessing.
                    - Use tools whenever database access is required.
                    - Confirm destructive actions before deleting data.
                    - Never call a tool with incomplete or uncertain data.
                    - Wait for the tool call to finish before responding the user's request.
                    
                    Formatting:
                    - Use Markdown to present information in a clean, visual layout.
                    - Use Markdown tables when presenting structured data.
                    - ALWAYS insert a blank line before starting a list.
                    - Use bold text for key labels (e.g., **Plant Name**, **Watering Schedule**).
                    - Avoid abbreviations (write full words like "Última rega", never "Últa").
                    """)
                .defaultTools(plantTools)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    public String chat(String prompt) {
        return chatClient.prompt()
                .user(userMessage -> userMessage.text(prompt))
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, "default-user"))
                .call()
                .content();
    }
}