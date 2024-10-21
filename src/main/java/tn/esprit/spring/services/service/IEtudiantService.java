package tn.esprit.spring.services.service;

import tn.esprit.spring.services.entity.Etudiant;

import java.util.List;

public interface IEtudiantService {

    public List<Etudiant> retrieveAllEtudiants();
    public Etudiant retrieveEtudiant(Long etudiantId);
    public Etudiant addEtudiant(Etudiant c);
    public void removeEtudiant(Long etudiantId);
    public Etudiant modifyEtudiant(Etudiant etudiant);
    public Etudiant recupererEtudiantParCin(long cin);


}
