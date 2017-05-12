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

    Noir("\u001B[30m", "\u001B[40m", Color.BLACK),
    Rouge("\u001B[31m", "\u001B[41m", Color.RED),
    Bleu("\u001B[34m", "\u001B[44m", Color.BLUE),
    Vert("\u001B[32m", "\u001B[42m", Color.GREEN),
    Jaune("\u001B[33m", "\u001B[43m", Color.YELLOW),
    RougeFX("\u001B[31m", "\u001B[41m", Color.RED, new File("ressources/img/pingouin_rouge_mini.png")),
    VioletFX("\u001B[34m", "\u001B[44m", Color.BLUE, new File("ressources/img/pingouin_violet_mini.png")),
    VertFX("\u001B[32m", "\u001B[42m", Color.GREEN, new File("ressources/img/pingouin_vert_mini.png")),
    JauneFX("\u001B[33m", "\u001B[43m", Color.YELLOW, new File("ressources/img/pingouin_jaune_mini.png"));

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    
    public static final Couleur[] VALEURS = {Couleur.Bleu, Couleur.Jaune, Couleur.Rouge, Couleur.Vert};

    private String colorAccessible;
    private String colorBackground;
    private Color couleur;
    private Image image;

    Couleur(String colorAccessible, String colorBackground, Color couleur) {
	this.colorAccessible = colorAccessible;
	this.colorBackground = colorBackground;
	this.couleur = couleur;
    }

    Couleur(String colorAccessible, String colorBackground, Color couleur, File f) {
	this.colorAccessible = colorAccessible;
	this.colorBackground = colorBackground;
	this.couleur = couleur;
	//setImage(f);
    }

    public void setImage(File f) {
	image = new Image(f.toURI().toString());
    }

    @Override
    public String toString() {
	return colorAccessible;
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
