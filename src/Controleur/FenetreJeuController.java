/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.ConfigurationPartie;
import Modele.Historique;
import Modele.Joueur;
import Vue.AnimationFX;
import Vue.DessinateurFX;
import Vue.RafraichissementFX;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 *
 * @author mariobap
 */
public class FenetreJeuController {    
    public void creerFenetreJeu(Stage stage){
        BorderPane b = new BorderPane();
        
        VBox scores = new VBox();
        scores.setSpacing(30);
        scores.setAlignment(Pos.CENTER);
        
        HBox bas = new HBox();
        
	AnchorPane root = new AnchorPane();
        
        b.setCenter(root);
        b.setRight(scores);
        b.setBottom(bas);
        
        setBannieresJoueurs(scores);

	Scene scene = new Scene(b, 1200, 900);
	
	stage.setScene(scene);
        ConfigurationPartie.getConfigurationPartie().setScene(scene);
        ConfigurationPartie.getConfigurationPartie().setRoot(root);
        ConfigurationPartie.getConfigurationPartie().setStage(stage);
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
    
    public void setBannieresJoueurs(Node v){
        ArrayList<Joueur> joueurs = ConfigurationPartie.getConfigurationPartie().getPartie().getJoueurs();
        for(Joueur j : joueurs){
            System.out.println(j.getNumero());
            AnchorPane ap = new AnchorPane();
            
            System.out.println(j.getNumero());
                    
            File f = new File("ressources/img/banniere_" + j.getCouleur().getNom() + ".png");
            Image imgBaniere = new Image(f.toURI().toString());
            
            ImageView ivBanniere = new ImageView(imgBaniere);
            ivBanniere.setLayoutX(0);
            ivBanniere.setLayoutY(50);
            ivBanniere.setFitHeight(150);
            ivBanniere.setPreserveRatio(true);

            Label labelNom = new Label();
            labelNom.setLayoutX(150);
            labelNom.setLayoutY(115);
            labelNom.setText(j.getNom());

            Label labelScore = new Label();
            labelScore.setLayoutX(50); 
            labelScore.setLayoutY(115);
            labelScore.setText(Integer.toString(j.getScorePoissons()));
            labelScore.setTextFill(Color.WHITE); 

            ap.getChildren().addAll(ivBanniere, labelNom, labelScore);
            
            ConfigurationPartie.getConfigurationPartie().setLabelScore(labelScore, j.getNumero());
            
            ((VBox) v).getChildren().add(ap);
        }
    }
}
