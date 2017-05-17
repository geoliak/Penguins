/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.util.ArrayList;

/**
 *
 * @author liakopog
 */
public class Coup {

    private Joueur joueurCourant;
    private ArrayList<Joueur> joueurs;
    private Plateau plateau;

    public Coup() {
	this.joueurCourant = ConfigurationPartie.getConfigurationPartie().getPartie().getJoueurCourant();
	this.joueurs = ConfigurationPartie.getConfigurationPartie().getPartie().getJoueurs();
	this.plateau = ConfigurationPartie.getConfigurationPartie().getPartie().getPlateau();
    }

    public Joueur getJoueurCourant() {
	return joueurCourant;
    }

    public ArrayList<Joueur> getJoueurs() {
	return joueurs;
    }

    public Plateau getPlateau() {
	return plateau;
    }

}
