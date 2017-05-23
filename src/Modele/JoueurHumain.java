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

    public JoueurHumain(String nom, Couleur couleur, int numero) {
	super(couleur, numero);
	this.setNom(nom);
	this.setEstHumain(true);
    }

    public abstract Case etablirCoup(Partie partie);
    
    @Override
    public String getSpecialitees() {
        return this.getNom() + " -> Joueur Humain";
    }
}
