package service.impl;

import Models.CDC;
import dto.CDCRequest;
import org.springframework.stereotype.Service;
import repository.CDCRepository;
import service.CDCService;
import utils.ValidationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CDCServiceImpl implements CDCService {

    private final CDCRepository cdcRepository;

    public CDCServiceImpl(CDCRepository cdcRepository) {
        this.cdcRepository = cdcRepository;
    }

    @Override
    public CDC createCDC(CDCRequest cdcRequest) throws Exception {
        // Validate required fields
       validateCDCRequest(cdcRequest, true);

        // Create new CDC entity
        CDC cdc = new CDC();
        mapRequestToEntity(cdcRequest, cdc);
        
        // Save and return
        return cdcRepository.save(cdc);
    }

    @Override
    public CDC updateCDC(UUID id, CDCRequest cdcRequest) throws Exception {
        // Find existing CDC or throw exception
        CDC existingCDC = cdcRepository.findById(id)
                .orElseThrow(() -> new Exception("CDC not found with id: " + id));

        // Validate required fields
        validateCDCRequest(cdcRequest, false);
        
        // Update entity
        mapRequestToEntity(cdcRequest, existingCDC);
        
        // Save and return
        return cdcRepository.save(existingCDC);
    }

    @Override
    public void deleteCDC(UUID id) throws Exception {
        if (!cdcRepository.existsById(id)) {
            throw new Exception("CDC not found with id: " + id);
        }
        cdcRepository.deleteById(id);
    }

    @Override
    public CDC getCDCById(UUID id) throws Exception {
        return cdcRepository.findById(id)
                .orElseThrow(() -> new Exception("CDC not found with id: " + id));
    }

    @Override
    public List<CDC> getAllCDCs() {
        return cdcRepository.findAll();
    }

    @Override
    public List<CDC> searchCDCsByNomProjet(String nomProjet) {
        if (ValidationUtils.isNullOrEmpty(nomProjet)) {
            return new ArrayList<>();
        }
        return cdcRepository.findByTitleContainingIgnoreCase(nomProjet);
   }

//    @Override
//    public List<CDC> searchCDCsByTitle(String title) {
//        if (ValidationUtils.isNullOrEmpty(title)) {
//            return new ArrayList<>();
//        }
//        return cdcRepository.findByTitleContainingIgnoreCase(title);
//   }

//    @Override
//    public List<CDC> getCDCsByType(String type) {
//        if (ValidationUtils.isNullOrEmpty(type)) {
//            return new ArrayList<>();
//        }
//        return cdcRepository.findByType(type);
//    }
    
    @Override
    public void validateCDCRequest(CDCRequest request, boolean isNewCdc) throws Exception {
        // Title is required
//        if (ValidationUtils.isNullOrEmpty(request.getTitle())) {
//            throw new Exception("Le titre du CDC est requis");
//        }
//
        // Check if title exists on create (not on update)
//        if (isNewCdc && cdcRepository.existsByTitle(request.getTitle())) {
//            throw new Exception("Un CDC avec ce titre existe déjà");
//        }

        // Page de garde validations
        if (request.getPageDeGarde() == null) {
            throw new Exception("Les informations de la page de garde sont requises");
        }
        
        if (ValidationUtils.isNullOrEmpty(request.getPageDeGarde().getNomProjet())) {
            throw new Exception("Le nom du projet est requis");
        }
        
        if (ValidationUtils.isNullOrEmpty(request.getPageDeGarde().getNomClient())) {
            throw new Exception("Le nom du client est requis");
        }
        
        if (request.getPageDeGarde().getDate() == null) {
            throw new Exception("La date du document est requise");
        }
        
        if (request.getPageDeGarde().getRedacteurs() == null || request.getPageDeGarde().getRedacteurs().isEmpty()) {
            throw new Exception("Au moins un rédacteur est requis");
        }
        
        // Introduction validations
        if (request.getIntroduction() == null) {
            throw new Exception("Les informations d'introduction sont requises");
        }
        
        if (ValidationUtils.isNullOrEmpty(request.getIntroduction().getContexteProjet())) {
            throw new Exception("Le contexte du projet est requis");
        }
        
        if (ValidationUtils.isNullOrEmpty(request.getIntroduction().getObjectifGlobal())) {
            throw new Exception("L'objectif global est requis");
        }
        
        if (ValidationUtils.isNullOrEmpty(request.getIntroduction().getPresentationCommanditaire())) {
            throw new Exception("La présentation du commanditaire est requise");
        }
        
        if (ValidationUtils.isNullOrEmpty(request.getIntroduction().getPorteeProjet())) {
            throw new Exception("La portée du projet est requise");
        }
        
        // Objectifs du projet validations
        if (request.getObjectifsProjet() == null) {
            throw new Exception("Les objectifs du projet sont requis");
        }
        
        if (ValidationUtils.isNullOrEmpty(request.getObjectifsProjet().getObjectifsFonctionnels())) {
            throw new Exception("Les objectifs fonctionnels sont requis");
        }
        
        if (ValidationUtils.isNullOrEmpty(request.getObjectifsProjet().getObjectifsNonFonctionnels())) {
            throw new Exception("Les objectifs non fonctionnels sont requis");
        }
        
        // Description du besoin validations
        if (request.getDescriptionBesoin() == null) {
            throw new Exception("La description du besoin est requise");
        }
        
        if (ValidationUtils.isNullOrEmpty(request.getDescriptionBesoin().getProblemesActuels())) {
            throw new Exception("La description des problèmes actuels est requise");
        }
        
        if (ValidationUtils.isNullOrEmpty(request.getDescriptionBesoin().getUtilisateursCibles())) {
            throw new Exception("La description des utilisateurs cibles est requise");
        }
        
        if (ValidationUtils.isNullOrEmpty(request.getDescriptionBesoin().getBesoinsExprimes())) {
            throw new Exception("La description des besoins exprimés est requise");
        }
    }
    
    private void mapRequestToEntity(CDCRequest request, CDC entity) {
        entity.setTitle(request.getPageDeGarde().getNomProjet());
        entity.setType(request.getType());
        entity.setVersion(request.getVersion());
        
        if (request.getContributors() != null) {
            entity.setContributors(request.getContributors());
        }
        
        // Map Page de Garde
        if (request.getPageDeGarde() != null) {
            CDC.PageDeGarde pageDeGarde = entity.getPageDeGarde();
            if (pageDeGarde == null) {
                pageDeGarde = new CDC.PageDeGarde();
                entity.setPageDeGarde(pageDeGarde);
            }
            
            pageDeGarde.setNomProjet(request.getPageDeGarde().getNomProjet());
            pageDeGarde.setNomClient(request.getPageDeGarde().getNomClient());
            pageDeGarde.setDate(request.getPageDeGarde().getDate());
            pageDeGarde.setVersionDocument(request.getPageDeGarde().getVersionDocument());
            
            if (request.getPageDeGarde().getRedacteurs() != null) {
                pageDeGarde.setRedacteurs(request.getPageDeGarde().getRedacteurs());
            }
        }
        
        // Map Introduction
        if (request.getIntroduction() != null) {
            CDC.Introduction intro = entity.getIntroduction();
            if (intro == null) {
                intro = new CDC.Introduction();
                entity.setIntroduction(intro);
            }
            
            intro.setContexteProjet(request.getIntroduction().getContexteProjet());
            intro.setObjectifGlobal(request.getIntroduction().getObjectifGlobal());
            intro.setPresentationCommanditaire(request.getIntroduction().getPresentationCommanditaire());
            intro.setPorteeProjet(request.getIntroduction().getPorteeProjet());
        }
        
        // Map Objectifs Projet
        if (request.getObjectifsProjet() != null) {
            CDC.ObjectifsProjet obj = entity.getObjectifsProjet();
            if (obj == null) {
                obj = new CDC.ObjectifsProjet();
                entity.setObjectifsProjet(obj);
            }
            
            obj.setObjectifsFonctionnels(request.getObjectifsProjet().getObjectifsFonctionnels());
            obj.setObjectifsNonFonctionnels(request.getObjectifsProjet().getObjectifsNonFonctionnels());
        }
        
        // Map Description Besoin
        if (request.getDescriptionBesoin() != null) {
            CDC.DescriptionBesoin desc = entity.getDescriptionBesoin();
            if (desc == null) {
                desc = new CDC.DescriptionBesoin();
                entity.setDescriptionBesoin(desc);
            }
            
            desc.setProblemesActuels(request.getDescriptionBesoin().getProblemesActuels());
            desc.setUtilisateursCibles(request.getDescriptionBesoin().getUtilisateursCibles());
            desc.setBesoinsExprimes(request.getDescriptionBesoin().getBesoinsExprimes());
        }
        
        // Map Périmètre Fonctionnel
        if (request.getPerimetreFonctionnel() != null) {
            CDC.PerimetreFonctionnel perim = entity.getPerimetreFonctionnel();
            if (perim == null) {
                perim = new CDC.PerimetreFonctionnel();
                entity.setPerimetreFonctionnel(perim);
            }
            
            perim.setAuthentification(request.getPerimetreFonctionnel().getAuthentification());
            perim.setTableauBord(request.getPerimetreFonctionnel().getTableauBord());
            perim.setGestionUtilisateurs(request.getPerimetreFonctionnel().getGestionUtilisateurs());
            perim.setGestionDonnees(request.getPerimetreFonctionnel().getGestionDonnees());
            perim.setNotifications(request.getPerimetreFonctionnel().getNotifications());
            perim.setAutresFonctionnalites(request.getPerimetreFonctionnel().getAutresFonctionnalites());
        }
        
        // Map Contraintes Techniques
        if (request.getContraintesTechniques() != null) {
            CDC.ContraintesTechniques contr = entity.getContraintesTechniques();
            if (contr == null) {
                contr = new CDC.ContraintesTechniques();
                entity.setContraintesTechniques(contr);
            }
            
            contr.setLangagesFrameworks(request.getContraintesTechniques().getLangagesFrameworks());
            contr.setBaseDonnees(request.getContraintesTechniques().getBaseDonnees());
            contr.setHebergement(request.getContraintesTechniques().getHebergement());
            contr.setSecurite(request.getContraintesTechniques().getSecurite());
            contr.setCompatibilite(request.getContraintesTechniques().getCompatibilite());
        }
        
        // Map Planning Prévisionnel
        if (request.getPlanningPrevisionnel() != null) {
            CDC.PlanningPrevisionnel plan = entity.getPlanningPrevisionnel();
            if (plan == null) {
                plan = new CDC.PlanningPrevisionnel();
                entity.setPlanningPrevisionnel(plan);
            }
            
            plan.setPhasesProjet(request.getPlanningPrevisionnel().getPhasesProjet());
            plan.setDatesCles(request.getPlanningPrevisionnel().getDatesCles());
            plan.setDureeEstimee(request.getPlanningPrevisionnel().getDureeEstimee());
        }
        
        // Map Budget
        if (request.getBudget() != null) {
            CDC.Budget budget = entity.getBudget();
            if (budget == null) {
                budget = new CDC.Budget();
                entity.setBudget(budget);
            }
            
            budget.setEstimationCouts(request.getBudget().getEstimationCouts());
        }
        
        // Map Critères de Validation
        if (request.getCriteresValidation() != null) {
            CDC.CriteresValidation crit = entity.getCriteresValidation();
            if (crit == null) {
                crit = new CDC.CriteresValidation();
                entity.setCriteresValidation(crit);
            }
            
            crit.setElementsVerifier(request.getCriteresValidation().getElementsVerifier());
            crit.setScenariosTest(request.getCriteresValidation().getScenariosTest());
        }
        
        // Map Annexes
        if (request.getAnnexes() != null) {
            CDC.Annexes annexes = entity.getAnnexes();
            if (annexes == null) {
                annexes = new CDC.Annexes();
                entity.setAnnexes(annexes);
            }
            
            annexes.setGlossaire(request.getAnnexes().getGlossaire());
            annexes.setDocumentsComplementaires(request.getAnnexes().getDocumentsComplementaires());
            annexes.setReferencesUtiles(request.getAnnexes().getReferencesUtiles());
        }
    }
}
