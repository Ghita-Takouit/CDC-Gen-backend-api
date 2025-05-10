package service;

import dto.CDCRequest;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class GeminiAIService {

    @Autowired
    private GenerativeModel generativeModel;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    /**
     * Enhances CDC content using Gemini AI
     * 
     * @param cdcRequest The original CDC request from the user
     * @return Enhanced CDC request with AI-improved content
     */
    public CDCRequest enhanceCDC(CDCRequest cdcRequest) {
        try {
            // Make a deep copy of the request to avoid modifying the original
            CDCRequest enhancedRequest = objectMapper.readValue(
                objectMapper.writeValueAsString(cdcRequest), 
                CDCRequest.class
            );

            // Enhance each section of the CDC
            enhancePageDeGarde(enhancedRequest);
            enhanceIntroduction(enhancedRequest);
            enhanceObjectifsProjet(enhancedRequest);
            enhanceDescriptionBesoin(enhancedRequest);
            enhancePerimetreFonctionnel(enhancedRequest);
            enhanceContraintesTechniques(enhancedRequest);
            enhancePlanningPrevisionnel(enhancedRequest);
            enhanceBudget(enhancedRequest);
            enhanceCriteresValidation(enhancedRequest);
            enhanceAnnexes(enhancedRequest);
            
            return enhancedRequest;
        } catch (Exception e) {
            // If enhancement fails, return the original request
            System.err.println("Error enhancing CDC with AI: " + e.getMessage());
            e.printStackTrace();
            return cdcRequest;
        }
    }
    
    /**
     * Helper method to make an AI enhancement request
     */
    private String enhanceText(String prompt, String originalText) {
        if (originalText == null || originalText.trim().isEmpty()) {
            return originalText;
        }
        
        try {
            String systemInstruction = 
                "Tu es un expert en rédaction de documents de spécification et cahiers des charges. " +
                "Ton rôle est d'améliorer et d'enrichir le texte fourni pour le rendre plus professionnel, " +
                "précis et complet, tout en maintenant le sens original. " +
                "Assure-toi que ta réponse est bien formatée, avec des paragraphes bien structurés " +
                "ou des listes à puces lorsque cela est approprié. " +
                "Ne change jamais radicalement le contenu ou l'intention du texte original.";

            String userPrompt = 
                "Voici un texte à améliorer pour un cahier des charges professionnel :\n\n" +
                prompt + ":\n" + originalText + "\n\n" +
                "Améliore ce texte pour le rendre plus professionnel tout en conservant son essence. " +
                "Réponds uniquement avec le texte amélioré, sans introduction ni conclusion.";
            
            String fullPrompt = systemInstruction + "\n\n" + userPrompt;
            
            // Use the direct generateContent method with ContentMaker
            GenerateContentResponse response = generativeModel.generateContent(ContentMaker.fromString(fullPrompt));
            
            // Extract text from the response
            String enhancedText = response.getCandidatesList().get(0).getContent().getPartsList().get(0).getText().trim();
            
            return enhancedText;
        } catch (IOException e) {
            System.err.println("Error calling Gemini API: " + e.getMessage());
            // Return original text if API call fails
            return originalText;
        }
    }
    
    // Individual section enhancement methods
    
    private void enhancePageDeGarde(CDCRequest request) {
        // For Page de Garde, we don't enhance project name, client name, or date
        // These are factual information that shouldn't be modified
    }
    
    private void enhanceIntroduction(CDCRequest request) {
        CDCRequest.Introduction intro = request.getIntroduction();
        if (intro != null) {
            intro.setContexteProjet(enhanceText("Contexte du projet", intro.getContexteProjet()));
            intro.setObjectifGlobal(enhanceText("Objectif global du projet", intro.getObjectifGlobal()));
            intro.setPresentationCommanditaire(enhanceText("Présentation du commanditaire", intro.getPresentationCommanditaire()));
            intro.setPorteeProjet(enhanceText("Portée du projet (inclusions/exclusions)", intro.getPorteeProjet()));
        }
    }
    
    private void enhanceObjectifsProjet(CDCRequest request) {
        CDCRequest.ObjectifsProjet obj = request.getObjectifsProjet();
        if (obj != null) {
            obj.setObjectifsFonctionnels(enhanceText("Objectifs fonctionnels du projet", obj.getObjectifsFonctionnels()));
            obj.setObjectifsNonFonctionnels(enhanceText("Objectifs non fonctionnels du projet", obj.getObjectifsNonFonctionnels()));
        }
    }
    
    private void enhanceDescriptionBesoin(CDCRequest request) {
        CDCRequest.DescriptionBesoin desc = request.getDescriptionBesoin();
        if (desc != null) {
            desc.setProblemesActuels(enhanceText("Problèmes actuels ou opportunités", desc.getProblemesActuels()));
            desc.setUtilisateursCibles(enhanceText("Utilisateurs cibles (profils types/personas)", desc.getUtilisateursCibles()));
            desc.setBesoinsExprimes(enhanceText("Besoins exprimés par le client", desc.getBesoinsExprimes()));
        }
    }
    
    private void enhancePerimetreFonctionnel(CDCRequest request) {
        CDCRequest.PerimetreFonctionnel perim = request.getPerimetreFonctionnel();
        if (perim != null) {
            perim.setAuthentification(enhanceText("Fonctionnalités d'authentification/inscription", perim.getAuthentification()));
            perim.setTableauBord(enhanceText("Fonctionnalités du tableau de bord", perim.getTableauBord()));
            perim.setGestionUtilisateurs(enhanceText("Fonctionnalités de gestion des utilisateurs", perim.getGestionUtilisateurs()));
            perim.setGestionDonnees(enhanceText("Fonctionnalités de gestion des données", perim.getGestionDonnees()));
            perim.setNotifications(enhanceText("Fonctionnalités de notifications", perim.getNotifications()));
            perim.setAutresFonctionnalites(enhanceText("Autres fonctionnalités", perim.getAutresFonctionnalites()));
        }
    }
    
    private void enhanceContraintesTechniques(CDCRequest request) {
        CDCRequest.ContraintesTechniques contr = request.getContraintesTechniques();
        if (contr != null) {
            contr.setLangagesFrameworks(enhanceText("Langages et frameworks techniques", contr.getLangagesFrameworks()));
            contr.setBaseDonnees(enhanceText("Contraintes de base de données", contr.getBaseDonnees()));
            contr.setHebergement(enhanceText("Exigences d'hébergement", contr.getHebergement()));
            contr.setSecurite(enhanceText("Contraintes de sécurité", contr.getSecurite()));
            contr.setCompatibilite(enhanceText("Exigences de compatibilité", contr.getCompatibilite()));
        }
    }
    
    private void enhancePlanningPrevisionnel(CDCRequest request) {
        CDCRequest.PlanningPrevisionnel plan = request.getPlanningPrevisionnel();
        if (plan != null) {
            plan.setPhasesProjet(enhanceText("Phases du projet", plan.getPhasesProjet()));
            plan.setDatesCles(enhanceText("Dates clés et livrables", plan.getDatesCles()));
            plan.setDureeEstimee(enhanceText("Durée estimée du projet", plan.getDureeEstimee()));
        }
    }
    
    private void enhanceBudget(CDCRequest request) {
        CDCRequest.Budget budget = request.getBudget();
        if (budget != null) {
            budget.setEstimationCouts(enhanceText("Estimation des coûts", budget.getEstimationCouts()));
        }
    }
    
    private void enhanceCriteresValidation(CDCRequest request) {
        CDCRequest.CriteresValidation crit = request.getCriteresValidation();
        if (crit != null) {
            crit.setElementsVerifier(enhanceText("Éléments à vérifier pour accepter le projet", crit.getElementsVerifier()));
            crit.setScenariosTest(enhanceText("Scénarios de test et critères d'acceptation", crit.getScenariosTest()));
        }
    }
    
    private void enhanceAnnexes(CDCRequest request) {
        CDCRequest.Annexes annexes = request.getAnnexes();
        if (annexes != null) {
            annexes.setGlossaire(enhanceText("Glossaire de termes techniques", annexes.getGlossaire()));
            annexes.setDocumentsComplementaires(enhanceText("Documents complémentaires", annexes.getDocumentsComplementaires()));
            if (annexes.getReferencesUtiles() != null) {
                annexes.setReferencesUtiles(enhanceText("Références utiles", annexes.getReferencesUtiles()));
            }
        }
    }
}