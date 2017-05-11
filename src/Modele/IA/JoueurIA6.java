/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA;

import Modele.Case;
import Modele.Couleur;
import Modele.Pinguin;
import Modele.Plateau;
import Vue.DessinateurTexte;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author novelm
 */
public class JoueurIA6 extends JoueurIA {

    public JoueurIA6(Couleur couleur) {
        super(couleur, "JoueurIA6");
    }

    @Override
    public Case phaseInitialisation(Plateau plateau) {
        return super.phaseInitialisationMaxPossibilitee(plateau);
    }

    
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

        return super.phaseJeuMaxPossibilitee(plateau);
    }

}
