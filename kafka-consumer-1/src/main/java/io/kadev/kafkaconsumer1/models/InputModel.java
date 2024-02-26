package io.kadev.kafkaconsumer1.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InputModel {
    public Data body;

    @lombok.Data
    @NoArgsConstructor
    public static class Data {
        public AXAContextHeader AXAContextHeader;
        public Message Message;
    }

    @lombok.Data
    @NoArgsConstructor
    public static class AXAContextHeader {
        public AemsContextHeader aems_contextHeader;
    }

    @lombok.Data
    @NoArgsConstructor
    public static class AemsContextHeader {
        public String aems_auditTimestamp;
        public String aems_functionalID;
        public Addressing aems_addressing;
        public MessageMetadata aems_messageMetadata;
        public Requesters aems_requesters;
        public AdditionalData aems_additionalData;
    }
    @lombok.Data
    @NoArgsConstructor
    public static class Addressing {
        public String aems_messageID;
        public String aems_conversationID;
        public String aems_precedingMessageID;
    }
    @lombok.Data
    @NoArgsConstructor
    public static class MessageMetadata {
        public String aems_serviceID;
        public String aems_serviceName;
        public String aems_stage;
        public String aems_subStage;
    }
    @lombok.Data
    @NoArgsConstructor
    public static class Requesters {
        public String totalCount;
        public List<Requester> aems_requester;
    }
    @lombok.Data
    @NoArgsConstructor
    public static class Requester {
        public String order;
        public String aems_opCo;
        public String aems_businessProcess;
        public String aems_businessSubProcess;
        public String aems_businessStep;
        public String aems_businessObjectID;
        public String aems_creationTimestamp;
    }
    @lombok.Data
    @NoArgsConstructor
    public static class AdditionalData {
        public List<DataEntry> aems_data;
    }
    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataEntry {
        public String body;
        public String key;


    }
    @lombok.Data
    @NoArgsConstructor
    public static class Message {
        public Souscription Souscription;
        public Contrat Contrat;
        public Personnes Personnes;
        public Documents Documents;
    }
    @lombok.Data
    @NoArgsConstructor
    public static class Souscription {
        public String Utilisateur;
        public String ReseauDistribution;
        public String DateReception;
        public String Consommateur;
        public String ComplementConsommateur;
        public String IdentifiantDemande;
        public String Horodatage;
        public String SignatureElectronique;
        public String ProfessionNonReferencee;
        public String QSMVDigital;
        public String EligibleAMEL;
        public Collaboration Collaboration;
        public Reglement Reglement;
        public EstimationRisque EstimationRisque;
    }
    @lombok.Data
    @NoArgsConstructor
    public static class Collaboration {
        public String Producteur;
    }
    @lombok.Data
    @NoArgsConstructor
    public static class Reglement {
        public Payeur Payeur;
        public String ModeDePaiement;
        public String MontantNewCash;
    }
    @lombok.Data
    @NoArgsConstructor
    public static class Payeur {
        public String IdLocalPersonne;
        public String IdLocalCoordonneesBancaires;
    }
    @lombok.Data
    @NoArgsConstructor
    public static class EstimationRisque {
        public InformationsMedicales InformationsMedicales;
    }
    @lombok.Data
    @NoArgsConstructor
    public static class InformationsMedicales {
        public String AssureFumeur;
        public String NiveauSelection;
        public String QuestionnaireMedicalSimplifie;
        public Caracteristiques Caracteristiques;
    }
    @lombok.Data
    @NoArgsConstructor
    public static class Caracteristiques {
        public List<Caracteristique> Caracteristique;
    }
    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Caracteristique {
        public String Identifiant;
        public String Reponse;
    }
    @lombok.Data
    @NoArgsConstructor
    public static class Contrat {
        public String Portefeuille;
        public String CodeProduitUV;
        public String DateEffet;
        public String CreditImpotA82;
        public String JourPrelevement;
        public String BaremePro;
        public Souscripteurs Souscripteurs;
        public Assures Assures;
        public PayeurPrime PayeurPrime;
        public Garanties Garanties;
        public PlanPrimesPeriodiques PlanPrimesPeriodiques;
        public String OptionInval15;
    }
    @lombok.Data
    @NoArgsConstructor
    public static class Souscripteurs {
        public List<Souscripteur> Souscripteur;
    }
    @lombok.Data
    @NoArgsConstructor
    public static class Souscripteur {
        public String IdLocalPersonne;
    }
    @lombok.Data
    @NoArgsConstructor
    public static class Assures {
        public List<Assure> Assure;
    }
    @lombok.Data
    @NoArgsConstructor
    public static class Assure {
        public String IdLocalPersonne;
    }
    @lombok.Data
    @NoArgsConstructor
    public static class PayeurPrime {
        public String IdLocalPersonne;
        public String IdLocalCoordonneesBancaires;
    }
    @lombok.Data
    @NoArgsConstructor
    public static class Garanties {
        public List<Garantie> Garantie;
    }
    @lombok.Data
    @NoArgsConstructor
    public static class Garantie {
        public String IdentifiantLocal;
        public String CodeUniteTechnique;
        public String MontantGaranti;
        public Tarification Tarification;
        public ClauseBeneficiaire ClauseBeneficiaire;
        public Franchise Franchise;
    }
    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Tarification {
        public ClasseTarification ClasseTarification;

    }
    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClasseTarification {
        public String Identifiant;
        public String Version;
    }
    @lombok.Data
    @NoArgsConstructor
    public static class ClauseBeneficiaire {
        public String CodeMnemonique;
    }
    @lombok.Data
    @NoArgsConstructor
    public static class Franchise {
        public String IdentifiantHospitalisation;
        public String IdentifiantAccident;
        public String IdentifiantMaladie;
        public String IdentifiantDureeMax;
    }
    @lombok.Data
    @NoArgsConstructor
    public static class PlanPrimesPeriodiques {
        public String Montant;
        public String Periodicite;
        public String Indexation;
        public String ModeDePaiement;
    }
    @lombok.Data
    @NoArgsConstructor
    public static class Personnes {
        public List<Personne> Personne;
    }
    @lombok.Data
    @NoArgsConstructor
    public static class Personne {
        public String IdLocal;
        public String NumAbonne;
        public String Qualite;
        public String Nom;
        public Adresse Adresse;
        public ListeCoordonneesBancaires ListeCoordonneesBancaires;
        public PersonnePhysique PersonnePhysique;
    }
    @lombok.Data
    @NoArgsConstructor
    public static class Adresse {
        public String NumeroVoie;
        public String CodePostal;
        public String Ville;
        public String Pays;
    }
    @lombok.Data
    @NoArgsConstructor
    public static class ListeCoordonneesBancaires {
        public List<CoordonneesBancaires> CoordonneesBancaires;
    }
    @lombok.Data
    @NoArgsConstructor
    public static class CoordonneesBancaires {
        public String IdLocal;
        public IBAN IBAN;
        public String BIC;
        public Titulaire Titulaire;
    }
    @lombok.Data
    @NoArgsConstructor
    public static class IBAN {
        public String CLE;
        public String RIBBanque;
        public String RIBGuichet;
        public String RIBCompte;
        public String RIBCle;
    }
    @lombok.Data
    @NoArgsConstructor
    public static class Titulaire {
        public String Qualite;
        public String Nom;
        public String Prenom;
    }
    @lombok.Data
    @NoArgsConstructor
    public static class PersonnePhysique {
        public String Prenom;
        public String DateNaissance;
        public String DepartementCodePostalNaissance;
        public String PaysNaissance;
        public String VilleNaissance;
        public String SituationFamille;
        public String Profession;
        public String IdentifiantProfession;
        public String CategorieSocioProfessionnelle;
        public String TypePieceIdentite;
        public String NumeroPieceIdentite;
        public String DateValiditePieceIdentite;
        public String PaysDelivrancePieceIdentite;
        public String VilleDelivrancePieceIdentite;
        public String SituationFinanciere;
        public String ScoreConso;
        public String ScoreSante;
        public String ScorePrevoyance;
        public String CodeGestionTitulaires;
        public String CodeCivilite;
        public String NumFiness;
        public String NumRepertoire;
        public String NumTrancheAge;
        public String Produit;
        public String SousProduit;
        public String StatutLegal;
        public String TypeAbo;
        public String MoyenPaiement;
        public String TypeAdresse;
        public String ProduitCmp;
    }
    @lombok.Data
    @NoArgsConstructor
    public static class Documents {
        public List<Document> Document;
    }
    @lombok.Data
    @NoArgsConstructor
    public static class Document {
        public String Type;
        public String Identifiant;
        public String URL;
        public String Date;
        public String Description;
        public String NomFichier;
        public String ReferenceContrat;
    }
}
