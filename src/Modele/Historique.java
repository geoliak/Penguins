/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author liakopog
 */
public class Historique implements Serializable {

    private String dossierNom;
    private int indice = 1;
    private static LinkedList<Coup> historiqueCoups;

    public Historique() {
	Historique.historiqueCoups = new LinkedList<>();
    }

    public void sauvegarderCoup() {
	historiqueCoups.add(new Coup());
	if (historiqueCoups.size() > indice) {
	    for (int i = indice + 1; i == historiqueCoups.size(); i++) {
		historiqueCoups.remove(i);
	    }
	}
	indice++;
    }

    public void rejouerCoup() {
	//Teste si on a des coups à refaire
	if (historiqueCoups.size() > indice) {
	    indice++;
	    //Si c'est le tour du meme joueur, alors on charge la partie
	    if (ConfigurationPartie.getConfigurationPartie().getPartie().getJoueurCourant().equals(historiqueCoups.get(indice).getJoueurCourant())) {
		ConfigurationPartie.getConfigurationPartie().getPartie().setJoueursEnJeu(historiqueCoups.get(indice).getJoueursEnJeu());
		ConfigurationPartie.getConfigurationPartie().getPartie().setPlateau(historiqueCoups.get(indice).getPlateau());
	    } else {
		indice++;
		rejouerCoup();
	    }
	}
    }

    public void annulerDernierCoup() {

	if (indice > 1) {
	    indice--;
	    if (ConfigurationPartie.getConfigurationPartie().getPartie().getJoueurCourant().equals(historiqueCoups.get(indice).getJoueurCourant())) {
		ConfigurationPartie.getConfigurationPartie().getPartie().setJoueursEnJeu(historiqueCoups.get(indice).getJoueursEnJeu());
		ConfigurationPartie.getConfigurationPartie().getPartie().setPlateau(historiqueCoups.get(indice).getPlateau());
	    } else {
		indice--;
		annulerDernierCoup();
	    }
	}

    }
}
