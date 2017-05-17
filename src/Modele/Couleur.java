/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.io.File;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 *
 * @author novelm
 */
public enum Couleur {

    Noir("noir","\u001B[30m", "\u001B[40m", Color.BLACK),
    Rouge("rouge","\u001B[31m", "\u001B[41m", Color.RED),
    Bleu("bleu","\u001B[34m", "\u001B[44m", Color.BLUE),
    Vert("verte","\u001B[32m", "\u001B[42m", Color.GREEN),
    Jaune("jaune","\u001B[33m", "\u001B[43m", Color.YELLOW),
    RougeFX("rouge","\u001B[31m", "\u001B[41m", Color.RED, new File("ressources/img/pingouin_rouge_mini.png")),
    VioletFX("violette","\u001B[34m", "\u001B[44m", Color.BLUE, new File("ressources/img/pingouin_violet_mini.png")),
    VertFX("verte","\u001B[32m", "\u001B[42m", Color.GREEN, new File("ressources/img/pingouin_vert_mini.png")),
    JauneFX("jaune","\u001B[33m", "\u001B[43m", Color.YELLOW, new File("ressources/img/pingouin_jaune_mini.png"));

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    
    public static final Couleur[] VALEURS = {Couleur.Bleu, Couleur.Jaune, Couleur.Rouge, Couleur.Vert};

    private String colorAccessible;
    private String colorBackground;
    private String nom;
    private Color couleur;
    private Image image;

    Couleur(String nom, String colorAccessible, String colorBackground, Color couleur) {
        this.nom = nom;
	this.colorAccessible = colorAccessible;
	this.colorBackground = colorBackground;
	this.couleur = couleur;
    }

    Couleur(String nom,String colorAccessible, String colorBackground, Color couleur, File f) {
        this.nom = nom;
	this.colorAccessible = colorAccessible;
	this.colorBackground = colorBackground;
	this.couleur = couleur;
        try {
            setImage(f);
        } catch (Exception e) {
            System.out.println("J'aime le fromage");
        }
    }

    public void setImage(File f) {
	image = new Image(f.toURI().toString());
    }

    @Override
    public String toString() {
	return colorAccessible;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Color getCouleur() {
        return couleur;
    }

    public void setCouleur(Color couleur) {
        this.couleur = couleur;
    }

    public String getColorAccessible() {
	return colorAccessible;
    }

    public String getColorBackground() {
	return colorBackground;
    }

    public Color getCouleurFX() {
	return couleur;
    }

    public Image getImage() {
	return image;
    }
}
