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
public class JoueurIA8 extends JoueurIA {

    public JoueurIA8(Couleur couleur, int numero) {
        super(couleur, "JoueurIA8", numero);
    }

    @Override
    public Case phaseInitialisation(Partie partie) {
        return super.phaseInitialisationMaxPossibilitee(partie);
    }

    @Override
    public Case phaseJeu(Partie partie) {
        //On regarde si on peut éliminer un pinguin
        Case caseChoisie = null;

        /*caseChoisie = JoueurIA8.minimax(this, partie, 20);
         if (caseChoisie != null) {
         System.out.println("Minimax");
         return caseChoisie;
         }*/
        caseChoisie = this.sauveQuiPeutBasique(partie);
        if (caseChoisie != null) {
            return caseChoisie;
        }

        caseChoisie = this.chercherVictimeIlot(partie);
        if (caseChoisie != null) {
            return caseChoisie;
        }

        caseChoisie = this.chercherVictimeSimple(partie);
        if (caseChoisie != null) {
            return caseChoisie;
        }

        /*caseChoisie = this.chercheIlot(partie);
        if (caseChoisie != null) {
            return caseChoisie;
        }*/

        
        return super.phaseJeuGourmand(partie);
    }
}
