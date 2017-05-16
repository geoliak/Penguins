/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA;

import Modele.Case;
import Modele.Couleur;
import Modele.Partie;
import Modele.Plateau;

/**
 *
 * @author novelm
 */
public class JoueurIA7 extends JoueurIA {

    public JoueurIA7(Couleur couleur, int numero) {
        super(couleur, "JoueurIA7", numero);
    }

    @Override
    public Case phaseInitialisation(Partie partie) {
        return super.phaseInitialisationGourmande(partie);
    }

    @Override
    public Case phaseJeu(Partie partie) {
        //On regarde si on peut éliminer un pinguin
        Case caseChoisie;

        caseChoisie = this.chercherVictimePremierDuNom(partie);
        if (caseChoisie != null) {
            return caseChoisie;

        }

        caseChoisie = super.chercheIlot(partie);
        if (caseChoisie != null) {
            return caseChoisie;

        } 
        
        return super.phaseJeuGourmand(partie);
        
    }

}
