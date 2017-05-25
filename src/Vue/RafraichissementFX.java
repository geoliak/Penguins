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
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
        if(partie.getDemo() != null && partie.getDemo().getPhase() == 5){
            stop();
        }
        
        if (!partie.equals(ConfigurationPartie.getConfigurationPartie().getPartie())) {
            this.partie = ConfigurationPartie.getConfigurationPartie().getPartie();
        }
        
        if(partie == null){
            stop();
        } else {           
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
                        
                        AnimationFX a = new AnimationFX();
                        File fmess = new File("ressources/img/img_menu/start_mess.png");
                        Image img = new Image(fmess.toURI().toString());
                        ImageView message = new ImageView(img);
                        message.setVisible(false);
                        message.setLayoutY(300);
                        message.setLayoutX(200);
                        System.out.println(message) ;
                        
                        
                        Transition t = a.scaleFromZero(message, 1, 600);

                        t.setOnFinished(new EventHandler<ActionEvent>(){
                            @Override
                            public void handle(ActionEvent event) {
                                Timer t = new Timer();
                                t.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        a.scaleToZero(message, 600);
                                    }
                                }, 2000);

                            }

                        });
                        
                        
                        ((AnchorPane) ConfigurationPartie.getConfigurationPartie().getRoot()).getChildren().add(message);
                        
                        System.out.println(message);
                        
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
                    //stop();
                    try {
                        PopUpBlurBG pu = new PopUpBlurBG(ConfigurationPartie.getConfigurationPartie().getStage(), ConfigurationPartie.getConfigurationPartie().getPartie());
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(RafraichissementFX.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        }
    }
}
