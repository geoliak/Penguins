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

import Controleur.MouseClicker;
import Modele.Case;
import Modele.Plateau;
import Modele.Visiteur;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 *
 * @author liakopog
 */
public class DessinateurFX extends Visiteur {

    GraphicsContext gc;
    Group root;

    public DessinateurFX(GraphicsContext gc, Group root) {
	this.gc = gc;
	this.root = root;
    }

    @Override
    public void visite(Plateau plateau) {
	ArrayList<GridPane> ag = new ArrayList<>();

	int rows = plateau.getNbLignes();
	int hexes = plateau.getNbColonnes();
	double proportion = 1;
	int gap = 4;
	double size = 50.0 * proportion;
	double height = size * 2;
	double width = height*Math.sqrt(3/2);


	for (int row = 0; row < rows; row++) {

	    GridPane grid = new GridPane();
	    grid.setHgap(gap);
	    grid.relocate(((row % 2) * size) + gap/2 * (row % 2), (row * size*1.5) + gap * row);
	    for (int i = 0; i < hexes; i++) {
		Polygon p;
		if (plateau.getCases()[row][i].estCoulee()) {
		    //System.out.println("COULE");
		    p = poly(size, Color.WHITE);
		} else {
		    p = poly(size, Color.CYAN);
		}
		EventHandler<? super MouseEvent> clicSourisFX = new MouseClicker(p, grid, ag);
		p.setOnMouseClicked(clicSourisFX);
		grid.add(p, i, 0);
	    }
	    ag.add(grid);
	    root.getChildren().add(grid);
	}
    }

    public static Polygon poly(double size, Color color) {
	Polygon p = new Polygon();
	p.setFill(color);

	double height = size * 2;
	double width = height*Math.sqrt(3/2);

	//p.setStroke(Color.BLACK);

	p.setLayoutX(0);
	p.setLayoutY(0);
	//point(50,0)
	p.getPoints().add(width/2);
	p.getPoints().add(0.0);
	//point(0, 50)
	p.getPoints().add(0.0);
	p.getPoints().add(height/4);
	//point(0,100)
	p.getPoints().add(0.0);
	p.getPoints().add(3*(height/4));
	//point(50,150)
	p.getPoints().add(width/2);
	p.getPoints().add(height);
	//point(100,100)
	p.getPoints().add(width);
	p.getPoints().add(3*(height/4));
	//point(100,50)
	p.getPoints().add(width);
	p.getPoints().add(height/4);
	return p;
    }

    @Override
    public void visite(Case c) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /*
     public static void drawGaufre(GraphicsContext gc, Group root) {
     int rows = 8;
     int hexes = 14;
     double proportion = 0.8;

     for (int row = 0; row < rows; row++) {

     TilePane tile = new TilePane();
     //	tile.setHgap(0);
     tile.relocate(((row + 1) % 2) * proportion * 51, row * proportion * 100);
     tile.setPrefColumns(hexes);
     for (int i = 0; i < hexes; i++) {
     tile.getChildren().add(poly(proportion));
     }
     root.getChildren().add(tile);
     }
     }*/
}
