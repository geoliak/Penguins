/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.Joueur;
import Modele.JoueurHumainLocal;
import Modele.Partie;
import Modele.Pinguin;
import Modele.Plateau;
import Vue.DessinateurTexte;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
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
            Plateau plateau = new Plateau("plateau1");
            DessinateurTexte dt = new DessinateurTexte();
            
            JoueurHumainLocal joueurH1 = new JoueurHumainLocal(45, "Jean",Joueur.ANSI_RED);
            JoueurHumainLocal joueurH2 = new JoueurHumainLocal(47, "Pierre",Joueur.ANSI_GREEN);
            ArrayList<Joueur> joueurs = new ArrayList<>();
            joueurs.add(joueurH1);
            joueurs.add(joueurH2);
            
            int nbPinguin = 0;
            switch (joueurs.size()) {
                case 2:
                    nbPinguin = 4;
                    break;
                case 3 :
                    nbPinguin = 3;
                case 4 :
                    nbPinguin = 2;
            }
            
            Partie partie = new Partie(plateau, joueurs);
            partie.joueurSuivant();
            
            //Placement des pinguins
            Boolean pinguinPlace = false;
            int numLigne = -1;
            int numColonne = 1;
            Scanner sc = new Scanner(System.in);   
            for (int i = 0; i < nbPinguin; i++) {
                for(Joueur j : joueurs) {
                    while(!pinguinPlace) {
                        System.out.println("== Joueur " + j.getNom());
                        System.out.print("numero ligne : ");
                        numLigne = sc.nextInt();
                        System.out.print("numero colonne : ");
                        numColonne = sc.nextInt();
                        if (plateau.estCaseLibre(numLigne, numColonne)) {
                            j.ajouterPinguin(plateau.getCases()[numLigne][numColonne]);
                            pinguinPlace = true;
                        } else {
                            System.out.println("Cette case est occupée ou coulé");
                        }
                    }
                    plateau.accept(dt);
                }
                
            }
            
            while(!partie.estTerminee()) {
                
            }
            
            
            plateau.accept(dt);
            
        } catch (IOException ex) {
            System.out.println("Erreur d'ouverture du fichier");
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
