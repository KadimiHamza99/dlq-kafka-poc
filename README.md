# Projet Spring Boot avec Apache Kafka

Ce projet implémente un système distribué basé sur Apache Kafka, comprenant un producteur et deux consommateurs Spring Boot. L'objectif est de simuler le traitement de messages, la gestion d'erreurs, et la résilience du système.

## Architecture

Le projet se compose des éléments suivants :
- Un producteur Kafka qui envoie un enregistrement au broker Kafka.
- Deux consommateurs Kafka pour traiter les enregistrements.
- Un topic des messages en erreur.
- Un service batch pour republier les enregistrements en échec.

## Fonctionnalités

1. **Producteur Kafka**:
   - Envoie un enregistrement au broker Kafka.

2. **Consommateurs Kafka**:
   - Traitent les enregistrements reçus.
   - L'un des consommateurs simule une erreur transitoire.
   - Les tentatives de retrait asynchrones et synchrones sont effectuées pour simuler la résilience.

3. **Dead Letters Topic**:
   - Les messages en erreur sont envoyés vers un topic spécial.
   - En fonction du type d'erreur, ils sont redirigés vers :
     - Un "Zombie Topic" nécessitant une intervention humaine.
     - Un "Save to Later Topic" pour une persistance ultérieure.

4. **Service Batch**:
   - Tourne périodiquement pour republier les enregistrements en échec vers le topic initial.
   - Les deux consommateurs écoutent sur des groupes de consommateurs différents, garantissant que seuls les consommateurs concernés reçoivent les messages réenvoyés.

## Exécution du Projet

Pour exécuter le projet :

1. Utilisez Docker Compose pour lancer les composants Kafka, Zookeeper, et AKHQ (interface de gestion de Kafka) :
   ```
   cd kafka-producer/resources/
   docker-compose up ```

2. Exécutez les services restants à partir de votre éditeur de code (IntelliJ ou Eclipse) ou via Maven :
   ```
   mvn spring-boot:run ```

## Remarques
Assurez-vous que les dépendances nécessaires sont installées et que les configurations sont correctement définies.
Pour visualiser les contenus des topics Kafka, accédez à l'interface AKHQ à l'adresse : http://localhost:8080.

---

## Contributions
Les contributions sont les bienvenues! Si vous avez des idées d'amélioration ou si vous souhaitez signaler un problème, n'hésitez pas à ouvrir une issue ou à soumettre une pull request.
   
