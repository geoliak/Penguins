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
    public Case phaseInitialisation(Partie partie) {
        return super.phaseInitialisationMaxPossibilitee(partie);
    }

    
    @Override
    public Case phaseJeu(Partie partie) {
        //On regarde si on peut éliminer un pinguin
        Case caseChoisie = null;
        if (super.getChemin().isEmpty()) {
            caseChoisie = this.chercherVictime(partie);
        }
        
        if (caseChoisie != null) {
            return caseChoisie;

        } else {
            caseChoisie = super.phaseJeuMeilleurChemin(partie);
            if (caseChoisie != null) {
                return caseChoisie;
            }
        }

        return super.phaseJeuMaxPossibilitee(partie);
    }

}
