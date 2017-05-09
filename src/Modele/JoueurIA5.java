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
 * @author novelm
 */
public class JoueurIA5 extends JoueurIA4 {

    private ArrayList<Case> chemin;

    public JoueurIA5(Couleur couleur) {
        super(couleur, "JoueurIA5");
        this.chemin = new ArrayList<>();
    }

    @Override
    public Case phaseJeu(Plateau plateau) {
        Case caseChoisie = super.chercherVictime(plateau);

        super.setPinguinsSeuls(plateau);
        Boolean sontSeuls = super.pinguinsSontSeuls();
        
        if (caseChoisie == null && sontSeuls && !this.chemin.isEmpty()) {
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
            this.chemin = plateau.getMeilleurChemin(p.getPosition(), new ArrayList<>(), tailleMaximale - (int) (tailleMaximale * 0.25));

            caseChoisie = this.chemin.remove(0);

        } else if (caseChoisie == null) {
            caseChoisie = super.phaseJeu(plateau);
        }

        return caseChoisie;
    }

}
