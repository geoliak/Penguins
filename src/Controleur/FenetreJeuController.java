/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.AnnulerCoup;
import Modele.ConfigurationPartie;
import Vue.AnimationFX;
import Vue.DessinateurFX;
import Vue.RafraichissementFX;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author mariobap
 */
public class FenetreJeuController {
    
    public void creerFenetreJeu(Stage stage){
        
	Group root = new Group();

	Scene scene = new Scene(root, 1200, 900);

	scene.setFill(Color.AQUA);
	stage.setScene(scene);
	AnimationFX a = new AnimationFX();
	DessinateurFX d = new DessinateurFX(root, a);

	AnnulerCoup histcoup = new AnnulerCoup(ConfigurationPartie.getConfigurationPartie().getPartie());
	EventHandler<KeyEvent> keypresser = new Keyboard_Handler(histcoup);
	scene.setOnKeyPressed(keypresser);

	//plateau.accept(d);
	RafraichissementFX r = new RafraichissementFX(d);
	r.start();
	stage.show();
    }
}
