/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import Views.*;
import java.util.ArrayList;

/**
 *
 * @author novelm
 */
public class Minimax {

    private ArbreMinimax arbre;
    private Plateau plateau;

    public Minimax(Plateau plateau) {
        this.arbre = new ArbreMinimax(null, 0, 0);
        this.plateau = plateau;

        this.enumeration(plateau, this.arbre, 0);
        System.out.println("Apres enumeration");
        //this.arbre.affiche(arbre);
    }

    private void enumeration(Plateau plateau, ArbreMinimax arbre, int poidsPere) {
        ArrayList<Case> caseLibres = plateau.getCasesValides();
        int poids;
        if (!caseLibres.isEmpty()) {
            for (Case c : caseLibres) {
                Plateau tmp = plateau.clone();
                //System.out.println("Minimax (" + c.getX() + "," + c.getY());
                tmp.coup(c.getX(), c.getY());
                Dessinateur d = new Dessinateur(tmp);
                //d.dessinePlateau();

                //Si perdu
                if (!tmp.getCases()[0][0].isJouable()) {
                    poids = poidsPere -1;
                } else {
                    //Si il ne reste plus que le poison après tour
                    if (!tmp.getCases()[0][1].isJouable() && !tmp.getCases()[1][0].isJouable()) {
                        poids = poidsPere + 1;
                    } else {
                        poids = poidsPere;
                    }
                }

                arbre.ajouterEnfant(new Case(c.getX(), c.getY(), c.isJouable()), poids);

                enumeration(tmp, arbre.getEnfants().get(arbre.getEnfants().size() - 1), poids);
                d = new Dessinateur(plateau);
                //System.out.println("plateau");
                //d.dessinePlateau();
            }
        }
    }

    /*
     minimax(p) = f(p) si p est une feuille de l’arbre où f est une fonction d’évaluation de la position du jeu
     minimax(p) = MAX(minimax( O 1 {\displaystyle O_{1}} O_{1}), …, minimax( O n {\displaystyle O_{n}} O_{n})) si p est un nœud Joueur avec fils O 1 {\displaystyle O_{1}} O_{1}, …, O n {\displaystyle O_{n}} O_{n}
     minimax(p) = MIN(minimax( O 1 {\displaystyle O_{1}} O_{1}), …, minimax( O n {\displaystyle O_{n}} O_{n})) si p est un nœud Opposant avec fils O 1 {\displaystyle O_{1}} O_{1}, …, O n {\displaystyle O_{n}} O_{n}
     */
    public ArbreMinimax execute(ArbreMinimax arbre, int tour) {
        if (arbre.estFeuille()) {
            return arbre;

            //Tour de l'IA
        } else if (tour % 2 == 0) {
            int max = Integer.MIN_VALUE;
            ArbreMinimax arbreCourant;
            ArbreMinimax arbreRes = arbre.getEnfants().get(0);
            for (ArbreMinimax a : arbre.getEnfants()) {
                arbreCourant = execute(a, ++tour);
                if (arbreCourant.getPoids() > max) {
                    arbreRes = arbreCourant;
                    arbre.setPoids(arbreRes.getPoids());
                    max = arbreCourant.getPoids();
                }
            }
            return arbreRes;

            //Tour du joueur
        } else {
            int min = Integer.MAX_VALUE;
            ArbreMinimax arbreCourant;
            ArbreMinimax arbreRes = arbre.getEnfants().get(0);
            for (ArbreMinimax a : arbre.getEnfants()) {
                arbreCourant = execute(a, ++tour);
                if (arbreCourant.getPoids() < min) {
                    arbreRes = arbreCourant;
                    arbre.setPoids(arbreRes.getPoids());
                    min = arbreCourant.getPoids();
                }
            }
            return arbreRes;
        }
    }

    public ArbreMinimax getArbre() {
        return arbre;
    }

    public void setArbre(ArbreMinimax arbre) {
        this.arbre = arbre;
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public void setPlateau(Plateau plateau) {
        this.plateau = plateau;
    }

    public class ArbreMinimax {

        private int poids;
        private int niveau;
        private ArbreMinimax parent;
        private Case c;
        private ArrayList<ArbreMinimax> enfants;

        public ArbreMinimax(Case c, int poids, int niveau) {
            this.c = c;
            this.poids = poids;
            this.niveau = niveau;
            this.parent = null;
            this.enfants = new ArrayList<>();
        }

        public void affiche(ArbreMinimax arbre) {
            for (ArbreMinimax a : arbre.getEnfants()) {
                affiche(a);
            }
            if (arbre.getC() == null) {
                System.out.println("Racine");
            } else if (arbre.getParent().getC() == null) {
                System.out.println("(" + arbre.c.getX() + "," + arbre.c.getY() + ") niveau : " + arbre.niveau + " -- poids : " + arbre.poids);
            } else {
                System.out.print("(" + arbre.c.getX() + "," + arbre.c.getY() + ")-");
                System.out.println("(" + arbre.getParent().getC().getX() + "," + arbre.getParent().getC().getY() + ") niveau : " + arbre.niveau + " -- poids : " + arbre.poids);
            }
        }

        public void ajouterEnfant(Case c, int poids) {
            ArbreMinimax enfant = new ArbreMinimax(c, poids, this.niveau + 1);
            this.enfants.add(enfant);
            enfant.setParent(this);
            enfant.affiche(enfant);
        }

        public ArbreMinimax getParent() {
            return parent;
        }

        public void setParent(ArbreMinimax parent) {
            this.parent = parent;
        }

        public int getNiveau() {
            return niveau;
        }

        public void setNiveau(int niveau) {
            this.niveau = niveau;
        }

        public boolean estFeuille() {
            return this.enfants.isEmpty();
        }

        public int getPoids() {
            return poids;
        }

        public void setPoids(int poids) {
            this.poids = poids;
        }

        public Case getC() {
            return c;
        }

        public void setC(Case c) {
            this.c = c;
        }

        public ArrayList<ArbreMinimax> getEnfants() {
            return enfants;
        }

        public void setEnfants(ArrayList<ArbreMinimax> enfants) {
            this.enfants = enfants;
        }

    }
}
