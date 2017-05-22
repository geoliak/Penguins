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
public class JoueurIA4 extends JoueurIA {

    public JoueurIA4(Couleur couleur, String nom, int numero) {
	super(couleur, nom, numero);
    }

    public JoueurIA4(Couleur couleur, int numero) {
	super(couleur, "JoueurIA4", numero);
    }

    @Override
    public Case phaseInitialisation(Partie partie) {
	return super.phaseInitialisationGourmande(partie);
    }

    @Override
    public Case phaseJeu(Partie partie) {
	return super.phaseJeuGourmand(partie);
    }

}
