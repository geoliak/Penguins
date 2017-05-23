/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author liakopog
 */
public class Historique implements Serializable {

    private int indice = 0;
//    private LinkedList<Coup> historiqueCoups;
    private Coup coupCourant;

    public Historique() {
	this.coupCourant = null;
    }

//    public Historique(Historique h) {
//	this.indice = h.indice;
//	this.historiqueCoups = (LinkedList<Coup>) h.historiqueCoups.clone();
//
//    }
    public void sauvegarderCoup() {
	if (ConfigurationPartie.getConfigurationPartie().getPartie().getJoueurCourant().getEstHumain()) {
	    Coup nouveauCoup = new Coup();
	    //Pas sure si on doit faire comme ça ou avec indice, à décider!
	    if (coupCourant == null) {
		System.out.println("premiere sauvegarde");

		nouveauCoup.setCoupPrecedent(null);
		coupCourant = nouveauCoup;
	    } else {
		System.out.println("sauvegarde fo sho");
		nouveauCoup.setCoupPrecedent(coupCourant);
		coupCourant.setCoupSuivant(nouveauCoup);
		coupCourant = nouveauCoup;

	    }
//	    indice++;
	}
    }

    public void rejouerCoup() {
	if (coupCourant != null && coupCourant.getCoupSuivant() != null) {
	    coupCourant = coupCourant.getCoupSuivant();

	    //	    ConfigurationPartie.getConfigurationPartie().getPartie().setTourFini(true);
	    try {
//		Historique newHist = new Historique(this);

		ByteArrayInputStream in = new ByteArrayInputStream(coupCourant.getPartie());

		ObjectInputStream instr = new ObjectInputStream(in);
		ConfigurationPartie.getConfigurationPartie().setPartie((Partie) instr.readObject());
//		ConfigurationPartie.getConfigurationPartie().getPartie().setHistorique(newHist);

	    } catch (IOException ex) {
		Logger.getLogger(Historique.class
			.getName()).log(Level.SEVERE, null, ex);
	    } catch (ClassNotFoundException ex) {
		Logger.getLogger(Historique.class
			.getName()).log(Level.SEVERE, null, ex);
	    }
	    ConfigurationPartie.getConfigurationPartie().getPartie().setReloadPartie(true);
	    ConfigurationPartie.getConfigurationPartie().getPartie().getPlateau().setEstModifié(true);
	} else {
	    System.out.println("Pas des coups a refaire");
	}
    }

    public void annulerDernierCoup() {

	if (coupCourant != null) {
	    try {

		ByteArrayInputStream in = new ByteArrayInputStream(coupCourant.getPartie());
		ObjectInputStream instr = new ObjectInputStream(in);

		ConfigurationPartie.getConfigurationPartie().setPartie((Partie) instr.readObject());
//	    ConfigurationPartie.getConfigurationPartie().getPartie().setHistorique(newHist);

	    } catch (IOException ex) {
		Logger.getLogger(Historique.class
			.getName()).log(Level.SEVERE, null, ex);
	    } catch (ClassNotFoundException ex) {
		Logger.getLogger(Historique.class
			.getName()).log(Level.SEVERE, null, ex);
	    }
	    ConfigurationPartie.getConfigurationPartie().getPartie().setReloadPartie(true);
	    ConfigurationPartie.getConfigurationPartie().getPartie().getPlateau().setEstModifié(true);
	    ConfigurationPartie.getConfigurationPartie().getPartie().setTourFini(true);
	    coupCourant = coupCourant.getCoupPrecedent();
	    System.out.println(indice + " charge");
	} else {
	    System.out.println("plus de coups a annuler");
	}
    }

    public boolean sauvegardeDebut() {
	return (indice == 0);
    }

    public void recommencer() {
	while (!coupCourant.isFirst()) {

	    this.annulerDernierCoup();

	}
    }
}
