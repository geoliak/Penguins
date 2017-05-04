/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

/**
 *
 * @author novelm
 */
public abstract class JoueurHumain extends Joueur {

    public JoueurHumain(int age, String nom,String colorBackground, String colorAccessible) {
        super(colorBackground,colorAccessible);
        this.setNom(nom);
        this.setAge(age);
    }
    
}
