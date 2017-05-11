/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA;

import Modele.Case;
import Modele.Couleur;
import Modele.Plateau;

/**
 *
 * @author novelm
 */
public class JoueurIA8 extends JoueurIA {

    public JoueurIA8(Couleur couleur) {
        super(couleur, "JoueurIA8");
    }

    @Override
    public Case phaseJeu(Plateau plateau) {
        //On regarde si on peut éliminer un pinguin
        Case caseChoisie = null;
        if (super.getChemin().isEmpty()) {
            caseChoisie = this.chercherVictimePremierDuNom(plateau);
            if (caseChoisie != null) {
                return caseChoisie;

            }
        }

        caseChoisie = super.phaseJeuMeilleurChemin(plateau);
        if (caseChoisie != null) {
            return caseChoisie;
        }

        return super.phaseJeuGourmand(plateau);
    }
}
