package com.example.jpa.test;

import com.example.jpa.dao.SalleDao;
import com.example.jpa.entities.Salle;
import com.example.jpa.entities.Utilisateur;
import com.example.jpa.service.ServiceGestion;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.Assert.*;

public class ServiceGestionTest {

    private ServiceGestion service;

    @Before
    public void setUp() {
        service = new ServiceGestion();
    }

    @After
    public void tearDown() {
        service.fermer();
    }

    @Test
    public void testCreationEtRechercheSalle() {
        Salle salle = new Salle("Salle Test", 20);
        service.ajouterSalle(salle);

        List<Salle> salles = service.listerSalles();
        assertFalse(salles.isEmpty());
        assertEquals("Salle Test", salles.get(0).getNom());
    }

    @Test
    public void testCreationUtilisateurAvecSalle() {
        Salle salle = new Salle("Salle B", 15);
        service.ajouterSalle(salle);

        Utilisateur utilisateur = new Utilisateur("Alice", "alice@example.com", salle);
        service.ajouterUtilisateur(utilisateur);

        List<Utilisateur> utilisateurs = service.listerUtilisateurs();
        assertFalse(utilisateurs.isEmpty());
        assertEquals("Alice", utilisateurs.get(0).getNom());
        assertEquals("Salle B", utilisateurs.get(0).getSalle().getNom());
    }

    @Test
    public void testRechercheUtilisateursParSalle() {
        Salle salle = new Salle("Salle C", 30);
        service.ajouterSalle(salle);

        service.ajouterUtilisateur(new Utilisateur("Bob", "bob@example.com", salle));
        service.ajouterUtilisateur(new Utilisateur("Carol", "carol@example.com", salle));

        List<Utilisateur> utilisateurs = service.utilisateursParSalle("Salle C");
        assertEquals(2, utilisateurs.size());
    }

    @Test(expected = RuntimeException.class)
    public void testNomSalleUnique() {
        service.ajouterSalle(new Salle("Doublon", 10));
        service.ajouterSalle(new Salle("Doublon", 20)); // violation de la contrainte unique
    }

    @Test
    public void testFindByCapaciteMin() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestionPU");
        EntityManager em = emf.createEntityManager();
        SalleDao dao = new SalleDao(em);

        dao.create(new Salle("Petite", 5));
        dao.create(new Salle("Grande", 50));

        List<Salle> grandes = dao.findByCapaciteMin(20);
        assertEquals(1, grandes.size());
        assertEquals("Grande", grandes.get(0).getNom());

        em.close();
        emf.close();
    }
}