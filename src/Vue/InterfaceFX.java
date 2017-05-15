/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vue;

import Modele.Couleur;
import Modele.IA.JoueurIA;
import Modele.IA.JoueurIA1;
import Modele.IA.JoueurIA3;
import Modele.IA.JoueurIA5;
import Modele.IA.JoueurIA8;
import Modele.Joueur;
import Modele.JoueurHumainLocal;
import Modele.Partie;
import Modele.Plateau;
import java.io.File;
import java.util.ArrayList;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author liakopog
 */
public class InterfaceFX extends Application {

    static Plateau plateau;
    private static Stage stage;

    @Override
    public void start(Stage primaryStage) {
        if(stage == null){
            stage = primaryStage;
        }
	stage.setTitle("Nom du jeu");

	Group root = new Group();
//        Parent parent = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

	//Canvas canvas = new Canvas(1200, 900);
	//GraphicsContext gc = canvas.getGraphicsContext2D();

//        root.getChildren().add(parent);
	Scene scene = new Scene(root, 1200,900);
        
	//root.getChildren().add(canvas);
	scene.setFill(Color.AQUA);
	stage.setScene(scene);

	//Image img = new Image(f.toURI().toString());
	//plateau.getCases()[4][4].setPinguin(new Pinguin(plateau.getCases()[4][4], new JoueurHumainLocal("Quentin", Couleur.Rouge), img));
	plateau.initCase();

	JoueurHumainLocal joueurH1 = new JoueurHumainLocal("Jean", Couleur.JauneFX);
	JoueurHumainLocal joueurH2 = new JoueurHumainLocal("Pierre", Couleur.RougeFX);
        
        JoueurIA joueuria = new JoueurIA5(Couleur.RougeFX);

	ArrayList<Joueur> joueurs = new ArrayList<>();
	joueurs.add(joueurH1);
	joueurs.add(joueuria);

	System.out.println(joueurs.size());

	Partie partie = new Partie(plateau, joueurs);
        AnimationFX a = new AnimationFX();
	DessinateurFX d = new  DessinateurFX(root, partie, a);

	//plateau.accept(d);
	RafraichissementFX r = new RafraichissementFX(d, partie);
	r.start();
	stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void creer(String[] args, Plateau p) {
	       creer(args, p, null);
    }
    
    public static void creer(String[] args, Plateau p, Stage s) {
        plateau = p;
        stage = s;
        System.out.println("creer -> launch");
	launch(args);
    }

}
