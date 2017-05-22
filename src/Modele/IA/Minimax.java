/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA;

import Modele.Case;
import Modele.Joueur;
import Modele.Partie;
import Modele.Pinguin;
import Modele.Plateau;
import Modele.TypeAutre.MyPair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author novelm
 */
public class Minimax {

    private Joueur joueurCourant;
    private Joueur joueurAdverse;

    private Partie partie;

    private ArrayList<Pinguin> pinguinsJoueur;
    private ArrayList<Pinguin> pinguinsAdverses;

    public Minimax(Partie partie, ArrayList<Pinguin> pinguinsJoueur, ArrayList<Pinguin> pinguinsAdverses) {
        this.joueurCourant = pinguinsJoueur.get(0).getGeneral();
        this.joueurAdverse = pinguinsAdverses.get(0).getGeneral();

        this.partie = partie;

        this.pinguinsJoueur = pinguinsJoueur;
        this.pinguinsAdverses = pinguinsAdverses;
    }

    public void affichePositionPinguin() {
        for (Case[] cs : this.partie.getPlateau().getCases()) {
            for (Case c : cs) {
                if (c.getPinguin() != null) {
                    System.out.println(c.getPinguin());
                }
            }
        }
    }

    public MyPair<Case, Pinguin> executeNegamax(int profondeur) {
        Case caseRes = null;
        Pinguin pinguinRep = null;
        int poidsCourant;
        Case anciennePositionPinguin;
        int meilleurPoids = Integer.MIN_VALUE;
        ArrayList<Case> movementPossibles;
        HashMap<Pinguin, Integer> debug = new HashMap<>();

        for (Pinguin p : pinguinsJoueur) {
            debug.put(p, Integer.MIN_VALUE);
            movementPossibles = p.getPosition().getCasePossibles();
            anciennePositionPinguin = p.getPosition();
            anciennePositionPinguin.setPinguin(null);
            anciennePositionPinguin.setCoulee(true);

            for (Case c : movementPossibles) {
                p.setPosition(c);
                c.setPinguin(p);

                poidsCourant = NegaMax(partie.getPlateau(), (ArrayList<Pinguin>) this.pinguinsAdverses.clone(), (ArrayList<Pinguin>) this.pinguinsJoueur.clone(), profondeur - 1, 0, c.getNbPoissons());
                if (poidsCourant > meilleurPoids) {
                    meilleurPoids = poidsCourant;
                    pinguinRep = p;
                    caseRes = c;
                }

                c.setPinguin(null);

            }
            p.setPosition(anciennePositionPinguin);
            anciennePositionPinguin.setCoulee(false);
            anciennePositionPinguin.setPinguin(p);
            debug.put(p, meilleurPoids);
        }

        MyPair<Case, Pinguin> rep = new MyPair(caseRes, pinguinRep);

        return rep;
    }

    public static int NegaMax(Plateau plateau, ArrayList<Pinguin> pinguinsJoueur1, ArrayList<Pinguin> pinguinsJoueur2, int profondeur, int poidsChemin1, int poidsChemin2) {
        int meilleurPoids = Integer.MIN_VALUE;
        int poidsCourant, poidsFeuille = 0;
        ArrayList<Case> mouvementsPossibles;
        Case anciennePositionPinguin;

        //On stop l'enumeration
        if (profondeur == 0) {
            //Evaluation
            return poidsChemin1 - poidsChemin2;

        } else {
            //Supprime les pinguins inutiles
            for (int i = 0; i < pinguinsJoueur1.size(); i++) {
                //Si un pinguin ne peut plus bouger
                if (pinguinsJoueur1.get(i).getPosition().getVoisinsJouable().isEmpty()) {
                    poidsFeuille += pinguinsJoueur1.get(i).getPosition().getNbPoissons();
                    pinguinsJoueur1.remove(i);
                    i--;

                    //Si le joueur est seul sur l'iceberg alors on considere la configuration comme une feuille et on retournera le poids de l'iceberg
                } else if (Plateau.getNbJoueurIceberg(Plateau.getCasesIceberg(pinguinsJoueur1.get(i).getPosition())) == 1) {
                    poidsFeuille += Plateau.getPoidsIceberg(Plateau.getCasesIceberg(pinguinsJoueur1.get(i).getPosition())) / Plateau.getNbPinguinIceberg(Plateau.getCasesIceberg(pinguinsJoueur1.get(i).getPosition()));
                    pinguinsJoueur1.remove(i);
                    i--;
                }
            }

            //Feuille
            if (pinguinsJoueur1.isEmpty()) {
                return poidsChemin1 + poidsFeuille - poidsChemin2;
            }

            //Pour tous les pinguins du joueur courant
            for (Pinguin p : pinguinsJoueur1) {
                mouvementsPossibles = p.getPosition().getCasePossibles();
                //sauvegarde de l'etat courant
                anciennePositionPinguin = p.getPosition();
                anciennePositionPinguin.setPinguin(null);
                anciennePositionPinguin.setCoulee(true);

                for (Case c : mouvementsPossibles) {
                    //Deplacement du pinguin
                    p.setPosition(c);
                    c.setPinguin(p);

                    poidsCourant = -NegaMax(plateau, (ArrayList<Pinguin>) pinguinsJoueur2.clone(), (ArrayList<Pinguin>) pinguinsJoueur1.clone(), profondeur - 1, poidsChemin2, c.getNbPoissons() + poidsChemin1 + poidsFeuille);
                    meilleurPoids = Integer.max(poidsCourant, meilleurPoids);

                    //undo coup
                    c.setPinguin(null);
                }
                //undo coup
                p.setPosition(anciennePositionPinguin);
                anciennePositionPinguin.setPinguin(p);
                anciennePositionPinguin.setCoulee(false);

            }

            return meilleurPoids;
        }
    }

    public MyPair<Case, Pinguin> executeNegamaxMultiThread(int profondeur) {
        Case caseRes = null;
        Pinguin pinguinRep = null;
        Case anciennePositionPinguin;
        int meilleurPoids = Integer.MIN_VALUE;
        ArrayList<Case> movementPossibles;
        CalculBrancheNegamax cb;
        ArrayList<CalculBrancheNegamax> cbs = new ArrayList<>();

        for (Pinguin p : pinguinsJoueur) {
            movementPossibles = p.getPosition().getCasePossibles();
            anciennePositionPinguin = p.getPosition();
            anciennePositionPinguin.setPinguin(null);
            anciennePositionPinguin.setCoulee(true);

            for (Case c : movementPossibles) {
                p.setPosition(c);
                c.setPinguin(p);

                Plateau plateauClone = partie.getPlateau().clone();

                ArrayList<Pinguin> joueur1 = new ArrayList<>();
                for (Pinguin pj1 : this.pinguinsJoueur) {
                    joueur1.add(pj1.myClone(plateauClone));
                }

                ArrayList<Pinguin> joueur2 = new ArrayList<>();
                for (Pinguin pj2 : this.pinguinsAdverses) {
                    joueur2.add(pj2.myClone(plateauClone));
                }

                cb = new CalculBrancheNegamax(plateauClone, joueur2, joueur1, profondeur - 1, c, p, anciennePositionPinguin.getNbPoissons());
                cbs.add(cb);
                cbs.get(cbs.size() - 1).start();

                c.setPinguin(null);

            }
            p.setPosition(anciennePositionPinguin);
            anciennePositionPinguin.setCoulee(false);
            anciennePositionPinguin.setPinguin(p);
        }

        for (CalculBrancheNegamax calcul : cbs) {
            try {
                calcul.join();
                if (calcul.getRes() > meilleurPoids) {
                    meilleurPoids = calcul.getRes();
                    pinguinRep = calcul.getPinguinEtudie();
                    caseRes = calcul.getCaseEtudiee();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Minimax.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        MyPair<Case, Pinguin> rep = new MyPair(caseRes, pinguinRep);

        return rep;
    }

    public class CalculBrancheNegamax extends Thread {

        private Plateau plateau;
        private ArrayList<Pinguin> pinguinsJoueur1;
        private ArrayList<Pinguin> pinguinsJoueur2;
        private int profondeur;
        private int res;
        private Case caseEtudiee;
        private Pinguin pinguinEtudie;
        private int pointsInitiaux;

        public CalculBrancheNegamax(Plateau plateau, ArrayList<Pinguin> pinguinsJoueur1, ArrayList<Pinguin> pinguinsJoueur2, int profondeur, Case caseEtudiee, Pinguin pinguinEtudie, int pointsInitiaux) {
            this.plateau = plateau;
            this.pinguinsJoueur1 = pinguinsJoueur1;
            this.pinguinsJoueur2 = pinguinsJoueur2;
            this.profondeur = profondeur;
            this.caseEtudiee = caseEtudiee;
            this.pinguinEtudie = pinguinEtudie;
            this.pointsInitiaux = pointsInitiaux;
        }

        public int getRes() {
            return res;
        }

        public Case getCaseEtudiee() {
            return caseEtudiee;
        }

        public Pinguin getPinguinEtudie() {
            return pinguinEtudie;
        }

        @Override
        public void run() {
            this.res = -Minimax.NegaMaxElagage(plateau, pinguinsJoueur2, pinguinsJoueur1, profondeur, Integer.MAX_VALUE, Integer.MIN_VALUE, 0, this.caseEtudiee.getNbPoissons());
            //this.res = -Minimax.NegaMax(plateau, pinguinsJoueur1, pinguinsJoueur2, profondeur, 0, this.caseEtudiee.getNbPoissons() + this.pointsInitiaux);
        }
    }

    public MyPair<Case, Pinguin> executeNegamaxElagage(int profondeur) {
        int alpha = Integer.MIN_VALUE, beta = Integer.MAX_VALUE;
        Case caseRes = null;
        Pinguin pinguinRep = null;
        int poidsCourant;
        Case anciennePositionPinguin;
        int meilleurPoids = Integer.MIN_VALUE;
        ArrayList<Case> movementPossibles;
        HashMap<Pinguin, Integer> debug = new HashMap<>();

        for (Pinguin p : pinguinsJoueur) {
            debug.put(p, Integer.MIN_VALUE);
            movementPossibles = p.getPosition().getCasePossibles();
            anciennePositionPinguin = p.getPosition();
            anciennePositionPinguin.setPinguin(null);
            anciennePositionPinguin.setCoulee(true);

            for (Case c : movementPossibles) {
                p.setPosition(c);
                c.setPinguin(p);

                poidsCourant = NegaMaxElagage(partie.getPlateau(), (ArrayList<Pinguin>) pinguinsJoueur.clone(), (ArrayList<Pinguin>) pinguinsAdverses.clone(), profondeur - 1, -beta, -alpha, 0, c.getNbPoissons());

                if (poidsCourant > meilleurPoids) {
                    meilleurPoids = poidsCourant;
                    caseRes = c;
                    pinguinRep = p;
                    if (meilleurPoids > alpha) {
                        alpha = meilleurPoids;
                        if (alpha > beta) {
                            p.setPosition(anciennePositionPinguin);
                            anciennePositionPinguin.setPinguin(p);
                            anciennePositionPinguin.setCoulee(false);
                            return new MyPair<>(caseRes, pinguinRep);
                        }
                    }
                }

                c.setPinguin(null);

            }

            p.setPosition(anciennePositionPinguin);
            anciennePositionPinguin.setCoulee(false);
            anciennePositionPinguin.setPinguin(p);
            debug.put(p, meilleurPoids);

        }

        MyPair<Case, Pinguin> rep = new MyPair(caseRes, pinguinRep);

        return rep;
    }

    public static int NegaMaxElagage(Plateau plateau, ArrayList<Pinguin> pinguinsJoueur1, ArrayList<Pinguin> pinguinsJoueur2, int profondeur, int alpha, int beta, int poidsChemin1, int poidsChemin2) {
        int meilleurPoids = Integer.MIN_VALUE;
        int poidsCourant, poidsFeuille = 0;
        ArrayList<Case> mouvementsPossibles;
        Case anciennePositionPinguin;

        //On stop l'enumeration
        if (profondeur == 0) {
            return poidsChemin1 + JoueurIA.evaluationEtatV2(pinguinsJoueur1, plateau);

        } else {
            //On retire les pinguins qui sont dans un etat final
            for (int i = 0; i < pinguinsJoueur1.size(); i++) {
                //Ceux qui n'ont plus de mouvements possible
                if (pinguinsJoueur1.get(i).getPosition().getVoisinsJouable().isEmpty()) {
                    poidsFeuille += pinguinsJoueur1.get(i).getPosition().getNbPoissons();
                    pinguinsJoueur1.remove(i);
                    i--;

                    //Si le joueur est seul sur l'iceberg alors on considere que le joueur gagne les points de cet iceberg
                } else if (Plateau.getNbJoueurIceberg(Plateau.getCasesIceberg(pinguinsJoueur1.get(i).getPosition())) == 1) {
                    poidsFeuille += Plateau.getPoidsIceberg(Plateau.getCasesIceberg(pinguinsJoueur1.get(i).getPosition())) / Plateau.getNbPinguinIceberg(Plateau.getCasesIceberg(pinguinsJoueur1.get(i).getPosition()));
                    pinguinsJoueur1.remove(i);
                    i--;
                }
            }

            //Feuille
            if (pinguinsJoueur1.isEmpty()) {
                return poidsChemin1 + poidsFeuille;
            }

            //Pour tous les pinguins du joueur courant
            for (Pinguin p : pinguinsJoueur1) {
                mouvementsPossibles = p.getPosition().getCasePossibles();
                //sauvegarde de l'etat courant
                anciennePositionPinguin = p.getPosition();
                anciennePositionPinguin.setPinguin(null);
                anciennePositionPinguin.setCoulee(true);

                for (Case c : mouvementsPossibles) {
                    //Deplacement du pinguin
                    p.setPosition(c);
                    c.setPinguin(p);

                    poidsCourant = -NegaMaxElagage(plateau, (ArrayList<Pinguin>) pinguinsJoueur2.clone(), (ArrayList<Pinguin>) pinguinsJoueur1.clone(), profondeur - 1, -beta, -alpha, poidsChemin2, c.getNbPoissons() + poidsChemin1 + poidsFeuille);
                    if (poidsCourant > meilleurPoids) {
                        meilleurPoids = poidsCourant;
                        if (meilleurPoids > alpha) {
                            alpha = meilleurPoids;
                            if (alpha > beta) {
                                //undo coup
                                p.setPosition(anciennePositionPinguin);
                                anciennePositionPinguin.setPinguin(p);
                                anciennePositionPinguin.setCoulee(false);
                                c.setPinguin(null);
                                return meilleurPoids;
                            }
                        }
                    }

                    //undo coup
                    c.setPinguin(null);
                }
                //undo coup
                p.setPosition(anciennePositionPinguin);
                anciennePositionPinguin.setPinguin(p);
                anciennePositionPinguin.setCoulee(false);
            }
        }

        return meilleurPoids;
    }

}
