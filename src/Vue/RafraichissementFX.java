/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vue;

import Modele.Joueur;
import Modele.Partie;
import Modele.Pinguin;
import Modele.Plateau;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;

/**
 *
 * @author liakopog
 */
public class RafraichissementFX extends AnimationTimer {

    DessinateurFX d;
    Partie partie;
    int i = 0;

    public RafraichissementFX(DessinateurFX d, Partie partie) {
	this.d = d;
	this.partie = partie;
    }

    @Override
    public void handle(long now) {
	if (partie.getPlateau().isEstModifié()) {

	    d.visite(partie.getPlateau());
	    partie.getPlateau().setEstModifié(false);
	}

	if (!partie.estTerminee()) {

	    if (partie.estEnInitialisation() && !partie.getJoueurCourant().getEstHumain()) {
		partie.getJoueurCourant().ajouterPinguin(partie.getJoueurCourant().etablirCoup(partie.getPlateau()));
		partie.getPlateau().setEstModifié(true);
		partie.joueurSuivant();

		if (partie.nbPingouinsTotal() == partie.getNbPingouinParJoueur() * partie.getJoueurs().size()) {
		    partie.setInitialisation(false);

		    for (Joueur j : partie.getJoueursEnJeu()) {
			j.setPret(Boolean.TRUE);
		    }
		    partie.getJoueurCourant().setPret(Boolean.TRUE);
		}
	    } else {
		if (!partie.getJoueurCourant().getEstHumain()) {
		    partie.getJoueurCourant().joueCoup(partie.getJoueurCourant().etablirCoup(partie.getPlateau()));
		    partie.getPlateau().setEstModifié(true);

		    partie.joueurSuivant();
		}
	    }

	    for (Joueur j : partie.getJoueurs()) {
		for (Pinguin p : j.getPinguinsVivants()) {
		    if (p.getPosition().getCasePossibles().size() == 0) {
			p.coullePinguin();
			partie.getPlateau().setEstModifié(true);
		    }
		}
	    }

	} else {
	    partie.afficheResultats();
	    Platform.exit();
	}
    }
}
