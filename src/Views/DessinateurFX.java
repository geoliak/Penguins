/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import static EssaiFX.drawGaufre;
import static EssaiFX.drawrect;
import Models.Plateau;
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
 * @author novelm
 */
public class DessinateurFX extends Application {
    private Plateau plateau;

    public DessinateurFX(Plateau plateau) {
        this.plateau = plateau;
    }
    
    public void init(){
        
    }

    @Override
    public void start(Stage stage) {
	stage.setTitle("Gaufre");
	Group root = new Group();

	//Parent parent = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
	//root.getChildren().add(parent);
	Canvas canvas = new Canvas(800, 600);
	GraphicsContext gc = canvas.getGraphicsContext2D();
	//int xgaufre = 500 / xgaufrecases();
	//int ygaufre = 300 / ygaufrecases();
	//int nbcases

	drawGaufre(gc, root);

	Scene scene = new Scene(root);
	root.getChildren().add(canvas);
	stage.setScene(scene);

	stage.show();

	//Mouse handler
	scene.setOnMousePressed(new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent event) {
		System.out.println("mouse click detected! " + event.getSource());
		double x = event.getX();
		double y = event.getY();
		System.out.println(x + " " + y);
		drawrect(x, y, gc);
	    }
	});
    }
}
