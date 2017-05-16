/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.Plateau;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author novelm
 */
public class TestClone {

    public static void main(String[] args) {
        try {
            Plateau plateau = new Plateau("ressources/plateaux/plateau5");
            
            Plateau plateau2 = plateau.clone();
            
            if (plateau.equals(plateau2)) {
                System.out.println("Loupé");
            }
            
            for (int i = 0; i < Plateau.LARGEUR; i++) {
                for (int j = 0; j < Plateau.LONGUEUR; j++) {
                    if (plateau.getCases()[i][j].equals(plateau2.getCases()[i][j])) {
                        System.out.println("Loupé");
                    }
                }
            }
            
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(TestClone.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
}
