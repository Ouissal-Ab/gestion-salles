package com.example.jpa.test;

import com.example.jpa.entities.Salle;
import com.example.jpa.entities.Utilisateur;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class JpqlTest {

    private EntityManagerFactory emf;
    private EntityManager em;

    @Before
    public void setUp() {
        emf = Persistence.createEntityManagerFactory("gestionPU");
        em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Salle petite = new Salle("Petite", 10);
        Salle grande = new Salle("Grande", 50);
        em.persist(petite);
        em.persist(grande);
        em.persist(new Utilisateur("Jean Dupont", "jean@example.com", grande));
        em.persist(new Utilisateur("Alice Martin", "alice@example.com", petite));
        em.persist(new Utilisateur("Marie-Jean Durand", "mj@example.com", grande));
        tx.commit();
    }

    @After
    public void tearDown() {
        em.close();
        emf.close();
    }

    @Test
    public void testSallesCapaciteSuperieure() {
        List<Salle> grandesSalles = em.createQuery(
                        "SELECT s FROM Salle s WHERE s.capacite > :minCapacite", Salle.class)
                .setParameter("minCapacite", 20)
                .getResultList();

        assertEquals(1, grandesSalles.size());
        assertEquals("Grande", grandesSalles.get(0).getNom());
    }

    @Test
    public void testUtilisateursNomContient() {
        List<Utilisateur> utilisateurs = em.createQuery(
                        "SELECT u FROM Utilisateur u WHERE u.nom LIKE :pattern", Utilisateur.class)
                .setParameter("pattern", "%Jean%")
                .getResultList();

        assertEquals(2, utilisateurs.size());
    }

    @Test
    public void testJointureImplicite() {
        List<Utilisateur> utilisateurs = em.createQuery(
                        "SELECT u FROM Utilisateur u WHERE u.salle.nom = :nom", Utilisateur.class)
                .setParameter("nom", "Grande")
                .getResultList();

        assertEquals(2, utilisateurs.size());
    }
}