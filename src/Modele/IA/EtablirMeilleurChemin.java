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
 * @author novelm
 */
public class EtablirMeilleurChemin extends Thread {

        private Case source;
        private int tailleMaximale;
        private ArrayList<Case> reponse;
        private Boolean continuer;
        private int max;

        public EtablirMeilleurChemin(Case source, int tailleMaximale, ArrayList<Case> reponse) {
            this.source = source;
            this.tailleMaximale = tailleMaximale;
            this.reponse = reponse;
            this.continuer = true;
            this.max = -1;
        }

        public void stopThread() {
            continuer = false;
        }

        @Override
        public void run() {
            ArrayList<Case> tmp = getMeilleurChemin(this.source, new ArrayList<>());
            if (continuer) {
                this.reponse = tmp;
            }
        }

        public ArrayList<Case> getMeilleurChemin(Case source, ArrayList<Case> cheminCourant) {
            if (cheminCourant.size() == tailleMaximale || !this.continuer) {
                //System.out.println(tailleMaximale + " - " + cheminCourant.size());
                return cheminCourant;
            } else {
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
                            return null;
                        } else if (branchementCourant.size() == tailleMaximale) {
                            this.reponse = branchementCourant;
                            return branchementCourant;
                        } else if (this.getPoidsChemin(branchementCourant) > max) {
                            branchementResultat = branchementCourant;
                            max = this.getPoidsChemin(branchementCourant);
                        } else if (this.getPoidsChemin(branchementCourant) == max && branchementCourant.size() > branchementResultat.size()) {
                            branchementResultat = branchementCourant;
                        }

                        if (this.getPoidsChemin(branchementCourant) > this.max) {
                            this.max = this.getPoidsChemin(branchementCourant);
                            this.reponse = branchementCourant;
                        }

                    }
                }

                if (!possible) {
                    return cheminCourant;
                } else {
                    return branchementResultat;
                }
            }

        }

        public int getPoidsChemin(ArrayList<Case> cases) {
            int res = 0;
            for (Case c : cases) {
                res += c.getNbPoissons();
            }
            return res;
        }

    }
