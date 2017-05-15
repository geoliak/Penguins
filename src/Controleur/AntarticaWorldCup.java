/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.Couleur;
import Modele.IA.JoueurIA1;
import Modele.IA.JoueurIA2;
import Modele.IA.JoueurIA3;
import Modele.IA.JoueurIA4;
import Modele.IA.JoueurIA5;
import Modele.IA.JoueurIA6;
import Modele.IA.JoueurIA7;
import Modele.IA.JoueurIA8;
import Modele.Tournoi;
import Vue.DessinateurTexte;

/**
 *
 * @author novelm
 */
public class AntarticaWorldCup {

    public static void main(String[] args) {
        Tournoi worldCup = new Tournoi(60);

       /*worldCup.ajouterIA(new JoueurIA1(Couleur.Rouge));
        worldCup.ajouterIA(new JoueurIA2(Couleur.Jaune));
        worldCup.ajouterIA(new JoueurIA3(Couleur.Vert));
        worldCup.ajouterIA(new JoueurIA4(Couleur.Bleu));*/
        worldCup.ajouterIA(new JoueurIA5(Couleur.Rouge));
        worldCup.ajouterIA(new JoueurIA6(Couleur.Jaune));
        worldCup.ajouterIA(new JoueurIA7(Couleur.Vert));
        worldCup.ajouterIA(new JoueurIA8(Couleur.Bleu));


        
        worldCup.executerLesCombats(true, true, true);
        
        worldCup.afficheResultats(true, true, true);
        
        
        
    }

    
}
