package com.example.jpa.service;

import com.example.jpa.dao.SalleDao;
import com.example.jpa.dao.UtilisateurDao;
import com.example.jpa.entities.Salle;
import com.example.jpa.entities.Utilisateur;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class ServiceGestion {

    private final EntityManagerFactory emf;
    private final EntityManager em;
    private final SalleDao salleDao;
    private final UtilisateurDao utilisateurDao;

    public ServiceGestion() {
        emf = Persistence.createEntityManagerFactory("gestionPU");
        em = emf.createEntityManager();
        salleDao = new SalleDao(em);
        utilisateurDao = new UtilisateurDao(em);
    }

    public void fermer() {
        em.close();
        emf.close();
    }

    public void ajouterSalle(Salle salle) { salleDao.create(salle); }

    public void ajouterUtilisateur(Utilisateur utilisateur) { utilisateurDao.create(utilisateur); }

    public List<Salle> listerSalles() { return salleDao.findAll(); }

    public List<Utilisateur> listerUtilisateurs() { return utilisateurDao.findAll(); }

    public List<Utilisateur> utilisateursParSalle(String nomSalle) {
        return utilisateurDao.findBySalleNom(nomSalle);
    }
}