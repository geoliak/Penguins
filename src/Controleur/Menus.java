/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

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
	    stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("img/penguin_miniature.png")));
	    stage.setScene(new Scene(root, 1200, 900));
	    stage.setResizable(false);
	    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
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
