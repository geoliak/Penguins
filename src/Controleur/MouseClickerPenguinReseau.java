/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.Case;
import Modele.Partie;
import Modele.Pinguin;
import Modele.ConfigurationPartie;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javax.security.auth.login.Configuration;

/**
 *
 * @author liakopog
 */
public class MouseClickerPenguinReseau implements EventHandler<MouseEvent> {

    Partie partie;
    Pinguin p;
    private int numero;

    public MouseClickerPenguinReseau(Pinguin clickedPenguin, Partie partie, int num) {
	this.p = clickedPenguin;
	this.partie = partie;
        numero = num;
    }

    @Override
    public void handle(MouseEvent event) {
	if(ConfigurationPartie.getConfigurationPartie().getPartie().getJoueurCourant().getNumero() == numero){
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
        }
       
    }

}
