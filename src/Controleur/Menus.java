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
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.WindowEvent;
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
            root = FXMLLoader.load(getClass().getClassLoader().getResource("Vue/Accueil.fxml"));
            stage.setTitle("Salut les Pingouins");
//            stage.getIcons().add(new Image(new File("./ressources/img/penguin_miniature.png").toURI().toString()));   
            stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("img/penguin_miniature.png")));
            stage.setScene(new Scene(root, 1200, 900));
            stage.setResizable(false);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>(){
                @Override
                public void handle(WindowEvent event) {
                    Platform.exit();
                    System.exit(0);
                }
            });
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Menus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
	launch(args);
    }
}
