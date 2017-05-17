/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA.Methodes;

import Modele.Case;

/**
 *
 * @author novelm
 */
public class GetNbCasesCoulees implements Methode {

    private int nbCasesRestantes;

    public GetNbCasesCoulees() {
        this.nbCasesRestantes = 0;
    }

    @Override
    public void execute(Case c) {
        if (!c.estCoulee()) {
            this.nbCasesRestantes++;
        }
    }

    public int getNbCasesCoulees() {
        return nbCasesRestantes;
    }
}
