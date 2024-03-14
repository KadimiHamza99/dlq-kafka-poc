package io.kadev.kafkatest.utils;

import io.kadev.kafkatest.models.InputModel;
import lombok.experimental.UtilityClass;

import java.util.Arrays;

@UtilityClass
public class ObjectFactory {
    public InputModel request1(){
        InputModel message = new InputModel();

        // Initialisation des champs de test
        message.body = new InputModel.Data();
        message.body.AXAContextHeader = new InputModel.AXAContextHeader();
        message.body.AXAContextHeader.aems_contextHeader = new InputModel.AemsContextHeader();
        message.body.AXAContextHeader.aems_contextHeader.aems_auditTimestamp = "2022-02-01T18:52:12.220";
        message.body.AXAContextHeader.aems_contextHeader.aems_functionalID = "1IAJD98DZA9";
        message.body.AXAContextHeader.aems_contextHeader.aems_addressing = new InputModel.Addressing();
        message.body.AXAContextHeader.aems_contextHeader.aems_addressing.aems_messageID = "5989f65-579-895-aeb7-hbi5af98fq";
        message.body.AXAContextHeader.aems_contextHeader.aems_addressing.aems_conversationID = "5989f65-579-895-aeb7-hbi5af98fq3cf43";
        message.body.AXAContextHeader.aems_contextHeader.aems_addressing.aems_precedingMessageID = "5989f65-579-895-aeb7-hbi5af98fq";
        message.body.AXAContextHeader.aems_contextHeader.aems_messageMetadata = new InputModel.MessageMetadata();
        message.body.AXAContextHeader.aems_contextHeader.aems_messageMetadata.aems_serviceID = "25989f65-579-895-aeb7-hbi5af98fq3125b";
        message.body.AXAContextHeader.aems_contextHeader.aems_messageMetadata.aems_serviceName = "Sefz.pub:afazf878";
        message.body.AXAContextHeader.aems_contextHeader.aems_messageMetadata.aems_stage = "PRD";
        message.body.AXAContextHeader.aems_contextHeader.aems_messageMetadata.aems_subStage = "MAIN";
        message.body.AXAContextHeader.aems_contextHeader.aems_requesters = new InputModel.Requesters();
        message.body.AXAContextHeader.aems_contextHeader.aems_requesters.totalCount = "1";
        message.body.AXAContextHeader.aems_contextHeader.aems_requesters.aems_requester = Arrays.asList(new InputModel.Requester());
        message.body.AXAContextHeader.aems_contextHeader.aems_requesters.aems_requester.get(0).order = "1";
        message.body.AXAContextHeader.aems_contextHeader.aems_requesters.aems_requester.get(0).aems_opCo = "FR";
        message.body.AXAContextHeader.aems_contextHeader.aems_requesters.aems_requester.get(0).aems_businessProcess = "DebugService";
        message.body.AXAContextHeader.aems_contextHeader.aems_requesters.aems_requester.get(0).aems_businessSubProcess = "DebugStep";
        message.body.AXAContextHeader.aems_contextHeader.aems_requesters.aems_requester.get(0).aems_businessStep = "Sefz.pub.pub";
        message.body.AXAContextHeader.aems_contextHeader.aems_requesters.aems_requester.get(0).aems_businessObjectID = "5989f65-579-895-aeb7-hbi5af98fq";
        message.body.AXAContextHeader.aems_contextHeader.aems_requesters.aems_requester.get(0).aems_creationTimestamp = "2022-02-01T18:52:12.220";
        message.body.AXAContextHeader.aems_contextHeader.aems_additionalData = new InputModel.AdditionalData();
        message.body.AXAContextHeader.aems_contextHeader.aems_additionalData.aems_data = Arrays.asList(
                new InputModel.DataEntry("1IAJD98DZA9", "IdentifiantDemande"),
                new InputModel.DataEntry("1IAJD98DZA7", "Portefeuille"),
                new InputModel.DataEntry("1IAJD98DZA6", "functionalID")
        );

        // Initialisation des autres champs
        message.body.Message = new InputModel.Message();
        message.body.Message.Souscription = new InputModel.Souscription();
        message.body.Message.Souscription.Utilisateur = "W06589898";
        message.body.Message.Souscription.ReseauDistribution = "10";
        message.body.Message.Souscription.DateReception = "2022-02-21";
        message.body.Message.Souscription.Consommateur = "1550";
        message.body.Message.Souscription.ComplementConsommateur = "89V11.3.1";
        message.body.Message.Souscription.IdentifiantDemande = "W06589898";
        message.body.Message.Souscription.Horodatage = "2022-02-01T18:52:12.220";
        message.body.Message.Souscription.SignatureElectronique = "true";
        message.body.Message.Souscription.ProfessionNonReferencee = "false";
        message.body.Message.Souscription.QSMVDigital = "true";
        message.body.Message.Souscription.EligibleAMEL = "true";
        message.body.Message.Souscription.Collaboration = new InputModel.Collaboration();
        message.body.Message.Souscription.Collaboration.Producteur = "W06589898";
        message.body.Message.Souscription.Reglement = new InputModel.Reglement();
        message.body.Message.Souscription.Reglement.Payeur = new InputModel.Payeur();
        message.body.Message.Souscription.Reglement.Payeur.IdLocalPersonne = "1";
        message.body.Message.Souscription.Reglement.Payeur.IdLocalCoordonneesBancaires = "1";
        message.body.Message.Souscription.Reglement.ModeDePaiement = "AD";
        message.body.Message.Souscription.Reglement.MontantNewCash = "587.64";
        message.body.Message.Souscription.EstimationRisque = new InputModel.EstimationRisque();
        message.body.Message.Souscription.EstimationRisque.InformationsMedicales = new InputModel.InformationsMedicales();
        message.body.Message.Souscription.EstimationRisque.InformationsMedicales.AssureFumeur = "false";
        message.body.Message.Souscription.EstimationRisque.InformationsMedicales.NiveauSelection = "1";
        message.body.Message.Souscription.EstimationRisque.InformationsMedicales.QuestionnaireMedicalSimplifie = "true";
        message.body.Message.Souscription.EstimationRisque.InformationsMedicales.Caracteristiques = new InputModel.Caracteristiques();
        message.body.Message.Souscription.EstimationRisque.InformationsMedicales.Caracteristiques.Caracteristique = Arrays.asList(
                new InputModel.Caracteristique("1", "true"),
                new InputModel.Caracteristique("2", "false"),
                new InputModel.Caracteristique("3", "false"),
                new InputModel.Caracteristique("4", "false"),
                new InputModel.Caracteristique("5", "false"),
                new InputModel.Caracteristique("6", "false"),
                new InputModel.Caracteristique("7", "false"),
                new InputModel.Caracteristique("8", "false"),
                new InputModel.Caracteristique("9", "false"),
                new InputModel.Caracteristique("10", "true"),
                new InputModel.Caracteristique("11", "false"),
                new InputModel.Caracteristique("12", "false"),
                new InputModel.Caracteristique("13", "false")
        );
        return message;
    }
}
