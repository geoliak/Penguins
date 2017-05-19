/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA.Methodes;

import Modele.Case;
import Modele.Joueur;
import Modele.Plateau;

/**
 *
 * @author Mathias
 */
public class GetEvaluationPlateau implements Methode {

    private Joueur joueur;
    private int evaluation;

    public GetEvaluationPlateau(Joueur joueur) {
        this.joueur = joueur;
        this.evaluation = 0;
    }

    public int getEvaluation() {
        return evaluation;
    }
    
    @Override
    public void execute(Case c) {
        if (c != null && !c.estCoulee() && c.getPinguin() != null && c.getPinguin().getGeneral() == this.joueur) {
            evaluation += Plateau.getPoidsIceberg(Plateau.getCasesIceberg(c.getPinguin().getPosition())) / Plateau.getNbJoueurIceberg(Plateau.getCasesIceberg(c.getPinguin().getPosition()));
        }
    }
    
}
