/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.util.Random;

/**
 *
 * @author novelm
 */
public abstract class JoueurIA extends Joueur {
    private final String[] nomsPosible = {"Jack","Tealc","Sam","Daniel"};
    
    public JoueurIA(String color) {
        super(color);
        Random r = new Random();
        this.setNom(this.nomsPosible[r.nextInt(nomsPosible.length)]);
        this.setAge(r.nextInt(123)); // Jeanne Calment
    }
    
    public abstract Case etablirCoup();
}
