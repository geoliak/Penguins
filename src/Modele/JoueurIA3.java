/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import Vue.DessinateurTexte;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Mathias
 */
public class JoueurIA3 extends JoueurIA {

    private ArrayList<Case> chemin;

    public JoueurIA3(Couleur couleur) {
        super(couleur, "JoueurIA3");
        this.chemin = new ArrayList<>();
    }

    @Override
    public void reset() {
        super.reset();
        this.chemin = new ArrayList<>();
    }

    @Override
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

    /**
     * Marche pas
     *
     * @param plateau
     * @return
     */
    @Override
    public Case phaseJeu(Plateau plateau) {
        Case caseChoisie = null;

        //Si il y a un chemin courant
        if (!this.chemin.isEmpty()) {
            System.out.println("chemin de longueur " + chemin.size() + " Pinnguin courant " + this.getPinguinCourant());
            caseChoisie = this.chemin.remove(0);

        } else {
            for (Pinguin p : this.getPinguinsVivants()) {
                //Si le pinguin est seul sur un iceberg alors il doit effectuer le meilleur chemin
                if (this.estSeulIceberg(plateau, p.getPosition())) {
                    DessinateurTexte dt = new DessinateurTexte();
                    System.out.println(this.getCouleur() + this.getNom() + Couleur.ANSI_RESET);
                    dt.visite(plateau);

                    this.setPinguinCourant(p);
                    System.out.println("seul " + p.getPosition().getNumLigne() + "," + p.getPosition().getNumColonne());

                    ArrayList<Case> iceberg = new ArrayList<>();
                    this.getCasesIceberg(plateau, p.getPosition(), iceberg);

                    this.chemin = this.getMeilleurChemin(plateau, p.getPosition(), new ArrayList<>(), iceberg.size());
                    /*MeilleurChemin mc = new MeilleurChemin(plateau, p.getPosition(), this.chemin);
                     mc.start();
                     try {
                     mc.join();
                     } catch (InterruptedException ex) {
                     Logger.getLogger(JoueurIAMathias3.class.getName()).log(Level.SEVERE, null, ex);
                     }*/

                    System.out.println("chemin de longueur " + chemin.size() + " Pinnguin courant " + this.getPinguinCourant());
                    this.afficherChemin();
                    return this.chemin.remove(0);

                }
            }

            //On regarde si on peut éliminer un pinguin
            System.out.println("kill ?");
            caseChoisie = this.chercherVictime(plateau);

            //Si elle ne peut tuer personne, alors elle joue aléatoirement
            if (caseChoisie == null) {
                System.out.println("Pas de kill");
                Random r = new Random();

                //Choix aléatoire d'un pinguin vivant
                Pinguin p = super.getPinguinsVivants().get(r.nextInt(super.getPinguinsVivants().size()));
                this.setPinguinCourant(p);

                //Choix aléatoire d'une case
                ArrayList<Case> casePossibles = p.getPosition().getCasePossibles();
                caseChoisie = casePossibles.get(r.nextInt(casePossibles.size()));
            }
        }

        System.out.println(" ");
        return caseChoisie;
    }

    public void afficherChemin() {
        System.out.println("Chemin de " + this.chemin.size());
        for (Case c : this.chemin) {
            System.out.println(c.getNumLigne() + "," + c.getNumColonne());
        }
    }
}
