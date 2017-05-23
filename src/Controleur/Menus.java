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
import java.net.URISyntaxException;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 *
 * @author mariobap
 */
public class Menus extends Application {
    @Override
    public void start(Stage stage) throws IOException, URISyntaxException {
	Parent root = FXMLLoader.load(getClass().getResource("../Vue/Accueil.fxml"));
	stage.setTitle("Salut les pingoins");
	stage.getIcons().add(new Image(new File("./ressources/img/penguin_miniature.png").toURI().toString()));
	stage.setScene(new Scene(root, 1200, 900));
	stage.setResizable(false);
        
        /*
        Media pick = new Media("Plongee-Nocturne.mp3");
        MediaPlayer player = new MediaPlayer(pick);
        player.play();

        //Add a mediaView, to display the media. Its necessary !
        //This mediaView is added to a Pane
        MediaView mediaView = new MediaView(player);
        ((Group) stage.getScene().getRoot()).getChildren().add(mediaView);
        */
        
	stage.show();
    }

    public static void main(String[] args) {
	launch(args);
    }

}
