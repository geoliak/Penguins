/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.Partie;
import Modele.Plateau;

/**
 *
 * @author liakopog
 */
public class AnnulerCoup {

    private Partie partie;
    private Plateau plateauDepart;

    public AnnulerCoup(Partie partie) {
	this.partie = partie;
	this.plateauDepart = partie.getPlateau();
    }

    public void Miseajour() {

    }
}
