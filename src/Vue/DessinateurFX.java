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

import Controleur.MouseClickerCase;
import Controleur.MouseClickerPenguin;
import Modele.Case;
import Modele.Couleur;
import Modele.MyPolygon;
import Modele.Partie;
import Modele.Pinguin;
import Modele.Plateau;
import Modele.Visiteur;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 *
 * @author liakopog
 */
public class DessinateurFX extends Visiteur {

    private Partie partie;
    private GraphicsContext gc;
    private Group root;
    private double sizeGlacon;
    private double proportion;
    private int gap;
    private double size;
    private double height;
    private double width;

    public DessinateurFX(GraphicsContext gc, Group root, Partie partie) {
	this.gc = gc;
	this.root = root;

	sizeGlacon = 50.0;
	proportion = 1;
	gap = 4;
	size = sizeGlacon * proportion;
	height = size * 2;
	width = height * Math.sqrt(3 / 2);
	this.partie = partie;
    }

    @Override
    public void visite(Plateau plateau) {
	root.getChildren().clear();
	int rows = plateau.getNbLignes();
	int columns = plateau.getNbColonnes();

	for (Case[] cases : plateau.getCases()) {
	    for (Case c : cases) {
		c.setAccessible(Boolean.FALSE);
	    }
	}

	if (partie.getJoueurCourant() != null && partie.getJoueurCourant().getPinguinCourant() != null) {
	    ArrayList<Case> casesaccessibles = partie.getJoueurCourant().getPinguinCourant().getPosition().getCasePossibles();
	    for (Case c : casesaccessibles) {
		c.setAccessible(Boolean.TRUE);
	    }
	}

	for (int i = 0; i < rows; i++) {
	    for (int j = 0; j < columns; j++) {
		plateau.getCases()[i][j].accept(this);
	    }
	}

	for (int i = 0; i < rows; i++) {
	    for (int j = 0; j < columns; j++) {
		if (plateau.getCases()[i][j].getPinguin() != null) {
		    System.out.println("PINGOUIN EN " + i + " " + j);
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
		    color = Color.CHOCOLATE;
		    break;
		case 3:
		    color = Color.GRAY;
		    break;
	    }
	    MyPolygon p = new MyPolygon(c.getNumColonne(), c.getNumLigne(), sizeGlacon, proportion, gap, color);
	    EventHandler<? super MouseEvent> clicSourisFX = new MouseClickerCase(p, partie);
	    p.setOnMouseClicked(clicSourisFX);

	    c.setPolygon(p);

	    if (c.getAccessible()) {
		p.setEffect(new InnerShadow(40, partie.getJoueurCourant().getCouleur().getCouleurFX()));
	    }

	    root.getChildren().add(p);
	}
    }

    @Override
    public void visite(Pinguin p) {
	System.out.println(p.getGeneral().getCouleur() + "COULEUR" + Couleur.Noir);
	ImageView iv = new ImageView(p.getGeneral().getCouleur().getImage());

	iv.setPreserveRatio(true);
	iv.setFitHeight(height);

	double x = p.getPosition().getPolygon().getXorigine() + width / 2;
	double y = p.getPosition().getPolygon().getYorigine() + height / 2;

	//System.out.println(iv.getFitWidth());
	iv.setX(x - width / 4);
	iv.setY(y - iv.getFitHeight() * 0.8);

	EventHandler<? super MouseEvent> clicSourisPenguin = new MouseClickerPenguin(p, partie);
	iv.setOnMouseClicked(clicSourisPenguin);

	if (p.getGeneral().getPinguinCourant() == p) {
	    System.out.println("PINGOUIN SELECTED");
	    iv.setEffect(new Bloom());
	}

	root.getChildren().add(iv);
    }
}
