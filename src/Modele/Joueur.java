/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author novelm
 */
public abstract class Joueur {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    private int age;
    private String nom;
    private String color;
    private int scoreGlacons;
    private int scorePoissons;
    private ArrayList<Pinguin> pinguins;
    private Pinguin pinguinCourant;

    public Joueur(String color) {
        this.pinguins = new ArrayList<>();
        this.color = color;
        this.pinguinCourant = null;
        this.scoreGlacons = 0;
        this.scorePoissons = 0;
        this.nom = "Ceci ne devrait pas apparaitre";
        this.age = -1;
        
    }
    
    public void ajouterPinguin(Case c) {
        Pinguin p = new Pinguin(c, this);
        c.setPinguin(p);
        this.pinguins.add(p);
    }

    public void joueCoup(Case c) {
        this.getPinguinCourant().deplace(c);
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

    public Pinguin getPinguinCourant() {
        return pinguinCourant;
    }

    public void setPinguinCourant(Pinguin pinguinCourant) {
        this.pinguinCourant = pinguinCourant;
    }

    public Boolean estEnJeu() {
        return this.pinguins.isEmpty();
    }

    public String getColor() {
        return color;
    }
    


}
