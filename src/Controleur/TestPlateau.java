/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.ConfigurationPartie;
import Modele.Couleur;
import Modele.Joueur;
import Modele.JoueurHumain;
import Modele.JoueurHumainLocal;
import Modele.Partie;
import Modele.Plateau;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author rozandq
 */
public class TestPlateau extends Application {
    public static void main(String args[]) throws IOException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Plateau p = new Plateau("ressources/plateaux_jeu/plateau3");
        
        ArrayList<Joueur> joueurs = new ArrayList<>();
        joueurs.add(new JoueurHumainLocal("jean", Couleur.RougeFX, 0));
        
        Partie partie = new Partie(p, joueurs);
        
        ConfigurationPartie.getConfigurationPartie().setPartie(partie);
        
        FenetreJeuController f = new FenetreJeuController();
        f.creerFenetreJeu(new Stage());
    }
}
