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
public class JoueurIA7 extends JoueurIA {

    public JoueurIA7(Couleur couleur) {
        super(couleur, "JoueurIA7");
    }

    @Override
    public Case phaseInitialisation(Plateau plateau) {
        return super.phaseInitialisationGourmande(plateau);
    }

    @Override
    public Case phaseJeu(Plateau plateau) {
        //On regarde si on peut éliminer un pinguin
        Case caseChoisie = null;
        if (super.getChemin().isEmpty()) {
            caseChoisie = this.chercherVictime(plateau);
        } else {
            caseChoisie = super.phaseJeuMeilleurChemin(plateau);
            if (caseChoisie != null) {
                return caseChoisie;
            } else {
                caseChoisie = super.chercheIlot(plateau);
            }
        }

        if (caseChoisie != null) {
            return caseChoisie;
        } else {
            return super.phaseJeuGourmand(plateau);
        }        
    }

}
