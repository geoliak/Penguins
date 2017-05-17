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
    public Case phaseJeu(Partie partie) {
        int nbCasesPossibles = 0;
        Case caseChoisie = null;
        for (Pinguin p : this.getPinguinNonIsole()) {
            nbCasesPossibles += p.getPosition().getCasePossibles().size();
        }

        if (nbCasesPossibles < 5 && super.nbCasesRestantesEstMoinsQue(partie, 12)) {
            caseChoisie = JoueurIA.minimax(this, partie);
            if (caseChoisie == null) {
                DessinateurTexte dt = new DessinateurTexte();
                partie.getPlateau().accept(dt);
                System.out.println("");
            }
        } else {
            caseChoisie = this.phaseJeuGourmand(partie);
            if (caseChoisie == null) {
                System.out.println("");
            }
        }

        return caseChoisie;
    }

}
