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
import Modele.MyPolygon;
import Modele.Plateau;
import Modele.Visiteur;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 *
 * @author liakopog
 */
public class DessinateurFX extends Visiteur {

    GraphicsContext gc;
    Group root;
    double sizeGlacon = 50.0;
    double proportion = 1;
    int gap = 4;
    double size = sizeGlacon * proportion;
    double height = size * 2;
    double width = height*Math.sqrt(3/2);

    public DessinateurFX(GraphicsContext gc, Group root) {
	this.gc = gc;
	this.root = root;
    }

    @Override
    public void visite(Plateau plateau) {
	ArrayList<GridPane> ag = new ArrayList<>();

	int rows = plateau.getNbLignes();
	int columns = plateau.getNbColonnes();

	for(int i = 0; i < rows; i++){
	    for(int j = 0; j < columns; j++){
		plateau.getCases()[i][j].accept(this);
	    }
	}
    }

    @Override
    public void visite(Case c) {
	MyPolygon p = new MyPolygon(c.getNumLigne(), c.getNumColonne(), sizeGlacon);
	EventHandler<? super MouseEvent> clicSourisFX = new MouseClicker(p);
	p.setOnMouseClicked(clicSourisFX);

	root.getChildren().add(p);
    }
}
