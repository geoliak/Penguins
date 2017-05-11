/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import Controleur.AnnulerCoup;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author novelm
 */
public class Partie implements Serializable {

    private Plateau plateau;
    private ArrayList<Joueur> joueurs;
    private ArrayList<Joueur> joueursEnJeu;
    private Joueur joueurCourant;
    private Boolean initialisation;
    private int nbPingouinParJoueur;
    private AnnulerCoup histcoup;

    public Partie(Plateau plateau, ArrayList<Joueur> joueurs) {
	this.initialisation = true;
	this.plateau = plateau;
	this.joueurs = joueurs;

	this.joueursEnJeu = (ArrayList<Joueur>) joueurs.clone();
	this.joueurCourant = joueursEnJeu.remove(0);

	//Ne pas mettre après remove joueur
	setNbPingouinParJoueur();
    }

    public int nbPingouinsTotal() {
	int nb = 0;
	for (Joueur j : joueurs) {
	    nb += j.getPinguins().size();
	}
	return nb;
    }

    public void setNbPingouinParJoueur() {
	int nbPinguin = 0;
	//System.out.println("JOUEURS SIZE: " + joueurs.size());
	switch (joueurs.size()) {
	    case 2:
		nbPinguin = 2;
		break;
	    case 3:
		nbPinguin = 3;
		break;
	    case 4:
		nbPinguin = 2;
		break;
	}
	nbPingouinParJoueur = nbPinguin;
    }

    public void joueurSuivant() {
	this.joueursEnJeu.add(this.joueurCourant);
	this.joueurCourant = this.joueursEnJeu.remove(0);
	if (!this.initialisation && !this.estTerminee() && !this.joueurCourant.estEnJeu()) {
	    joueurSuivant();
	}

	if (joueurCourant.getEstHumain()) {
	    histcoup.sauvegarderCoup();
	    if (this.joueurCourant.getPinguinsVivants().size() == 1 && !this.initialisation) {
		this.joueurCourant.setPinguinCourant(this.joueurCourant.getPinguinsVivants().get(0));
	    } else {
		this.joueurCourant.setPinguinCourant(null);
	    }
	}
    }

    public void afficheJoueurs() {
	System.out.println("Joueur courant : " + joueurCourant.getCouleur() + joueurCourant.getNom() + Couleur.ANSI_RESET + " enJeu " + this.joueurCourant.estEnJeu());
	System.out.println("Joueur(s) dans la liste : ");
	for (Joueur j : this.joueursEnJeu) {
	    System.out.println(j.getCouleur() + j.getNom() + Couleur.ANSI_RESET + " enJeu " + j.estEnJeu());
	}
    }

    public Joueur getJoueurCourant() {
	return joueurCourant;
    }

    public Boolean estTerminee() {
	Boolean res = joueurCourant.estEnJeu();
	for (Joueur j : this.joueursEnJeu) {
	    res = res || j.estEnJeu();
	}
	return !res;
    }

    public ArrayList<Joueur> getJoueurGagnant() {
	ArrayList<Joueur> gagnants = new ArrayList<>();
	gagnants.add(joueurCourant);
	for (Joueur j : this.joueursEnJeu) {
	    //Si un joueur à un meilleur score qu'un autre
	    if (j.getScorePoissons() > gagnants.get(0).getScorePoissons()) {
		gagnants = new ArrayList<>();
		gagnants.add(j);
		//Si plusieurs Joueurs ont le meme code
	    } else if (j.getScorePoissons() == gagnants.get(0).getScorePoissons()) {
		if (j.getScoreGlacons() > gagnants.get(0).getScoreGlacons()) {
		    gagnants = new ArrayList<>();
		    gagnants.add(j);
		} else if (j.getScoreGlacons() == gagnants.get(0).getScoreGlacons()) {
		    gagnants.add(j);
		}
	    }
	}
	return gagnants;
    }

    public void afficheResultats() {
	if (this.estTerminee()) {
	    this.joueursEnJeu.add(joueurCourant);
	    for (Joueur j : this.getJoueurGagnant()) {
		System.out.println(j.getCouleur() + j.getNom() + Couleur.ANSI_RESET + " a gagne la partie");
	    }
	    for (Joueur j : this.joueursEnJeu) {
		System.out.println(j.getCouleur() + j.getNom() + Couleur.ANSI_RESET + " => " + j.getScorePoissons() + "," + j.getScoreGlacons());
	    }
	}
    }

    public Plateau getPlateau() {
	return plateau;
    }

    public Boolean sauvegarde() {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Boolean estEnInitialisation() {
	return initialisation;
    }

    public void setInitialisation(boolean initialisation) {
	this.initialisation = initialisation;
    }

    public ArrayList<Joueur> getJoueurs() {
	return joueurs;
    }

    public Boolean getInitialisation() {
	return initialisation;
    }

    public int getNbPingouinParJoueur() {
	//System.out.println("NOMBRE DE PINGOUINS PAR JOUEUR:" + nbPingouinParJoueur);
	return nbPingouinParJoueur;
    }

    public ArrayList<Joueur> getJoueursEnJeu() {
	return joueursEnJeu;
    }

    public void setJoueursEnJeu(ArrayList<Joueur> joueursEnJeu) {
	this.joueursEnJeu = joueursEnJeu;
    }

    public void setJoueurCourant(Joueur joueurCourant) {
	this.joueurCourant = joueurCourant;
    }

    public void setPlateau(Plateau plateau) {
	this.plateau = plateau;
    }

    public void setJoueurs(ArrayList<Joueur> joueurs) {
	this.joueurs = joueurs;
    }

    public void setAc(AnnulerCoup histcoup) {
	this.histcoup = histcoup;
    }

}
