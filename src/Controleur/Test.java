/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.Case;
import Modele.Joueur;
import Modele.JoueurHumainLocal;
import Modele.Partie;
import Modele.Pinguin;
import Modele.Plateau;
import Vue.DessinateurTexte;
import java.awt.Color;
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
            Plateau plateau = new Plateau("plateau2");
            DessinateurTexte dt = new DessinateurTexte();

            JoueurHumainLocal joueurH1 = new JoueurHumainLocal(45, "Jean", Joueur.ANSI_BLUE_BACKGROUND, Joueur.ANSI_BLUE);
            JoueurHumainLocal joueurH2 = new JoueurHumainLocal(47, "Pierre", Joueur.ANSI_RED_BACKGROUND, Joueur.ANSI_RED);
            ArrayList<Joueur> joueurs = new ArrayList<>();
            joueurs.add(joueurH1);
            joueurs.add(joueurH2);

            int nbPinguin = 0;
            switch (joueurs.size()) {
                case 2:
                    nbPinguin = 1;
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
            Scanner sc = new Scanner(System.in);
            Joueur joueurCourant;

            for (int i = 0; i < nbPinguin * (joueurs.size() + 1); i++) {
                joueurCourant = partie.getJoueurCourant();
                pinguinPlace = false;
                while (!pinguinPlace) {
                    System.out.println("\n== Joueur " + joueurCourant.getNom());
                    System.out.print("numero ligne : ");
                    numLigne = sc.nextInt();
                    System.out.print("numero colonne : ");
                    numColonne = sc.nextInt();
                    if (plateau.estCaseLibre(numLigne, numColonne)) {
                        joueurCourant.ajouterPinguin(plateau.getCases()[numLigne][numColonne]);
                        pinguinPlace = true;
                    } else {
                        System.out.println("Cette case est occupée ou coulé");
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
            ArrayList<Case> casesPossibles = new ArrayList<>();
            HashMap<Pinguin, ArrayList<Case>> memoisation = new HashMap<>();
            while (!partie.estTerminee()) {
                aJoue = false;
                while (!aJoue) {
                    joueurCourant = partie.getJoueurCourant();
                    for (Pinguin p : joueurCourant.getPinguins()) {
                        casesPossibles = p.getCasePossibles();
                        memoisation.put(p, casesPossibles);
                        if (casesPossibles.isEmpty()) {
                            p.coullePinguin();
                        }
                    }

                    if (joueurCourant.estEnJeu()) {
                        System.out.println("\n== Choisi un pinguin " + joueurCourant.getColorAccessible() + joueurCourant.getNom() + Joueur.ANSI_RESET);
                        System.out.print("numero ligne : ");
                        numLigne = sc.nextInt();
                        System.out.print("numero colonne : ");
                        numColonne = sc.nextInt();

                        s = plateau.getCases()[numLigne][numColonne];
                        if (s.getPinguin() != null && s.getPinguin().getGeneral() == joueurCourant) {
                            casesPossibles = memoisation.get(s.getPinguin());
                            for (Case cp : casesPossibles) {
                                cp.setAccessible(true);
                            }
                            plateau.accept(dt);
                            for (Case cp : casesPossibles) {
                                cp.setAccessible(false);
                            }
                            System.out.println("\n== Choisi une case " + joueurCourant.getColorAccessible() + joueurCourant.getNom() + Joueur.ANSI_RESET);
                            System.out.print("numero ligne : ");
                            numLigne = sc.nextInt();
                            System.out.print("numero colonne : ");
                            numColonne = sc.nextInt();
                            d = plateau.getCases()[numLigne][numColonne];
                            if (casesPossibles.contains(d)) {
                                joueurCourant.setPinguinCourant(s.getPinguin());
                                joueurCourant.joueCoup(d);
                                aJoue = true;
                            } else {
                                System.out.println("Veuillez choisir une case accessible");
                            }
                        } else {
                            System.out.println("Choississez un de vos pinguin");
                        } 
                    } else {
                        aJoue = true;
                    }

                }
                plateau.accept(dt);
                partie.joueurSuivant();
            }

            for (Joueur j : partie.getJoueurGagnant()) {
                System.out.println(j.getColorAccessible() + j.getNom() + Joueur.ANSI_RESET + " a gagne la partie");
            }

        } catch (IOException ex) {
            System.out.println("Erreur d'ouverture du fichier");
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
