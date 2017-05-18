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
 * @author rozandq
 */
public class JoueurIASauveQuiPeut extends JoueurIA {

    public JoueurIASauveQuiPeut(Couleur couleur, int numero) {
        super(couleur, "JoueurIA SauveQuiPeut", numero);
    }

    @Override
    public Case phaseJeu(Partie partie) {
        //On regarde si on peut éliminer un pinguin
        Case caseChoisie = this.sauveQuiPeutBasique(partie);
        if (caseChoisie != null) {
            return caseChoisie;
        }

        System.out.println("Pas sauve qui peut");
        return super.phaseJeuGourmand(partie);
    }

}
