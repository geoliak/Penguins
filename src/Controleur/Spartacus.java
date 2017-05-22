/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.Couleur;
import Modele.IA.EnumIA;
import Modele.IA.GenereIA;
import Modele.IA.JoueurIA;
import Modele.IA.JoueurIA4;
import Modele.IA.JoueurIA5;
import Modele.IA.JoueurIA6;
import Modele.IA.JoueurIA7;
import Modele.IA.JoueurIA8;
import Modele.Joueur;
import Modele.Tournoi;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Mathias
 */
public class Spartacus {

    public static void main(String[] args) {
	GenereIA genereIA = new GenereIA();
	int tailleGroupe = 30;
	int generation = 0;
	Tournoi theArena;
	ArrayList<Joueur> groupeCourant = genereIA.genererGroupeIndividu(tailleGroupe, generation, 0);
	
	ArrayList<Joueur> resultats;

	Scanner sc = new Scanner(System.in);
	System.out.println("Combien de generation ? ");
	int nbGeneration = sc.nextInt();
        
        
	while (generation < nbGeneration) {

	    theArena = new Tournoi(1);
	    for (Joueur j : groupeCourant) {
		theArena.ajouterIA(j);
	    }
            
	    theArena.executerLesCombats(true, false, false);
	    resultats = theArena.getAfficheCroissantDeux();
            
	    System.out.println("Generation " + generation);
	    theArena.afficheResultats(true, false, false);

	    groupeCourant = genereIA.nouvelleGeneration(resultats, tailleGroupe, generation);
            
            
            
	    generation++;
	}

	theArena = new Tournoi(20);
	theArena.ajouterIA(new JoueurIA5(Couleur.Rouge, 1));
	theArena.ajouterIA(new JoueurIA6(Couleur.Jaune, 2));
	theArena.ajouterIA(new JoueurIA7(Couleur.Vert, 3));
	theArena.ajouterIA(new JoueurIA8(Couleur.Bleu, 4));
	theArena.ajouterIA(groupeCourant.get(0));
	theArena.ajouterIA(groupeCourant.get(1));
	theArena.ajouterIA(groupeCourant.get(2));
	theArena.ajouterIA(groupeCourant.get(3));

	theArena.executerLesCombats(true, false, false);

	theArena.afficheResultats(true, false, false);
    }
}
