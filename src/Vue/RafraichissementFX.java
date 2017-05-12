/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vue;

import Modele.Joueur;
import Modele.Partie;
import Modele.Pinguin;
import Modele.Plateau;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;

/**
 *
 * @author liakopog
 */
public class RafraichissementFX extends AnimationTimer {

    DessinateurFX d;
    Partie partie;
    int i = 0;

    public RafraichissementFX(DessinateurFX d, Partie partie) {
	this.d = d;
	this.partie = partie;
    }

    @Override
    public void handle(long now) {  
        
        // Rafraichissement du plateau
        if (partie.getPlateau().isEstModifié()) {
            d.visite(partie.getPlateau());
            partie.getPlateau().setEstModifié(false);
        }
        
        // Si la partie n'est pas terminée
        if(!partie.estTerminee()){
            if (partie.estEnInitialisation()) {
            // Si tout les pingouins ont été placés
                if (partie.nbPingouinsTotal() == partie.getNbPingouinParJoueur() * partie.getJoueurs().size()) {
                    partie.setInitialisation(false);

                    for (Joueur j : partie.getJoueursEnJeu()) {
                        j.setPret(Boolean.TRUE);
                    }
                    partie.getJoueurCourant().setPret(Boolean.TRUE);

                // Sinon initialisation : Tour IA
                }
            }
            if(partie.isTourFini()){
                partie.getJoueurCourant().attendreCoup(partie);
            }
            for(Joueur j : partie.getJoueurs()){
                for(Pinguin p : j.getPinguinsVivants()){                    
                    if (p.getPosition().estCoulee()) {
                        p.coullePinguin();
                        partie.getPlateau().setEstModifié(true);
                    }
                }
            }
            
            
        } else {
            System.out.println("PARTIE TERMINEE ===============");
            partie.afficheResultats();
            Platform.exit();
        }
    }
}
