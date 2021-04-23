# GLP-GPS

Ce projet a été réalisé par Paul CHEVALIER, William DENOYER & Benjamin WALLETH en L2 Informatique à CY Cergy Paris Université dans le cadre du module de **Génie Logiciel & Projet**. Sa vocation a donc été de résumer et de mettre en pratique les connaissances acquises tout au long du module.

![CY Cergy Paris Université](https://upload.wikimedia.org/wikipedia/fr/thumb/6/69/Logo_CY_Cergy_Paris_Universit%C3%A9.svg/129px-Logo_CY_Cergy_Paris_Universit%C3%A9.svg.png)

Ce projet a été réalisé Java.

## Description du projet

Ce projet a pour objectif la réalisation d’un indicateur d’itinéraire multimodal incluant les différents transports en commun et individuel (la marche à pied, le vélo...).

Ce logiciel permettra aux utilisateurs de pouvoir programmer un itinéraire d’un point A à un point B en utilisant le  réseau de transport. L’utilisateur pourra paramétrer selon ses critères les détails de son trajet.

## Installation & Exécution du logiciel

> Pour installer ce logiciel, il est nécessaire d'avoir installé [Java JDK 11](https://www.oracle.com/fr/java/technologies/javase-jdk11-downloads.html) ou supérieur.

### Instructions via Eclipse

Commencez par ajouter les fichiers sources sur Eclipse :

1. Placez le répertoire "src" présent dans l'archive .zip dans un nouveau répertoire nommé "glp-gps"
2. Ouvrez l'IDE Eclipse
3. Allez dans le menu "file" (en haut de la fenêtre Eclipse) puis cliquez sur l'onglet "Open projects from File System..."
4. Sélectionnez le chemin de votre répertoire "glp-gps" précédemment créé.
5. Appuyez sur le bouton "Finish"
6. Vous allez voir apparaître le dossier "glp-gps" dans la sous-fenêtre "Package Explorer" d'Eclipse (à gauche de la fenêtre Eclipse)

Ensuite, vous allez devoir ajouter les librairies "JUnit" et "Log4J" dans Eclipse :

1. Faites un click-droit sur votre dossier "glp-gps" dans la sous fenêtre "Package Explorer" d'Eclipse
2. Puis cliquez sur l'onglet "Build Path" > "Configure Build Path"
3. Cliquez sur "Classpath" lorsque la fenêtre "Build Path" est apparu
4. Enfin cliquez sur le bouton "Add External JARs..." (à droite de la fenêtre) et sélectionnez les JARs junit, log4j, hamcrest-core

Enfin, il vous suffit de lancer le logiciel via le bouton "Run" (en haut de la fenêtre Eclipse).