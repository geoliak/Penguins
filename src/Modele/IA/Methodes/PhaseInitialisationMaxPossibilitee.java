/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA.Methodes;

import Modele.Case;
import java.util.ArrayList;

/**
 *
 * @author novelm
 */
public class PhaseInitialisationMaxPossibilitee implements Methode {

    private Case caseChoisie;

    @Override
    public void execute(Case caseCourante) {
        int maxCasesAtteignable = -1;
        ArrayList<Case> casesAtteignable;

        if (caseCourante != null && !caseCourante.estCoulee() && caseCourante.getPinguin() == null && caseCourante.getNbPoissons() == 1) {
            casesAtteignable = caseCourante.getCasePossibles();
            if (casesAtteignable.size() > maxCasesAtteignable) {
                maxCasesAtteignable = casesAtteignable.size();
                caseChoisie = caseCourante;
            }
        }
    }

    public Case getCaseChoisie() {
        return caseChoisie;
    }
    
    

}
