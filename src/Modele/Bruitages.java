/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Quentin
 */
public class Bruitages {
    private static Clip sound;

    public Bruitages() {
        try {
            sound = AudioSystem.getClip();
        } catch (LineUnavailableException ex) {
            Logger.getLogger(Bruitages.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void playGlace(){
        if(ConfigurationPartie.getConfigurationPartie().isEnableSounds()){
            try {
                Bruitages.sound.close();
//                Clip glace = AudioSystem.getClip();
                
                AudioInputStream inputStream;   
                inputStream = AudioSystem.getAudioInputStream(new File("plouf.wav"));
                
                Bruitages.sound.open(inputStream);
//                glace.open(inputStream);

                FloatControl gainControl = (FloatControl) Bruitages.sound.getControl(FloatControl.Type.MASTER_GAIN);
//                FloatControl gainControl = (FloatControl) glace.getControl(FloatControl.Type.MASTER_GAIN);
                
                gainControl.setValue(5.0f); // Reduce volume by 10 decibels.
                
                Bruitages.sound.start();
//                glace.start();
            } catch (Exception ex) {
                Logger.getLogger(Bruitages.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
        
    }
    
    public static void playPose(){
        if(ConfigurationPartie.getConfigurationPartie().isEnableSounds()){
            try {
                Clip sound = AudioSystem.getClip();
                AudioInputStream inputStream;   
                inputStream = AudioSystem.getAudioInputStream(new File("pose.wav"));
                sound.open(inputStream);
                FloatControl gainControl = (FloatControl) sound.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-10.0f); // Reduce volume by 10 decibels.
                sound.start();
            } catch (Exception ex) {
                Logger.getLogger(Bruitages.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
