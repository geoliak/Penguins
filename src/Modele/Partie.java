/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.util.ArrayList;

/**
 *
 * @author novelm
 */
public class Partie {

    private Plateau plateau;
    private ArrayList<Joueur> joueurs;
    private Joueur joueurCourant;
    private Boolean initialisation;

    public Partie(Plateau plateau, ArrayList<Joueur> joueurs) {
        this.initialisation = true;
        this.plateau = plateau;
        this.joueurs = joueurs;
        this.joueurCourant = this.joueurs.remove(0);
    }

    public void joueurSuivant() {
        this.joueurs.add(this.joueurCourant);
        this.joueurCourant = this.joueurs.remove(0);
        if (!this.initialisation && !this.estTerminee() && !this.joueurCourant.estEnJeu()) {
            joueurSuivant();
        }
    }

    public void afficheJoueurs() {
        System.out.println("Joueur courant : " + joueurCourant.getCouleur() + joueurCourant.getNom() + Couleur.ANSI_RESET + " enJeu " + this.joueurCourant.estEnJeu());
        System.out.println("Joueur(s) dans la liste : ");
        for (Joueur j : this.joueurs) {
            System.out.println(j.getCouleur() + j.getNom() + Couleur.ANSI_RESET + " enJeu " + j.estEnJeu());
        }
    }

    public Joueur getJoueurCourant() {
        return joueurCourant;
    }

    public Boolean estTerminee() {
        Boolean res = joueurCourant.estEnJeu();
        for (Joueur j : this.joueurs) {
            res = res || j.estEnJeu();
        }
        return !res;
    }

    public ArrayList<Joueur> getJoueurGagnant() {
        ArrayList<Joueur> gagnants = new ArrayList<>();
        gagnants.add(joueurCourant);
        for (Joueur j : this.joueurs) {
            //Si un joueur à un meilleur score qu'un autre
            if (j.getScorePoissons() > gagnants.get(0).getScorePoissons()) {
                gagnants = new ArrayList<>();
                gagnants.add(j);
                //Si plusieurs Joueurs ont le meme code
            } else if (j.getScorePoissons() == gagnants.get(0).getScorePoissons()) {
                if (j.getScoreGlacons() > gagnants.get(0).getScoreGlacons()) {
                    gagnants = new ArrayList<>();
                    gagnants.add(j);
                } else if (j.getScoreGlacons() == gagnants.get(0).getScoreGlacons()) {
                    gagnants.add(j);
                }
            }
        }
        return gagnants;
    }

    public void afficheResultats() {
        if (this.estTerminee()) {
            this.joueurs.add(joueurCourant);
            for (Joueur j : this.getJoueurGagnant()) {
                System.out.println(j.getCouleur() + j.getNom() + Couleur.ANSI_RESET + " a gagne la partie");
            }
            for (Joueur j : this.joueurs) {
                System.out.println(j.getCouleur() + j.getNom() + Couleur.ANSI_RESET + " => " + j.getScorePoissons() + "," + j.getScoreGlacons());
            }
        }
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public Boolean sauvegarde() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Boolean estEnInitialisation() {
        return initialisation;
    }

    public void setInitialisation(Boolean initialisation) {
        this.initialisation = initialisation;
    }

    public ArrayList<Joueur> getJoueurs() {
        return joueurs;
    }

    public Boolean getInitialisation() {
        return initialisation;
    }

}
