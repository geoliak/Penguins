/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import Controllers.ClicSourisFX;
import Models.Joueur;
import Models.JoueurHumain;
import Models.Partie;
import static Views.DessinateurFX.arrondirPourCases;
import static Views.DessinateurFX.drawrect;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author rozandq
 */
public class InterfaceFX extends Application{
    static Partie partie;
    static Canvas canvas;
    static Group root;
    static Scene scene;
    static double xcasecliquee;
    static double ycasecliquee;    
    DessinateurFX d;

    public Partie getPartie() {
        return partie;
    }
    
    public Canvas getCanvas() {
        return canvas;
    }

    public Group getRoot() {
        return root;
    }

    public Scene getScene() {
        return scene;
    }

    public DessinateurFX getD() {
        return d;
    }
    
    @Override
    public void start(Stage stage) throws InterruptedException {
        //System.out.println(partie);
	stage.setTitle("Gaufre");
	root = new Group();

	//Parent parent = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
	//root.getChildren().add(parent);
	canvas = new Canvas(800, 600);
	
	int xgaufre = 50;
	int ygaufre = 50;

	scene = new Scene(root);
	root.getChildren().add(canvas);
	stage.setScene(scene);
        
        DessinateurFX d = new DessinateurFX(canvas, root);
        this.d = d;
        RafraichissementFX r = new RafraichissementFX(this);
        
        d.dessinePlateau(partie.getPlateau(), partie, scene);
        
        r.start();
        
	//Mouse handler
	//scene.setOnMousePressed(new ClicSourisFX(partie));
        
        stage.show();
    }
    
    public static void creer(String[] args, Partie p) {
        partie = p;
        launch(args);
    }
    
}
