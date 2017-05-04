/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import javafx.scene.paint.Color;

/**
 *
 * @author novelm
 */
public class JoueurIAFacile extends JoueurIA {

    public JoueurIAFacile(Couleur couleur) {
        super(couleur);
    }

    @Override
    public Case etablirCoup() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
