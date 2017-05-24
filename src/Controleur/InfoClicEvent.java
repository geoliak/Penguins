/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import java.io.File;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import Modele.ConfigurationPartie;

/**
 *
 * @author rozandq
 */
public class InfoClicEvent implements EventHandler<MouseEvent>{
    ImageView infoicon;

    public InfoClicEvent(ImageView infoicon) {
        this.infoicon = infoicon;
    }

    @Override
    public void handle(MouseEvent event) {
        File finfo = new File("ressources/img/img_menu/info.png");
	Image info = new Image(finfo.toURI().toString());
        
        File finfo_croix = new File("ressources/img/img_menu/info_croix.png");
	Image info_croix = new Image(finfo_croix.toURI().toString());  
        
        if(ConfigurationPartie.getConfigurationPartie().isEnableHelp()){
            infoicon.setImage(info_croix);
            ConfigurationPartie.getConfigurationPartie().setEnableHelp(false);
            ConfigurationPartie.getConfigurationPartie().getPartie().getPlateau().setEstModifié(true);
        } else {
            infoicon.setImage(info);
            ConfigurationPartie.getConfigurationPartie().setEnableHelp(true);
            ConfigurationPartie.getConfigurationPartie().getPartie().getPlateau().setEstModifié(true);
        }
    }
}
