/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.util.ArrayList;
import javafx.scene.image.ImageView;

/**
 *
 * @author novelm
 */
public class Pinguin {

    private Boolean vivant;
    private Case position;
    private Joueur general;
    private Boolean estSeul;
    private ImageView iv;

    public Pinguin(Case position, Joueur maitre) {
	this.vivant = true;
	this.position = position;
	this.general = maitre;
	this.estSeul = false;
        //this.iv = new ImageView(general.getCouleur().getImage());
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
	this.position.setPinguin(null);
	this.position.setCoulee(true);

	this.general.setScoreGlacons(this.general.getScoreGlacons() + 1);
	this.general.setScorePoissons(this.general.getScorePoissons() + position.getNbPoissons());

	this.vivant = false;
        
        /*
        if(general.getEstHumain()){
            if(general.getPinguinsVivants().size() == 1){
                general.setPinguinCourant(general.getPinguinsVivants().get(0));
            } else {
                general.setPinguinCourant(null);
            }
        }
        */
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
	ArrayList<Case> iceberg = plateau.getCasesIceberg(this.position);
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

    public ImageView getIv() {
        return iv;
    }

    public void setIv(ImageView iv) {
        this.iv = iv;
    }
}
