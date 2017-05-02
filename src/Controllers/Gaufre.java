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
        
        Plateau plateau = new Plateau(3, 6);
        
        Partie partie = new Partie(plateau);
        
        JoueurHumain joueur1 = new JoueurHumain("Blorgo", partie);
        JoueurHumain joueur2 = new JoueurHumain("Mayaak", partie);
        
        JoueurIA joueuria = new JoueurIA("MAYAAK LE ROBOT", partie, 1);
        JoueurIA joueuria2 = new JoueurIA("MATHIAS LE ROBOT", partie, 2);
        
        partie.setJoueur1(joueur1);
        partie.setJoueur2(joueuria2);
        
        partie.setJoueurCourant(partie.getJoueur1());
        
        InterfaceFX i = new InterfaceFX();
        //i.creer(args, partie);
        
        
        /*
        DessinateurFX dessinateur = new DessinateurFX(i.getCanvas(), i.getRoot());
        dessinateur.dessinePlateau(partie.getPlateau(), partie, i.getScene());
        */
        
        DessinateurText d = new DessinateurText();
        d.dessinePlateau(partie.getPlateau());   
        
        boolean termine = false;
        int tour = 1;
        
        while (!termine) {
            System.out.println("**************** Tour " + tour + " : " + partie.getJoueurCourant().getName() + " ****************");
            partie.jouerTour();
            d.dessinePlateau(partie.getPlateau());   
            
            tour++;

            if (partie.partieTermine()) {
                termine = true;
                System.out.println(partie.getJoueurCourant().getName() + " a gagné !");
            }
            
            if (tour % 2 == 1) {
                partie.setJoueurCourant(partie.getJoueur1());
            } else {
                partie.setJoueurCourant(partie.getJoueur2());
            }
        }
    }
}
