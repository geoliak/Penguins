/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import Modele.IA.JoueurIA8;
import Modele.IA.JoueurIA9;
import Modele.IA.JoueurMinimax;

/**
 *
 * @author liakopog
 */
public class SuggestionCoup {

    public static boolean suggestionCoup() throws InterruptedException {

	for (Case[] cases : ConfigurationPartie.getConfigurationPartie().getPartie().getPlateau().getCases()) {
	    for (Case c : cases) {
		c.setAccessible(Boolean.FALSE);
	    }
	}
	ConfigurationPartie.getConfigurationPartie().getPartie().getPlateau().setEstModifié(true);

	JoueurHumainLocal joumain = (JoueurHumainLocal) ConfigurationPartie.getConfigurationPartie().getPartie().getJoueurCourant();
	JoueurIA9 jia = new JoueurIA9(joumain.getCouleur(), joumain.getNumero());
	jia.setPinguins(joumain.getPinguins());
	for (Pinguin p : joumain.getPinguins()) {
	    p.setGeneral(jia);
	}

	jia.setNom(joumain.getNom());
	jia.setPret(joumain.getPret());
	Case c = jia.etablirCoup(ConfigurationPartie.getConfigurationPartie().getPartie());
	for (Pinguin p : jia.getPinguins()) {
	    p.setGeneral(joumain);
	}
	ConfigurationPartie.getConfigurationPartie().getPartie().getJoueurCourant().setPinguinCourant(jia.getPinguinCourant());
	c.setAccessible(Boolean.TRUE);
//	    System.out.println(c + " " + ConfigurationPartie.getConfigurationPartie().getPartie().getJoueurCourant().getPinguinCourant() + " " + jia.getPinguinCourant());
	ConfigurationPartie.getConfigurationPartie().getPartie().getPlateau().setEstModifié(true);
        
        return true;
    }
}
