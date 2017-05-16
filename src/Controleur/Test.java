/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.Case;
import Modele.Couleur;
import Modele.Joueur;
import Modele.JoueurHumainLocal;
import Modele.IA.JoueurIA5;
import Modele.Partie;
import Modele.Pinguin;
import Modele.Plateau;
import Vue.DessinateurTexte;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author novelm
 */
public class Test {

    public static void main(String args[]) {
	System.out.println("======= Pour l'Antartique ! =======");
	try {
	    Plateau plateau = new Plateau("ressources/plateaux/plateau1");
	    DessinateurTexte dt = new DessinateurTexte();

	    JoueurHumainLocal joueurH1 = new JoueurHumainLocal("Jean", Couleur.Bleu, 1);
	    JoueurHumainLocal joueurH2 = new JoueurHumainLocal("Pierre", Couleur.Rouge, 2);
	    JoueurIA5 joueurIA5 = new JoueurIA5(Couleur.Rouge, 3);
	    ArrayList<Joueur> joueurs = new ArrayList<>();
	    joueurs.add(joueurH1);
	    joueurs.add(joueurIA5);

	    int nbPinguin = 0;
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
	    System.out.println(nbPinguin + " pinguins par joueur");

	    Partie partie = new Partie(plateau, joueurs);

	    plateau.accept(dt);
	    //Placement des pinguins
	    Boolean pinguinPlace = false;
	    int numLigne = -1;
	    int numColonne = 1;
	    int[] res = new int[2];
	    Case caseChoisie;
	    Scanner sc = new Scanner(System.in);
	    Joueur joueurCourant;

	    for (int i = 0; i < nbPinguin * (joueurs.size() + 1); i++) {
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
		plateau.accept(dt);
		partie.joueurSuivant();
	    }
	    partie.getJoueurCourant().setPret(true);
	    for (Joueur j : joueurs) {
		j.setPret(true);
	    }
	    partie.setInitialisation(false);

	    //Jeu
	    boolean aJoue;
	    Case s, d;
	    ArrayList<Case> casesPossibles;
	    HashMap<Pinguin, ArrayList<Case>> memoisation = new HashMap<>();
	    while (!partie.estTerminee()) {
		aJoue = false;
		while (!aJoue) {
		    joueurCourant = partie.getJoueurCourant();

		    for (Pinguin p : joueurCourant.getPinguins()) {
			casesPossibles = p.getPosition().getCasePossibles();
			memoisation.put(p, casesPossibles);
			if (casesPossibles.isEmpty()) {
			    p.coullePinguin();
			}
		    }

		    //Si le joueur n'est pas elimine
		    if (joueurCourant.estEnJeu()) {

			//Si le joueur est humain
			if (joueurCourant.getEstHumain()) {
			    System.out.println("== Veuillez selectionner un pinguin ");
			    s = joueurCourant.etablirCoup(partie);

			    //Si case libre
			    if (s.getPinguin() != null && s.getPinguin().getGeneral() == joueurCourant) {
				casesPossibles = memoisation.get(s.getPinguin());
				plateau.surligneCases(casesPossibles);
				plateau.accept(dt);
				plateau.desurligneCases(casesPossibles);

				System.out.print("Veuillez Selectionner une case ");
				d = joueurCourant.etablirCoup(partie);

				//Si case accessible
				if (casesPossibles.contains(d)) {
				    joueurCourant.setPinguinCourant(s.getPinguin());
				    joueurCourant.joueCoup(d);
				    aJoue = true;
				    //Si choix de case inaccessible
				} else {
				    System.out.println("Veuillez choisir une case accessible");
				}
				//Si le joueur n'a pas selectionne de pinguin
			    } else {
				System.out.println("Choississez un de vos pinguin");
			    }

			    //Si IA
			} else {
			    joueurCourant.joueCoup(joueurCourant.etablirCoup(partie));
			    aJoue = true;
			}
		    } else {
			aJoue = true;
		    }

		}
		plateau.accept(dt);
		partie.joueurSuivant();
	    }

	    partie.afficheResultats();

	} catch (IOException ex) {
	    System.out.println("Erreur d'ouverture du fichier");
	    Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
}
