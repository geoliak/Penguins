/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

//import static EssaiFX.drawGaufre;
//import static EssaiFX.drawrect;
import Controllers.ClicSourisFX;
import Models.Case;
import Models.Partie;
import Models.Plateau;
import static essaifx.EssaiFX.arrondirPourCases;
import static essaifx.EssaiFX.drawGaufre;
import static essaifx.EssaiFX.drawrect;
import javafx.*;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

/**
 *
 * @author novelm
 */
public class DessinateurFX {
    Canvas canvas;
    GraphicsContext gc;
    static double xcasecliquee;
    static double ycasecliquee;  

    public DessinateurFX(Canvas canvas, Group root) {
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
    }

    public void dessinePlateau(Plateau plateau, Partie partie, Scene scene) {
        Group root = new Group();
	Image gaufre = new Image("/gaufre.jpg");
        
        //System.out.println("BLABLA");
        
        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        for (int i = 0; i < plateau.getLargeur(); i++) {
            for(int j = 0; j < plateau.getLongueur(); j++){
                if(plateau.getCases()[i][j].isJouable()){
                    //System.out.println(j + " "  + i);
                    
                    ImageView iv = new ImageView(gaufre);
                    iv.setFitHeight(50);
                    iv.setFitWidth(50);
                    //Button b = new Button();
                    EventHandler<? super MouseEvent> clicSourisFX = new ClicSourisFX(partie, grid, iv);
                    iv.setOnMouseClicked(clicSourisFX);
                    grid.add(iv, j, i);
                }
            }
        }

	root.getChildren().add(grid);
        scene.setRoot(root);
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