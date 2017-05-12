/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vue;

import Controleur.AnnulerCoup;
import Controleur.Keyboard_Handler;
import Modele.Couleur;
import Modele.IA.JoueurIA;
import Modele.IA.JoueurIA1;
import Modele.IA.JoueurIA5;
import Modele.Joueur;
import Modele.JoueurHumainLocal;
import Modele.Partie;
import Modele.Plateau;
import java.io.File;
import static java.time.Instant.now;
import java.util.ArrayList;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author liakopog
 */
public class InterfaceFX extends Application {

    static Plateau plateau;
    GraphicsContext gc;
    Group root;

    @Override
    public void start(Stage stage) {
	stage.setTitle("Nom du jeu");

	this.root = new Group();
//        Parent parent = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

	Canvas canvas = new Canvas(1200, 900);
	this.gc = canvas.getGraphicsContext2D();

//        root.getChildren().add(parent);
	Scene scene = new Scene(root);
	root.getChildren().add(canvas);
	scene.setFill(Color.AQUA);
	stage.setScene(scene);

//	File f = new File("ressources/img/pango.png");
	//Image img = new Image(f.toURI().toString());
	//plateau.getCases()[4][4].setPinguin(new Pinguin(plateau.getCases()[4][4], new JoueurHumainLocal("Quentin", Couleur.Rouge), img));
	plateau.initCase();

	JoueurHumainLocal joueurH1 = new JoueurHumainLocal("Jean", Couleur.VertFX);
	JoueurHumainLocal joueurH2 = new JoueurHumainLocal("Pierre", Couleur.RougeFX);

	JoueurIA joueuria = new JoueurIA5(Couleur.RougeFX);

	ArrayList<Joueur> joueurs = new ArrayList<>();
	joueurs.add(joueurH1);
	joueurs.add(joueuria);

	System.out.println(joueurs.size());

	Partie partie = new Partie(plateau, joueurs);
	DessinateurFX d = new DessinateurFX(gc, root, partie);

	//plateau.accept(d);
	RafraichissementFX r = new RafraichissementFX(d, partie);

	//Initialisation d'Annuler coup et de capteur de clavier
	AnnulerCoup histcoup = new AnnulerCoup(partie);
	EventHandler<KeyEvent> keypresser = new Keyboard_Handler(histcoup, partie, this, stage);
	scene.setOnKeyPressed(keypresser);

	r.start();
	stage.show();
    }

    public void continuer(Stage stage, Partie partie) {
	stage.setTitle("Nom du jeu");

	this.root = new Group();
//        Parent parent = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

	Canvas canvas = new Canvas(1200, 900);
	this.gc = canvas.getGraphicsContext2D();

	Scene scene = new Scene(root);
	root.getChildren().add(canvas);
	scene.setFill(Color.AQUA);
	stage.setScene(scene);

//	File f = new File("ressources/img/pango.png");
	//Image img = new Image(f.toURI().toString());
	//plateau.getCases()[4][4].setPinguin(new Pinguin(plateau.getCases()[4][4], new JoueurHumainLocal("Quentin", Couleur.Rouge), img));
	plateau.initCase();

	JoueurHumainLocal joueurH1 = new JoueurHumainLocal("Jean", Couleur.VertFX);
	JoueurHumainLocal joueurH2 = new JoueurHumainLocal("Pierre", Couleur.RougeFX);

	JoueurIA joueuria = new JoueurIA5(Couleur.RougeFX);

//	partie = new Partie(plateau, joueurs);
	DessinateurFX d = new DessinateurFX(gc, root, partie);
	RafraichissementFX r = new RafraichissementFX(d, partie);
	d.visite(plateau);
	plateau.setEstModifié(true);

	r.start();
	stage.show();

    }

    /**
     * @param args the command line arguments
     * @param p
     */
    public static void creer(String[] args, Plateau p) {
	plateau = p;
	launch(args);
    }

}
