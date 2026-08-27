import React from 'react';
import Link from '@docusaurus/Link';
import styles from './styles.module.css';
import { 
  faUsers, faCode, faLaptopCode, faServer, faDatabase, 
  faShieldHalved, faListCheck, faComments, faBullseye, 
  faUser, faIdCard, faFile, faPlug, faEnvelope, faBook, faCheck
} from '@fortawesome/free-solid-svg-icons';
import { faGithub, faLinkedin } from '@fortawesome/free-brands-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export default function Home(): React.ReactNode {
  return (
    <>
      {/* HERO BANNER AVEC LOGO ET BOUTONS CTA */}
      <header className={styles.hero}>
        <div className="container">
          <img src="img/logo_nexteam.png" alt="NexTeam Logo" className={styles.heroLogo} />
          <h1 className={styles.title}>NexTeam</h1>
          <p className={styles.subtitle}>
            Plateforme Intranet centralisée : Communication temps réel & Gestion RH
          </p>

          <div className={styles.heroActions}>
            <Link className="button button--primary button--lg" to="/docs/cahierDesCharges">
              <FontAwesomeIcon icon={faBook} /> Consulter la Documentation
            </Link>
            <a 
              className="button button--secondary button--lg" 
              href="https://github.com/JonatanNs/appNexTeam" 
              target="_blank" 
              rel="noopener noreferrer"
            >
              <FontAwesomeIcon icon={faGithub} /> Repository GitHub
            </a>
          </div>
        </div>
      </header>

      <main>
        {/* À PROPOS */}
        <section className={styles.section}>
          <div className="container">
            <div className={styles.sectionHeader}>
              <FontAwesomeIcon icon={faUsers} />
              <h2>À propos</h2>
            </div>

            <div className={styles.card}>
              <p>
                <strong>NexTeam</strong> est une plateforme intranet qui centralise la communication et la gestion RH
                d'une entreprise en un seul outil, là où la plupart des entreprises jonglent aujourd'hui entre
                plusieurs abonnements SaaS (Slack ou Teams pour la messagerie, un outil RH séparé pour
                les fiches de paie, un intranet distinct pour les actualités).
              </p>
              <p>
                Cette dispersion a un coût réel : plusieurs abonnements à gérer,
                des informations éparpillées entre plusieurs outils, et des équipes qui perdent du temps
                à chercher la bonne information au bon endroit.
              </p>
              <p className={styles.highlightText}>
                NexTeam répond à ce besoin avec une plateforme unique qui couvre à la fois la communication interne et
                les besoins administratifs RH — un positionnement que les outils de messagerie généralistes 
                ne couvrent pas nativement.
              </p>
            </div>
          </div>
        </section>

        {/* STACK TECHNIQUE */}
        <section className={styles.sectionAlt}>
          <div className="container">
            <div className={styles.sectionHeader}>
              <FontAwesomeIcon icon={faCode} />
              <h2>Stack technique</h2>
            </div>

            <div className={styles.grid}>
              <div className={styles.techCard}>
                <FontAwesomeIcon icon={faLaptopCode} />
                <div className={styles.techInfo}>
                  <strong>Angular</strong>
                  <span>v22</span>
                </div>
              </div>

              <div className={styles.techCard}>
                <FontAwesomeIcon icon={faServer} />
                <div className={styles.techInfo}>
                  <strong>Spring Boot</strong>
                  <span>v4.1.0</span>
                </div>
              </div>

              <div className={styles.techCard}>
                <FontAwesomeIcon icon={faDatabase} />
                <div className={styles.techInfo}>
                  <strong>PostgreSQL</strong>
                  <span>v16</span>
                </div>
              </div>

              <div className={styles.techCard}>
                <FontAwesomeIcon icon={faPlug} />
                <div className={styles.techInfo}>
                  <strong>WebSocket</strong>
                  <span>STOMP / SockJS</span>
                </div>
              </div>

              <div className={styles.techCard}>
                <FontAwesomeIcon icon={faShieldHalved} />
                <div className={styles.techInfo}>
                  <strong>Security</strong>
                  <span>JWT & BCrypt</span>
                </div>
              </div>

              <div className={styles.techCard}>
                <FontAwesomeIcon icon={faGithub} />
                <div className={styles.techInfo}>
                  <strong>CI / CD</strong>
                  <span>GitHub Actions</span>
                </div>
              </div>
            </div>
          </div>
        </section>

        {/* FONCTIONNALITÉS */}
        <section className={styles.section}>
          <div className="container">
            <div className={styles.sectionHeader}>
              <FontAwesomeIcon icon={faListCheck} />
              <h2>Fonctionnalités clés</h2>
            </div>

            <div className={styles.featureGrid}>
              <div className={styles.featureCard}>
                <FontAwesomeIcon icon={faComments} />
                <h3>Communication Instantanée</h3>
                <p>Messagerie temps réel via WebSockets, canaux de discussion et fil d'actualité d'entreprise.</p>
              </div>

              <div className={styles.featureCard}>
                <FontAwesomeIcon icon={faListCheck} />
                <h3>Organisation & Tâches</h3>
                <p>Gestion des tâches, attribution d'objectifs et suivi en direct de l'avancement des projets.</p>
              </div>

              <div className={styles.featureCard}>
                <FontAwesomeIcon icon={faShieldHalved} />
                <h3>Sécurité & Droits</h3>
                <p>Authentification JWT, gestion fine des rôles (Admin, Manager, Employé) et hashage sécurisé.</p>
              </div>

              <div className={styles.featureCard}>
                <FontAwesomeIcon icon={faIdCard} />
                <h3>Carte Employé Digitale</h3>
                <p>Profil numérique enrichi avec statuts de présence et annuaire interactif de l'entreprise.</p>
              </div>

              <div className={styles.featureCard}>
                <FontAwesomeIcon icon={faFile} />
                <h3>Espace RH & Documents</h3>
                <p>Accès centralisé aux fiches de paie, demandes de congés et avantages de l'entreprise.</p>
              </div>
            </div>
          </div>
        </section>

        {/* OBJECTIFS */}
        <section className={styles.sectionAlt}>
          <div className="container">
            <div className={styles.sectionHeader}>
              <FontAwesomeIcon icon={faBullseye} />
              <h2>Objectifs du Projet</h2>
            </div>

            <div className={styles.card}>
              <ul className={styles.goalList}>
                <li>
                  <FontAwesomeIcon icon={faCheck} className={styles.goalIcon} />
                  <span>Un seul outil à déployer et administrer, remplaçant la multiplication des abonnements SaaS.</span>
                </li>
                <li>
                  <FontAwesomeIcon icon={faCheck} className={styles.goalIcon} />
                  <span>Une communication centralisée réunissant messagerie, actualités et gestion RH sur un écran unique.</span>
                </li>
                <li>
                  <FontAwesomeIcon icon={faCheck} className={styles.goalIcon} />
                  <span>Une hiérarchie claire et paramétrable adaptée à la gouvernance de chaque organisation.</span>
                </li>
                <li>
                  <FontAwesomeIcon icon={faCheck} className={styles.goalIcon} />
                  <span>Une expérience employé optimisée avec un accès immédiat aux documents et outils du quotidien.</span>
                </li>
              </ul>
            </div>
          </div>
        </section>

        {/* AUTEUR / PROFIL */}
        <section className={styles.section}>
          <div className="container">
            <div className={styles.sectionHeader}>
              <FontAwesomeIcon icon={faUser} />
              <h2>Auteur & Concepteur</h2>
            </div>

            <div className={`${styles.card} ${styles.authorCard}`}>
              <img 
                src="https://github.com/JonatanNs.png" 
                alt="Jonatan Ns" 
                className={styles.authorAvatar} 
              />
              <div className={styles.authorContent}>
                <h3>Jonatan Ns</h3>
                <span className={styles.authorRole}>Développeur Full Stack | Java · Spring Boot · Angular · Python · IA & Automatisation</span>
              
                <div className={styles.authorLinks}>
                  <a href="https://github.com/JonatanNs" target="_blank" rel="noopener noreferrer" className={styles.authorLinkBtn}>
                    <FontAwesomeIcon icon={faGithub} /> GitHub
                  </a>
                  <a href="https://linkedin.com/in/jonatan-nsualu/" target="_blank" rel="noopener noreferrer" className={styles.authorLinkBtn}>
                    <FontAwesomeIcon icon={faLinkedin} /> LinkedIn
                  </a>
                  <a href="mailto:nsualu.jonatan@gmail.com" className={styles.authorLinkBtn}>
                    <FontAwesomeIcon icon={faEnvelope} /> Email
                  </a>
                </div>
              </div>
            </div>
          </div>
        </section>
      </main>
    </>
  );
}