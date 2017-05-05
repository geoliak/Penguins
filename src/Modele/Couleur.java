/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

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
    Jaune("\u001B[33m", "\u001B[43m", Color.YELLOW);
    
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    private String colorAccessible;
    private String colorBackground;
    private Color couleur;

    Couleur(String colorAccessible, String colorBackground, Color couleur) {
        this.colorAccessible = colorAccessible;
        this.colorBackground = colorBackground;
        this.couleur = couleur;
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
}
