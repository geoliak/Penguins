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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
public class Musique {
    private static boolean play = true;
    private static File path;
    private static Clip clip;

    public Musique(String path) {
        Musique.path = new File(path);
        try {
            Musique.clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(Musique.path);
            Musique.clip.open(inputStream);
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException ex) {
            Logger.getLogger(Musique.class.getName()).log(Level.SEVERE, null, ex);
        }
        Musique.play = true;
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-20.0f); // Reduce volume by 10 decibels.
        Musique.clip.start();
        Musique.clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    
    public static void jouerStopperMusique(ImageView iv) {
        if(play){
            clip.stop();
            play = false;
            iv.setImage(new Image(new File("ressources/img/img_menu/note_croix.png").toURI().toString()));
        } else {
            clip.start();
            play = true;
            iv.setImage(new Image(new File("ressources/img/img_menu/note.jpg").toURI().toString()));
        }
    }

    public static boolean isPlay() {
        return play;
    }
    
    
}
