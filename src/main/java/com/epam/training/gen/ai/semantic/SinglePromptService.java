package com.epam.training.gen.ai.semantic;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import com.epam.training.gen.ai.dto.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SinglePromptService {
    private final OpenAIAsyncClient aiClientService;
    @Value("${client-azureopenai-deployment-name}")
    private String deploymentOrModelName;

    public ChatResponse getSinglePromptChatCompletions(String input) {
        ChatCompletions completions = aiClientService.getChatCompletions(
                        deploymentOrModelName,
                        new ChatCompletionsOptions(
                                List.of(new ChatRequestUserMessage(input)))
                )
                .block();

        List<String> messages = completions.getChoices().stream()
                .map(c -> c.getMessage().getContent())
                .collect(Collectors.toList());
        log.info("Received messages: {}", messages);
        return new ChatResponse(messages);
    }
}