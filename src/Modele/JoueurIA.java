/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;

/**
 *
 * @author novelm
 */
public abstract class JoueurIA extends Joueur {

    public JoueurIA(Couleur couleur, String nom) {
        super(couleur);
        this.setEstHumain(false);
        Random r = new Random();
        this.setNom(nom);
        this.setAge(r.nextInt(123)); // Jeanne Calment
    }

    /**
     * Cette methode prend un plateau en parametre. L'IA va ensuite l'utiliser
     * pour déterminer quelle pinguin jouer et ou. Avant de renvoyer la case
     * choisie, l'IA place le pinguin choisi en tant que pinguinCourant
     *
     * @param plateau : plateau de jeu
     * @return la case jouee par l'IA
     */
    public Case etablirCoup(Plateau plateau) {
        if (!super.getPret()) {
            return this.phaseInitialisation(plateau);
        } else {
            return this.phaseJeu(plateau);
        }
    }
    
    

    public abstract Case phaseInitialisation(Plateau plateau);

    public abstract Case phaseJeu(Plateau plateau);

    /**
     *
     * @param plateau : plateau de jeu
     * @return Une case permettant de tuer un pinguin adverse ou null si une
     * telle case n'existe pas
     */
    public Case chercherVictime(Plateau plateau) {
        Case caseCourante = null;
        ArrayList<Pinguin> pinguins = new ArrayList<>();

        //Recuperation de tous les pinguins adverse vivants qui n'ont qu'un seul mouvement possible
        for (int i = 0; i < plateau.getNbLignes(); i++) {
            for (int j = 0; j < plateau.getNbColonnes(); j++) {
                caseCourante = plateau.getCases()[i][j];
                if (!caseCourante.estCoulee() && caseCourante.getPinguin() != null && caseCourante.getNbVoisins() == 1 && caseCourante.getPinguin().getGeneral() != this) {
                    pinguins.add(caseCourante.getPinguin());
                }
            }
        }

        //On cherche une case permettant de bloquer un pinguin adverse
        ArrayList<Case> mouvementsPossibles;
        for (Pinguin p : this.getPinguinsVivants()) {
            mouvementsPossibles = p.getPosition().getCasePossibles();

            for (Pinguin ennemi : pinguins) {
                for (Case voisin : ennemi.getPosition().getVoisinsJouable()) {
                    if (mouvementsPossibles.contains(voisin)) {
                        this.setPinguinCourant(p);
                        return voisin;
                    }
                }
            }
        }

        return null;
    }

    /**
     * A l'air de marcher
     *
     * @param plateau
     * @param source
     * @return True si le pinguin est seul ou avec un de ses collègues sur
     * l'iceberg
     */
    public Boolean estSeulIceberg(Plateau plateau, Case source) {
        Boolean res = true;

        //Si la case courante n'est pas coulee
        if (!source.estCoulee()) {

            //Si la case courante contient un adversaire
            if (source.getPinguin() != null && source.getPinguin().getGeneral() != this) {
                return false;

            } else {
                for (Case c : source.getVoisinsEmerges()) {
                    source.setCoulee(true);
                    res = res && this.estSeulIceberg(plateau, c);
                    source.setCoulee(false);
                }
            }
        }

        return res;
    }

    public ArrayList<Case> getCasesIceberg(Plateau plateau, Case source, ArrayList<Case> iceberg) {
        if (!source.estCoulee()) {
            iceberg.add(source);
            source.setCoulee(true);
            for (Case c : source.getVoisinsEmerges()) {
                this.getCasesIceberg(plateau, c, iceberg);
            }
            source.setCoulee(false);
        }
        return iceberg;
    }

    /**
     * A l'air de marcher (quand ca fini)
     *
     * @param plateau
     * @param source
     * @param file
     * @return
     */
    public ArrayList<Case> getMeilleurChemin(Plateau plateau, Case source, ArrayList<Case> file, int tailleRecherchee) {
        ArrayList<Case> casesPossibles = source.getCasePossibles();

        //System.out.println("-\n" + source.getNumLigne() + "," + source.getNumColonne() + " -> " + file.size());
        if (casesPossibles.isEmpty() || file.size() == tailleRecherchee) {
            return file;
        } else {
            source.setCoulee(true);
            int max = Integer.MIN_VALUE;
            ArrayList<Case> branchementCourant, branchementResultat = null;

            for (Case c : casesPossibles) {
                file.add(c);
                branchementCourant = getMeilleurChemin(plateau, c, (ArrayList<Case>) file.clone(), tailleRecherchee);
                if (branchementCourant.size() + file.size() - 1 == tailleRecherchee) {
                    source.setCoulee(false);
                    file.addAll(branchementCourant);
                    return file;
                }
                file.remove(c);

                if (this.getPoidsChemin(branchementCourant) > max) {
                    max = this.getPoidsChemin(branchementCourant);
                    branchementResultat = branchementCourant;
                } else if (this.getPoidsChemin(branchementCourant) == max && branchementCourant.size() > branchementResultat.size()) {
                    branchementResultat = branchementCourant;
                }

            }

            file.addAll(branchementResultat);
            //System.out.println(source.getNumLigne() + "," + source.getNumColonne() + " -> " + file.size() + "\n-");
            source.setCoulee(false);
            return branchementResultat;
        }

    }

    public int getPoidsChemin(ArrayList<Case> cases) {
        int res = 0;
        for (Case c : cases) {
            res += c.getNbPoissons();
        }
        return res;
    }

    /**
     * Marche pas (fork bombe lol)
     */
    public class MeilleurChemin extends Thread {

        private Plateau plateau;
        private Case source;
        private ArrayList<Case> file;

        public MeilleurChemin(Plateau plateau, Case source, ArrayList<Case> file) {
            this.plateau = plateau.clone();
            this.source = this.plateau.getCases()[source.getNumLigne()][source.getNumColonne()];
            this.file = file;
        }

        @Override
        public void run() {
            file = getMeilleurChemin(this.plateau, this.source, (ArrayList<Case>) file.clone());
        }

        public ArrayList<Case> getMeilleurChemin(Plateau plateau, Case source, ArrayList<Case> reponse) {
            ArrayList<Case> casesPossibles = source.getCasePossibles();

            //System.out.println("-\n" + source.getNumLigne() + "," + source.getNumColonne() + " -> " + file.size());
            if (casesPossibles.isEmpty()) {
                /*System.out.println("nnChemin de " + reponse.size());
                 for (Case c : reponse) {
                 System.out.println(c.getNumLigne() + "," + c.getNumColonne());
                 }*/
                return reponse;
            } else {
                source.setCoulee(true);
                ArrayList<MeilleurChemin> threads = new ArrayList<>();
                ArrayList<ArrayList<Case>> reponses = new ArrayList<>();

                for (Case c : casesPossibles) {
                    reponses.add((ArrayList<Case>) reponse.clone());
                    reponses.get(reponses.size() - 1).add(c);
                    threads.add(new MeilleurChemin(plateau, c, reponses.get(reponses.size() - 1)));
                    threads.get(threads.size() - 1).start();
                }
                for (MeilleurChemin mc : threads) {
                    try {
                        mc.join();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(JoueurIA.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                int max = Integer.MIN_VALUE;
                ArrayList<Case> branchementResultat = null;
                for (ArrayList<Case> cases : reponses) {
                    if (this.getPoidsChemin(cases) > max) {
                        max = this.getPoidsChemin(cases);
                        branchementResultat = cases;
                    } else if (this.getPoidsChemin(cases) == max && cases.size() > branchementResultat.size()) {
                        branchementResultat = cases;
                    }
                }

                source.setCoulee(false);
                reponse.addAll(branchementResultat);
                return reponse;
                //System.out.println(source.getNumLigne() + "," + source.getNumColonne() + " -> " + file.size() + "\n-");
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

}

/*

 explorer(graphe G, sommet s)
 marquer le sommet s
 pour tout sommet t voisin du sommet s
 si t n'est pas marqué alors
 explorer(G, t);
 */
