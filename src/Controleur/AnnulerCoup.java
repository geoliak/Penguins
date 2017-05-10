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
	System.out.println(partie.getPlateau());
	Plateau s = partie.getPlateau();
	historique_plat.add(s);
	historique_jenj.add(partie.getJoueursEnJeu());
	historique_jc.add(partie.getJoueurCourant());
    }

    void annulerDernierCoup() {
	if (historique_plat.isEmpty() || historique_jenj.isEmpty() || historique_jc.isEmpty()) {
	    System.err.println("Impossible d'annuler coup");
	} else {
	    System.err.println("possible d'annuler coup");

	    partie.setPlateau(historique_plat.remove(historique_plat.size() - 1));
	    partie.setJoueursEnJeu(historique_jenj.remove(historique_jenj.size() - 1));
	    partie.setJoueurCourant(historique_jc.remove(historique_jc.size() - 1));
	}
    }
}
