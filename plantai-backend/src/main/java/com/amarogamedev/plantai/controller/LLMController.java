package com.amarogamedev.plantai.controller;

import com.amarogamedev.plantai.service.LLMService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/llm")
public class LLMController {

    private final LLMService llmService;

    public LLMController(LLMService llmService) {
        this.llmService = llmService;
    }

    @GetMapping
    public String ask(@RequestParam String prompt) {
        return llmService.ask(prompt);
    }
}