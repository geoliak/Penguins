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

    private final Joueur joueurCourant;
    private final ArrayList<Joueur> joueursEnJeu;
    private final Plateau plateau;

    public Coup() {
	this.joueurCourant = ConfigurationPartie.getConfigurationPartie().getPartie().getJoueurCourant();
	this.joueursEnJeu = ConfigurationPartie.getConfigurationPartie().getPartie().getJoueursEnJeu();
	this.plateau = ConfigurationPartie.getConfigurationPartie().getPartie().getPlateau();
    }

    public Joueur getJoueurCourant() {
	return joueurCourant;
    }

    public ArrayList<Joueur> getJoueursEnJeu() {
	return joueursEnJeu;
    }

    public Plateau getPlateau() {
	return plateau;
    }

}
