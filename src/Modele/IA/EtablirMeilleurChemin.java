/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA;

import Modele.Case;
import Modele.Joueur;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

/**
 *
 * @author novelm
 */
public class EtablirMeilleurChemin extends Thread {

    private Case source;
    private int tailleMaximale;
    private JoueurIA joueur;
    private Boolean continuer;
    private int max;
    private long debutThread;
    private long lastModif;
    private int taillePrecedente;

    public EtablirMeilleurChemin(Case source, int tailleMaximale, JoueurIA joueur) {
        this.source = source;
        this.tailleMaximale = tailleMaximale;
        this.joueur = joueur;
        this.continuer = true;
        this.max = -1;
        this.lastModif = System.nanoTime();
        this.debutThread = System.nanoTime();
        this.taillePrecedente = 0;
    }

    public void stopThread() {
        continuer = false;
    }

    @Override
    public void run() {
        getMeilleurChemin(this.source, new ArrayList<>());
    }   

    public ArrayList<Case> getMeilleurChemin(Case source, ArrayList<Case> cheminCourant) {
        if (this.continuer && cheminCourant.size() == tailleMaximale) {
            this.stopThread();
            this.joueur.setChemin(cheminCourant);
            return cheminCourant;
            
        } else if (this.continuer) {
            ArrayList<Case> casesPossible = source.getCasePossibles();
            boolean possible = false;
            ArrayList<Case> branchementCourant, branchementResultat = null;
            int max = 0;
            //Pour toutes les cases qui n'ont pas ete visitee
            for (Case c : casesPossible) {
                if (!cheminCourant.contains(c)) {
                    possible = true;

                    branchementCourant = new ArrayList<>();
                    branchementCourant.addAll(cheminCourant);
                    branchementCourant.add(c);
                    source.setCoulee(true);
                    branchementCourant = getMeilleurChemin(c, branchementCourant);
                    source.setCoulee(false);
                    
                    if (!continuer) {
                        return branchementCourant;
                    } else if (branchementCourant.size() == tailleMaximale) {
                        this.joueur.setChemin(branchementCourant);
                        this.stopThread();
                        return branchementCourant;
                    } else if (this.getPoidsChemin(branchementCourant) > max) {
                        branchementResultat = branchementCourant;
                        max = this.getPoidsChemin(branchementCourant);
                    } else if (this.getPoidsChemin(branchementCourant) == max && branchementCourant.size() > branchementResultat.size()) {
                        branchementResultat = branchementCourant;
                    }

                    if (this.getPoidsChemin(branchementCourant) > this.max) {
                        this.max = this.getPoidsChemin(branchementCourant);
                        this.joueur.setChemin(branchementCourant);
                        this.lastModif = System.nanoTime();
                    }

                    if (System.nanoTime() - this.lastModif > 5E6) {
                        System.out.println("lastModif perime " + (System.nanoTime() - this.debutThread));
                        this.stopThread();
                        this.stop();
                    }
                    

                }
            }

            if (!possible) {
                return cheminCourant;
            } else {
                return branchementResultat;
            }
            
        } else {
            return null;
        }
    }

    public Boolean getContinuer() {
        return continuer;
    }

    public int getPoidsChemin(ArrayList<Case> cases) {
        int res = 0;
        for (Case c : cases) {
            res += c.getNbPoissons();
        }
        return res;
    }

}
