/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.ConfigurationPartie;
import Modele.IA.JoueurIA1;
import Modele.IA.JoueurIA8;
import Modele.IA.JoueurMinimax;
import Modele.Joueur;
import Modele.Partie;
import Modele.Pinguin;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author mariobap
 */
public class MouseClickerStar implements EventHandler<MouseEvent> {

    private int nbStar;
    private ImageView [] stars;
    private Image yellow;
    private Image grey;
    private int numJoueur;

    public MouseClickerStar(int nb, ImageView[] s, Image y, Image g, int num) {
        nbStar = nb;
        stars = s;
        yellow = y;
        grey = g;
        numJoueur = num;
    }
    
    
    
    @Override
    public void handle(MouseEvent event) {
            System.out.println("test");
            for(int i = 0; i<stars.length; i++){
                if(i <= nbStar){
                    stars[i].setImage(yellow);
                }
                else {
                    stars[i].setImage(grey);
                }
            }

            ArrayList<Joueur> joueurs = ConfigurationPartie.getConfigurationPartie().getPartie().getJoueurs();
            Joueur j1 = null, j2 = null;

            switch(nbStar){
                case 0:
                    j1 = joueurs.remove(numJoueur);
                    j2 = new JoueurIA1(j1.getCouleur(), numJoueur);
                    break;
                case 1:
                    j1 = joueurs.remove(numJoueur);
                    j2 =new JoueurIA8(j1.getCouleur(), numJoueur);
                    break;
                case 2:
                    j1 = joueurs.remove(numJoueur);
                    j2 = new JoueurMinimax(j1.getCouleur(), numJoueur);
                    break;
            }
            
            for(Pinguin p : j1.getPinguins()){
                j2.ajouterPinguin(p.getPosition());
            }
            
            joueurs.add(j2);
    }
    
}
