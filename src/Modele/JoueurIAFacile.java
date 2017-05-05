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
 * @author novelm
 */
public class JoueurIAFacile extends JoueurIA {

    public JoueurIAFacile(Couleur couleur) {
        super(couleur);
    }

    @Override
    public Case etablirCoup(Plateau plateau) {
        Case caseChoisie;
        Random r = new Random();
        if (super.getPret()) {

            //Choix aléatoire d'un pinguin vivant
            Pinguin p;
            do {
                p = super.getPinguins().get(r.nextInt(super.getPinguins().size()));
            } while (!p.estVivant());
            this.setPinguinCourant(p);

            //Choix aléatoire d'une case
            ArrayList<Case> casePossibles = p.getPosition().getCasePossibles();
            caseChoisie = casePossibles.get(r.nextInt(casePossibles.size()));

            
            //Lors de l'initialisation, l'IA choisie les cases ou elle place ses pinguins
        } else {
            int i, j;
            do {
                i = r.nextInt(8);
                j = r.nextInt(8);
                caseChoisie = plateau.getCases()[i][j];
            } while (caseChoisie.estCoulee() || caseChoisie.getPinguin() != null || caseChoisie.getNbPoissons() > 1);

        }

        return caseChoisie;
    }

}
