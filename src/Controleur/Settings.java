/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.ConfigurationPartie;
import Modele.Musique;
import Vue.AnimationFX;
import java.io.File;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author rozandq
 */
public class Settings {
    public static void setSettings(AnchorPane ap){
        Tooltip tooltip = new Tooltip();
        AnimationFX a = new AnimationFX();
        
        double offset = 0.0;
        if(ConfigurationPartie.getConfigurationPartie().getPartie() != null && ConfigurationPartie.getConfigurationPartie().getPartie().getJoueurs().size() == 4){
            offset = -50.0;
        }
        
	ImageView gear = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("img/img_menu/gear.png")));
        gear.setId("close");
        gear.setLayoutY(offset - 10);
        gear.setLayoutX(10);
        
        gear.setOnMouseEntered(new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent event) {
		a.scale(gear, 1.15, 200);
	    }
	});

	gear.setOnMouseExited(new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent event) {
		a.scale(gear, 1, 200);
	    }
	});
        
        
        String path;
        if(Musique.isPlay()){
            path = "img/img_menu/note.jpg";
        } else {
            path = "img/img_menu/note_croix.png";
        }
        File fnote = new File(path);
	ImageView note = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream(path)));
        tooltip = new Tooltip();
        tooltip.setText("Désactiver la musique\n");
        Tooltip.install(note, tooltip);
        note.setLayoutY(offset - 7);
        note.setLayoutX(80);
        note.setVisible(false);
        note.setOnMouseClicked(new MouseClickerMusique(note));
        
        if(ConfigurationPartie.getConfigurationPartie().isEnableSounds()){
            path = "img/img_menu/volume.png";
        } else {
            path = "img/img_menu/volume_croix.png";
        }
        File fvol = new File(path);
	ImageView volume = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream(path)));
        tooltip = new Tooltip();
        tooltip.setText("Désactiver les bruitages\n");
        Tooltip.install(volume, tooltip);
        volume.setLayoutY(offset - 7);
        volume.setLayoutX(120);
        volume.setVisible(false);
        volume.setOnMouseClicked(new MouseClickerVolume(volume));
        
        ImageView info;
        if(ConfigurationPartie.getConfigurationPartie().isEnableHelp()){
            info = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("img/img_menu/info.png")));
        } else {
            info = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("img/img_menu/info_croix.png")));
        }
        tooltip = new Tooltip();
        tooltip.setText("Désactiver les aides visuelles\n");
        Tooltip.install(info, tooltip);
	
        info.setId("info");
        info.setLayoutY(offset - 10);
        info.setLayoutX(160);
        info.setVisible(false);
        info.setOnMouseClicked(new InfoClicEvent(info));
        
        ArrayList<ImageView> ivs2 = new ArrayList<>();
        ivs2.add(note);
        ivs2.add(volume);
        ivs2.add(info);
        
        gear.setOnMouseClicked(new ClicSettingsEvent(gear, ivs2));
        
        setSettingsAnim(ivs2);
        
        ap.getChildren().addAll(gear, note, volume);
        if(ConfigurationPartie.getConfigurationPartie().getPartie() != null && ConfigurationPartie.getConfigurationPartie().getPartie().getDemo() == null) {
            ap.getChildren().addAll(info);
        }
    }
    
    public static void setSettingsAnim(ArrayList<ImageView> ivs){
        AnimationFX a = new AnimationFX();
        for(ImageView iv : ivs){
            iv.setOnMouseEntered(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    a.scale(iv, 1.2, 200);
                }

            });
            
            iv.setOnMouseExited(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    a.scale(iv, 1.0, 200);
                }

            });
        }
    }
}
