/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vue;

import Controleur.MouseClickerCase;
import Controleur.MouseClickerPenguin;
import Modele.Case;
import Modele.ConfigurationPartie;
import Modele.Demo;
import Modele.Joueur;
import Modele.MyPolygon;
import Modele.Partie;
import Modele.Pinguin;
import Modele.Plateau;
import Modele.Visiteur;
import java.util.ArrayList;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.ImageView;
import Modele.MyImageView;
import java.io.File;
import javafx.animation.Animation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

/**
 *
 * @author liakopog
 */
public class DessinateurFX extends Visiteur {

    private AnimationFX a;
    private Partie partie;
    private Node root;
    private double sizeGlacon;
    private double proportion;
    private int gap;
    private double size;
    private double height;
    private double width;
    private Joueur joueurCourant;

    public DessinateurFX(Node root, AnimationFX a) {
	this.root = root;
	this.a = a;
	this.partie = ConfigurationPartie.getConfigurationPartie().getPartie();
	sizeGlacon = 50.0;
	proportion = 1;
	gap = 4;
	size = sizeGlacon * proportion;
	height = size * 2;
	width = height * Math.sqrt(3 / 2);
        joueurCourant = partie.getJoueurCourant();
        a.scale(ConfigurationPartie.getConfigurationPartie().getLabelScores()[joueurCourant.getNumero()].getParent(), 1.2, 200);
        
        if(partie.getDemo() != null){
            Label label = new Label();
            label.setId("consigne");
            ((AnchorPane) this.root).getChildren().add(label);
            label.setLayoutX(100);
            label.setLayoutY(100);
        }
    }

    @Override
    public void visite(Plateau plateau) {
	int rows = plateau.getNbLignes();
	int columns = plateau.getNbColonnes();
        
        if(joueurCourant != partie.getJoueurCourant() && !partie.estTerminee()){
            Transition t1 = a.scale(ConfigurationPartie.getConfigurationPartie().getLabelScores()[joueurCourant.getNumero()].getParent(), 1, 200);
            Transition t2 = a.scale(ConfigurationPartie.getConfigurationPartie().getLabelScores()[partie.getJoueurCourant().getNumero()].getParent(), 1.2, 200);
            
            joueurCourant = partie.getJoueurCourant();
        } else if(partie.estTerminee()){
            Transition t3 = a.scale(ConfigurationPartie.getConfigurationPartie().getLabelScores()[joueurCourant.getNumero()].getParent(), 1, 200);
        }

	if (ConfigurationPartie.getConfigurationPartie().getPartie().isReloadPartie()) {
	    //System.out.println("===================RELOAD=======================");
	    this.partie = ConfigurationPartie.getConfigurationPartie().getPartie();

	    for (Case[] cases : plateau.getCases()) {
		for (Case c : cases) {
//		    c.getPolygon().setImage(null);
		    c.setPolygon(null);
		}
	    }

	    for (int i = 0; i < rows; i++) {
		for (int j = 0; j < columns; j++) {
		    if (plateau.getCases()[i][j].getPinguin() != null) {
			plateau.getCases()[i][j].getPinguin().setIv(null);
		    }
		}
	    }
	    partie.setReloadPartie(false);
	    ((AnchorPane) root).getChildren().clear();
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
//	if (c.getPinguin() != null && c.getCasePossibles().size() == 0) {
//	    c.setCoulee(Boolean.TRUE);
//	}

	if (!c.estCoulee() && c.getPolygon() == null) {
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
	    c.setPolygon(new MyPolygon(c.getNumColonne(), c.getNumLigne(), sizeGlacon, proportion, gap, color, c.getNbPoissons()));

	    EventHandler<? super MouseEvent> clicSourisFX = new MouseClickerCase(c.getPolygon());
	    c.getPolygon().getImage().setOnMouseClicked(clicSourisFX);
//	    EventHandler<? super MouseEvent> clicSourisFX = new MouseClickerCase(c.getPolygon(), partie);
//	    c.getPolygon().setOnMouseClicked(clicSourisFX);

	    ((AnchorPane) root).getChildren().add(c.getPolygon().getImage());
	} else if (!c.estCoulee() && c.getPolygon() != null && !partie.getInitialisation()){
            if (c.getAccessible() && partie.getJoueurCourant().getEstHumain()) {
                c.getPolygon().getImage().setEffect(new InnerShadow(40, partie.getJoueurCourant().getCouleur().getCouleurFX()));
            } else {
                c.getPolygon().getImage().setEffect(null);
            }
        } else if(c.estCoulee() && c.getPolygon()!=null && c.getPinguin() == null){
            a.efface(c.getPolygon().getImage());
            c.setPolygon(null);
        }
    }

    @Override
    public void visite(Pinguin p) {
        //System.out.println("PINGOUIN DESSIN");
	if (p.estVivant() && p.getIv() == null) {
            //System.out.println("premier dessin");
	    MyImageView iv = new MyImageView(p.getGeneral().getCouleur().getImage());
	    //MyImageView iv = p.getIv();

	    iv.setPreserveRatio(true);
	    iv.setFitHeight(height * 0.8);

	    double x = p.getPosition().getPolygon().getXorigine() + width / 2;
	    double y = p.getPosition().getPolygon().getYorigine() + height / 2;

	    //System.out.println(iv.getFitWidth());
	    iv.setX(x - width / 2.8);
	    iv.setY(y - iv.getFitHeight());

	    EventHandler<? super MouseEvent> clicSourisPenguin = new MouseClickerPenguin(p, partie);
	    iv.setOnMouseClicked(clicSourisPenguin);
//	    iv.setEffect(new DropShadow());

	    p.setIv(iv);
	    ((AnchorPane) root).getChildren().add(p.getIv());
	    partie.setTourFini(true);
	} else if (p.estVivant() && p.getIv() != null) {

	    if (!partie.estEnInitialisation()) {
		if (partie.getJoueurCourant().getPinguinCourant() == p && partie.isTourFini()) {
		    partie.setTourFini(false);
		}
		Transition t = a.mouvementImage(p.getPosition().getPolygon(), p.getIv(), p.getPosition().getNumColonne(), p.getPosition().getNumLigne(), sizeGlacon, proportion);
                
		t.setOnFinished(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
			partie.setTourFini(true);
		    }
		});
	    }

	    if (p.getGeneral().getPinguinCourant() == p && p.getGeneral() == partie.getJoueurCourant() && partie.getJoueurCourant().getEstHumain()) {
		//System.out.println("POINT ORIGINE PINGOUIN : " + p.getIv().getX() + " " + p.getIv().getY());
		p.getIv().setEffect(new Bloom());
	    } else {
		p.getIv().setEffect(null);
	    }
	} else if (!p.estVivant() && p.getIv() != null) {
	    //a.efface(p.getIv());
            Transition t = a.mouvementImage(p.getPosition().getPolygon(), p.getIv(), p.getPosition().getNumColonne(), p.getPosition().getNumLigne(), sizeGlacon, proportion);
            t.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    partie.setTourFini(true);
                }
            });
            p.getIv().setEffect(new SepiaTone());
	    p.getPosition().getPolygon().getImage().setEffect(null);
            for(int i = ConfigurationPartie.getConfigurationPartie().getPartie().getNbPingouinParJoueur()-1; i > p.getGeneral().getPinguinsVivants().size()-1; i--){
                ConfigurationPartie.getConfigurationPartie().getInitpingoos()[p.getGeneral().getNumero()][i].setVisible(false);
            }
	}
    }
    
    public void visit(Joueur j) {
        try{
            Image ping = new Image(new File("ressources/img/pingoo.png").toURI().toString());
            
            ConfigurationPartie.getConfigurationPartie().getLabelScores()[j.getNumero()].setText(""+j.getScorePoissons());
            
            //ImageView[][] ivs = ;
            
            //int nbPingouinsParJoueur = ConfigurationPartie.getConfigurationPartie().getPartie().getNbPingouinParJoueur();
            for(int i = 0; i < j.getPinguins().size(); i++){
                ConfigurationPartie.getConfigurationPartie().getInitpingoos()[j.getNumero()][i].setImage(ping);
            }
        }
        catch (Exception e){
            System.out.println("erreur - visisteScore : " + e.getMessage());
        }
    }
    
    public void visit(Demo d){
        Label label = (Label) ((AnchorPane) this.root).lookup("#consigne");
        if(!partie.getDemo().getConsigne().equalsIgnoreCase(label.getText())){
            label.setText("");

            a.AnimateText(label, partie.getDemo().getConsigne());
        }
    }
    
    

    public void setPartie(Partie partie) {
	this.partie = partie;
    }
}
