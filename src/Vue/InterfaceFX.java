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

import Modele.Plateau;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
	stage.setScene(scene);

	plateau.accept(d);

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
