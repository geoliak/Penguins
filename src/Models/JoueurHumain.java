/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author rozandq
 */
public class JoueurHumain extends Joueur {

    public JoueurHumain(String name, Partie partie) {
        super(name, partie);
    }
    
    public void jouerCoup(int x, int y){
        Coup coup = new Coup(new ArrayList<>(), this);
        Case[][] cases = partie.getPlateau().getCases();
        int largeur = partie.getPlateau().getLargeur();
        int longueur = partie.getPlateau().getLongueur();
        for(int i = y; i < largeur; i++){
            for(int j = x; j < longueur; j++){
                coup.getCases().add(cases[i][j]);
            }
        }
        
        partie.ajouterCoups(coup);
    }

    public void attendreCoup(){
        Scanner sc = new Scanner(System.in);        
        System.out.println("Veuillez saisir la colonne :");
        int x = sc.nextInt();
        System.out.println("Veuillez saisir la ligne :");
        int y = sc.nextInt();
        System.out.println();
        this.jouerCoup(x, y);
    }
}
