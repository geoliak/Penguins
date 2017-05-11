/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA;

import Modele.JoueurIA;
import Modele.Case;
import Modele.Couleur;
import Modele.Pinguin;
import Modele.Plateau;
import Vue.DessinateurTexte;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author novelm
 */
public class JoueurIA6 extends JoueurIA {

    private ArrayList<Case> chemin;

    public JoueurIA6(Couleur couleur) {
        super(couleur, "JoueurIA6");
        this.chemin = new ArrayList<>();
    }

    @Override
    public Case phaseInitialisation(Plateau plateau) {
        Case caseCourante, CaseRes = null;
        int maxCasesAtteignable = -1;
        ArrayList<Case> casesAtteignable;
        for (int i = 0; i < plateau.getNbLignes(); i++) {
            for (int j = 0; j < plateau.getNbColonnes(); j++) {
                caseCourante = plateau.getCases()[i][j];
                if (caseCourante != null && !caseCourante.estCoulee() && caseCourante.getPinguin() == null && caseCourante.getNbPoissons() == 1) {
                    casesAtteignable = caseCourante.getCasePossibles();
                    if (casesAtteignable.size() > maxCasesAtteignable) {
                        maxCasesAtteignable = casesAtteignable.size();
                        CaseRes = caseCourante;
                    }
                }
            }
        }

        return CaseRes;
    }

    public Case phaseJeu(Plateau plateau) {
        Case caseChoisie = null, CaseRes = null;
        Random r = new Random();
        ArrayList<Case> casesPossibles;

        Pinguin pCourant = null;

        super.setPinguinsSeuls(plateau);
        Boolean sontSeuls = super.pinguinsSontSeuls();

        if (sontSeuls && !this.chemin.isEmpty()) {
            System.out.println("chemin de longueur " + chemin.size() + " Pinnguin courant " + this.getPinguinCourant());
            caseChoisie = this.chemin.remove(0);

        } else if (sontSeuls && this.chemin.isEmpty()) {

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
            this.chemin = plateau.getMeilleurChemin(p.getPosition(), new ArrayList<>(), tailleMaximale - (int) (tailleMaximale * 0.4));
            
            System.out.println("chemin de longueur " + chemin.size() + " Pinnguin courant " + this.getPinguinCourant() + "taille iceberg " + iceberg.size() + iceberg);
            
            caseChoisie = this.chemin.remove(0);

        } else {
            //Selection du pinguin qui a le moins de possibilitee de mouvement
            for (Pinguin p : super.getPinguinsVivants()) {
                int min = Integer.MAX_VALUE;
                casesPossibles = p.getPosition().getCasePossibles();
                if (casesPossibles.size() < min) {
                    pCourant = p;
                    min = casesPossibles.size();
                }
            }
            super.setPinguinCourant(pCourant);

            int max = -1;
            ArrayList<Case> tmp;
            for (Case c : pCourant.getPosition().getCasePossibles()) {
                tmp = c.getCasePossibles();
                if (tmp.size() > max) {
                    max = tmp.size();
                    caseChoisie = c;
                }
            }
        }

        return caseChoisie;
    }

}
