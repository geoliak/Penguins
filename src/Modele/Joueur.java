/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;

/**
 *
 * @author novelm
 */
public abstract class Joueur {

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

    public Joueur(Couleur couleur) {
        this.pinguins = new ArrayList<>();
        this.pinguinCourant = null;
        this.pret = false;
        this.scoreGlacons = 0;
        this.scorePoissons = 0;
        this.nom = "Ceci ne devrait pas apparaitre";
        this.age = -1;
        this.couleur = couleur;
    }

    public void reset() {
        this.pinguins = new ArrayList<>();
        this.pinguinCourant = null;
        this.pret = false;
        this.scoreGlacons = 0;
        this.scorePoissons = 0;
    }

    public void ajouterPinguin(Case c) {
        Pinguin p = new Pinguin(c, this);
        //System.out.println("AJOUT PINGOUIN");
        c.setPinguin(p);
        this.pinguins.add(p);
    }

    public void joueCoup(Case c) {
        try {
            this.getPinguinCourant().deplace(c);
        } catch (Exception e) {
            Logger.getLogger(Joueur.class.getName()).log(Level.SEVERE, null, e);
            //System.out.println("yolo");
        }
    }

    /**
     * Si cette methode est utilisee par l'IA alors elle va place le pinguin
     * choisi en tant que pinguinCourant puis renvoyer la case destination de ce
     * pinguin. Sinon cette methode va demander à l'utilisateur de saisir les
     * coordonnees de la case desiree (uniquement utilise pour la version
     * textuel)
     *
     * @param plateau : plateau de jeu
     * @return Case : case jouee
     */
    public abstract Case etablirCoup(Plateau plateau);

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
            Boolean enJeu = false;
            for (Pinguin p : this.pinguins) {
                enJeu = enJeu || p.estVivant();
            }
            return enJeu;
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
}
