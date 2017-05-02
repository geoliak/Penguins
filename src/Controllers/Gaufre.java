/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.*;
import Views.*;

/**
 *
 * @author rozandq
 */
public class Gaufre {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        Plateau plateau = new Plateau(4, 4);
        
        Partie partie = new Partie(plateau);
        
        JoueurHumain joueur1 = new JoueurHumain("Blorgo", partie);
        JoueurHumain joueur2 = new JoueurHumain("Mayaak", partie);
        
        JoueurIA joueuria = new JoueurIA("MAYAAK LE ROBOT", partie, 1);
        JoueurIA joueuria2 = new JoueurIA("MATHIAS LE ROBOT", partie, 2);
        JoueurIA joueuria3 = new JoueurIA("MINIMAX LE ROBOT", partie, 3);
        
        partie.setJoueur1(joueur1);
        partie.setJoueur2(joueuria3);
        
        partie.jouerPartie();
    }
}
