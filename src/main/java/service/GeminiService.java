package service;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.gemini.GeminiChatModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GeminiService {

    private final GeminiChatModel geminiChatModel;

    /**
     * Generates a simple text completion using Gemini AI.
     * 
     * @param prompt The text prompt to send to Gemini
     * @return Generated text response
     */
    public String generateText(String prompt) {
        UserMessage userMessage = UserMessage.from(prompt);
        AiMessage response = geminiChatModel.generate(userMessage).content();
        return response.text();
    }

    /**
     * Generates text with a system instruction and a user prompt.
     * 
     * @param systemInstruction The system instruction to guide the AI's behavior
     * @param prompt The user prompt to send to Gemini
     * @return Generated text response
     */
    public String generateTextWithSystemInstruction(String systemInstruction, String prompt) {
        SystemMessage systemMessage = SystemMessage.from(systemInstruction);
        UserMessage userMessage = UserMessage.from(prompt);
        
        AiMessage response = geminiChatModel.generate(List.of(systemMessage, userMessage)).content();
        return response.text();
    }
}