package com.amarogamedev.plantai.controller;

import com.amarogamedev.plantai.dto.ChatPromptDTO;
import com.amarogamedev.plantai.service.ChatService;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat(@RequestBody @Valid ChatPromptDTO chatPromptDTO) {
        return chatService.chat(chatPromptDTO.prompt());
    }
}