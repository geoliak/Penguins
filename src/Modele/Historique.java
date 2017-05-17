/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author liakopog
 */
public class Historique implements Serializable {

    private String dossierNom;
    private int indice = 0;
    private static LinkedList<Coup> historiqueCoups;

    public Historique() {
	Historique.historiqueCoups = new LinkedList<>();
    }

    public void rejouerCoup() {
	//Teste si on a des coups à refaire
	if (historiqueCoups.size() > indice) {
	    //Si oui, alors 
	    if (ConfigurationPartie.getConfigurationPartie().getPartie().getJoueurCourant().equals(historiqueCoups.get(indice))) {

	    }
	}
    }

    public void sauvegarderCoup() {
	historiqueCoups.add(new Coup());
	indice++;

    }

    public void annulerDernierCoup() throws FileNotFoundException {

    }
}
