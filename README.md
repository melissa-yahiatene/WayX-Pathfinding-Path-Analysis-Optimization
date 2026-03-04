# 🕸️ WayX : Optimisation de Graphes et Analyse de Flux

**WayX** est une plateforme de simulation et de visualisation d'algorithmes de recherche de chemin optimal. Le projet permet de modéliser des environnements complexes (réseaux, topographies, matrices de données) sous forme de graphes pondérés pour en extraire des chemins critiques.

---

## 🚀 Présentation du Projet

L'application permet d'exécuter et de comparer des algorithmes de plus court chemin sur deux types de supports :
* **Mode Graphe (Discret) :** Chargement de cartes structurées (`.txt`) simulant des terrains avec des coûts de friction variables (forêt, eau, route).
* **Mode Image (Continu) :** Transformation d'images pixelisées en graphes. Le poids des arêtes est calculé selon le gradient d'intensité lumineuse (niveaux de gris), permettant de trouver le chemin de "moindre variation".

### 🧠 Expertise Algorithmique
* **Dijkstra** : Recherche exhaustive garantissant l'optimalité. Idéal pour l'analyse de résilience réseau.
* **A* (A-Star)** : Optimisation par guidage heuristique (**Manhattan, Euclidienne, Tchebychev**). Réduction drastique de l'espace de recherche tout en conservant l'optimalité.

---

## 📊 Comparaison des Performances

L'étude comparative menée lors des tests de charge met en évidence l'efficacité relative des algorithmes selon la topologie des données :

| Algorithme | Nœuds Explorés | Garantie d'Optimalité | Utilisation Type |
| :--- | :--- | :--- | :--- |
| **Dijkstra** | Élevé (Exploration large) | **Oui** | Analyse de topologie complète |
| **A* (Euclide)** | Faible (Directionnel) | **Oui** | Navigation temps réel / GPS |
| **A* (Manhattan)** | Très faible | **Oui** (si grille) | Optimisation de réseaux filaires |



---
### 🔍 Analyse Technique
* **Dijkstra** : Indispensable pour une cartographie exhaustive des risques, il explore toutes les directions sans a priori.
* **A-Star (A*)** : Grâce à l'injection d'heuristiques géométriques, il réduit drastiquement la complexité spatiale. C'est l'approche privilégiée pour les systèmes réactifs et la cybersécurité réseau où la rapidité de décision est critique.


---

## 🛡️ Perspectives en Cybersécurité

Ce projet constitue une base solide pour des problématiques de sécurité numérique :
* **Analyse de Propagation de Menaces** : Modélisation du chemin le plus rapide pour un malware au sein d'une topologie réseau.
* **Routage Réseau & QoS** : Optimisation de la transmission de paquets sous contraintes de latence.
* **Architecture Robuste** : Utilisation du pattern **MVP (Model-View-Presenter)** pour une séparation stricte entre la logique de calcul et l'interface, facilitant l'audit et la sécurisation du code.



---

## ⚙️ Exécution du Programme

### 1. Prérequis
* Avoir installé le **Java JDK 21**.

### 2. Lancement via le binaire (.jar)
1.  Téléchargez le fichier `WayX.jar` fourni dans le rendu.
2.  Ouvrez un terminal (cmd, PowerShell ou terminal Linux).
3.  Placez-vous dans le dossier contenant le fichier :
    ```bash
    cd chemin/vers/MonDossierWayX
    ```
4.  Lancez l'application avec la commande :
    ```bash
    java -jar WayX.jar
    ```
---
### 3. Compilation depuis les sources
```bash
mvn clean package
java -cp target/Projet-1.0-SNAPSHOT.jar org.algo.wayx.WayXApp
````
---
Le programme démarre sur un menu principal vous permettant de choisir entre le **Mode Graphe** et le **Mode Image**.

---

## 🏗️ Structure Technique
* **Langage** : Java 21
* **Interface** : Swing (Visualisation dynamique de l'exploration des nœuds)
* **Gestionnaire** : Maven (dépendances JUnit pour les tests)
* **Traitement de données** : Conversion matricielle Image-vers-Graphe (4-connexité)

---
*Projet réalisé dans le cadre de l'UE Algorithmique avancée et Théorie des Graphes.*
