/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA;

import Modele.Case;
import Modele.Joueur;
import Modele.Pinguin;
import Modele.Plateau;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author novelm
 */
public class Minimax {

    private Joueur joueurCourant;
    private Joueur joueurAdverse;

    private Plateau plateau;

    private ArrayList<Pinguin> pinguinsJoueur;
    private ArrayList<Pinguin> pinguinsAdverses;

    public Minimax(Plateau plateau, ArrayList<Pinguin> pinguinsJoueur, ArrayList<Pinguin> pinguinsAdverses) {
        this.joueurCourant = pinguinsJoueur.get(0).getGeneral();
        this.joueurAdverse = pinguinsAdverses.get(0).getGeneral();

        this.plateau = plateau;

        this.pinguinsJoueur = pinguinsJoueur;
        this.pinguinsAdverses = pinguinsAdverses;
    }

    /*
     minimax(p) = f(p) si p est une feuille de l’arbre où f est une fonction d’évaluation de la position du jeu
     minimax(p) = MAX(minimax( O 1 {\displaystyle O_{1}} O_{1}), …, minimax( O n {\displaystyle O_{n}} O_{n})) si p est un nœud Joueur avec fils O 1 {\displaystyle O_{1}} O_{1}, …, O n {\displaystyle O_{n}} O_{n}
     minimax(p) = MIN(minimax( O 1 {\displaystyle O_{1}} O_{1}), …, minimax( O n {\displaystyle O_{n}} O_{n})) si p est un nœud Opposant avec fils O 1 {\displaystyle O_{1}} O_{1}, …, O n {\displaystyle O_{n}} O_{n}    
     */
    public MyPair<Case, Pinguin> execute() {
        Case caseRes = null;
        Pinguin pinguinRep = null;
        int poidsCourant;
        Case anciennePositionPinguin = null;
        int max = -1;
        ArrayList<Case> movementPossibles;

        for (Pinguin p : pinguinsJoueur) {
            movementPossibles = p.getPosition().getCasePossibles();
            anciennePositionPinguin = p.getPosition();
            anciennePositionPinguin.setPinguin(null);
            anciennePositionPinguin.setCoulee(true);

            for (Case c : movementPossibles) {

                p.setPosition(c);
                c.setPinguin(p);

                if ((poidsCourant = minimaxWorker(1, (ArrayList<Pinguin>) pinguinsJoueur.clone(), (ArrayList<Pinguin>) pinguinsAdverses.clone()) + c.getNbPoissons()) > max) {
                    max = poidsCourant;
                    pinguinRep = p;
                    caseRes = c;
                }

                c.setPinguin(null);

            }
            p.setPosition(anciennePositionPinguin);
            anciennePositionPinguin.setCoulee(false);
            anciennePositionPinguin.setPinguin(p);
        }

        MyPair<Case, Pinguin> rep = new MyPair(caseRes, pinguinRep);

        return rep;
    }

    public void affichePositionPinguin() {
        for (Case[] cs : this.plateau.getCases()) {
            for (Case c : cs) {
                if (c.getPinguin() != null) {
                    System.out.println(c.getPinguin());
                }
            }
        }
    }

    public MyPair<Case, Pinguin> executeMultiThread() {
        Case caseRes = null;
        Pinguin pinguinRep = null;
        Case anciennePositionPinguin = null;
        int max = -1;
        CalculBranche cb;
        ArrayList<CalculBranche> cbs = new ArrayList<>();
        ArrayList<Case> movementPossibles;
        for (Pinguin p : pinguinsJoueur) {
            movementPossibles = p.getPosition().getCasePossibles();
            anciennePositionPinguin = p.getPosition();
            anciennePositionPinguin.setPinguin(null);
            anciennePositionPinguin.setCoulee(true);

            for (Case c : movementPossibles) {
                p.setPosition(c);
                c.setPinguin(p);

                /*
                 System.out.println("---");
                 this.affichePositionPinguin();
                 System.out.println("---");
                 */
                Plateau plateauClone = plateau.clone();

                ArrayList<Pinguin> joueur1 = new ArrayList<>();
                for (Pinguin pj1 : this.pinguinsJoueur) {
                    joueur1.add(p.myClone(plateauClone));
                }

                ArrayList<Pinguin> joueur2 = new ArrayList<>();
                for (Pinguin pj2 : this.pinguinsAdverses) {
                    joueur2.add(p.myClone(plateauClone));
                }

                cb = new CalculBranche(joueur1, joueur2, c, plateauClone, p);
                cbs.add(cb);
                cbs.get(cbs.size() - 1).start();

                c.setPinguin(null);
            }
            p.setPosition(anciennePositionPinguin);
            anciennePositionPinguin.setPinguin(p);
            anciennePositionPinguin.setCoulee(false);
        }

        for (CalculBranche calcul : cbs) {
            try {
                calcul.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Minimax.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for (CalculBranche calcul : cbs) {
            if (calcul.getRes() > max) {
                max = calcul.getRes();
                pinguinRep = calcul.getPinguin();
                caseRes = calcul.getCasePrecedente();
            }
        }

        MyPair<Case, Pinguin> rep = new MyPair(caseRes, pinguinRep);

        return rep;
    }

    public class MyPair<L, R> {

        private L l;
        private R r;

        public MyPair(L l, R r) {
            this.l = l;
            this.r = r;
        }

        public L getL() {
            return l;
        }

        public void setL(L l) {
            this.l = l;
        }

        public R getR() {
            return r;
        }

        public void setR(R r) {
            this.r = r;
        }
    }

    public int minimaxWorker(int tour, ArrayList<Pinguin> pinguinsJoueur, ArrayList<Pinguin> pinguinsAdverses) {
        int bonus = 0;

        ArrayList<Pinguin> potence = new ArrayList<>();
        if (tour % 2 == 0) {

            for (Pinguin p : pinguinsJoueur) {
                if (this.plateau.getNbJoueurIceberg(plateau.getCasesIceberg(p.getPosition())) == 1) {
                    potence.remove(p);
                    bonus += this.plateau.getPoidsIceberg(this.plateau.getCasesIceberg(p.getPosition())) / this.plateau.getNbPinguinIceberg(this.plateau.getCasesIceberg(p.getPosition()));
                } else if (p.getPosition().getCasePossibles().isEmpty()) {
                    bonus += p.getPosition().getNbPoissons();
                    potence.remove(p);
                }
            }
            pinguinsJoueur.removeAll(potence);

        } else {
            for (Pinguin p : pinguinsAdverses) {
                if (this.plateau.getNbJoueurIceberg(plateau.getCasesIceberg(p.getPosition())) == 1) {
                    potence.remove(p);
                    bonus += this.plateau.getPoidsIceberg(this.plateau.getCasesIceberg(p.getPosition())) / this.plateau.getNbPinguinIceberg(this.plateau.getCasesIceberg(p.getPosition()));
                } else if (p.getPosition().getCasePossibles().isEmpty()) {
                    bonus += p.getPosition().getNbPoissons();
                    potence.remove(p);
                }
            }
            pinguinsAdverses.removeAll(potence);
        }

        int poidsCourant;
        Case anciennePositionPinguin = null;
        ArrayList<Case> movementPossibles;

        if (pinguinsJoueur.isEmpty()) {
            return bonus;

            //Tour de l'IA
        } else if (tour % 2 == 0) {
            int max = -1;

            for (Pinguin p : pinguinsJoueur) {
                movementPossibles = p.getPosition().getCasePossibles();
                anciennePositionPinguin = p.getPosition();
                anciennePositionPinguin.setPinguin(null);
                anciennePositionPinguin.setCoulee(true);

                for (Case c : movementPossibles) {

                    p.setPosition(c);
                    c.setPinguin(p);

                    if ((poidsCourant = minimaxWorker(tour + 1, (ArrayList<Pinguin>) pinguinsJoueur.clone(), (ArrayList<Pinguin>) pinguinsAdverses.clone()) + c.getNbPoissons()) > max) {
                        max = poidsCourant;
                    }

                    c.setPinguin(null);
                }

                p.setPosition(anciennePositionPinguin);
                anciennePositionPinguin.setPinguin(p);
                anciennePositionPinguin.setCoulee(false);
            }

            return max;

            //Tour de l'adversaire                    
        } else {
            int min = Integer.MAX_VALUE;

            for (Pinguin p : pinguinsAdverses) {
                movementPossibles = p.getPosition().getCasePossibles();
                anciennePositionPinguin = p.getPosition();
                anciennePositionPinguin.setPinguin(null);
                anciennePositionPinguin.setCoulee(true);

                for (Case c : movementPossibles) {

                    p.setPosition(c);
                    c.setPinguin(p);

                    if ((poidsCourant = minimaxWorker(tour + 1, (ArrayList<Pinguin>) pinguinsJoueur.clone(), (ArrayList<Pinguin>) pinguinsAdverses.clone()) + +c.getNbPoissons()) < min) {
                        min = poidsCourant;
                    }

                    c.setPinguin(null);

                }
                p.setPosition(anciennePositionPinguin);
                anciennePositionPinguin.setPinguin(p);
                anciennePositionPinguin.setCoulee(false);
            }
            return min;

        }
    }

    public class CalculBranche extends Thread {

        private int res;
        private Case casePrecedente;
        private ArrayList<Pinguin> pinguinsJoueur;
        private ArrayList<Pinguin> pinguinsAdverses;
        private Plateau plateau;
        private Pinguin pinguin;

        public CalculBranche(ArrayList<Pinguin> pinguinsJoueur, ArrayList<Pinguin> pinguinsAdverses, Case casePrecedente, Plateau plateau, Pinguin pinguin) {
            this.res = 0;
            this.pinguin = pinguin;
            this.casePrecedente = casePrecedente;

            this.plateau = plateau;

            this.pinguinsJoueur = pinguinsJoueur;

            this.pinguinsAdverses = pinguinsAdverses;
        }

        public int getRes() {
            return res + this.casePrecedente.getNbPoissons();
        }

        public Pinguin getPinguin() {
            return this.pinguin;
        }

        public Case getCasePrecedente() {
            return casePrecedente;
        }

        @Override
        public void run() {
            this.res = this.minimaxWorker(1, this.pinguinsJoueur, this.pinguinsAdverses);
        }

        public int minimaxWorker(int tour, ArrayList<Pinguin> pinguinsJoueur, ArrayList<Pinguin> pinguinsAdverses) {
            int bonus = 0;

            ArrayList<Pinguin> potence = new ArrayList<>();
            if (tour % 2 == 0) {

                for (Pinguin p : pinguinsJoueur) {
                    if (this.plateau.getNbJoueurIceberg(plateau.getCasesIceberg(p.getPosition())) == 1) {
                        potence.remove(p);
                        try {
                            bonus += this.plateau.getPoidsIceberg(this.plateau.getCasesIceberg(p.getPosition())) / this.plateau.getNbPinguinIceberg(this.plateau.getCasesIceberg(p.getPosition()));
                        } catch (Exception e) {
                            System.out.println("ERREUR ==================================");
                            System.out.println("taille iceberg : " + this.plateau.getCasesIceberg(p.getPosition()).size());
                            System.out.println("Nb Pinguin : " + this.plateau.getNbPinguinIceberg(this.plateau.getCasesIceberg(p.getPosition())));
                        }
                    } else if (p.getPosition().getCasePossibles().isEmpty()) {
                        bonus += p.getPosition().getNbPoissons();
                        potence.remove(p);
                    }
                }
                pinguinsJoueur.removeAll(potence);

            } else {
                for (Pinguin p : pinguinsAdverses) {
                    if (this.plateau.getNbJoueurIceberg(plateau.getCasesIceberg(p.getPosition())) == 1) {
                        potence.remove(p);
                        bonus += this.plateau.getPoidsIceberg(this.plateau.getCasesIceberg(p.getPosition())) / this.plateau.getNbPinguinIceberg(this.plateau.getCasesIceberg(p.getPosition()));
                    } else if (p.getPosition().getCasePossibles().isEmpty()) {
                        bonus += p.getPosition().getNbPoissons();
                        potence.remove(p);
                    }
                }
                pinguinsAdverses.removeAll(potence);
            }

            int poidsCourant;
            Case anciennePositionPinguin = null;
            ArrayList<Case> movementPossibles;

            if (pinguinsJoueur.isEmpty()) {
                return bonus;

                //Tour de l'IA
            } else if (tour % 2 == 0) {
                int max = -1;

                for (Pinguin p : pinguinsJoueur) {
                    movementPossibles = p.getPosition().getCasePossibles();
                    anciennePositionPinguin = p.getPosition();
                    anciennePositionPinguin.setCoulee(true);
                    anciennePositionPinguin.setPinguin(null);
                    
                    for (Case c : movementPossibles) {

                        p.setPosition(c);
                        c.setPinguin(p);

                        if ((poidsCourant = minimaxWorker(tour + 1, (ArrayList<Pinguin>) pinguinsJoueur, (ArrayList<Pinguin>) pinguinsAdverses) + c.getNbPoissons()) > max) {
                            max = poidsCourant;
                        }

                        c.setPinguin(null);

                    }
                    p.setPosition(anciennePositionPinguin);
                    anciennePositionPinguin.setCoulee(false);
                    anciennePositionPinguin.setPinguin(p);
                }

                return max;

                //Tour de l'adversaire                    
            } else {
                int min = Integer.MAX_VALUE;

                for (Pinguin p : pinguinsAdverses) {
                    movementPossibles = p.getPosition().getCasePossibles();
                    anciennePositionPinguin = p.getPosition();
                    anciennePositionPinguin.setCoulee(true);
                    anciennePositionPinguin.setPinguin(null);
                    
                    for (Case c : movementPossibles) {

                        p.setPosition(c);
                        c.setPinguin(p);

                        if ((poidsCourant = minimaxWorker(tour + 1, (ArrayList<Pinguin>) pinguinsJoueur, (ArrayList<Pinguin>) pinguinsAdverses) + +c.getNbPoissons()) < min) {
                            min = poidsCourant;
                        }

                        c.setPinguin(null);

                    }
                    p.setPosition(anciennePositionPinguin);
                    anciennePositionPinguin.setCoulee(false);
                    anciennePositionPinguin.setPinguin(p);
                }
                return min;

            }
        }

    }

    public class Arbre<T> {

        private ArrayList<T> fils;
        private Arbre<T> pere;
        private T t;
        private int poids;

        public Arbre(T t) {
            this.poids = 0;
            this.fils = new ArrayList<>();
            this.pere = null;
            this.t = t;
        }

        public Arbre(Arbre<T> pere, T t) {
            this.poids = 0;
            this.pere = pere;
            this.fils = new ArrayList<>();
            this.t = t;
        }

        public Arbre<T> getPere() {
            return pere;
        }

        public void setPere(Arbre<T> pere) {
            this.pere = pere;
        }

        public T getObjetCourante() {
            return t;
        }

        public void setObjetCourante(T t) {
            this.t = t;
        }

        public int getPoids() {
            return poids;
        }

        public void setPoids(int poids) {
            this.poids = poids;
        }

        public void ajouterFils(T fils) {
            this.fils.add(fils);
        }

        public ArrayList<T> getFils() {
            return fils;
        }

        public void setFils(ArrayList<T> fils) {
            this.fils = fils;
        }
    }
}
