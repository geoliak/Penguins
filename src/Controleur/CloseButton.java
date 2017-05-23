/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import java.io.File;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author rozandq
 */
public class CloseButton {
    ImageView closeiv;
    
    public CloseButton(ImageView fermer) {
        Image close = new Image(new File("ressources/img/img_menu/quitter.png").toURI().toString());
        fermer.setImage(close);
        fermer.setFitWidth(40);
        fermer.setX(1200 - fermer.getFitWidth() - 10);
        fermer.setY(10);
        fermer.setEffect(new DropShadow());
        fermer.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                fermer.setEffect(new InnerShadow());
            }
        });
        
        fermer.setOnMouseExited(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                fermer.setEffect(new DropShadow());
            }
        });
        
        fermer.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                Platform.exit();
            }
        });
        
        this.closeiv = fermer;
    }
        
}
