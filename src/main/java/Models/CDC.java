package Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cdc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CDC {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String title;

    private String type;
    
    @UpdateTimestamp
    @Column(name = "last_modified")
    private LocalDateTime lastModified;
    
    private String version;
    
    @Column(name = "contributor")
    private String contributors;
    
    @Embedded
    private PageDeGarde pageDeGarde;
    
    @Embedded
    private Introduction introduction;
    
    @Embedded
    private ObjectifsProjet objectifsProjet;
    
    @Embedded
    private DescriptionBesoin descriptionBesoin;
    
    @Embedded
    private PerimetreFonctionnel perimetreFonctionnel;
    
    @Embedded
    private ContraintesTechniques contraintesTechniques;
    
    @Embedded
    private PlanningPrevisionnel planningPrevisionnel;
    
    @Embedded
    private Budget budget;
    
    @Embedded
    private CriteresValidation criteresValidation;
    
    @Embedded
    private Annexes annexes;
    
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageDeGarde {
        @Column(name = "nom_projet", nullable = false)
        private String nomProjet;
        
        @Column(name = "nom_client", nullable = false)
        private String nomClient;
        
        @Column(nullable = false)
        private LocalDate date;
        
        @Column(name = "version_document")
        private String versionDocument = "1.0";
        
        @Column(name = "redacteur")
        private String redacteurs;
    }
    
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Introduction {
        @Column(name = "contexte_projet", nullable = false, columnDefinition = "TEXT")
        private String contexteProjet;
        
        @Column(name = "objectif_global", nullable = false, columnDefinition = "TEXT")
        private String objectifGlobal;
        
        @Column(name = "presentation_commanditaire", nullable = false, columnDefinition = "TEXT")
        private String presentationCommanditaire;
        
        @Column(name = "portee_projet", nullable = false, columnDefinition = "TEXT")
        private String porteeProjet;
    }
    
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ObjectifsProjet {
        @Column(name = "objectifs_fonctionnels", nullable = false, columnDefinition = "TEXT")
        private String objectifsFonctionnels;
        
        @Column(name = "objectifs_non_fonctionnels", nullable = false, columnDefinition = "TEXT")
        private String objectifsNonFonctionnels;
    }
    
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DescriptionBesoin {
        @Column(name = "problemes_actuels", nullable = false, columnDefinition = "TEXT")
        private String problemesActuels;
        
        @Column(name = "utilisateurs_cibles", nullable = false, columnDefinition = "TEXT")
        private String utilisateursCibles;
        
        @Column(name = "besoins_exprimes", nullable = false, columnDefinition = "TEXT")
        private String besoinsExprimes;
    }
    
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PerimetreFonctionnel {
        @Column(columnDefinition = "TEXT")
        private String authentification;
        
        @Column(name = "tableau_bord", columnDefinition = "TEXT")
        private String tableauBord;
        
        @Column(name = "gestion_utilisateurs", columnDefinition = "TEXT")
        private String gestionUtilisateurs;
        
        @Column(name = "gestion_donnees", columnDefinition = "TEXT")
        private String gestionDonnees;
        
        @Column(columnDefinition = "TEXT")
        private String notifications;
        
        @Column(name = "autres_fonctionnalites", columnDefinition = "TEXT")
        private String autresFonctionnalites;
    }
    
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContraintesTechniques {
        @Column(name = "langages_frameworks", columnDefinition = "TEXT")
        private String langagesFrameworks;
        
        @Column(name = "base_donnees", columnDefinition = "TEXT")
        private String baseDonnees;
        
        @Column(columnDefinition = "TEXT")
        private String hebergement;
        
        @Column(columnDefinition = "TEXT")
        private String securite;
        
        @Column(columnDefinition = "TEXT")
        private String compatibilite;
    }
    
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlanningPrevisionnel {
        @Column(name = "phases_projet", columnDefinition = "TEXT")
        private String phasesProjet;
        
        @Column(name = "dates_cles", columnDefinition = "TEXT")
        private String datesCles;
        
        @Column(name = "duree_estimee", columnDefinition = "TEXT")
        private String dureeEstimee;
    }
    
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Budget {
        @Column(name = "estimation_couts", columnDefinition = "TEXT")
        private String estimationCouts;
    }
    
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CriteresValidation {
        @Column(name = "elements_verifier", columnDefinition = "TEXT")
        private String elementsVerifier;
        
        @Column(name = "scenarios_test", columnDefinition = "TEXT")
        private String scenariosTest;
    }
    
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Annexes {
        @Column(columnDefinition = "TEXT")
        private String glossaire;
        
        @Column(name = "documents_complementaires", columnDefinition = "TEXT")
        private String documentsComplementaires;
        
        @Column(name = "references_utiles", columnDefinition = "TEXT")
        private String referencesUtiles;
    }
}
