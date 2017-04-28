/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import Views.Dessinateur;
import java.util.ArrayList;

/**
 *
 * @author rozandq
 */
public class Partie {

    private Plateau plateau;
    private Joueur joueurCourant;

    private Joueur joueur1;
    private Joueur joueur2;

    private ArrayList<Coup> coups;

    public Partie(Plateau plateau) {
        this.plateau = plateau;

        coups = new ArrayList<>();
    }

    public Joueur getJoueurCourant() {
        return joueurCourant;
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public Joueur getJoueur1() {
        return joueur1;
    }

    public Joueur getJoueur2() {
        return joueur2;
    }

    public ArrayList<Coup> getCoups() {
        return coups;
    }

    public void setPlateau(Plateau plateau) {
        this.plateau = plateau;
    }

    public void setJoueur1(Joueur joueur1) {
        this.joueur1 = joueur1;
    }

    public void setJoueur2(Joueur joueur2) {
        this.joueur2 = joueur2;
    }

    public void ajouterCoups(Coup coup) {
        coups.add(coup);
    }

    public void jouerPartie() {
        boolean termine = false;
        int tour = 1;

        Dessinateur dessinateur = new DessinateurFX(plateau);

        System.out.println("Affichage de la gaufre");
        dessinateur.dessinePlateau();

        joueurCourant = joueur1;

        while (!termine) {
            System.out.println("**************** Tour " + tour + " : " + joueurCourant.getName() + " ****************");

            joueurCourant.attendreCoup();

            plateau.masquerCases(coups.get(coups.size() - 1));

            dessinateur.dessinePlateau();

            tour++;

            if (partieTermine()) {
                termine = true;
                System.out.println(joueurCourant.getName() + " a gagné !");
            }
            
            if (tour % 2 == 1) {
                joueurCourant = joueur1;
            } else {
                joueurCourant = joueur2;
            }
        }
    }

    private boolean partieTermine() {
        return !plateau.getCases()[0][1].isJouable() && !plateau.getCases()[1][0].isJouable();
    }

}
