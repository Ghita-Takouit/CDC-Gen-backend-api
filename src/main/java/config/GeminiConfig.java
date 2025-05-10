package config;

import com.google.cloud.ai.generativelanguage.v1.GenerationConfig;
import com.google.cloud.ai.generativelanguage.v1.SafetySetting;
import dev.langchain4j.model.gemini.GeminiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeminiConfig {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Bean
    public GeminiChatModel geminiChatModel() {
        return GeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName("models/gemini-2.0-flash")
                .temperature(0.7f)
                .maxOutputTokens(2048)
                .build();
    }
}