/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vue;

import Modele.Visiteur;
import Modele.Case;
import Modele.Joueur;
import Modele.Pinguin;
import Modele.Plateau;

/**
 *
 * @author novelm
 */
public class DessinateurTexte extends Visiteur {

    @Override
    public void visite(Plateau plateau) {
        System.out.println("==== Plateau ====");
        for (int i = 0; i < plateau.getNbLignes(); i++) {
            if (i % 2 == 0) {
                System.out.print(" ");
            } else {
                System.out.print("  ");
            }
            for (int j = 0; j < plateau.getNbColonnes(); j++) {
                plateau.getCases()[i][j].accept(this);
                System.out.print(" ");
            }
            System.out.println("");
        }
    }

    @Override
    public void visite(Case c) {
        if (c.estCoulee()) {
            System.out.print(" ");
        } else {
            Pinguin p = c.getPinguin();
            if (p != null) {
                System.out.print(p.getGeneral().getCouleur().getColorBackground() + c.getNbPoissons() + Joueur.ANSI_RESET);
            } else if (c.getAccessible()) {
                System.out.print(Joueur.ANSI_GREEN + c.getNbPoissons() + Joueur.ANSI_RESET);
            } else {
                System.out.print(c.getNbPoissons());
            }
        }
    }

    @Override
    public void visite(Pinguin p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
