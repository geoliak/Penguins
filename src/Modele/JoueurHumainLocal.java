/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.util.Scanner;
import javafx.scene.paint.Color;

/**
 *
 * @author novelm
 */
public class JoueurHumainLocal extends JoueurHumain {

    public JoueurHumainLocal(String nom, Couleur couleur) {
        super(nom, couleur);
    }

    /**
     * Cette methode prend le plateau de jeu en parametre, demande au joueur de choisir une case via scanner, puis renvoit la case concernée
     * @param plateau : plateau de jeu
     * @return la case, non verifiee, désignee par le joueur
     */
    @Override
    public Case etablirCoup(Plateau plateau) {
        Scanner sc = new Scanner(System.in);
        int numLigne, numColonne;

        System.out.println(super.getCouleur() + super.getNom() + Couleur.ANSI_RESET);

        do {
            System.out.print("numero ligne (0 -> 7) : ");
            numLigne = sc.nextInt();
        } while (numLigne < 0 || numLigne >= Plateau.LARGEUR);

        do {
            System.out.print("numero colonne (0 -> 7) : ");
            numColonne = sc.nextInt();
        } while (numColonne < 0 || numColonne >= Plateau.LONGUEUR);

        return plateau.getCases()[numLigne][numColonne];
    }

}
