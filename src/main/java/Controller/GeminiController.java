package Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.GeminiService;

import java.util.Map;

@RestController
@RequestMapping("/api/gemini")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class GeminiController {

    private final GeminiService geminiService;
    
    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generateText(@RequestBody Map<String, String> request) {
        String prompt = request.get("prompt");
        String response = geminiService.generateText(prompt);
        return ResponseEntity.ok(Map.of("response", response));
    }
    
    @PostMapping("/generate-with-system")
    public ResponseEntity<Map<String, String>> generateWithSystem(@RequestBody Map<String, String> request) {
        String systemInstruction = request.get("systemInstruction");
        String prompt = request.get("prompt");
        String response = geminiService.generateTextWithSystemInstruction(systemInstruction, prompt);
        return ResponseEntity.ok(Map.of("response", response));
    }
}