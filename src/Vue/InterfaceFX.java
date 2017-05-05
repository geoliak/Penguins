/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vue;

import Modele.Couleur;
import Modele.JoueurHumainLocal;
import Modele.Pinguin;
import Modele.Plateau;
import java.io.File;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author liakopog
 */
public class InterfaceFX extends Application {

    static Plateau plateau;

    @Override
    public void start(Stage stage) {
	stage.setTitle("Nom du jeu");

	Group root = new Group();
//        Parent parent = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

	Canvas canvas = new Canvas(1200, 900);
	GraphicsContext gc = canvas.getGraphicsContext2D();

//        root.getChildren().add(parent);
	DessinateurFX d = new DessinateurFX(gc, root);

	Scene scene = new Scene(root);
	root.getChildren().add(canvas);
	scene.setFill(Color.AQUA);
	stage.setScene(scene);

	File f = new File("ressources/img/pango.png");
        File f2 = new File(".");
        System.out.println(f2.getAbsolutePath());
	Image img = new Image(f.toURI().toString());
	plateau.getCases()[4][4].setPinguin(new Pinguin(plateau.getCases()[0][0], new JoueurHumainLocal("Quentin", Couleur.Rouge), img));

	plateau.accept(d);

	//EventHandler<? super MouseEvent> clicSourisFX = new MouseClicker(scene, grid, ag);
	//p.setOnMouseClicked(clicSourisFX);

	stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void creer(String[] args, Plateau p) {
	plateau = p;
	launch(args);
    }

}
