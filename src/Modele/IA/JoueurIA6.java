/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA;

import Modele.Case;
import Modele.Couleur;
import Modele.Partie;

/**
 *
 * @author novelm
 */
public class JoueurIA6 extends JoueurIA {

    public JoueurIA6(Couleur couleur, int numero) {
	super(couleur, "JoueurIA6", numero);
    }

    @Override
    public Case phaseInitialisation(Partie partie) {
	return super.phaseInitialisationMaxPossibilitee(partie);
    }

    @Override
    public Case phaseJeu(Partie partie) {
	//On regarde si on peut éliminer un pinguin
	Case caseChoisie = null;

	caseChoisie = this.chercherVictimeIlot(partie);
	if (caseChoisie != null) {
	    return caseChoisie;

	}

	return super.phaseJeuMaxPossibilitee(partie);
    }
}
