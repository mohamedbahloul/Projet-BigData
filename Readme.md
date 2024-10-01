# Projet Clash Royale - Analyse des Cartes

## Description
Ce projet est une application Java développée pour analyser les données du jeu **Clash Royale**, en particulier l'utilisation des cartes par les joueurs, les victoires, et les statistiques relatives aux decks et aux clans. Il permet de générer des résumés et des statistiques basées sur les données des parties jouées.

L'application utilise Hadoop pour stocker et traiter les données, et plusieurs classes Java pour gérer les statistiques des joueurs et des cartes. Les joueurs et leurs performances sont comparés en fonction de plusieurs critères comme le nombre de victoires, l'usage des cartes, et la différence de puissance des decks.

## Fonctionnalités
- **Stockage et traitement des données de jeu :**
  - Informations sur les joueurs (deck, clan, trophées, etc.)
  - Comparaison des performances entre deux joueurs dans un match
  - Calcul de statistiques globales sur plusieurs parties (victoires, différences de deck, nombre d'utilisations, etc.)

- **Classement et tri des cartes :**
  - Comparaison des cartes en fonction de différents critères (nombre de victoires, utilisation, différence de deck)
  - Support de plusieurs filtres de tri : par moyenne, par utilisation, par nombre de joueurs distincts

- **Classes principales :**
  - `Game` : Stocke les informations sur un match entre deux joueurs.
  - `GameSummary` : Résume les statistiques d'un joueur ou d'une série de parties.
  - `TopK` : Contient les informations pour le classement des meilleures cartes.
  - `TopKKey` : Définit la clé de tri des cartes en fonction des filtres sélectionnés.

## Prérequis
- **Java 8 ou supérieur**
- **Apache Hadoop** (pour la gestion et le traitement des données en parallèle)
- **Maven** pour la gestion des dépendances et la compilation du projet

## Installation

1. Cloner ce repository :
   ```bash
   git clone https://github.com/ton-utilisateur/projet-clashroyale.git
   cd projet-clashroyale
2. Compiler et packager le projet avec Maven :
    mvn clean
    mvn compile
    mvn package

3. Exécution du projet :
   hadoop jar projet-clashroyale.jar

4. Structure du projet :
    ├── src/
    │   ├── main/
    │   │   ├── java/
    │   │   │   ├── Game.java          # Classe représentant un match entre deux joueurs
    │   │   │   ├── GameSummary.java   # Classe résumant les statistiques d'un joueur
    │   │   │   ├── TopK.java          # Classe pour le classement des meilleures cartes
    │   │   │   ├── TopKKey.java       # Classe définissant la clé de tri des cartes
    │   │   └── resources/
    │   └── test/                      # Tests unitaires 
    ├── pom.xml                        # Fichier de configuration Maven
    └── README.md                      # Ce fichier



