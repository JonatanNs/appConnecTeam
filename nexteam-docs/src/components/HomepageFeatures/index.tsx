import React from 'react';
import styles from './styles.module.css';
import { faUsers, faCode, faLaptopCode, faServer, faDatabase, faShieldHalved, faListCheck, faComments, faBullseye, faUser, faIdCard, faArchive, faStoreAlt } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faFile } from '@fortawesome/free-solid-svg-icons/faFile';

export default function Home(): React.ReactNode {
  return (
    <>
      <main>

        <section className={styles.section}>
          <div className="container">

            <div className={styles.sectionHeader}>
              <FontAwesomeIcon icon={faUsers} />
              <h2>À propos</h2>
            </div>

            <div className={styles.card}>
              <p>
                NexTeam est une plateforme intranet qui centralise la communication et la gestion RH 
                d'une entreprise en un seul outil là où la plupart des entreprises jonglent aujourd'hui entre 
                plusieurs abonnements SaaS (Slack ou Teams pour la messagerie, un outil RH séparé pour 
                les fiches de paie, un intranet distinct pour les actualités).
                Cette dispersion a un coût réel : plusieurs abonnements à gérer, 
                des informations éparpillées entre plusieurs outils, et des équipes qui perdent du temps 
                à chercher la bonne information au bon endroit.
                NexTeam répond à ce besoin avec une plateforme unique qui couvre à la fois la communication interne et 
                les besoins administratifs RH un positionnement que les outils de messagerie généralistes (Slack, Teams) 
                ne couvrent pas nativement.
              </p>
            </div>

          </div>
        </section>

        <section className={styles.sectionAlt}>
          <div className="container">

            <div className={styles.sectionHeader}>
              <FontAwesomeIcon icon={faCode} />
              <h2>Stack technique</h2>
            </div>

            <div className={styles.grid}>

              <div className={styles.techCard}>
                <FontAwesomeIcon icon={faLaptopCode} />
                <span>Angular 22</span>
              </div>

              <div className={styles.techCard}>
                <FontAwesomeIcon icon={faServer} />
                <span>Spring Boot 4.1.0</span>
              </div>

              <div className={styles.techCard}>
                <FontAwesomeIcon icon={faDatabase} />
                <span>PostgreSQL 16</span>
              </div>

              <div className={styles.techCard}>
                <FontAwesomeIcon icon={faArchive} />
                <span>Docker</span>
              </div>

              <div className={styles.techCard}>
                <FontAwesomeIcon icon={faFile} />
                <span>GitHub</span>
              </div>

              

            </div>

          </div>
        </section>

        <section className={styles.section}>
          <div className="container">

            <div className={styles.sectionHeader}>
              <FontAwesomeIcon icon={faListCheck} />
              <h2>Fonctionnalités</h2>
            </div>

            <div className={styles.featureGrid}>

              <div className={styles.featureCard}>
                <FontAwesomeIcon icon={faComments} />
                <h3>Communication</h3>
                <p>Messagerie temps réel avec WebSocket et fil d'actualité.</p>
              </div>

              <div className={styles.featureCard}>
                <FontAwesomeIcon icon={faListCheck} />
                <h3>Organisation</h3>
                <p>Gestion des tâches et suivi des missions.</p>
              </div>

              <div className={styles.featureCard}>
                <FontAwesomeIcon icon={faShieldHalved} />
                <h3>Sécurité</h3>
                <p>JWT, rôles utilisateurs et BCrypt.</p>
              </div>

              <div className={styles.featureCard}>
                <FontAwesomeIcon icon={faIdCard} />
                <h3>Carte</h3>
                <p>Carte employé numérique.</p>
              </div>

              <div className={styles.featureCard}>
                <FontAwesomeIcon icon={faFile} />
                <h3>Document contractuel</h3>
                <p>Accès aux fiches de paie et avantages.</p>
              </div>

            </div>

          </div>
        </section>

        <section className={styles.sectionAlt}>
          <div className="container">

            <div className={styles.sectionHeader}>
              <FontAwesomeIcon icon={faBullseye} />
              <h2>Objectif</h2>
            </div>

            <div className={styles.card}>
              <p>Un seul outil à déployer et administrer, au lieu de plusieurs abonnements SaaS.</p>
              <p>Une communication centralisée : messagerie temps réel, actualités, tâches, RH au même endroit.</p>
              <p>Une gestion fine des rôles et permissions, adaptée à la hiérarchie de l'entreprise.</p>
              <p>Une expérience employé simplifiée : carte employé numérique, fiches de paie et avantages accessibles directement depuis l'outil de communication quotidien.</p>
            </div>

          </div>
        </section>

        <section className={styles.section}>
          <div className="container">

            <div className={styles.sectionHeader}>
              <FontAwesomeIcon icon={faUser} />
              <h2>Auteur</h2>
            </div>

            <div className={styles.card}>
              <p>
                <strong>Jonatan Ns</strong><br />
                Développeur Full Stack 
              </p>
            </div>

          </div>
        </section>

      </main>
    </>
  );
}