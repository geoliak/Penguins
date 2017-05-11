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
        return super.phaseInitialisationGourmande(plateau);
    }

    @Override
    public Case phaseJeu(Plateau plateau) {
        return super.phaseJeuGourmand(plateau);
    }

}
