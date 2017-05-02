/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package essaifx;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import java.lang.Math;

/**
 *
 * @author liakopog
 */
public class EssaiFX extends Application {

    static double xcasecliquee;
    static double ycasecliquee;

    @Override
    public void start(Stage stage) {
	stage.setTitle("Gaufre");
	Group root = new Group();

	//Parent parent = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
	//root.getChildren().add(parent);
	Canvas canvas = new Canvas(800, 600);
	GraphicsContext gc = canvas.getGraphicsContext2D();
	int xgaufre = 500 / 5;
	int ygaufre = 300 / 3;

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


		drawrect(((int) (x / xgaufre) * xgaufre), ((int) (y / ygaufre) * ygaufre), gc);
		arrondirPourCases(x, xgaufre, y, ygaufre);
		System.out.println(xcasecliquee + " cassseees " + ycasecliquee);

	    }
	});
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	launch(null);
    }

    public static void drawGaufre(GraphicsContext gc, Group root) {
	Image gaufre = new Image("/gaufre.jpg");

	/*
	 gc.drawImage(image1, 10, 10);
	 gc.setFill(Color.BROWN);
	 gc.setStroke(Color.BLUE);
	 gc.setLineWidth(5);
	 gc.fillRect(100, 100, 600, 400);
	 */
	Polygon p = new Polygon();

	p.setLayoutX(0);
	p.setLayoutY(0);

	//point(10,10)
	p.getPoints().add(10.0);
	p.getPoints().add(10.0);
	//point(510,10)
	p.getPoints().add(510.0);
	p.getPoints().add(10.0);
	//point(510,310)
	p.getPoints().add(510.0);
	p.getPoints().add(310.0);
	//point(10,310)
	p.getPoints().add(10.0);
	p.getPoints().add(310.0);

	p.setFill(new ImagePattern(gaufre, 0, 0, 1, 1, true));

	root.getChildren().add(p);

    }

    public static void drawrect(double x, double y, GraphicsContext gc) {
	gc.setFill(Color.WHITE);
	gc.fillRect(x, y, 10000, 10000);

    }

    public static void arrondirPourCases(double x, double taillex, double y, double tailley) {
	if ((x / taillex) == x % taillex) {
	    xcasecliquee = (x / taillex);
	} else {
	    xcasecliquee = ((int) (x / taillex));
	}

	if ((y / tailley) == y % tailley) {
	    ycasecliquee = (y / tailley);
	} else {
	    ycasecliquee = ((int) (y / tailley));
	}
    }
}
