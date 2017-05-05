/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.*;
import Views.*;
import java.awt.TextField;

/**
 *
 * @author rozandq
 */
public class Gaufre {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        
        Plateau plateau = new Plateau(4, 4);
        
        Partie partie = new Partie(plateau);
        
        JoueurHumain joueur1 = new JoueurHumain("Joueur Humain 1", partie);
        JoueurHumain joueur2 = new JoueurHumain("Joueur Humain 2", partie);
        
        JoueurIA joueuria = new JoueurIA("IA facile", partie, 1);
        JoueurIA joueuria2 = new JoueurIA("IA moyen", partie, 2);
        JoueurIA joueuria3 = new JoueurIA("IA hard", partie, 3);
        
        partie.setJoueur1(joueur1);
        partie.setJoueur2(joueuria);
        
        partie.setJoueurCourant(partie.getJoueur1());
        
        System.out.println("Tour de " + partie.getJoueurCourant().getName());
        
        InterfaceFX i = new InterfaceFX();
        i.creer(args, partie);
    }
}
