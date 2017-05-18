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
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
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

    public void creerFenetreJeu(Stage stage) {
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
        BackgroundImage bg = new BackgroundImage(new Image(new File("ressources/img/img_menu/banquise_fenetre_jeu.png").toURI().toString()), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
	b.setBackground(new Background(bg));
        
	stage.setScene(scene);
	//stage.initStyle(StageStyle.TRANSPARENT);
	ConfigurationPartie.getConfigurationPartie().setScene(scene);
	ConfigurationPartie.getConfigurationPartie().setRoot(root);
	ConfigurationPartie.getConfigurationPartie().setStage(stage);
	AnimationFX a = new AnimationFX();
	DessinateurFX d = new DessinateurFX(root, a);

	ConfigurationPartie.getConfigurationPartie().getPartie().getPlateau().accept(d);

	EventHandler<KeyEvent> keypresser = new Keyboard_Handler();
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
        Image ping = new Image(new File("ressources/img/pingouin_init.png").toURI().toString());
            
        ImageView[][] initpingoos = new ImageView[ConfigurationPartie.getConfigurationPartie().getPartie().getJoueurs().size()][ConfigurationPartie.getConfigurationPartie().getPartie().getNbPingouinParJoueur()];
        
        for(Joueur j : joueurs){
            System.out.println(j.getNumero());
            AnchorPane ap = new AnchorPane();
                    
            File f = new File("ressources/img/banniere_" + j.getCouleur().getNom() + ".png");
            Image imgBaniere = new Image(f.toURI().toString());
            
            ImageView ivBanniere = new ImageView(imgBaniere);
            ivBanniere.setLayoutX(0);
            ivBanniere.setLayoutY(50);
            ivBanniere.setFitHeight(150);
            ivBanniere.setPreserveRatio(true);

    public void setBannieresJoueurs(Node v) {
	ArrayList<Joueur> joueurs = ConfigurationPartie.getConfigurationPartie().getPartie().getJoueurs();
	Image grey = new Image(new File("./ressources/img/grey_star.png").toURI().toString());
	Image yellow = new Image(new File("./ressources/img/yellow_star.png").toURI().toString());

	for (Joueur j : joueurs) {
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

                if(j.getDifficulte() > 2){
                    etoile3.setImage(yellow);
                }
                
                ap.getChildren().addAll(etoile1, etoile2, etoile3);
            }
            
            ImageView[] init = new ImageView[ConfigurationPartie.getConfigurationPartie().getPartie().getNbPingouinParJoueur()];
            
            ImageView pInit;
            for(int i = 0; i < ConfigurationPartie.getConfigurationPartie().getPartie().getNbPingouinParJoueur(); i++){
                System.out.println("init");
                pInit = new ImageView(ping);
                pInit.setLayoutX(130 + 30*i);
                pInit.setLayoutY(65);
                pInit.setPreserveRatio(true);
                pInit.setFitHeight(30);
                ap.getChildren().add(pInit);
                init[i] = pInit;
            }
            initpingoos[j.getNumero()] = init;
            
            ConfigurationPartie.getConfigurationPartie().setInitpingoos(initpingoos);
            
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
                abandonner.setEffect(new Lighting());
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
        
        File f3 = new File("ressources/img/img_menu/note.jpg");
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

	abandonner.setOnMouseExited(new EventHandler<MouseEvent>() {

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
	volume.setOnMouseEntered(new EventHandler<MouseEvent>() {

	    @Override
	    public void handle(MouseEvent event) {
		a.scale(volume, 1.2, 200);
	    }

	});
	volume.setOnMouseExited(new EventHandler<MouseEvent>() {

	    @Override
	    public void handle(MouseEvent event) {
		a.scale(volume, 1, 200);
	    }

	});

	note.setOnMouseEntered(new EventHandler<MouseEvent>() {

	    @Override
	    public void handle(MouseEvent event) {
		a.scale(note, 1.2, 200);
	    }

	});
	note.setOnMouseExited(new EventHandler<MouseEvent>() {

	    @Override
	    public void handle(MouseEvent event) {
		a.scale(note, 1, 200);
	    }

	});

    }
}
