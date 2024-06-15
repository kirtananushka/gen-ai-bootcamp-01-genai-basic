package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.dto.ChatBookResponse;
import com.epam.training.gen.ai.dto.ChatKernelRequest;
import com.epam.training.gen.ai.dto.ChatResponse;
import com.epam.training.gen.ai.semantic.FunctionMessagePromptService;
import com.epam.training.gen.ai.semantic.SemanticKernelService;
import com.epam.training.gen.ai.semantic.SinglePromptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SinglePromptService singlePromptService;
    private final FunctionMessagePromptService functionMessagePromptService;
    private final SemanticKernelService semanticKernelService;

    @GetMapping("/chat/simple")
    public ResponseEntity<ChatResponse> getSimplePromptResponse(@RequestParam String input) {
        ChatResponse response = singlePromptService.getSinglePromptChatCompletions(input);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/chat/function-message")
    public ResponseEntity<ChatResponse> getFunctionMessagePromptResponse(@RequestParam String input) {
        ChatResponse response = functionMessagePromptService.getChatCompletionsWithFunction(input);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/chat/function-message-changed")
    public ResponseEntity<ChatResponse> getFunctionMessageWithChangedSettingsPromptResponse(@RequestParam String input) {
        ChatResponse response = functionMessagePromptService.getChatCompletionsWithFunctionAndChangedSettings(input);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/kernel/simple")
    public ResponseEntity<ChatResponse> getSimplePromptKernelResponse(@RequestBody ChatKernelRequest chatKernelRequest) {
        ChatResponse response = semanticKernelService.getKernelResponseUsingSimplePrompt(chatKernelRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/kernel/json")
    public ResponseEntity<ChatBookResponse> getJsonPromptKernelResponse(@RequestBody ChatKernelRequest chatKernelRequest) {
        ChatBookResponse response = semanticKernelService.getKernelJsonResponse(chatKernelRequest);
        return ResponseEntity.ok(response);
    }
}
