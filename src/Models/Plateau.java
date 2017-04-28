/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.util.ArrayList;


public class Plateau {
    private int largeur, longueur;
    private Case[][] cases;
    

    public Plateau(int largeur, int longueur) {
        this.largeur = largeur;
        this.longueur = longueur;
        this.cases = setCases();
    }

    public int getLargeur() {
        return largeur;
    }

    public int getLongueur() {
        return longueur;
    }

    public Case[][] getCases() {
        return cases;
    }
    
    public Case[][] setCases(){
        Case[][] cases = new Case[largeur][longueur];
        for(int i = 0; i < largeur; i++){
            for(int j = 0; j < longueur; j++){
                cases[i][j] = new Case(j, i, true);
            }
        }
        return cases;
    }
    
    public void masquerCases(Coup coup){
        for(Case c : coup.getCases()){
            cases[c.getY()][c.getX()].setJouable(false);
        }
    }
    
    public boolean estCaseValide(int x, int y){
        if(x == 0 && y == 0){
            return false;
        } else {
            return cases[y][x].isJouable();
        }
    }
    
    public ArrayList<Case> getCasesValides() {
        ArrayList<Case> casesValides = new ArrayList<>();
        Case c[][] = getCases();

        for (int i = 0; i < getLargeur(); i++) {
            for (int j = 0; j < getLongueur(); j++) {
                if (c[i][j].isJouable()) {
                    casesValides.add(c[i][j]);
                }
            }
        }

        return casesValides;
    }
}
