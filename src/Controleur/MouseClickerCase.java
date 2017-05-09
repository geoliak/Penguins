/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.Case;
import Modele.MyPolygon;
import Modele.Partie;
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
	    System.out.println("CLIC");
	    System.out.println(p.getY() + " " + p.getX());

	    // FIX IT
	    if (partie.getPlateau().getCases()[rowclic][columnclic].estCaseValideInit()) {
		partie.getJoueurCourant().ajouterPinguin(partie.getPlateau().getCases()[rowclic][columnclic]);
		partie.getPlateau().setEstModifié(true);
		partie.joueurSuivant();
		System.out.println("PINGOUINS TOTAL: " + partie.nbPingouinsTotal() + " PINGOUINS : " + partie.getNbPingouinParJoueur() * partie.getJoueurs().size());
		if (partie.nbPingouinsTotal() == partie.getNbPingouinParJoueur() * (partie.getJoueurs().size() + 1)) {
		    partie.setInitialisation(false);
		}
	    }
	} else {
	    if (partie.getJoueurCourant().getPinguinCourant() != null) {
		Case caseDest = partie.getPlateau().getCases()[rowclic][columnclic];
		if (caseDest.estCaseLibre() && caseDest.getAccessible()) {
		    partie.getJoueurCourant().getPinguinCourant().deplace(caseDest);
		    partie.joueurSuivant();
		    partie.getPlateau().setEstModifié(true);
		}
	    }
	}
    }
}
