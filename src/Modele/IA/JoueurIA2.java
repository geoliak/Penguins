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
    public Case phaseJeu(Plateau plateau) {
        Case caseChoisie = this.chercherVictime(plateau);
        //Si elle ne peut tuer personne, alors elle joue aléatoirement
        if (caseChoisie == null) {
            Random r = new Random();

            //Choix aléatoire d'un pinguin vivant
            Pinguin p = super.getPinguinsVivants().get(r.nextInt(super.getPinguinsVivants().size()));

            this.setPinguinCourant(p);

            //Choix aléatoire d'une case
            ArrayList<Case> casePossibles = p.getPosition().getCasePossibles();
            caseChoisie = casePossibles.get(r.nextInt(casePossibles.size()));
        }
        return caseChoisie;
    }

}
