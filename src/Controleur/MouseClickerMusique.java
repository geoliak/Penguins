/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.ConfigurationPartie;
import Modele.Musique;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Quentin
 */
public class MouseClickerMusique implements javafx.event.EventHandler<MouseEvent>{
    private ImageView iv;
    private Image note;

    public MouseClickerMusique(ImageView iv) {
        this.iv = iv;
    }

    @Override
    public void handle(MouseEvent event) {
        Musique.jouerStopperMusique(iv);
    }
}
