package Controller;

import Models.CDC;
import dto.CDCRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.CDCService;
import service.GeminiAIService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cdc")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CDCController {
    
    private final CDCService cdcService;
    private final GeminiAIService geminiAIService;
    
    @PostMapping
    public ResponseEntity<?> createCDC(@RequestBody CDCRequest cdcRequest) {
        try {
            CDC createdCDC = cdcService.createCDC(cdcRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCDC);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Erreur lors de la création du CDC: " + e.getMessage());
        }
    }
    
    /**
     * Endpoint for enhancing CDC with AI and saving to database
     */
    @PostMapping("/enhance")
    public ResponseEntity<?> enhanceAndCreateCDC(@RequestBody CDCRequest cdcRequest) {
        try {
            // Step 1: Use AI to enhance the CDC content
            CDCRequest enhancedRequest = geminiAIService.enhanceCDC(cdcRequest);
            
            // Step 2: Save the enhanced CDC to the database
            CDC createdCDC = cdcService.createCDC(enhancedRequest);
            
            // Return both the enhanced request and the created entity
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCDC);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Erreur lors de l'amélioration et la création du CDC: " + e.getMessage());
        }
    }
    
    @GetMapping
    public ResponseEntity<List<CDC>> getAllCDCs() {
        return ResponseEntity.ok(cdcService.getAllCDCs());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getCDCById(@PathVariable UUID id) {
        try {
            CDC cdc = cdcService.getCDCById(id);
            return ResponseEntity.ok(cdc);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("CDC non trouvé: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCDC(@PathVariable UUID id, @RequestBody CDCRequest cdcRequest) {
        try {
            CDC updatedCDC = cdcService.updateCDC(id, cdcRequest);
            return ResponseEntity.ok(updatedCDC);
        } catch (Exception e) {
            HttpStatus status = e.getMessage().contains("not found") ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            return ResponseEntity
                    .status(status)
                    .body("Erreur lors de la mise à jour du CDC: " + e.getMessage());
        }
    }
    
    /**
     * Endpoint for enhancing an existing CDC with AI
     */
    @PutMapping("/{id}/enhance")
    public ResponseEntity<?> enhanceExistingCDC(@PathVariable UUID id, @RequestBody CDCRequest cdcRequest) {
        try {
            // Step 1: Use AI to enhance the CDC content
            CDCRequest enhancedRequest = geminiAIService.enhanceCDC(cdcRequest);
            
            // Step 2: Update the existing CDC with enhanced content
            CDC updatedCDC = cdcService.updateCDC(id, enhancedRequest);
            
            return ResponseEntity.ok(updatedCDC);
        } catch (Exception e) {
            HttpStatus status = e.getMessage().contains("not found") ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            return ResponseEntity
                    .status(status)
                    .body("Erreur lors de l'amélioration du CDC: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCDC(@PathVariable UUID id) {
        try {
            cdcService.deleteCDC(id);
            return ResponseEntity.ok("CDC supprimé avec succès");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Erreur lors de la suppression du CDC: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<CDC>> searchCDCsByNomProjet(@RequestParam String nomProjet) {
        return ResponseEntity.ok(cdcService.searchCDCsByNomProjet(nomProjet));
    }
    
//    @GetMapping("/search")
//    public ResponseEntity<List<CDC>> searchCDCsByTitle(@RequestParam String title) {
//        return ResponseEntity.ok(cdcService.searchCDCsByTitle(title));
//    }
//
//    @GetMapping("/type/{type}")
//    public ResponseEntity<List<CDC>> getCDCsByType(@PathVariable String type) {
//        return ResponseEntity.ok(cdcService.getCDCsByType(type));
//    }
}
