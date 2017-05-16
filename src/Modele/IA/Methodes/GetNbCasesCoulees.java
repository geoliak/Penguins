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

    private int nbCasesCoulees;

    public GetNbCasesCoulees() {
        this.nbCasesCoulees = 0;
    }

    @Override
    public void execute(Case c) {
        if (c.estCoulee()) {
            this.nbCasesCoulees++;
        }
    }

    public int getNbCasesCoulees() {
        return nbCasesCoulees;
    }
}
