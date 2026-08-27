# <img src="nexteam-docs/static/img/logo_nexteam.png" alt="Nexteam" width="40" /> NexTeam

![Angular](https://img.shields.io/badge/Angular-v22-DD0031?logo=angular&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-v4.1.0-6DB33F?logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-v16-4169E1?logo=postgresql&logoColor=white)
![WebSocket](https://img.shields.io/badge/WebSocket-STOMP%20%2F%20SockJS-black)
![Security](https://img.shields.io/badge/Security-JWT%20%26%20BCrypt-green)
![CI/CD](https://img.shields.io/badge/CI%2FCD-GitHub%20Actions-2088FF?logo=githubactions&logoColor=white)


NexTeam est une plateforme intranet qui centralise la communication et la gestion RH d'une entreprise en un seul outil, là où la plupart des entreprises jonglent aujourd'hui entre plusieurs abonnements SaaS (Slack ou Teams pour la messagerie, un outil RH séparé pour les fiches de paie, un intranet distinct pour les actualités).

Cette dispersion a un coût réel : plusieurs abonnements à gérer, des informations éparpillées entre plusieurs outils, et des équipes qui perdent du temps à chercher la bonne information au bon endroit.

NexTeam répond à ce besoin avec une plateforme unique qui couvre à la fois la communication interne et les besoins administratifs RH — un positionnement que les outils de messagerie généralistes ne couvrent pas nativement.

📖 **Documentation complète : [jonatanns.github.io/appNexTeam](https://jonatanns.github.io/appNexTeam/)**

---

## 📁 Structure du dépôt

```bash
/backend       → API REST + WebSocket (Spring Boot)
/frontend      → Application web (Angular)
/nexteam-docs  → Documentation technique (Docusaurus)
```

---

## 🚀 Installation

### Backend

```bash
cd backend
./gradlew bootRun
```
→ http://localhost:8080 (Swagger : `/swagger-ui/index.html`)

## ⚙️ Configuration

Avant de lancer le backend, crée un fichier `.env` à la racine de `/backend` à partir du modèle fourni :

```bash
cd backend
cp .env.example .env
```

Renseigne ensuite tes propres valeurs pour `DB_USERNAME`, `DB_PASSWORD` et `JWT_SECRET`.

> ⚠️ Le fichier `.env` ne doit jamais être commité il est déjà exclu via `.gitignore`.

### Frontend

```bash
cd frontend
npm install
npm start
```
→ http://localhost:4200

### Documentation

```bash
cd nexteam-docs
npm install
npm start
```
→ http://localhost:3000

---

## 📌 Auteur

**Jonatan Ns** — Développeur Full Stack (Angular / Spring Boot)
GitHub : https://github.com/JonatanNs/appNexTeam
