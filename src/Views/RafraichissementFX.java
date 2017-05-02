/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import Models.JoueurHumain;
import Models.JoueurIA;
import Models.Partie;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;

/**
 *
 * @author rozandq
 */
public class RafraichissementFX extends AnimationTimer {
    InterfaceFX i;

    public RafraichissementFX(InterfaceFX i) {
        this.i = i;
    }

    @Override
    public void handle(long now) {
        if (i.getPartie().getJoueurCourant().getClass() == JoueurIA.class && !i.getPartie().partieTermine()){
            System.out.println("");
            JoueurIA j = (JoueurIA) i.getPartie().getJoueurCourant();
            j.jouerCoup(j.getDifficulte());

            i.getD().dessinePlateau(i.getPartie().getPlateau(), i.getPartie(), i.getScene());
            i.getPartie().switchJoueur();
            System.out.println("Tour de " + i.getPartie().getJoueurCourant().getName());
            if(i.getPartie().joueurCourantAPerdu()){
                System.out.println(i.getPartie().getJoueurCourant().getName() + " a perdu !");
                Platform.exit();
            }
        } else {
            if(i.getPartie().isaJoue() && !i.getPartie().partieTermine()){
                i.getPartie().setaJoue(false);

                i.getD().dessinePlateau(i.getPartie().getPlateau(), i.getPartie(), i.getScene());
                i.getPartie().switchJoueur();
                System.out.println("Tour de " + i.getPartie().getJoueurCourant().getName());
                
                if(i.getPartie().joueurCourantAPerdu()){
                    System.out.println(i.getPartie().getJoueurCourant().getName() + " a perdu !");
                    Platform.exit();
                }
            }
        }


             
    }
}
