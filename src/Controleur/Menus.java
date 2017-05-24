/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import Modele.Music;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author mariobap
 */
public class Menus extends Application {
    @Override
    public void start(Stage stage) {
	Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("../Vue/Accueil.fxml"));
            stage.setTitle("Salut les pingoins");
            stage.getIcons().add(new Image(new File("./ressources/img/penguin_miniature.png").toURI().toString()));
            stage.setScene(new Scene(root, 1200, 900));
            stage.setResizable(false);
            
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Menus.class.getName()).log(Level.SEVERE, null, ex);
        }
	
        
//        playSound();
        
        /*
        Media pick = new Media("Plongee-Nocturne.mp3");
        MediaPlayer player = new MediaPlayer(pick);
        player.play();

        //Add a mediaView, to display the media. Its necessary !
        //This mediaView is added to a Pane
        MediaView mediaView = new MediaView(player);
        ((Group) stage.getScene().getRoot()).getChildren().add(mediaView);
        */
    }

    public static void main(String[] args) {
	launch(args);
    }
    
    public void playSound() {
        try {
            /*
            File yourFile = new File("Plongee-Nocturne.ogg");
            
            System.out.println(yourFile.exists());
            
            AudioInputStream stream;
            AudioFormat format;
            DataLine.Info info;
            Clip clip;

            stream = AudioSystem.getAudioInputStream(yourFile);
            System.out.println(stream);
            
            format = stream.getFormat();
            info = new DataLine.Info(Clip.class, format);
            clip = (Clip) AudioSystem.getClip();
            clip.open(stream);
            clip.start();
                    */
            
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("Plongee-Nocturne.wav"));
            clip.open(inputStream);
            clip.start();
        }
        catch (Exception e) {
            System.out.println("Erreur lecture fichier audio");
            e.printStackTrace();
        }
    }
}
