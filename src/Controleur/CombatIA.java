/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.Case;
import Modele.Couleur;
import Modele.Joueur;
import Modele.IA.JoueurIA1;
import Modele.IA.JoueurIA2;
import Modele.IA.JoueurIA3;
import Modele.IA.JoueurIA4;
import Modele.IA.JoueurIA5;
import Modele.IA.JoueurIA6;
import Modele.IA.JoueurIA8;
import Modele.Partie;
import Modele.Pinguin;
import Modele.Plateau;
import Vue.DessinateurTexte;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mathias
 */
public class CombatIA {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

	int nbCombats = 5000;
	HashMap<Joueur, Integer> victoires = new HashMap<>();

	DessinateurTexte dt = new DessinateurTexte();

	JoueurIA1 joueurIA1 = new JoueurIA1(Couleur.Rouge);
	JoueurIA2 joueurIA2 = new JoueurIA2(Couleur.Jaune);
	JoueurIA3 joueurIA3 = new JoueurIA3(Couleur.Vert);
	JoueurIA4 joueurIA4 = new JoueurIA4(Couleur.Bleu);

	JoueurIA5 joueurIA5 = new JoueurIA5(Couleur.Rouge);
	JoueurIA6 joueurIA6 = new JoueurIA6(Couleur.Jaune);
        JoueurIA8 joueurIA8 = new JoueurIA8(Couleur.Jaune);

	ArrayList<Joueur> joueurs = new ArrayList<>();
	joueurs.add(joueurIA8);
	joueurs.add(joueurIA1);

	for (Joueur j : joueurs) {
	    victoires.put(j, 0);
	}

	int nbPinguin = 0;
	switch (joueurs.size()) {
	    case 2:
		nbPinguin = 4;
		break;
	    case 3:
		nbPinguin = 3;
		break;
	    case 4:
		nbPinguin = 2;
		break;
	}

	int l = 0;
	while (l < nbCombats) {
	    System.out.println("\n========== " + l);
	    Plateau plateau = null;
	    try {
		plateau = new Plateau("ressources/plateaux/plateau1");
	    } catch (IOException ex) {
		Logger.getLogger(CombatIA.class.getName()).log(Level.SEVERE, null, ex);
		System.exit(-1);
	    }

	    System.out.println(nbCombats);
	    Partie partie = new Partie(plateau, joueurs);

            //plateau.accept(dt);
	    //Placement des pinguins
	    Boolean pinguinPlace = false;
	    int numLigne = -1;
	    int numColonne = 1;
	    Case caseChoisie;
	    Joueur joueurCourant = null;

	    for (int i = 0; i < nbPinguin * (joueurs.size()); i++) {
		joueurCourant = partie.getJoueurCourant();
		pinguinPlace = false;
		while (!pinguinPlace) {
		    caseChoisie = joueurCourant.etablirCoup(partie);
		    numLigne = caseChoisie.getNumLigne();
		    numColonne = caseChoisie.getNumColonne();

		    if (plateau.estCaseLibre(numLigne, numColonne) && plateau.getCases()[numLigne][numColonne].getNbPoissons() == 1) {
			joueurCourant.ajouterPinguin(plateau.getCases()[numLigne][numColonne]);
			pinguinPlace = true;
		    } else {
			System.out.println("Cette case est occupée ou coulé ou n'a pas un poisson");
		    }
		}
		//plateau.accept(dt);
		partie.joueurSuivant();
	    }
	    partie.getJoueurCourant().setPret(true);
	    for (Joueur j : joueurs) {
		j.setPret(true);
	    }
	    partie.setInitialisation(false);

	    //Jeu
	    boolean aJoue;
	    ArrayList<Case> casesPossibles = new ArrayList<>();
	    while (!partie.estTerminee()) {
		aJoue = false;
		while (!aJoue) {
		    joueurCourant = partie.getJoueurCourant();

		    //On tue les pinguins qui n'ont plus de cases accessibles
		    for (Pinguin p : joueurCourant.getPinguinsVivants()) {
			casesPossibles = p.getPosition().getCasePossibles();
			if (casesPossibles.isEmpty()) {
			    p.coullePinguin();
			}
		    }

		    //Si le joueur n'est pas elimine
		    if (joueurCourant.estEnJeu()) {
			joueurCourant.joueCoup(joueurCourant.etablirCoup(partie));
			aJoue = true;
		    } else {
			aJoue = true;
		    }

		}
		plateau.accept(dt);
		partie.joueurSuivant();
	    }

	    //partie.afficheResultats();
	    for (Joueur j : partie.getJoueurGagnant()) {
		victoires.put(j, victoires.get(j) + 1);
	    }

	    System.out.println("==========" + l);
	    partie.afficheResultats();

	    //Reinitialisation des joueurs
	    for (Joueur j : joueurs) {
		j.reset();
	    }
	    partie.getJoueurCourant().reset();

	    l++;
	}

	System.out.println("=====================================");
	System.out.println("======= RESULTATS DES COMBATS =======");
	System.out.println("=====================================");
	for (Joueur j : victoires.keySet()) {
	    System.out.println(j.getCouleur() + j.getNom() + Couleur.ANSI_RESET + " -> " + victoires.get(j) + " partie(s) remportée(s)\t (" + (float) ((float) victoires.get(j) / nbCombats) * 100 + "%)");
	}

    }
}
