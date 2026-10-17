package com.example.jpa;

import com.example.jpa.entities.Salle;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class App {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestionPU");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();


        Salle salle = new Salle("Salle A", 10);
        try {
            tx.begin();
            em.persist(salle);
            tx.commit();
            System.out.println(">>> COMMIT réussi : " + salle);
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        }


        Salle trouvee = em.find(Salle.class, salle.getId());
        System.out.println(">>> Même instance dans le contexte de persistance ? " + (trouvee == salle));


        try {
            tx.begin();
            em.persist(new Salle("Salle A", 99));
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            System.out.println(">>> ROLLBACK effectué : " + e.getClass().getSimpleName());
        } finally {
            em.close();
            emf.close();
            System.out.println(">>> EntityManager et EntityManagerFactory fermés");
        }
    }
}