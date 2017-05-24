/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.SuggestionCoup;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.effect.Bloom;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author rozandq
 */
public class hintClicEvent implements EventHandler<MouseEvent> {
    ImageView iv;

    public hintClicEvent(ImageView iv) {
        this.iv = iv;
    }

    @Override
    public void handle(MouseEvent event) {
        try {
            SuggestionCoup.suggestionCoup();
        } catch (InterruptedException ex) {
            Logger.getLogger(hintClicEvent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
