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
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author novelm
 */
public class JoueurIA4 extends JoueurIA {

    public JoueurIA4(Couleur couleur, String nom) {
        super(couleur, nom);
    }

    public JoueurIA4(Couleur couleur) {
        super(couleur, "JoueurIA4");
    }

    @Override
    public Case phaseInitialisation(Plateau plateau) {
        Case caseChoisie = null;
        ArrayList<Case> casesAccessible = null;
        int nbPoissons = 3;
        Random r = new Random();
        int debutligne = r.nextInt(plateau.getNbLignes());
        int debutColonne = r.nextInt(plateau.getNbColonnes());

        while (nbPoissons > 0) {
            for (int i = debutligne; i < debutligne + plateau.getNbLignes(); i++) {
                for (int j = debutColonne; j < debutColonne + plateau.getNbColonnes(); j++) {
                    Case caseCourante = plateau.getCases()[i % plateau.getNbLignes()][j % plateau.getNbColonnes()];
                    if (caseCourante != null && !caseCourante.estCoulee() && caseCourante.getNbPoissons() == 1 && caseCourante.getPinguin() == null) {
                        casesAccessible = caseCourante.getCasePossibles();
                        for (Case c : casesAccessible) {
                            if (c.getNbPoissons() == nbPoissons) {
                                return caseCourante;
                            }
                        }
                    }
                }
            }
            nbPoissons--;
        }

        return caseChoisie;
    }

    @Override
    public Case phaseJeu(Plateau plateau) {
        Case caseChoisie = null;
        ArrayList<Case> casesAccessible = null;
        int nbPoissons = 3;
        while (nbPoissons > 0) {
            for (Pinguin p : super.getPinguinsVivants()) {
                casesAccessible = p.getPosition().getCasePossibles();
                for (Case caseCourante : casesAccessible) {
                    if (caseCourante.getNbPoissons() == nbPoissons) {
                        super.setPinguinCourant(p);
                        return caseCourante;
                    }
                }
            }
            nbPoissons--;
        }

        return caseChoisie;
    }

}
