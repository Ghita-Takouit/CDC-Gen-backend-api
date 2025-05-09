package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CDCRequest {
    private String title;
    private String type;
    private String version;
    private String contributors;
    
    private PageDeGarde pageDeGarde;
    private Introduction introduction;
    private ObjectifsProjet objectifsProjet;
    private DescriptionBesoin descriptionBesoin;
    private PerimetreFonctionnel perimetreFonctionnel;
    private ContraintesTechniques contraintesTechniques;
    private PlanningPrevisionnel planningPrevisionnel;
    private Budget budget;
    private CriteresValidation criteresValidation;
    private Annexes annexes;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageDeGarde {
        private String nomProjet;
        private String nomClient;
        private LocalDate date;
        private String versionDocument;
        private String redacteurs;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Introduction {
        private String contexteProjet;
        private String objectifGlobal;
        private String presentationCommanditaire;
        private String porteeProjet;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ObjectifsProjet {
        private String objectifsFonctionnels;
        private String objectifsNonFonctionnels;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DescriptionBesoin {
        private String problemesActuels;
        private String utilisateursCibles;
        private String besoinsExprimes;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PerimetreFonctionnel {
        private String authentification;
        private String tableauBord;
        private String gestionUtilisateurs;
        private String gestionDonnees;
        private String notifications;
        private String autresFonctionnalites;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContraintesTechniques {
        private String langagesFrameworks;
        private String baseDonnees;
        private String hebergement;
        private String securite;
        private String compatibilite;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlanningPrevisionnel {
        private String phasesProjet;
        private String datesCles;
        private String dureeEstimee;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Budget {
        private String estimationCouts;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CriteresValidation {
        private String elementsVerifier;
        private String scenariosTest;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Annexes {
        private String glossaire;
        private String documentsComplementaires;
        private String referencesUtiles;
    }
}