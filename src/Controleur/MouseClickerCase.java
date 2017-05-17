/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.Case;
import Modele.ConfigurationPartie;
import Modele.Joueur;
import Modele.JoueurHumain;
import Modele.MyPolygon;
import Modele.Partie;
import Modele.Pinguin;
import Vue.AnimationFX;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

/**
 *
 * @author liakopog
 */
public class MouseClickerCase implements EventHandler<MouseEvent> {

    Partie partie;
    MyPolygon p;
    int rowclic;
    int columnclic;

    int size;

    public MouseClickerCase(MyPolygon p) {
	this.p = p;
	this.partie = ConfigurationPartie.getConfigurationPartie().getPartie();
        System.out.println(partie);
    }

    @Override
    public void handle(MouseEvent event) {
        System.out.println("POINT CLIC : " + (event.getX()+p.getXorigine()) + " " + (event.getY()+p.getYorigine()));
	// Récupération de la ligne et colonne de l'ilot cliqué
	rowclic = p.getY();
	columnclic = p.getX();
        
        // Joueur Humain
        if(partie.getJoueurCourant().getEstHumain() && partie.isTourFini()){
            
            partie.setTourFini(false);
            // Initialisation : Placement pingouins
            if (partie.estEnInitialisation()) {
                if (partie.getPlateau().getCases()[rowclic][columnclic].estCaseValideInit()) {
                    System.out.println("BLABLA");
                    partie.getJoueurCourant().ajouterPinguin(partie.getPlateau().getCases()[rowclic][columnclic]);
                    partie.getPlateau().setEstModifié(true);
                    partie.joueurSuivant();
                } else {
                    partie.setTourFini(true);
                }
                
            // Phase de jeu
            } else {
                Pinguin pingouin = partie.getJoueurCourant().getPinguinCourant();

                if (pingouin != null) {
                    Case caseDest = partie.getPlateau().getCases()[rowclic][columnclic];
                    if (caseDest.estCaseLibre() && caseDest.getAccessible()) {
                        
                        pingouin.deplace(caseDest);
                        
                        partie.getPlateau().setEstModifié(true);
                        for (Joueur j : partie.getJoueurs()) {
                            for (Pinguin p : j.getPinguinsVivants()) {
                                if (p.getPosition().estCoulee()) {
                                    p.coullePinguin();
                                    partie.getPlateau().setEstModifié(true);
                                } else if (p.getPosition().getCasePossibles().size() == 0) {
                                    p.coullePinguin();
                                    partie.getPlateau().setEstModifié(true);
                                }
                            }
                        }
                        partie.joueurSuivant();
                    } else {
                        partie.setTourFini(true);
                    }
                } else {
                    partie.setTourFini(true);
                }
            }
            for (Joueur j : partie.getJoueurs()) {
                for (Pinguin p : j.getPinguinsVivants()) {
                    if (p.getPosition().estCoulee()) {
                        p.coullePinguin();
                        partie.getPlateau().setEstModifié(true);
                    } else if (p.getPosition().getCasePossibles().size() == 0) {
                        p.coullePinguin();
                        partie.getPlateau().setEstModifié(true);
                    }
                }
            }
        }
    }
}
