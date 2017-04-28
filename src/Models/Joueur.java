/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author rozandq
 */
public abstract class Joueur {
    Partie partie;
    String name;
    
    public Joueur(String name, Partie partie) {
        this.partie = partie;
        this.name = name;
    }

    public Partie getPartie() {
        return partie;
    }

    public String getName() {
        return name;
    }
    
    public abstract void attendreCoup();
}
