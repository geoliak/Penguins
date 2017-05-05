/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.util.Random;
import javafx.scene.paint.Color;

/**
 *
 * @author novelm
 */
public abstract class JoueurIA extends Joueur {
    private final String[] nomsPosible = {"Jack","Tealc","Sam","Daniel"};
    
    public JoueurIA(Couleur couleur) {
        super(couleur);
        this.setEstHumain(false);
        Random r = new Random();
        this.setNom(this.nomsPosible[r.nextInt(nomsPosible.length)]);
        this.setAge(r.nextInt(123)); // Jeanne Calment
    }  
    
    /**
     * Cette methode prend un plateau en parametre. L'IA va ensuite l'utiliser pour déterminer quelle pinguin jouer et ou.
     * Avant de renvoyer la case choisie, l'IA place le pinguin choisi en tant que pinguinCourant
     * @param plateau : plateau de jeu
     * @return la case jouee par l'IA
     */
    public abstract Case etablirCoup(Plateau plateau);
}
