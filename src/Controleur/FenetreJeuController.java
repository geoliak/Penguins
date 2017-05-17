/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.ConfigurationPartie;
import Modele.Historique;
import Vue.AnimationFX;
import Vue.DessinateurFX;
import Vue.RafraichissementFX;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author mariobap
 */
public class FenetreJeuController {    
    public void creerFenetreJeu(Stage stage){
        HBox h = new HBox();
        VBox v = new VBox();
        
	AnchorPane root = new AnchorPane();
        
        h.getChildren().add(root);
        h.getChildren().add(v);

	Scene scene = new Scene(h, 1200, 900);

	
	stage.setScene(scene);
	AnimationFX a = new AnimationFX();
	DessinateurFX d = new DessinateurFX(root, a);
        
        ConfigurationPartie.getConfigurationPartie().getPartie().getPlateau().accept(d);

	Historique histcoup = new Historique();
	EventHandler<KeyEvent> keypresser = new Keyboard_Handler(histcoup);
	scene.setOnKeyPressed(keypresser);

	//plateau.accept(d);
	RafraichissementFX r = new RafraichissementFX(d);
	r.start();
	stage.show();
    }
}
