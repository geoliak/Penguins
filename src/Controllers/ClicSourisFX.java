/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.Case;
import Models.Coup;
import Models.JoueurHumain;
import Models.Partie;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 *
 * @author rozandq
 */
public class ClicSourisFX implements EventHandler<MouseEvent> {
    Partie partie;
    ImageView iv;
    GridPane gp;
    int xclic;
    int yclic;

    public ClicSourisFX(Partie partie, GridPane gp, ImageView iv) {
        this.partie = partie;
        this.gp = gp;
        this.iv = iv;
    }

    @Override
    public void handle(MouseEvent event) {
        if(partie.getJoueurCourant().getClass() == JoueurHumain.class){
            if(!partie.isaJoue()){
                JoueurHumain j = (JoueurHumain) partie.getJoueurCourant();
                partie.setaJoue(true);
                //System.out.println("TOUR HUMAIN");

                xclic = GridPane.getColumnIndex(iv);
                yclic = GridPane.getRowIndex(iv);

                System.out.println(xclic + " " + yclic);

                j.jouerCoup(xclic, yclic);
            }
        }
    }   
}
