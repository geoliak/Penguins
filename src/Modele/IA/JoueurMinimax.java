/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA;

import Modele.Case;
import Modele.Couleur;
import Modele.IA.Methodes.PhaseInitialisationGourmande;
import Modele.Partie;
import Modele.Pinguin;
import Vue.DessinateurTexte;

/**
 *
 * @author novelm
 */
public class JoueurMinimax extends JoueurIA {

    public JoueurMinimax(Couleur couleur, int numero) {
        super(couleur, "JoueurIA Minimax", numero);
    }

    @Override
    public Case phaseInitialisation(Partie partie) {
        return this.phaseInitialisationMaxPossibilitee(partie);
    }

    @Override
    public Case phaseJeu(Partie partie) {
        return JoueurIA.minimax(this, partie);
    }

}
