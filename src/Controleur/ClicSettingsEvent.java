/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Vue.AnimationFX;
import java.beans.EventHandler;
import java.util.ArrayList;
import javafx.animation.Animation;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author rozandq
 */
public class ClicSettingsEvent implements javafx.event.EventHandler<MouseEvent> {
    boolean open = false;
    ImageView gear;
    ArrayList<ImageView> ivs2;

    public ClicSettingsEvent(ImageView gear, ArrayList<ImageView> ivs) {
        this.gear = gear;
        this.ivs2 = ivs;
    }
       
    @Override
    public void handle(MouseEvent event) {
        AnimationFX a = new AnimationFX();
        if(!open){
            for(ImageView imageview : ivs2){
                a.scaleFromZero(imageview, 1, 200);
                open = true;
            }
        } else {
            for(ImageView imageview : ivs2){
                a.scaleToZero(imageview, 200);
                open = false;
            }
        }
    }
}
