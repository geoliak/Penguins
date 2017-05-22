/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.Case;
import Modele.Partie;
import Modele.Pinguin;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author liakopog
 */
public class MouseClickerPenguin implements EventHandler<MouseEvent> {

    Partie partie;
    Pinguin p;

    public MouseClickerPenguin(Pinguin clickedPenguin, Partie partie) {
	this.p = clickedPenguin;
	this.partie = partie;
    }

    @Override
    public void handle(MouseEvent event) {
	//System.out.println(p.getPosition().getNumColonne() + " " + p.getPosition().getNumLigne() + " pingouin");
        if(p.estVivant()){
            if (!partie.getInitialisation()) {
                if (partie.getJoueurCourant() == p.getGeneral() && partie.getJoueurCourant().getEstHumain() && partie.isTourFini()) {
                    p.getGeneral().setPinguinCourant(p);

                    for(Case[] cases : partie.getPlateau().getCases()){
                        for(Case c : cases){
                            c.setAccessible(false);
                        }
                    }

                    ArrayList<Case> casesaccessibles = p.getPosition().getCasePossibles();
                    for (Case c : casesaccessibles) {
                        c.setAccessible(true);
                    }
                    partie.getPlateau().setEstModifié(true);
                }
            }
        }
	
	//il faut recuperer le prochain clic comme destination si possible
	//faut aussi feedforwarder (wow) les cases ou on peut se deplacer
    }

}
