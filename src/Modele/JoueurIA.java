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

    public Case phaseInitialisation(Plateau plateau) {
        Case caseChoisie;
        Random r = new Random();
        int i, j;
        do {
            i = r.nextInt(8);
            j = r.nextInt(8);
            caseChoisie = plateau.getCases()[i][j];
        } while (caseChoisie.estCoulee() || caseChoisie.getPinguin() != null || caseChoisie.getNbPoissons() > 1);

        return caseChoisie;
    }

    public Case phaseJeu(Plateau plateau) {
        Random r = new Random();

        //Choix aléatoire d'un pinguin vivant
        Pinguin p = super.getPinguinsVivants().get(r.nextInt(super.getPinguinsVivants().size()));
        this.setPinguinCourant(p);

        //Choix aléatoire d'une case
        ArrayList<Case> casePossibles = p.getPosition().getCasePossibles();
        Case caseChoisie = casePossibles.get(r.nextInt(casePossibles.size()));

        return caseChoisie;
    }

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
     * Parcours l'ensemble des pinguins vivants pour determiner si ils sont finalement seuls
     * Met la variable estSeul a True des pinguins dans ce cas
     * @param plateau : plateau de jeu
     */
    public void setPinguinsSeuls(Plateau plateau) {
        for (Pinguin p : super.getPinguinsVivants()) {
            if (!p.estSeul() && p.estSeulIceberg(plateau)) {
                p.setEstSeul(true);
            }
        }
    }
    
    /**
     * Parcours les pinguins vivants pour determiner si ils sont tous seuls sur leur iceberg
     * @return True si les pinguins sont tous seuls
     */
    public Boolean pinguinsSontSeuls() {
        boolean sontSeuls = true;
        for (Pinguin p : super.getPinguinsVivants()) {
            sontSeuls = sontSeuls && p.estSeul();
        }
        return sontSeuls;
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
