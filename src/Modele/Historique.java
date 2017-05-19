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
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public void sauvegarderCoup() {
	if (ConfigurationPartie.getConfigurationPartie().getPartie().getJoueurCourant().getEstHumain()) {
	    System.out.println("indice ou le nouveau coup va " + indice);
	    Coup nouveauCoup = new Coup();
	    historiqueCoups.add(indice, nouveauCoup);
	    indice++;
	    System.out.println("indice " + indice + " size " + historiqueCoups.size());

	    if (historiqueCoups.size() > indice) {
		for (int i = indice; i < historiqueCoups.size();) {
		    historiqueCoups.remove(i);
		    System.out.println("remove" + "indice " + indice + " size " + historiqueCoups.size());
		}
	    }
	}
    }

    public void rejouerCoup() {
	//Teste si on a des coups à refaire, le +1 sert parce que normalement l'emplacement "indice" doit etre vide
	if (historiqueCoups.size() > indice + 1) {
	    indice++;
	    try {
		ByteArrayInputStream in = new ByteArrayInputStream(historiqueCoups.get(indice).getOut().toByteArray());
		ObjectInputStream instr = new ObjectInputStream(in);
		ConfigurationPartie.getConfigurationPartie().setPartie((Partie) instr.readObject());
	    } catch (IOException ex) {
		Logger.getLogger(Historique.class.getName()).log(Level.SEVERE, null, ex);
	    } catch (ClassNotFoundException ex) {
		Logger.getLogger(Historique.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    ConfigurationPartie.getConfigurationPartie().getPartie().setReloadPartie(true);
	    ConfigurationPartie.getConfigurationPartie().getPartie().getPlateau().setEstModifié(true);
	} else {
	    System.out.println("Pas des coups a refaire");
	}
    }

    public void annulerDernierCoup() {
	System.out.println("indice avant decrementation " + indice);

	if (indice > 0) {
	    indice--;
	    try {
		ByteArrayInputStream in = new ByteArrayInputStream(historiqueCoups.get(indice).getOut().toByteArray());
		ObjectInputStream instr = new ObjectInputStream(in);
		ConfigurationPartie.getConfigurationPartie().setPartie((Partie) instr.readObject());
	    } catch (IOException ex) {
		Logger.getLogger(Historique.class.getName()).log(Level.SEVERE, null, ex);
	    } catch (ClassNotFoundException ex) {
		Logger.getLogger(Historique.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    ConfigurationPartie.getConfigurationPartie().getPartie().setReloadPartie(true);
	    ConfigurationPartie.getConfigurationPartie().getPartie().getPlateau().setEstModifié(true);
	    System.out.println(indice + " charge");
	} else {
	    System.out.println("plus de coups a annuler");
	}
    }

    public boolean sauvegardeDebut() {
	return (indice == 0);
    }

    public void recommencer() {
	this.indice = 1;
	this.annulerDernierCoup();
    }
}
