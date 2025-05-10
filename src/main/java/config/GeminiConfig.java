package config;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeminiConfig {

    @Value("${gemini.api.key}")
    private String apiKey;
    
    // Default project and location for Gemini API
    private static final String PROJECT_ID = "generative-ai-project";  // This is a placeholder
    private static final String LOCATION = "us-central1";              // Standard location

    @Bean
    public GenerativeModel geminiModel() {
        try {
            // Set API key as environment variable that Google libraries typically look for
            System.setProperty("GOOGLE_API_KEY", apiKey);
            
            // Initialize the VertexAI client with project ID and location
            // The API key will be picked up from the system property
            VertexAI vertexAI = new VertexAI(PROJECT_ID, LOCATION);
            
            // Create a generative model for Gemini
            GenerativeModel model = new GenerativeModel("gemini-2.0-flash", vertexAI);
            
            return model;
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Gemini model: " + e.getMessage(), e);
        }
    }
}