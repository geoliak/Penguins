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
 * @author Mathias
 */
public class JoueurIA2 extends JoueurIA {

    public JoueurIA2(Couleur couleur) {
        super(couleur, "JoueurIA2");
    }

    @Override
    public Case phaseJeu(Plateau plateau) {
        return super.phaseJeuElimination(plateau);
    }

}
