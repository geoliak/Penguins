/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import Modele.IA.JoueurIA8;

/**
 *
 * @author liakopog
 */
public class SuggestionCoup {

    public static void suggestionCoup() throws InterruptedException {

	for (Case[] cases : ConfigurationPartie.getConfigurationPartie().getPartie().getPlateau().getCases()) {
	    for (Case c : cases) {
		c.setAccessible(Boolean.FALSE);
	    }
	}
	ConfigurationPartie.getConfigurationPartie().getPartie().getPlateau().setEstModifié(true);

	JoueurHumainLocal joumain = (JoueurHumainLocal) ConfigurationPartie.getConfigurationPartie().getPartie().getJoueurCourant();
	JoueurIA8 jia = new JoueurIA8(joumain.getCouleur(), joumain.getNumero());
	jia.setAge(1);
	jia.setPinguins(joumain.getPinguins());
	jia.setNom(joumain.getNom());
	jia.setPret(joumain.getPret());
	Case c = jia.etablirCoup(ConfigurationPartie.getConfigurationPartie().getPartie());

	ConfigurationPartie.getConfigurationPartie().getPartie().getJoueurCourant().setPinguinCourant(jia.getPinguinCourant());
	c.setAccessible(Boolean.TRUE);
//	    System.out.println(c + " " + ConfigurationPartie.getConfigurationPartie().getPartie().getJoueurCourant().getPinguinCourant() + " " + jia.getPinguinCourant());
	ConfigurationPartie.getConfigurationPartie().getPartie().getPlateau().setEstModifié(true);
    }

}
