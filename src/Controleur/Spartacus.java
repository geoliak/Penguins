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
        GenereIA genereIA = new GenereIA(new JoueurIA(Couleur.Rouge, "Dans ta gueule"));
        int tailleGroupe = 70;
        int generation = 0;
        ArrayList<Joueur> Anciengroupe , groupeCourant = genereIA.genererGroupeIndividu(tailleGroupe, generation);
        generation++;
        ArrayList<Joueur> resultats;

        Scanner sc = new Scanner(System.in);
        System.out.println("Continue ?");
        String lecture = sc.nextLine();

        while (!lecture.equals("non")) {
            
            Tournoi theArena = new Tournoi(10);
            for (Joueur j : groupeCourant) {
                theArena.ajouterIA(j);
            }
            theArena.executerLesCombats(true, false, false);
            resultats = theArena.getAfficheCroissantDeux();
            theArena.afficheResultats(true, false, false);
            
            
            Anciengroupe = groupeCourant;
            groupeCourant = new ArrayList<>(); 
            //On conserve les 10% meilleurs
            groupeCourant.addAll(resultats.subList(0, (int) (tailleGroupe * 0.1)));
            //On fait reproduire les 50% meileurs
            for (int i = 0; i < (int) (tailleGroupe * 0.5) / 2; i+=2) {
                groupeCourant.add(((EnumIA) Anciengroupe.get(i)).reproduction(((EnumIA) Anciengroupe.get(i + 1))));
            }
            //Le reste est regenere aleatoirement
            groupeCourant.addAll(genereIA.genererGroupeIndividu((int) (tailleGroupe * 0.65), generation));
            generation++;
            
            lecture = sc.nextLine();
            System.out.println("===================\nContinue ?");
        }

    }
}
