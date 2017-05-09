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
import Controleur.MouseClickerPenguin;
import Modele.Case;
import Modele.MyPolygon;
import Modele.Pinguin;
import Modele.Plateau;
import Modele.Visiteur;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 *
 * @author liakopog
 */
public class DessinateurFX extends Visiteur {

    private GraphicsContext gc;
    private Group root;
    private double sizeGlacon;
    private double proportion;
    private int gap;
    private double size;
    private double height;
    private double width;

    public DessinateurFX(GraphicsContext gc, Group root) {
	this.gc = gc;
	this.root = root;

	sizeGlacon = 50.0;
	proportion = 1;
	gap = 4;
	size = sizeGlacon * proportion;
	height = size * 2;
	width = height * Math.sqrt(3 / 2);

    }

    @Override
    public void visite(Plateau plateau) {
	ArrayList<GridPane> ag = new ArrayList<>();

	int rows = plateau.getNbLignes();
	int columns = plateau.getNbColonnes();

	for (int i = 0; i < rows; i++) {
	    for (int j = 0; j < columns; j++) {
		plateau.getCases()[i][j].accept(this);
	    }
	}

	for (int i = 0; i < rows; i++) {
	    for (int j = 0; j < columns; j++) {
		if (plateau.getCases()[i][j].getPinguin() != null) {
		    System.out.println("BLABLA " + i + " " + j);

		    plateau.getCases()[i][j].getPinguin().accept(this);
		}
	    }
	}
    }

    @Override
    public void visite(Case c) {
	if (!c.estCoulee()) {
	    Color color = Color.IVORY;
	    switch (c.getNbPoissons()) {
		case 2:
		    color.darker();
		    break;
		case 3:
		    color.darker();
		    color.darker();
		    break;
	    }
	    MyPolygon p = new MyPolygon(c.getNumColonne(), c.getNumLigne(), sizeGlacon, proportion, gap, color);
	    EventHandler<? super MouseEvent> clicSourisFX = new MouseClicker(p);
	    p.setOnMouseClicked(clicSourisFX);

	    c.setPolygon(p);

	    root.getChildren().add(p);
	}
    }

    @Override
    public void visite(Pinguin p) {
	ImageView iv = new ImageView(p.getImage());

	iv.setPreserveRatio(true);
	iv.setFitHeight(height);

	double x = p.getPosition().getPolygon().getXorigine() + width / 2;
	double y = p.getPosition().getPolygon().getYorigine() + height / 2;

	System.out.println(iv.getFitWidth());

	iv.setX(x - width / 4);
	iv.setY(y - iv.getFitHeight() * 0.8);

	EventHandler<? super MouseEvent> clicSourisPenguin = new MouseClickerPenguin(p);
	iv.setOnMouseClicked(clicSourisPenguin);

	root.getChildren().add(iv);
    }
}
