/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vue;

import Modele.Case;
import Modele.ConfigurationPartie;
import Modele.Joueur;
import Modele.Partie;
import Modele.Pinguin;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 *
 * @author liakopog
 */
public class RafraichissementFX extends AnimationTimer {

    DessinateurFX d;
    Partie partie;
    int i = 0;
    private boolean resultatAffiches;

    public RafraichissementFX(DessinateurFX d) {
	this.d = d;
	this.partie = ConfigurationPartie.getConfigurationPartie().getPartie();
	this.resultatAffiches = false;
        
        if(partie.getDemo()!=null){
            partie.getDemo().accept(d);
        }
    }

    @Override
    public void handle(long now) {        
	if (!partie.equals(ConfigurationPartie.getConfigurationPartie().getPartie())) {
	    System.out.println("Mise a jour de la partie");
	    this.partie = ConfigurationPartie.getConfigurationPartie().getPartie();
	}

        if(partie.getDemo()!=null && partie.getDemo().isEstModifie()){
            partie.getDemo().accept(d);
        }
        
	// Rafraichissement du plateau
	if (partie.getPlateau().isEstModifié()) {
            
	    partie.getPlateau().accept(d);
            
            for(Joueur j : partie.getJoueurs()){
                j.accept(d);
            }
	    partie.getPlateau().setEstModifié(false);
	}

	// Si la partie n'est pas terminée
	if (!partie.estTerminee()) {
	    if (partie.estEnInitialisation()) {
		// Si tout les pingouins ont été placés
		if (partie.nbPingouinsTotal() == partie.getNbPingouinParJoueur() * partie.getJoueurs().size()) {
		    for (Case[] cases : partie.getPlateau().getCases()) {
			for (Case c : cases) {
			    c.setAccessible(false);
			}
		    }
		    partie.setInitialisation(false);
                    
                    if(partie.getDemo() != null){
                        partie.getDemo().nextPhase();
                        partie.getDemo().setEstModifie(true);
                    }

		    for (Joueur j : partie.getJoueursEnJeu()) {
			j.setPret(Boolean.TRUE);
		    }

		    partie.getJoueurCourant().setPret(Boolean.TRUE);
                                        
                    partie.getPlateau().setEstModifié(true);
		}
	    }

	    if (partie.isTourFini()) {
		//partie.getJoueurCourant().attendreCoup(partie);
                if (partie.estEnInitialisation()) {
                    if (!partie.getJoueurCourant().getEstHumain()) {
                        //Défini placement pingouin
                        partie.getJoueurCourant().ajouterPinguin(partie.getJoueurCourant().etablirCoup(partie));
                        partie.getPlateau().setEstModifié(true);
                        partie.joueurSuivant();
                    }
                    // Phase de jeu : Tour IA
                } else {
                    if (!partie.getJoueurCourant().getEstHumain()) {
                        partie.setTourFini(false);
                        //
                        //System.out.println("JOUEUR COURANT " + partie.getJoueurCourant().getNom() + " " + partie.getJoueurCourant().getCouleur().getNom());
                        partie.getJoueurCourant().joueCoup(partie.getJoueurCourant().etablirCoup(partie));
                        //System.out.println("COUP IA " + partie.getJoueurCourant().getPinguinCourant().getPosition().getNumLigne() + " " + partie.getJoueurCourant().getPinguinCourant().getPosition().getNumColonne());
                        
                        for (Joueur j : partie.getJoueurs()) {
                            for (Pinguin p : j.getPinguinsVivants()) {
                                if (p.getPosition().getCasePossibles().size() == 0) {
                                    p.coullePinguin();
                                    partie.getPlateau().setEstModifié(true);
                                }
                            }
                        }
                        
                        partie.joueurSuivant();
                        //System.out.println("JOUEUR COURANT " + partie.getJoueurCourant());
                        //System.out.println("DEMO: " + partie.getDemo().getPhase());
                                                
                        partie.getPlateau().setEstModifié(true);
                    }
                }
                
                
	    }

	    for (Joueur j : partie.getJoueurs()) {
                for (Pinguin p : j.getPinguinsVivants()) {
                    /*if (p.getPosition().estCoulee()) {
                        p.coullePinguin();
                        partie.getPlateau().setEstModifié(true);
                    } else*/ if (p.getPosition().getCasePossibles().size() == 0) {
                        p.coullePinguin();
                        partie.getPlateau().setEstModifié(true);
                    }
                }
            }
	} else if (partie.estTerminee() && partie.isTourFini() && partie.getDemo() == null){
	    if (!this.resultatAffiches) {
		System.out.println("PARTIE TERMINEE ===============");
		partie.afficheResultats();
		//Platform.exit();
		this.resultatAffiches = true;
		try {
		    PopUpBlurBG pu = new PopUpBlurBG(ConfigurationPartie.getConfigurationPartie().getStage(), ConfigurationPartie.getConfigurationPartie().getPartie());
		} catch (FileNotFoundException ex) {
		    Logger.getLogger(RafraichissementFX.class.getName()).log(Level.SEVERE, null, ex);
		}
	    }

	}
    }
}
