/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.ConfigurationPartie;
import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Quentin
 */
public class MouseClickerVolume implements javafx.event.EventHandler<MouseEvent>{
    ImageView iv;

    public MouseClickerVolume(ImageView iv) {
        this.iv = iv;
    }

    @Override
    public void handle(MouseEvent event) {
        if(ConfigurationPartie.getConfigurationPartie().isEnableSounds()){
            iv.setImage(new Image(new File("ressources/img/img_menu/volume_croix.png").toURI().toString()));
            ConfigurationPartie.getConfigurationPartie().setEnableSounds(false);
        } else {
            iv.setImage(new Image(new File("ressources/img/img_menu/volume.png").toURI().toString()));
            ConfigurationPartie.getConfigurationPartie().setEnableSounds(true);
        }
    }
    
}
