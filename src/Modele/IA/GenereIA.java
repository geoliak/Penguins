/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA;

import Modele.Couleur;
import Modele.Joueur;
import Modele.Plateau;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 *
 * @author novelm
 */
public class GenereIA {
    private ArrayList<Consumer<Plateau>> initialisation;
    private ArrayList<Consumer<Plateau>> debutJeu;
    private ArrayList<Consumer<Plateau>> millieuJeu;
    
    
    public GenereIA(JoueurIA IA) {
        //Methode pouvant etre utilisées pour l'initialisation
        this.initialisation = new ArrayList<>();        
        this.initialisation.add(IA::phaseInitialisation);
        this.initialisation.add(IA::phaseInitialisationGourmande);
        
        
        //Methode pouvant etre utilisées pour le debut de partie
        this.debutJeu = new ArrayList<>();
        
        //Methode pouvant etre utilisées pour le millieu de partie
        this.millieuJeu = new ArrayList<>();
    }

    public ArrayList<Joueur> genereIA() {
        
    }

}
