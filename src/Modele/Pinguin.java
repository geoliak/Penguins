/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.io.Serializable;
import java.util.ArrayList;
import Modele.MyImageView;

/**
 *
 * @author novelm
 */
public class Pinguin implements Serializable {

    private Boolean vivant;
    private Case position;
    private Joueur general;
    private Boolean estSeul;
    private MyImageView iv;

    public Pinguin(Case position, Joueur maitre) {
	this.vivant = true;
	this.position = position;
	this.general = maitre;
	this.estSeul = false;
	//this.iv = new MyImageView(general.getCouleur().getImage());
    }

    public Pinguin myClone(Plateau plateau) {
	Case c = plateau.getCases()[this.getPosition().getNumLigne()][this.getPosition().getNumColonne()];
	Pinguin clone = new Pinguin(c, this.general);
	c.setPinguin(clone);
	return clone;
    }

    public void deplace(Case c) {
	this.position.setPinguin(null);
	this.position.setCoulee(true);

	this.general.setScoreGlacons(this.general.getScoreGlacons() + 1);
	this.general.setScorePoissons(this.general.getScorePoissons() + position.getNbPoissons());

	this.position = c;
	c.setPinguin(this);
    }

    public void coullePinguin() {
	//this.position.setPinguin(null);
	this.position.setCoulee(true);

	this.general.setScoreGlacons(this.general.getScoreGlacons() + 1);
	this.general.setScorePoissons(this.general.getScorePoissons() + position.getNbPoissons());

	this.vivant = false;

	if (general.getEstHumain()) {
	    if (general.getPinguinsVivants().size() == 1) {
		general.setPinguinCourant(general.getPinguinsVivants().get(0));
	    } else {
		general.setPinguinCourant(null);
	    }
	}
    }

    /**
     * A l'air de marcher
     *
     * @param plateau
     * @param source
     * @return True si le pinguin est seul ou avec un de ses collègues sur
     * l'iceberg
     */
    public Boolean estSeulIceberg(Plateau plateau) {
	ArrayList<Case> iceberg = plateau.getCasesIcebergSansCassures(this.position);
	for (Case c : iceberg) {
	    if (c.getPinguin() != null && c.getPinguin().getGeneral() != this.general) {
		return false;
	    }
	}
	return true;
    }
    
    

    public void accept(Visiteur v) {
	v.visite(this);
    }

    public Boolean estVivant() {
	return vivant;
    }

    public void setVivant(Boolean vivant) {
	this.vivant = vivant;
    }

    public Case getPosition() {
	return position;
    }

    public void setPosition(Case position) {
	this.position = position;
    }

    public Joueur getGeneral() {
	return general;
    }

    @Override
    public String toString() {
	return this.getGeneral().getCouleur() + "(" + this.position.getNumLigne() + "," + this.position.getNumColonne() + ")" + Couleur.ANSI_RESET;

    }

    public Boolean estSeul() {
	return estSeul;
    }

    public void setEstSeul(Boolean estSeul) {
	this.estSeul = estSeul;
    }

    public MyImageView getIv() {
	return iv;
    }

    public void setIv(MyImageView iv) {
	this.iv = iv;
    }
}
