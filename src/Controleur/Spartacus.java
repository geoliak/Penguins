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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Mathias
 */
public class Spartacus {

    public static void main(String[] args) {
        GenereIA genereIA = new GenereIA(new JoueurIA(Couleur.Rouge, "Dans ta gueule", 1));
        int tailleGroupe = 20;
        int generation = 0;
        Tournoi theArena;
        ArrayList<Joueur> Anciengroupe = null, groupeCourant = genereIA.genererGroupeIndividu(tailleGroupe, generation, 0);
        generation++;
        ArrayList<Joueur> resultats;

        Scanner sc = new Scanner(System.in);
        System.out.println("Combien de generation ? ");
        int nbGeneration = sc.nextInt();
        while (generation < nbGeneration) {

            theArena = new Tournoi(10);
            for (Joueur j : groupeCourant) {
                theArena.ajouterIA(j);
            }
            theArena.executerLesCombats(true, false, false);
            resultats = theArena.getAfficheCroissantDeux();
            System.out.println("Generation " + generation);
            theArena.afficheResultats(true, false, false);

            Anciengroupe = groupeCourant;
            groupeCourant = new ArrayList<>();
            //On conserve les 10% meilleurs
            groupeCourant.addAll(resultats.subList(0, (int) (tailleGroupe * 0.1)));
            //On fait reproduire les 50% meileurs
            for (int i = 0; i < tailleGroupe; i += 2) {
                groupeCourant.add(((EnumIA) Anciengroupe.get(i)).reproduction(((EnumIA) Anciengroupe.get(i + 1)), i + (int) (tailleGroupe * 0.1)));
            }
            //Le reste est regenere aleatoirement
            groupeCourant.addAll(genereIA.genererGroupeIndividu(tailleGroupe, generation, (int) (tailleGroupe * 0.60)));
            generation++;
        }

        theArena = new Tournoi(20);
        theArena.ajouterIA(new JoueurIA5(Couleur.Rouge, 1));
        theArena.ajouterIA(new JoueurIA6(Couleur.Jaune, 2));
        theArena.ajouterIA(new JoueurIA7(Couleur.Vert, 3));
        theArena.ajouterIA(new JoueurIA8(Couleur.Bleu, 4));
        theArena.ajouterIA(Anciengroupe.get(0));
        theArena.ajouterIA(Anciengroupe.get(1));
        theArena.ajouterIA(Anciengroupe.get(2));
        theArena.ajouterIA(Anciengroupe.get(3));

        theArena.executerLesCombats(true, true, true);

        theArena.afficheResultats(true, true, true);
    }
}
