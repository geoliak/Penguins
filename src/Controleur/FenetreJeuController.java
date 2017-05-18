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
import javafx.animation.ScaleTransition;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

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
        setBottom(bas);

	Scene scene = new Scene(b, 1200, 900);
        b.setStyle("-fx-background-color: #addaf9;");
	
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
        Image grey = new Image(new File("./ressources/img/grey_star.png").toURI().toString());
        Image yellow = new Image(new File("./ressources/img/yellow_star.png").toURI().toString());
            
        for(Joueur j : joueurs){
            AnchorPane ap = new AnchorPane();
                    
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
            
            if(j.getDifficulte() != 0) {
                //étoiles
                ImageView etoile1 = new ImageView(yellow);
                etoile1.setLayoutX(130);
                etoile1.setLayoutY(155);
                ImageView etoile2 = new ImageView(grey);
                etoile2.setLayoutX(160);
                etoile2.setLayoutY(155);
                ImageView etoile3 = new ImageView(grey);
                etoile3.setLayoutX(190);
                etoile3.setLayoutY(155);


                if(j.getDifficulte() > 1){
                    etoile2.setImage(yellow);
                }

                if(j.getDifficulte() > 2){
                    etoile3.setImage(yellow);
                }
                
                ap.getChildren().addAll(etoile1, etoile2, etoile3);
            }
            
            ConfigurationPartie.getConfigurationPartie().setLabelScore(labelScore, j.getNumero());
            
            ((VBox) v).getChildren().add(ap);
        }
    }
    
    public void setBottom(Node n){
        VBox v = new VBox();
        HBox h = new HBox();
        File f = new File("ressources/img/img_menu/abandonner.png");
        ImageView abandonner = new ImageView(new Image(f.toURI().toString()));
        abandonner.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                abandonner.setEffect(new DropShadow());
            }
        });
        
        abandonner.setOnMouseExited(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {
                abandonner.setEffect(null);
            }
            
        });
        
        File f2 = new File("ressources/img/img_menu/volume.png");
        ImageView volume = new ImageView(new Image(f2.toURI().toString()));
        volume.setPreserveRatio(true);
        volume.setFitHeight(30);
        
        File f3 = new File("ressources/img/img_menu/note.png");
        ImageView note = new ImageView(new Image(f3.toURI().toString()));
        note.setPreserveRatio(true);
        note.setFitHeight(30);
        
        h.getChildren().addAll(volume, note);
        h.setAlignment(Pos.TOP_LEFT);
        h.setSpacing(20);
        h.setPadding(new Insets(0, 0, 0, 20));
        
        v.getChildren().addAll(abandonner, h);
        v.setAlignment(Pos.TOP_LEFT);
        v.setSpacing(20);
        
        ((HBox) n).getChildren().add(v);
        ((HBox) n).setAlignment(Pos.TOP_LEFT);
        ((HBox) n).setPadding(new Insets(0, 0, 20, 0));
        
        AnimationFX a = new AnimationFX();
        volume.setOnMouseEntered(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {
                a.scale(volume, 1.2, 200);
            }
            
        });
        volume.setOnMouseExited(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {
                a.scale(volume, 1, 200);
            }
            
        });
        
        note.setOnMouseEntered(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {
                a.scale(note, 1.2, 200);
            }
            
        });
        note.setOnMouseExited(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {
                a.scale(note, 1, 200);
            }
            
        });
        
    }
}
