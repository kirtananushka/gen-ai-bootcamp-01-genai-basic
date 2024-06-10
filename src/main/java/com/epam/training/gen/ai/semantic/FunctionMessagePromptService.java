package com.epam.training.gen.ai.semantic;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestFunctionMessage;
import com.azure.ai.openai.models.ChatRequestSystemMessage;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import com.epam.training.gen.ai.dto.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FunctionMessagePromptService {
    private final OpenAIAsyncClient aiClientService;
    @Value("${client-azureopenai-deployment-name}")
    private String deploymentOrModelName;

    public ChatResponse getChatCompletionsWithFunction(String userInput) {
        String functionName = "ContextMessage";
        String functionContent = "Focus on historical accuracy";
        ChatRequestFunctionMessage functionMessage = new ChatRequestFunctionMessage(functionName, functionContent);
        ChatRequestSystemMessage systemMessage = new ChatRequestSystemMessage("Include only the required numbered list of titles and authors in your response. Do not add any comments or other information.");

        ChatCompletions completions = aiClientService.getChatCompletions(
                deploymentOrModelName,
                new ChatCompletionsOptions(List.of(
                        new ChatRequestUserMessage(userInput),
                        functionMessage,
                        systemMessage
                ))
        ).block();

        return getChatResponse(userInput, completions);
    }

    public ChatResponse getChatCompletionsWithFunctionAndChangedSettings(String userInput) {
        String functionName = "ContextMessage";
        String functionContent = "Focus on historical accuracy";
        ChatRequestFunctionMessage functionMessage = new ChatRequestFunctionMessage(functionName, functionContent);
        ChatRequestSystemMessage systemMessage = new ChatRequestSystemMessage("Include only the required numbered list of titles and authors in your response. Do not add any comments or other information.");

        ChatCompletions completions = aiClientService.getChatCompletions(
                deploymentOrModelName,
                new ChatCompletionsOptions(List.of(
                        new ChatRequestUserMessage(userInput),
                        functionMessage,
                        systemMessage
                ))
                        .setMaxTokens(300)
                        .setTemperature(0.2)
                        .setTopP(0.9)
                        .setFrequencyPenalty(0.5)
                        .setPresencePenalty(0.5)
        ).block();

        return getChatResponse(userInput, completions);
    }

    private ChatResponse getChatResponse(String userInput, ChatCompletions completions) {
        String content = completions.getChoices().stream()
                .findFirst()
                .map(c -> c.getMessage().getContent()).orElse("No content available");

        List<String> messages = Arrays.asList(content.split("\\n"));

        String validationStatus = validateResponse(content, userInput);

        log.info("Received messages: {}", messages);
        log.info("Validation status: {}", validationStatus);
        return new ChatResponse(messages);
    }

    private String validateResponse(String content, String originalRequest) {
        boolean hasTenItems = (content.split("\\n").length == 10);
        boolean aiValidation = requestAIValidation(content, originalRequest);

        log.info("AI validation. Is valid: {}", aiValidation);
        log.info("Item number validation. Is valid: {}", hasTenItems);

        if (hasTenItems && aiValidation) {
            return "Valid";
        } else if (hasTenItems || aiValidation) {
            return "Partially valid";
        } else {
            return "Invalid";
        }
    }

    private boolean requestAIValidation(String content, String originalRequest) {
        String userInput = "Is this response valid? Request: ```" + originalRequest + "```. Response: ```" + content + "```";
        ChatRequestSystemMessage systemMessage = new ChatRequestSystemMessage("Include only `Yes` or `No`");

        ChatCompletions completions = aiClientService.getChatCompletions(
                        deploymentOrModelName,
                        new ChatCompletionsOptions(
                                List.of(
                                        new ChatRequestUserMessage(userInput),
                                        systemMessage
                                ))
                )
                .block();
        String answer = completions.getChoices().get(0).getMessage().getContent();
        log.info("AI validation response: {}", answer);
        return answer.contains("Yes");
    }
}
