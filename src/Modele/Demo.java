/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.io.Serializable;
import javafx.animation.Transition;
import javafx.scene.control.Label;

/**
 *
 * @author rozandq
 */
public class Demo implements Serializable {
    private int phase;
    private String[] consignes;
    private boolean estModifie;
    
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
        consignes = new String[10];
        consignes[0] = "Pendant la première phase, chaque joueur doit placer ses\n"
                + "pingouins sur des cases avec UN poisson.";
        consignes[1] = "Clique sur une case avec UN poisson pour placer tes pingouins.";
        
        consignes[2] = "Le but du jeu est de manger plus de poissons que tes\n"
                + "adversaires.\n"
                + "Tu manges les poissons sur les cases où tes pingouins s'arrêtent.\n"
                + "Chaque pingouin peut se déplacer dans toute les directions, en ligne\n"
                + "droite et d'autant de cases que tu veux.\n"
                + "Les obstacles (autres pingouins et trous dans la banquise) bloquent \n"
                + "les pingouins.";
        consignes[3] = "Pour déplacer un pingouin, séléctionne le en cliquant dessus\n"
                + "puis clique sur la case où tu veux te déplacer.";
        consignes[5] = "Tu es maintenant un expert, tu vas pouvoir commencer à jouer.";        
    }

    public String getConsigne() {
        return consignes[phase];
    } 
    
    public Transition accept(Visiteur v){
        return v.visit(this);
    }

    public boolean isEstModifie() {
        return estModifie;
    }

    public void setEstModifie(boolean estModifie) {
        this.estModifie = estModifie;
    }
    
    public boolean isClicOK(){
        return phase == 1 || phase >= 3;
    }    
}
