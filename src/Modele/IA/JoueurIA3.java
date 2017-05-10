/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA;

import Modele.Case;
import Modele.Couleur;
import Modele.Pinguin;
import Modele.Plateau;
import Vue.DessinateurTexte;
import java.util.ArrayList;
import java.util.HashSet;
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

    /**
     * Marche pas
     *
     * @param plateau
     * @return
     */
    @Override
    public Case phaseJeu(Plateau plateau) {
        //On regarde si on peut éliminer un pinguin
        Case caseChoisie = this.chercherVictime(plateau);

        //Si il n'y a plus pinguin adverse sur l'iceberg
        //System.out.println("Sont seuls ?");
        super.setPinguinsSeuls(plateau);
        Boolean sontSeuls = super.pinguinsSontSeuls();
        if (caseChoisie == null && sontSeuls && !this.chemin.isEmpty()) {
            System.out.println("chemin de longueur " + chemin.size() + " Pinnguin courant " + this.getPinguinCourant());
            caseChoisie = this.chemin.remove(0);

        } else if (sontSeuls && this.chemin.isEmpty()) {
            Random r = new Random();

            Pinguin p = super.getPinguinsVivants().get(r.nextInt(super.getPinguinsVivants().size()));

            DessinateurTexte dt = new DessinateurTexte();
            System.out.println(this.getCouleur() + this.getNom() + Couleur.ANSI_RESET);
            dt.visite(plateau);

            this.setPinguinCourant(p);
            System.out.println("seul " + p.getPosition().getNumLigne() + "," + p.getPosition().getNumColonne());

            ArrayList<Case> iceberg = plateau.getCasesIceberg(p.getPosition());
            int tailleMaximale = iceberg.size();
            for (Case c : iceberg) {
                if (c.getPinguin() != null) {
                    tailleMaximale--;
                }
            }
            this.chemin = plateau.getMeilleurChemin(p.getPosition(), new ArrayList<>(),   tailleMaximale - (int) (tailleMaximale * 0.25));
            
            //this.chemin = plateau.getMeilleurCheminV2(p.getPosition(), new ArrayList<>(), iceberg.size() - 1);
            

            System.out.println("chemin de longueur " + chemin.size() + " Pinnguin courant " + this.getPinguinCourant() + "taille iceberg " + iceberg.size() + iceberg);

            this.afficherChemin();
            caseChoisie = this.chemin.remove(0);

        } else  if (caseChoisie == null) {
            caseChoisie = super.phaseJeu(plateau);
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
