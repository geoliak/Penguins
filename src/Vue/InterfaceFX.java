/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vue;

import Controleur.AnnulerCoup;
import Controleur.Keyboard_Handler;
import Modele.*;
import Modele.IA.JoueurIA;
import Modele.IA.JoueurIA3;
import Modele.IA.JoueurIA5;
import Modele.IA.JoueurIA7;
import Modele.IA.JoueurIA8;
import Modele.IA.JoueurMinimax;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 *
 * @author liakopog
 */
public class InterfaceFX extends Application {

    static Plateau plateau;
    static Label labelScores[] = new Label[4];
    static ImageView imagePing[] = new ImageView[4]; // imageViews pour stocker l'image des pingouins à afficher derrière la banniere score
    static Partie partie;
    static Group root;

    @Override
    public void start(Stage stage) throws FileNotFoundException {
	System.out.println("start");
	stage.setTitle("Nom du jeu");
        BorderPane borderP = new BorderPane();
        borderP.setBackground(new Background(new BackgroundFill(Color.DODGERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        borderP.setPadding(new Insets(0, 0, 0, 0));
	root = new Group();
        Rectangle rect = new Rectangle(0, -40, 30, 30);
        rect.setFill(Color.DODGERBLUE);
        root.getChildren().add(rect);

	Scene scene = new Scene(borderP, 1200, 900);

	//root.getChildren().add(canvas);
	scene.setFill(Color.DODGERBLUE);
	
      
        borderP.setCenter(root);
        VBox v = new VBox();
        v.setAlignment(Pos.CENTER);
        
        JoueurHumainLocal joueurH1 = new JoueurHumainLocal("Jean", Couleur.VertFX, 1);
	JoueurIA joueurIA8 = new JoueurIA8(Couleur.RougeFX, 2);
        JoueurIA joueurIA3 = new JoueurIA3(Couleur.VioletFX, 3);
        JoueurIA joueurIA5 = new JoueurIA5(Couleur.JauneFX, 4);
        JoueurIA joueurIA7 = new JoueurIA7(Couleur.JauneFX, 4);

        JoueurIA joueurIAMinimax = new JoueurMinimax(Couleur.RougeFX, 2);

	ArrayList<Joueur> joueurs = new ArrayList<>();
        joueurs.add(joueurH1);
        joueurs.add(joueurIAMinimax);
        
        for(Joueur j : joueurs) {
            v.getChildren().add(setBanner(j, j.getCouleur().getNom()));
        }
        
        /*v.getChildren().add(setBanner(joueurH1, "verte"));
        joueurs.add(joueurH1);
        v.getChildren().add(setBanner(joueurIA8, "rouge"));
	joueurs.add(joueurIA8);   
        v.getChildren().add(setBanner(joueurIA3, "violette"));
        joueurs.add(joueurIA3);   
        v.getChildren().add(setBanner(joueurIA5, "jaune"));
        joueurs.add(joueurIA5); */
        
        borderP.setRight(v);
        System.out.println("VBOX AJOUTEE");
	//Image img = new Image(f.toURI().toString());
	//plateau.getCases()[4][4].setPinguin(new Pinguin(plateau.getCases()[4][4], new JoueurHumainLocal("Quentin", Couleur.Rouge), img));
	//plateau.initCase();
	System.out.println(joueurs.size());

	InterfaceFX.partie = new Partie(plateau, joueurs);
	AnimationFX a = new AnimationFX();
	DessinateurFX d = new DessinateurFX(root, a);
	//plateau.accept(d);
	RafraichissementFX r = new RafraichissementFX(d, this);
	//Initialisation d'Annuler coup et de capteur de clavier
	AnnulerCoup histcoup = new AnnulerCoup(partie);
	EventHandler<KeyEvent> keypresser = new Keyboard_Handler(histcoup);
	scene.setOnKeyPressed(keypresser);
        stage.setScene(scene);
        stage.getIcons().add(new Image(new FileInputStream("ressources/img/pingoo_jaune.png")));
	r.start();
	stage.show();
    }

    /**
     * @param args the command line arguments
     * @param p
     */
    public static void creer(String[] args, Plateau p) {
	System.out.println("creer");
	plateau = p;
	launch(args);
    }

    public static Partie getPartie() {
	return partie;
    }

    public static void setPartie(Partie partie) {
	InterfaceFX.partie = partie;
    }

    public static Group getRoot() {
	return root;
    }
    public static Label[] getLabelScores() {
        return labelScores;
    }
    
    public static AnchorPane setBanner(Joueur j, String s) throws FileNotFoundException{
        AnchorPane ap = new AnchorPane();
        
        Image imgBaniere = new Image(new FileInputStream("ressources/img/banniere_"+s+".png"));
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
        System.out.println("J.getNumero() = " + j.getNumero());
        labelScores[j.getNumero()-1]= labelScore;
        
        return ap;
    }
}
