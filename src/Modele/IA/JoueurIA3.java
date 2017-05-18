/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA;

import Modele.Case;
import Modele.Couleur;
import Modele.Partie;
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

    public JoueurIA3(Couleur couleur, int numero) {
        super(couleur, "JoueurIA3", numero);
    }

    /**
     * Marche pas
     *
     * @param plateau
     * @return
     */
    @Override
    public Case phaseJeu(Partie partie) {
        //On regarde si on peut éliminer un pinguin
        Case caseChoisie = null;

        caseChoisie = this.chercherVictimeIlot(partie);
        if (caseChoisie != null) {
            return caseChoisie;

        }

        return super.phaseJeu(partie);
    }

}
