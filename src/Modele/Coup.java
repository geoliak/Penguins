/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author liakopog
 */
public class Coup implements Serializable {

    private Joueur joueurCourant;
    private byte[] partie;
    private Coup coupPrecedent;
    private Coup coupSuivant;

    public Coup() {
	try {
	    this.joueurCourant = ConfigurationPartie.getConfigurationPartie().getPartie().getJoueurCourant();
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ObjectOutputStream oos = new ObjectOutputStream(out);
	    oos.writeObject(ConfigurationPartie.getConfigurationPartie().getPartie());
	    partie = out.toByteArray();
	} catch (IOException ex) {
	    Logger.getLogger(Coup.class.getName()).log(Level.SEVERE, null, ex);
	}

    }

    public Joueur getJoueurCourant() {
	return this.joueurCourant;
    }

    public boolean getEstJoueurHumain() {
	return this.getJoueurCourant().getEstHumain();
    }

    public Coup getCoupPrecedent() {
	return coupPrecedent;
    }

    public void setCoupPrecedent(Coup coupPrecedent) {
	this.coupPrecedent = coupPrecedent;
    }

    public Coup getCoupSuivant() {
	return coupSuivant;
    }

    public void setCoupSuivant(Coup coupSuivant) {
	this.coupSuivant = coupSuivant;
    }

    public byte[] getPartie() {
	return partie;
    }

    public boolean isFirst() {
	return this.coupPrecedent == null;
    }

}
