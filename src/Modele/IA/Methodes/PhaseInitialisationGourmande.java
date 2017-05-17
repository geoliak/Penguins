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
public class PhaseInitialisationGourmande implements Methode {

    private Case caseChoisie;
    private int nbPoissons;

    public PhaseInitialisationGourmande(int nbPoissons) {
        this.caseChoisie = null;
        this.nbPoissons = nbPoissons;
    }
    
    @Override
    public void execute(Case caseCourante) {
        if (caseCourante != null && !caseCourante.estCoulee() && caseCourante.getNbPoissons() == 1 && caseCourante.getPinguin() == null) {
            ArrayList<Case> casesAccessible = caseCourante.getCasePossibles();
            for (Case c : casesAccessible) {
                if (c.getNbPoissons() == this.nbPoissons) {
                    this.caseChoisie = caseCourante;
                }
            }
        }
    }

    public Case getCaseChoisie() {
        return caseChoisie;
    }

    public void setCaseChoisie(Case caseChoisie) {
        this.caseChoisie = caseChoisie;
    }

    public int getNbPoissons() {
        return nbPoissons;
    }

    public void setNbPoissons(int nbPoissons) {
        this.nbPoissons = nbPoissons;
    }
    
    

}
