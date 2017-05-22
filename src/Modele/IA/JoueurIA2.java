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
public class JoueurIA2 extends JoueurIA {

    public JoueurIA2(Couleur couleur, int numero) {
        super(couleur, "JoueurIA2", numero);
    }

    @Override
    public Case phaseJeu(Partie partie) {
        //On regarde si on peut éliminer un pinguin
        Case caseChoisie = this.chercherVictimeIlot(partie);
        if (caseChoisie != null) {
            return caseChoisie;
        }

        System.out.println("Pas de meurtre ilot");
        return super.phaseJeu(partie);
    }
}
