/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.io.Serializable;
import javafx.scene.control.Label;

/**
 *
 * @author rozandq
 */
public class Demo implements Serializable {
    private int phase;
    private String[] consignes;
    
    public Demo(){
        phase = 0;
        setConsignes();
    }
    
    public void nextPhase(){
        phase ++;
        System.out.println("PHASE: " + phase);
    }

    public int getPhase() {
        return phase;
    }
    
    public boolean isDemoFinie(){
        if(phase == 2){
            return true;
        } else {
            return false;
        }
    }
    
    public void setConsignes(){
        consignes = new String[3];
        consignes[0] = "Pendant la phase d'initialisation, chaque joueur place ses pingouins sur le plateau.\n"
                + "Vous ne pouvez placer les pingouins que sur des cases à UN poisson.";
        
        consignes[1] = "Le but du jeu est de manger plus de poissons que tes adversaires.\n"
                + "Ton pingouins mange les poissons des cases qu'il parcours.\n"
                + "Chaque pingouin peut se déplacer dans toute les directions, en ligne droite,\n"
                + "et d'autant case que tu veux. Les obstacles bloquent le déplacement.\n"
                + "Pour déplacer un pingouin, séléctionne le en cliquant dessus puis clique sur la case où tu veux te déplacer.";
    }

    public String getConsigne() {
        return consignes[phase];
    } 
    
    public void accept(Visiteur v){
        v.visit(this);
    }
}
