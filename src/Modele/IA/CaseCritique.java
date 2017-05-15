/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA;

import Modele.Case;
import java.util.ArrayList;

/**
 *
 * @author Mathias
 */
public class CaseCritique {

    private ArrayList<Case> ilot1;
    private ArrayList<Case> ilot2;
    private Case cassure;

    public CaseCritique(Case cassure, ArrayList<Case> voisins, Integer[][] dijkstra) {
        this.cassure = cassure;
        this.ilot1 = new ArrayList<>();
        this.ilot2 = new ArrayList<>();
        this.init(voisins, dijkstra);
    }

    public void init(ArrayList<Case> voisins, Integer[][] dijkstra) {
        Case ancien = null;
        for (Case c : voisins) {
            if (ancien == null) {
                this.ilot1.add(c);
            } else if (dijkstra[c.getNumLigne()][c.getNumColonne()] < Integer.MAX_VALUE) {
                this.ilot1.add(c);
            } else {
                this.ilot2.add(c);
            }
            ancien = c;
        }
    }

    public ArrayList<Case> getIlot1() {
        return ilot1;
    }

    public ArrayList<Case> getIlot2() {
        return ilot2;
    }

    public Case getCassure() {
        return cassure;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Case critique : ").append(this.cassure).append("\n");
        for (Case c : this.ilot1) {
            sb.append("ilot1 : ").append(c).append("\n");
        }
        
        for (Case c : this.ilot2) {
            sb.append("ilot2 : ").append(c).append("\n");
        }
        sb.append("\n");
        
        return sb.toString();
    }

}
