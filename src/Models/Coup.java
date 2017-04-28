/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.util.ArrayList;
/**
 *
 * @author rozandq
 */
public class Coup {
    private ArrayList<Case> cases;
    private Joueur joueur;

    public Coup(ArrayList<Case> cases, Joueur joueur) {
        this.cases = cases;
        this.joueur = joueur;
    }

    public ArrayList<Case> getCases() {
        return cases;
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public void setCases(ArrayList<Case> cases) {
        this.cases = cases;
    }

    public void setJoueur(JoueurHumain joueur) {
        this.joueur = joueur;
    }
}
