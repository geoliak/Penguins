/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author rozandq
 */
public class JoueurIA extends Joueur {
    int difficulte;

    public JoueurIA(String name, Partie partie, int diff) {
        super(name, partie);
        difficulte = diff;
    }

    public void jouerCoup(int diff) {
        switch (diff) {
            case 1:
                jouerCoupFacile();
                break;
            case 2:
                jouerCoupMoyen();
                break;
            case 3:
                jouerCoupDifficile();
                break;
        }
    }

    @Override
    public void attendreCoup() {
        this.jouerCoup(difficulte);
    }

    public int tirerInt(int max) {

        Random random = new Random();
        int x = random.nextInt(max);

        return x;
    }
    
    /*
    private void jouerCoupFacile() {
        int longueur = partie.getPlateau().getLongueur();
        int largeur = partie.getPlateau().getLargeur();

        int x = tirerInt(longueur);
        int y = tirerInt(largeur);

        while (!partie.getPlateau().estCaseValide(x, y)) {
            x = tirerInt(longueur);
            y = tirerInt(largeur);
        }

        Coup coup = new Coup(new ArrayList<>(), this);
        Case[][] cases = partie.getPlateau().getCases();
        for (int i = y; i < largeur; i++) {
            for (int j = x; j < longueur; j++) {
                coup.getCases().add(cases[i][j]);
            }
        }

        partie.ajouterCoups(coup);
    }
    */
    
    private void jouerCoupFacile() {
        ArrayList<Case> casePossible = this.partie.getPlateau().getCasesValides();
        
        int longueur = partie.getPlateau().getLongueur();
        int largeur = partie.getPlateau().getLargeur();
        
        Case caseJouee = casePossible.get(this.tirerInt(casePossible.size()));
        
        while(!partie.getPlateau().estCaseValide(caseJouee.getX(), caseJouee.getY())){
            caseJouee = casePossible.get(this.tirerInt(casePossible.size()));
        }
        
        Coup coup = new Coup(new ArrayList<>(), this);
        Case[][] cases = partie.getPlateau().getCases();
        for (int i = caseJouee.getY(); i < largeur; i++) {
            for (int j = caseJouee.getX(); j < longueur; j++) {
                coup.getCases().add(cases[i][j]);
            }
        }
        
        partie.ajouterCoups(coup);
    }

    private void jouerCoupMoyen() {
        ArrayList<Case> casePossible = this.partie.getPlateau().getCasesValides();
        
        int longueur = partie.getPlateau().getLongueur();
        int largeur = partie.getPlateau().getLargeur();
        
        Case cg = getCoupGagnant();
        
        if(cg == null){
            cg = casePossible.get(this.tirerInt(casePossible.size()));
            while((cg.getX() == 1 && cg.getY() == 0) || (cg.getX() == 0 && cg.getY() == 1)){
                cg = casePossible.get(this.tirerInt(casePossible.size()));
            }
        }
        
        Coup coup = new Coup(new ArrayList<>(), this);
        Case[][] cases = partie.getPlateau().getCases();
        for (int i = cg.getY(); i < largeur; i++) {
            for (int j = cg.getX(); j < longueur; j++) {
                coup.getCases().add(cases[i][j]);
            }
        }
        
        partie.ajouterCoups(coup);
    }

    private void jouerCoupDifficile() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Case getCoupGagnant(){
        Case cg;
        if(!partie.getPlateau().estCaseValide(1, 0) && partie.getPlateau().estCaseValide(0, 1)){
            cg = new Case(0, 1, true);
        } else if(partie.getPlateau().estCaseValide(1, 0) && !partie.getPlateau().estCaseValide(0, 1)){
            cg = new Case(1, 0, true);
        } else {
            cg = null;
        }        
                
        return cg;
    }
}
