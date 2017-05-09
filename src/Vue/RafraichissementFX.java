/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vue;

import Modele.Partie;
import javafx.animation.AnimationTimer;

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
	//i++;
	//if ((i % 1000) == 5) {
	if (partie.getPlateau().isEstModifié()) {
	    d.visite(partie.getPlateau());
	    partie.getPlateau().setEstModifié(false);
	}

	//}
    }
}
