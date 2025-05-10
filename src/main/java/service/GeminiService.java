package service;

import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class GeminiService {

    private final GenerativeModel generativeModel;

    /**
     * Generates a simple text completion using Gemini AI.
     * 
     * @param prompt The text prompt to send to Gemini
     * @return Generated text response
     */
    public String generateText(String prompt) {
        try {
            // Use direct generateContent method with ContentMaker
            GenerateContentResponse response = generativeModel.generateContent(ContentMaker.fromString(prompt));
            return response.getCandidatesList().get(0).getContent().getPartsList().get(0).getText();
        } catch (IOException e) {
            System.err.println("Error generating text: " + e.getMessage());
            return "Error generating response: " + e.getMessage();
        }
    }

    /**
     * Generates text with a system instruction and a user prompt.
     * 
     * @param systemInstruction The system instruction to guide the AI's behavior
     * @param prompt The user prompt to send to Gemini
     * @return Generated text response
     */
    public String generateTextWithSystemInstruction(String systemInstruction, String prompt) {
        try {
            String fullPrompt = systemInstruction + "\n\n" + prompt;
            
            // Use direct generateContent method with ContentMaker
            GenerateContentResponse response = generativeModel.generateContent(ContentMaker.fromString(fullPrompt));
            return response.getCandidatesList().get(0).getContent().getPartsList().get(0).getText();
        } catch (IOException e) {
            System.err.println("Error generating text with system instruction: " + e.getMessage());
            return "Error generating response: " + e.getMessage();
        }
    }
}