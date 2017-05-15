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
 * @author Mathias
 */
public class JoueurIAchercheIlot extends JoueurIA {

    public JoueurIAchercheIlot(Couleur couleur) {
        super(couleur, "JoueurTestChercherIlot");
    }
    
    
    @Override
    public Case phaseJeu(Partie partie) {
        Case CaseChoisie = this.chercheIlot(partie);
        
        if (CaseChoisie != null) {
            System.out.println("Ilot trouvé ! case critique : " + CaseChoisie);
            return CaseChoisie;
        }
        
        return this.phaseJeuGourmand(partie);
    }
}
