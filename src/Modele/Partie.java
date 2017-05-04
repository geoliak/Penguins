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
    }

    public void joueurSuivant() {
        this.joueurs.add(this.joueurCourant);
        this.joueurCourant = this.joueurs.remove(0);
    }

    public Joueur getJoueurCourant() {
        return joueurCourant;
    }

    public Boolean estTerminee() {
        Boolean res = joueurCourant.estEnJeu();
        for (Joueur j : this.joueurs) {
            res = res || j.estEnJeu();
        }
        return res;
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
}
