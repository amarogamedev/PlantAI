package com.amarogamedev.plantai.controller;

import com.amarogamedev.plantai.service.LLMService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/llm")
public class LLMController {

    private final LLMService llmService;

    public LLMController(LLMService llmService) {
        this.llmService = llmService;
    }

    @GetMapping
    public String ask(@RequestBody String prompt) {
        return llmService.ask(prompt);
    }
}