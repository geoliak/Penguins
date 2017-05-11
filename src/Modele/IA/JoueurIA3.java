/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA;

import Modele.JoueurIA;
import Modele.Case;
import Modele.Couleur;
import Modele.Pinguin;
import Modele.Plateau;
import Vue.DessinateurTexte;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 *
 * @author Mathias
 */
public class JoueurIA3 extends JoueurIA {

    public JoueurIA3(Couleur couleur) {
        super(couleur, "JoueurIA3");
    }

    /**
     * Marche pas
     *
     * @param plateau
     * @return
     */
    @Override
    public Case phaseJeu(Plateau plateau) {
        //On regarde si on peut éliminer un pinguin
        Case caseChoisie = null;
        if (super.getChemin().isEmpty()) {
            caseChoisie = this.chercherVictime(plateau);
        }
        if (caseChoisie != null) {
            return caseChoisie;

        } else {
            caseChoisie = super.phaseJeuMeilleurChemin(plateau);
            if (caseChoisie != null) {
                return caseChoisie;
            }
        }

        return super.phaseJeu(plateau);
    }

}
