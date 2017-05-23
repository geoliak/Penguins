/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA;

import Modele.Case;
import Modele.Couleur;
import Modele.Partie;
import Modele.Pinguin;
import Modele.Plateau;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Mathias
 */
public class JoueurIA2 extends JoueurIA {

    public JoueurIA2(Couleur couleur, int numero) {
        super(couleur, "JoueurIA2", numero);
    }

    @Override
    public Case phaseJeu(Partie partie) {
        return super.phaseJeuElimination(partie);
    }

}