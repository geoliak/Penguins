/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.Joueur;
import Modele.Partie;
import Modele.Plateau;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author liakopog
 */
public class AnnulerCoup implements Serializable {

    private ArrayList<Plateau> historique_plat = new ArrayList<>();
    private ArrayList<ArrayList<Joueur>> historique_jenj = new ArrayList<>();
    private ArrayList<Joueur> historique_jc = new ArrayList<>();
    private Partie partie;
//  private Plateau plateauDepart;

    public AnnulerCoup(Partie partie) {
	this.partie = partie;
//	this.plateauDepart = partie.getPlateau();

    }

    public void Miseajour() {
	historique_plat.add(partie.getPlateau().clone());
	historique_jenj.add((ArrayList<Joueur>) partie.getJoueursEnJeu().clone());
	try {
	    historique_jc.add((Joueur) partie.getJoueurCourant().clone());
	} catch (CloneNotSupportedException ex) {
	    Logger.getLogger(AnnulerCoup.class.getName()).log(Level.SEVERE, null, ex);
	}

    }

    void annulerDernierCoup() {
	if (historique_plat.isEmpty() || historique_jenj.isEmpty() || historique_jc.isEmpty()) {
	    System.out.println("Impossible d'annuler coup!!");
	} else {
	    System.out.println("annulation coup!");

	    partie.setPlateau(historique_plat.remove(historique_plat.size() - 1));
	    partie.setJoueursEnJeu(historique_jenj.remove(historique_jenj.size() - 1));
	    partie.setJoueurCourant(historique_jc.remove(historique_jc.size() - 1));
	}
    }
}
