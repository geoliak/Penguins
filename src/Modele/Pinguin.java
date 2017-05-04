/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.util.ArrayList;

/**
 *
 * @author novelm
 */
public class Pinguin {
    private Boolean vivant;
    private Case position;
    private Joueur general;

    public Pinguin(Case position, Joueur maitre) {
        this.vivant = true;
        this.position = position;
        this.general = maitre;
    }
    
    public void deplace(Case c) {
        this.position.setPinguin(null);
        this.position.setCoulee(true);
        
        this.general.setScoreGlacons(this.general.getScoreGlacons() + 1);
        this.general.setScorePoissons(this.general.getScorePoissons() + position.getNbPoissons());
        
        this.position = c;
        c.setPinguin(this);
        
        this.general.setPinguinCourant(null);
    }
    
    public void coullePinguin() {
        this.position.setPinguin(null);
        this.position.setCoulee(true);
        
        this.general.setScoreGlacons(this.general.getScoreGlacons() + 1);
        this.general.setScorePoissons(this.general.getScorePoissons() + position.getNbPoissons());
        
        this.vivant = false;
        this.general.setPinguinCourant(null);
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
    
    
    
    
}
