/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.io.Serializable;
import java.util.ArrayList;
import javafx.scene.image.Image;

/**
 *
 * @author novelm
 */
public abstract class Joueur implements Serializable {

    private int age;
    private String nom;
    private Couleur couleur;
    private int scoreGlacons;
    private int scorePoissons;
    private ArrayList<Pinguin> pinguins;
    private Pinguin pinguinCourant;
    private Boolean pret;
    private Boolean estHumain;
    private Image image;
    private int numero;
    private int difficulte;

    public Joueur(Couleur couleur, int numero) {
	this.pinguins = new ArrayList<>();
	this.pinguinCourant = null;
	this.pret = false;
	this.scoreGlacons = 0;
	this.scorePoissons = 0;
	this.nom = "Ceci ne devrait pas apparaitre";
	this.age = -1;
	this.couleur = couleur;
	this.numero = numero;
	this.difficulte = 0;
    }

    @Override
    public Joueur clone() throws CloneNotSupportedException {
	return (Joueur) (super.clone());
    }

    /**
     * Le joueur va reprendre le meme etat que lors de sa creation
     */
    public void reset() {
	this.pinguins = new ArrayList<>();
	this.pinguinCourant = null;
	this.pret = false;
	this.scoreGlacons = 0;
	this.scorePoissons = 0;
    }

    public int getNumero() {
	return numero;
    }

    public void ajouterPinguin(Case c) {
	Pinguin p = new Pinguin(c, this);
	c.setPinguin(p);
	this.pinguins.add(p);
        Bruitages.playPose();
    }

    public void joueCoup(Case c) {
	this.getPinguinCourant().deplace(c);
    }

    /**
     * Si cette methode est utilisee par l'IA alors elle va place le pinguin
     * choisi en tant que pinguinCourant puis renvoyer la case destination de ce
     * pinguin. Sinon cette methode va demander à l'utilisateur de saisir les
     * coordonnees de la case desiree (uniquement utilise pour la version
     * textuel)
     *
     * @param partie
     * @param plateau : plateau de jeu
     * @return Case : case jouee
     */
    public abstract Case etablirCoup(Partie partie);

    public ArrayList<Pinguin> getPinguinNonIsole() {
	ArrayList<Pinguin> pinguins = new ArrayList<>();

	for (Pinguin p : this.getPinguinsVivants()) {
	    if (!p.estSeul() && p.estVivant()) {
		pinguins.add(p);
	    }
	}

	return pinguins;
    }

    public int getAge() {
	return age;
    }

    public void setAge(int age) {
	this.age = age;
    }

    public String getNom() {
	return nom;
    }

    public void setNom(String nom) {
	this.nom = nom;
    }

    public int getScoreGlacons() {
	return scoreGlacons;
    }

    public void setScoreGlacons(int scoreGlacons) {
	this.scoreGlacons = scoreGlacons;
    }

    public int getDifficulte() {
	return difficulte;
    }

    public void setDifficulte(int difficulte) {
	this.difficulte = difficulte;
    }

    public int getScorePoissons() {
	return scorePoissons;
    }

    public void setScorePoissons(int scorePoissons) {
	this.scorePoissons = scorePoissons;
    }

    public ArrayList<Pinguin> getPinguins() {
	return pinguins;
    }

    public void setPinguins(ArrayList<Pinguin> pinguins) {
	this.pinguins = pinguins;
    }

    public ArrayList<Pinguin> getPinguinsVivants() {
	ArrayList<Pinguin> res = new ArrayList<>();
	for (Pinguin p : this.pinguins) {
	    if (p.estVivant()) {
		res.add(p);
	    }
	}
	return res;
    }

    public Pinguin getPinguinCourant() {
	return pinguinCourant;
    }

    public void setPinguinCourant(Pinguin pinguinCourant) {
	this.pinguinCourant = pinguinCourant;
    }

    public Boolean estEnJeu() {

	if (this.pret) {
	    return !this.getPinguinsVivants().isEmpty();
	} else {
	    return true;
	}
    }

    public Boolean getPret() {
	return pret;
    }

    public void setPret(Boolean pret) {
	this.pret = pret;
    }

    public Couleur getCouleur() {
	return couleur;
    }

    public Boolean getEstHumain() {
	return estHumain;
    }

    public void setEstHumain(Boolean estHumain) {
	this.estHumain = estHumain;
    }

    public void accept(Visiteur v) {
	v.visit(this);
    }

    @Override
    public String toString() {
	return this.getCouleur() + this.nom + Couleur.ANSI_RESET;
    }
    
    public abstract String getSpecialitees();
}
