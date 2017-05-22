/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA.Methodes;

import Modele.Case;
import Modele.IA.CaseCritique;
import Modele.IA.JoueurIA;
import Modele.Plateau;
import java.util.ArrayList;

/**
 *
 * @author novelm
 */
public class GetIlotsPossibles implements Methode {

    private ArrayList<CaseCritique> ilotsPossibles;
    private Plateau plateau;

    public GetIlotsPossibles(Plateau plateau) {
        this.ilotsPossibles = new ArrayList<>();
        this.plateau = plateau;
    }

    @Override
    public void execute(Case c) {
        CaseCritique caseCritique = JoueurIA.estIlot(c, this.plateau);
        if (caseCritique != null && !c.estCoulee() && c.getPinguin() == null) {
            this.ilotsPossibles.add(caseCritique);
        }
    }

    public ArrayList<CaseCritique> getIlotsPossibles() {
        return ilotsPossibles;
    }

    public void setIlotsPossibles(ArrayList<CaseCritique> ilotsPossibles) {
        this.ilotsPossibles = ilotsPossibles;
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public void setPlateau(Plateau plateau) {
        this.plateau = plateau;
    }
    
    
}
