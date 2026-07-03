# NexTeam

### *L'intranet nouvelle génération qui unifie vos équipes.*

Plateforme intranet permettant la communication et la gestion interne des employés. NexTeam répond à un besoin majeur rencontré dans de nombreuses entreprises : le manque de communication et de centralisation des échanges entre les employés, les ressources humaines, les responsables et les managers.

L’application NexTeam permet de pallier cette problématique grâce à un système de messagerie intégré favorisant les échanges rapides et directs entre les différents acteurs de l’entreprise. La plateforme permet également de centraliser plusieurs outils internes au sein d’une seule application afin de simplifier l’organisation et améliorer la productivité des équipes.

[![Angular](https://img.shields.io/badge/Angular-DD0031?style=for-the-badge\&logo=angular\&logoColor=white)](https://angular.io/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge\&logo=springboot\&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/postgresql-4479A1?style=for-the-badge\&logo=postgresql\&logoColor=white)](https://www.postgresql.com/)
[![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge\&logo=docker\&logoColor=white)](https://www.docker.com/)

---

## 💡 La Problématique

Dans de nombreuses entreprises, l'information est **fragmentée** :

* Messages dispersés entre plusieurs outils
* Communication interne difficile à suivre
* Documents et informations difficiles à retrouver
* Manque de centralisation des processus RH et opérationnels

Cette dispersion réduit la productivité et complique la collaboration entre les équipes.

---

## 🎯 La Solution : NexTeam

**NexTeam** est une plateforme centralisée qui regroupe communication, gestion et informations internes dans une interface moderne, sécurisée et intuitive.

L'objectif est de fournir un véritable espace de travail numérique permettant aux collaborateurs d'accéder à l'ensemble des outils essentiels depuis une seule application.

---

## 📸 Aperçu du Dashboard

![NexTeam Dashboard](nexteam-docs/static/img/maquettes/screen.png)

---

## 🔥 Fonctionnalités Clés

### 💬 Communication Instantanée

* Messagerie temps réel via **WebSockets (STOMP)**
* Fil d’actualités interne dynamique
* Échanges rapides entre collaborateurs

### 📋 Productivité & Organisation

* Gestion de tâches et suivi des missions
* Carte employé numérique
* Centralisation des informations internes
* Outils collaboratifs intégrés

### 🛡️ Sécurité & Accès

* Authentification sécurisée **JWT**
* Hashage des mots de passe avec **BCrypt**
* Gestion des rôles (**Admin**, **RH**, **Manager**, **Employé**)
* Protection des accès et des ressources

---

## 🛠️ Stack Technique

| Frontend          | Backend               | Base de données & Ops |
| ----------------- | --------------------- | --------------------- |
| Angular 21        | Spring Boot 3         | PostgreSQL / MySQL    |
| TypeScript        | Java 21               | JPA / Hibernate       |
| css / RxJS       | Maven                 | Docker                |
| WebSocket (STOMP) | Spring Security / JWT | REST API              |

---

## 📚 Documentation Technique

Le projet dispose d'une documentation complète réalisée avec **Docusaurus**.

Cette documentation regroupe :

* L'architecture du projet
* Les choix techniques
* Les conventions de développement
* La documentation API
* Les guides d'installation
* Les bonnes pratiques utilisées dans le projet

### Lancer la documentation

```bash
cd documentation
npm install
npm run start
```

La documentation sera accessible à l'adresse :

```text
http://localhost:3000
```

---

## ⚙️ Installation Rapide

### 1. Prérequis

* Node.js
* Angular CLI
* Java 21 (JDK)
* PostgreSQL
* Docker (optionnel)

### 2. Lancer le Backend

```bash
cd backend
./mvnw spring-boot:run
```

### 3. Lancer le Frontend

```bash
cd frontend
npm install
ng serve
```

### 🌐 Accès à l'application

```text
http://localhost:4200
```

---

## 📖 Pourquoi ce projet ?

NexTeam est conçu avec une approche **Product-First** et orientée entreprise.

### Objectifs

* Réduire la fragmentation des outils internes
* Améliorer la communication en temps réel
* Centraliser les informations de l'entreprise
* Expérimenter une architecture moderne Angular + Spring Boot
* Mettre en œuvre des bonnes pratiques de développement Full Stack
* Concevoir une documentation technique complète et maintenable

---

## 🚀 Roadmap

* [ ] Gestion avancée des notifications
* [ ] Calendrier collaboratif
* [ ] Gestion documentaire
* [ ] Tableau de bord RH
* [ ] Déploiement Cloud

---

## 👨‍💻 Auteur

**JonatanNS**

Développeur Full Stack Java / Angular

Projet réalisé dans une démarche d'apprentissage avancé du développement d'applications d'entreprise modernes.
