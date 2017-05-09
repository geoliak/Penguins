/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Mathias
 */
public class JoueurIA2 extends JoueurIA {

    public JoueurIA2(Couleur couleur) {
        super(couleur, "JoueurIA2");
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

    @Override
    public Case phaseJeu(Plateau plateau) {
        Case caseChoisie = this.chercherVictime(plateau);
        //Si elle ne peut tuer personne, alors elle joue aléatoirement
        if (caseChoisie == null) {
            Random r = new Random();

            //Choix aléatoire d'un pinguin vivant
            Pinguin p;
            do {
                p = super.getPinguins().get(r.nextInt(super.getPinguins().size()));
            } while (!p.estVivant());
            this.setPinguinCourant(p);

            //Choix aléatoire d'une case
            ArrayList<Case> casePossibles = p.getPosition().getCasePossibles();
            caseChoisie = casePossibles.get(r.nextInt(casePossibles.size()));
        }
        return caseChoisie;
    }

}
