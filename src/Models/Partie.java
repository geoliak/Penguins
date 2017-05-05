/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import Views.DessinateurText;
import Views.DessinateurFX;
import java.util.ArrayList;

/**
 *
 * @author rozandq
 */
public class Partie {

    private Plateau plateau;
    private Joueur joueurCourant;
    private boolean aJoue;

    private Joueur joueur1;
    private Joueur joueur2;

    private ArrayList<Coup> coups;

    public Partie(Plateau plateau) {
        this.plateau = plateau;
        coups = new ArrayList<>();
        aJoue = false;
    }

    public boolean isaJoue() {
        return aJoue;
    }

    public void setaJoue(boolean aJoue) {
        this.aJoue = aJoue;
    }

    public void setJoueurCourant(Joueur joueurCourant) {
        this.joueurCourant = joueurCourant;
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
        getPlateau().masquerCases(coup);
    }
    
    public void jouerTour(){
        System.out.println(joueurCourant.getName());
        joueurCourant.attendreCoup();
        plateau.masquerCases(coups.get(coups.size() - 1));
    }   
    
    public void switchJoueur(){
        if (coups.size() % 2 == 1) {
            joueurCourant = joueur2;
        } else {
            joueurCourant = joueur1;
        }
        System.out.println("Nouveau joueur courant: " + joueurCourant.getName());
    }
    
    public void jouerPartie() {
        boolean termine = false;
        int tour = 1;

        DessinateurText dessinateur = new DessinateurText();

        System.out.println("Affichage de la gaufre");
        dessinateur.dessinePlateau(plateau);

        joueurCourant = joueur1;

        while (!termine) {
            System.out.println("**************** Tour " + tour + " : " + joueurCourant.getName() + " ****************");

            joueurCourant.attendreCoup();

            plateau.masquerCases(coups.get(coups.size() - 1));

            dessinateur.dessinePlateau(plateau);

            tour++;

            if (partieTermine()) {
                termine = true;
                if (tour % 2 == 1) {
                    System.out.println(joueur1.getName() + " a gagné !");
                } else {
                    System.out.println(joueur2.getName() + " a gagné !");
                }
            }

            if (tour % 2 == 1) {
                joueurCourant = joueur1;
            } else {
                joueurCourant = joueur2;
            }
        }
    }

    public boolean partieTermine() {
        return !plateau.getCases()[0][0].isJouable();
    }
    
    public boolean joueurCourantAPerdu(){
        if(!plateau.getCases()[1][0].isJouable() && !plateau.getCases()[0][1].isJouable()){
            plateau.getCases()[0][0].setJouable(false);
            return true;
        } else {
            return false;
        }
    }
}
