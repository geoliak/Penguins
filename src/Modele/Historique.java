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

    private int indice = 0;
    private LinkedList<Coup> historiqueCoups;

    public Historique() {
	this.historiqueCoups = new LinkedList<>();
    }
    
    public Historique(Historique h){
        this.indice = h.indice;
        this.historiqueCoups = (LinkedList<Coup>)h.historiqueCoups.clone();

    }

    public void sauvegarderCoup() {
	if (ConfigurationPartie.getConfigurationPartie().getPartie().getJoueurCourant().getEstHumain()) {
	    Coup nouveauCoup = new Coup();
	    historiqueCoups.add(indice, nouveauCoup);
	    indice++;
	    //System.out.println("indice " + indice + " size " + historiqueCoups.size());

	    if (historiqueCoups.size() > indice) {
		for (int i = indice; i < historiqueCoups.size();) {
		    historiqueCoups.remove(i);
		    //System.out.println("remove" + "indice " + indice + " size " + historiqueCoups.size());
		}
	    }
	}
    }

    public void rejouerCoup() {
	//Teste si on a des coups à refaire, le +1 sert parce que normalement l'emplacement "indice" doit etre vide
        System.out.println("INDICE: " + indice + " SIZE: " + historiqueCoups.size());
	if (historiqueCoups.size() > indice+1) {
            ConfigurationPartie.getConfigurationPartie().getPartie().setTourFini(true);
	    indice++;
	    try {                
                Historique newHist = new Historique(this);
                
                ByteArrayInputStream in = new ByteArrayInputStream(historiqueCoups.get(indice).partie);
                
		ObjectInputStream instr = new ObjectInputStream(in);
		ConfigurationPartie.getConfigurationPartie().setPartie((Partie) instr.readObject());
                ConfigurationPartie.getConfigurationPartie().getPartie().setHistorique(newHist);
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
            
            if (historiqueCoups.size() == indice) {
                sauvegarderCoup();
                indice--;
            }
            
	    indice--;
	    try {
                Historique newHist = new Historique(this);
                
		ByteArrayInputStream in = new ByteArrayInputStream(historiqueCoups.get(indice).partie);
		ObjectInputStream instr = new ObjectInputStream(in);
                
		ConfigurationPartie.getConfigurationPartie().setPartie((Partie) instr.readObject());
                ConfigurationPartie.getConfigurationPartie().getPartie().setHistorique(newHist);
	    } catch (IOException ex) {
		Logger.getLogger(Historique.class.getName()).log(Level.SEVERE, null, ex);
	    } catch (ClassNotFoundException ex) {
		Logger.getLogger(Historique.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    ConfigurationPartie.getConfigurationPartie().getPartie().setReloadPartie(true);
	    ConfigurationPartie.getConfigurationPartie().getPartie().getPlateau().setEstModifié(true);
            ConfigurationPartie.getConfigurationPartie().getPartie().setTourFini(true);
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
        for (int i = 1; i < historiqueCoups.size();) {
		    historiqueCoups.remove(i);
		    System.out.println("remove" + "indice " + indice + " size " + historiqueCoups.size());
        }
    }
}
