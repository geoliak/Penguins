/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.Case;
import Modele.ClientJeu;
import Modele.ConfigurationPartie;
import Modele.Joueur;
import Modele.MyPolygon;
import Modele.Partie;
import Modele.Pinguin;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author liakopog
 */
public class MouseClickerCaseReseau implements EventHandler<MouseEvent> {

    Partie partie;
    MyPolygon p;
    int rowclic;
    int columnclic;
    private int numJoueur;
    int size;
    private ClientJeu client;

    public MouseClickerCaseReseau(MyPolygon p, ClientJeu cl) {
	this.p = p;
	this.partie = ConfigurationPartie.getConfigurationPartie().getPartie();
        numJoueur = cl.getNumJoueur();
        client = cl;
    }

    @Override
    public void handle(MouseEvent event) {
        if(ConfigurationPartie.getConfigurationPartie().getPartie().getJoueurCourant().getNumero() == numJoueur){
            //System.out.println("POINT CLIC : " + (event.getX()+p.getXorigine()) + " " + (event.getY()+p.getYorigine()));
            // Récupération de la ligne et colonne de l'ilot cliqué
            rowclic = p.getY();
            columnclic = p.getX();

            // Joueur Humain
            if (partie.getJoueurCourant().getEstHumain() && partie.isTourFini()) {
                if (partie.getHistorique().sauvegardeDebut()) {
                    partie.getHistorique().sauvegarderCoup();
                }

                partie.setTourFini(false);
                // Initialisation : Placement pingouins
                if (partie.estEnInitialisation()) {
                    if (partie.getPlateau().getCases()[rowclic][columnclic].estCaseValideInit()) {

                        partie.getJoueurCourant().ajouterPinguin(partie.getPlateau().getCases()[rowclic][columnclic]);
                        partie.getPlateau().getCases()[rowclic][columnclic].setAccessible(false);
                        partie.getPlateau().setEstModifié(true);
                        partie.joueurSuivant();
                        System.out.println("Joueur suivant");
                    } else {
                        partie.setTourFini(true);
                    }

                    // Phase de jeu
                } else {
                    Pinguin pingouin = partie.getJoueurCourant().getPinguinCourant();

                    if (pingouin != null) {
                        Case caseDest = partie.getPlateau().getCases()[rowclic][columnclic];
                        if (caseDest.estCaseLibre() && caseDest.getAccessible()) {
                            partie.getHistorique().sauvegarderCoup();
                            pingouin.deplace(caseDest);

    //			partie.getPlateau().setEstModifié(true);
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
}
