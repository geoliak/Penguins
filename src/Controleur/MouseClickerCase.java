/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.Case;
import Modele.Joueur;
import Modele.MyPolygon;
import Modele.Partie;
import Modele.Pinguin;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author liakopog
 */
public class MouseClickerCase implements EventHandler<MouseEvent> {

    Partie partie;
    MyPolygon p;
    int rowclic;
    int columnclic;

    int size;

    public MouseClickerCase(MyPolygon p, Partie partie) {
	this.p = p;
	this.partie = partie;
    }

    @Override
    public void handle(MouseEvent event) {
	rowclic = p.getY();
	columnclic = p.getX();
	if (partie.estEnInitialisation()) {
	    //System.out.println("CLIC");
	    //System.out.println(p.getY() + " " + p.getX());

	    // FIX IT
	    if (partie.getPlateau().getCases()[rowclic][columnclic].estCaseValideInit()) {
		partie.getJoueurCourant().ajouterPinguin(partie.getPlateau().getCases()[rowclic][columnclic]);
		partie.getPlateau().setEstModifié(true);
		partie.joueurSuivant();
		//System.out.println("PINGOUINS AJOUTES: " + partie.nbPingouinsTotal() + " PINGOUINS MAX: " + partie.getNbPingouinParJoueur() * partie.getJoueurs().size());
		//System.out.println("=================" + partie.getNbPingouinParJoueur() + " " + partie.getJoueurs().size());
		if (partie.nbPingouinsTotal() == partie.getNbPingouinParJoueur() * partie.getJoueurs().size()) {
		    partie.setInitialisation(false);

		    for (Joueur j : partie.getJoueursEnJeu()) {
			j.setPret(Boolean.TRUE);
		    }
		    partie.getJoueurCourant().setPret(Boolean.TRUE);
		}
	    }
	} else {
	    Pinguin pingouin = partie.getJoueurCourant().getPinguinCourant();
	    if (pingouin != null) {
		Case caseDest = partie.getPlateau().getCases()[rowclic][columnclic];
		if (caseDest.estCaseLibre() && caseDest.getAccessible()) {
		    pingouin.deplace(caseDest);

		    if (pingouin.getPosition().getCasePossibles().size() == 0) {
			pingouin.setVivant(Boolean.FALSE);
		    }

		    partie.joueurSuivant();
		    partie.getPlateau().setEstModifié(true);
		}
	    }
	}
    }
}
