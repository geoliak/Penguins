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
public class JoueurMinimaxElagage extends JoueurIA {

    public JoueurMinimaxElagage(Couleur couleur, int numero) {
        super(couleur, "JoueurIA MinimaxElagage", numero);
    }

    @Override
    public Case phaseInitialisation(Partie partie) {
        return this.phaseInitialisationMaxPossibilitee(partie);
    }

    @Override
    public Case phaseJeu(Partie partie) {
        Case caseChoisie = null;

        caseChoisie = JoueurIA.minimax(this, partie, true);
        if (caseChoisie != null) {
            return caseChoisie;
        }

        caseChoisie = this.chercherVictimeSimple(partie);
        if (caseChoisie != null) {
            return caseChoisie;
        }

        return super.phaseJeuMaxPossibilitee(partie);
    }

}
