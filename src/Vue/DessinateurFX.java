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
import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.ImageInput;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

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
	//root.getChildren().clear();
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
		    plateau.getCases()[i][j].getPinguin().accept(this);
		}
	    }
	}
    }

    @Override
    public void visite(Case c) {
	if (c.getPinguin() != null && c.getCasePossibles().size() == 0) {
	    c.setCoulee(Boolean.TRUE);
	}
	if (!c.estCoulee() && c.getPolygon()==null) {
	    Color color = Color.IVORY;

	    switch (c.getNbPoissons()) {
		case 2:
		    color = Color.CHOCOLATE;
		    break;
		case 3:
		    color = Color.GRAY;
		    break;
	    }
	    //MyPolygon p = new MyPolygon(c.getNumColonne(), c.getNumLigne(), sizeGlacon, proportion, gap, color);
            c.setPolygon(new MyPolygon(c.getNumColonne(), c.getNumLigne(), sizeGlacon, proportion, gap, color));
	    EventHandler<? super MouseEvent> clicSourisFX = new MouseClickerCase(c.getPolygon(), partie);
	    c.getPolygon().setOnMouseClicked(clicSourisFX);

	    if (c.getAccessible() && partie.getJoueurCourant().getEstHumain()) {
		c.getPolygon().setEffect(new InnerShadow(40, partie.getJoueurCourant().getCouleur().getCouleurFX()));
	    }

	    root.getChildren().add(c.getPolygon());
	} else if(c.estCoulee() && c.getPolygon()!=null){
            System.out.println("BLABLA");
            c.getPolygon().setVisible(false);
        }
    }

    @Override
    public void visite(Pinguin p) {
	if (p.estVivant() && p.getIv() == null) {
	    ImageView iv = new ImageView(p.getGeneral().getCouleur().getImage());
            //ImageView iv = p.getIv();
            
	    iv.setPreserveRatio(true);
	    iv.setFitHeight(height);

	    double x = p.getPosition().getPolygon().getXorigine() + width / 2;
	    double y = p.getPosition().getPolygon().getYorigine() + height / 2;

	    //System.out.println(iv.getFitWidth());
	    iv.setX(x - width / 4);
	    iv.setY(y - iv.getFitHeight()*0.8);

	    EventHandler<? super MouseEvent> clicSourisPenguin = new MouseClickerPenguin(p, partie);
	    iv.setOnMouseClicked(clicSourisPenguin);
	    iv.setEffect(new DropShadow());

            p.setIv(iv);
	    root.getChildren().add(p.getIv());
	} else if (p.estVivant() && p.getIv() != null){
            AnimationFX a = new AnimationFX();
            a.mouvementImage(p.getPosition().getPolygon(), p.getIv(), p.getPosition().getNumColonne(), p.getPosition().getNumLigne());
            
            if (p.getGeneral().getPinguinCourant() == p && p.getGeneral() == partie.getJoueurCourant()) {
                System.out.println("POINT ORIGINE PINGOUIN : " + p.getIv().getX() + " " + p.getIv().getY());
		p.getIv().setEffect(new Bloom());
	    } else {
                p.getIv().setEffect(null);
            }
        } else if(!p.estVivant()) {
            System.out.println("MORT");
            FadeTransition ft = new FadeTransition(Duration.millis(3000), p.getIv());
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            ft.play();
        }
        
            
    }
}
