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
import Modele.IA.JoueurIA3;
import Modele.IA.JoueurIA5;
import Modele.IA.JoueurIA8;
import Modele.IA.JoueurIASauveQuiPeut;
import Modele.IA.JoueurIAchercheIlot;
import Modele.Joueur;
import Modele.JoueurHumainLocal;
import Modele.Partie;
import Modele.Plateau;
import java.util.ArrayList;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author liakopog
 */
public class InterfaceFX extends Application {

    static Plateau plateau;
    static Partie partie;
    static Group root;

    @Override
    public void start(Stage stage) {
	System.out.println("start");
	stage.setTitle("Nom du jeu");

	root = new Group();
//        Parent parent = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

	//Canvas canvas = new Canvas(1200, 900);
	//GraphicsContext gc = canvas.getGraphicsContext2D();
//        root.getChildren().add(parent);
	Scene scene = new Scene(root, 1200, 900);

	//root.getChildren().add(canvas);
	scene.setFill(Color.AQUA);
	stage.setScene(scene);

	//Image img = new Image(f.toURI().toString());
	//plateau.getCases()[4][4].setPinguin(new Pinguin(plateau.getCases()[4][4], new JoueurHumainLocal("Quentin", Couleur.Rouge), img));
	//plateau.initCase();
	JoueurHumainLocal joueurH1 = new JoueurHumainLocal("Jean", Couleur.JauneFX);
	JoueurHumainLocal joueurH2 = new JoueurHumainLocal("Pierre", Couleur.RougeFX);

	JoueurIA joueuria = new JoueurIA8(Couleur.RougeFX);

	ArrayList<Joueur> joueurs = new ArrayList<>();
	joueurs.add(joueurH1);
	joueurs.add(joueuria);

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

}
