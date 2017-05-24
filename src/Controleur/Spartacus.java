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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        ArrayList<Joueur> resultats = null;
        ArrayList<Joueur> adversaire = new ArrayList<>();
        adversaire.add(new JoueurIA5(Couleur.Rouge, 1));
        adversaire.add(new JoueurIA6(Couleur.Jaune, 2));
        adversaire.add(new JoueurIA7(Couleur.Vert, 3));
        adversaire.add(new JoueurIA8(Couleur.Bleu, 4));

        Scanner sc = new Scanner(System.in);
        System.out.println("Combien de generation ? ");
        int nbGeneration = sc.nextInt();

        while (generation <= nbGeneration) {
            System.out.println("\n\nGeneration " + generation);
            theArena = new Tournoi(10);
            for (Joueur j : groupeCourant) {
                theArena.ajouterIA(j);
            }
            theArena.ajouterIA(adversaire);

            theArena.executerLesCombats(true, false, false);
            theArena.afficheResultats(true, false, false);
            resultats = (ArrayList<Joueur>) theArena.getAfficheCroissantDeux().clone();
            resultats.removeAll(adversaire);

            generation++;
            groupeCourant = genereIA.nouvelleGeneration(resultats, tailleGroupe, generation);
        }

        theArena = new Tournoi(30);
        theArena.ajouterIA(adversaire);
        theArena.ajouterIA(resultats.get(0));
        theArena.ajouterIA(resultats.get(1));
        theArena.ajouterIA(resultats.get(2));
        theArena.ajouterIA(resultats.get(3));

        theArena.executerLesCombats(true, false, false);

        theArena.afficheResultats(true, false, false);

        try {
            theArena.sortirResultat();
        } catch (IOException ex) {
            Logger.getLogger(Spartacus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
