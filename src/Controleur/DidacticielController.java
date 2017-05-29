/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.ConfigurationPartie;
import Modele.Couleur;
import Modele.IA.JoueurIA1;
import Modele.Joueur;
import Modele.JoueurHumain;
import Modele.JoueurHumainLocal;
import Modele.Partie;
import Modele.Plateau;
import Vue.AnimationFX;
import Vue.DessinateurFX;
import Vue.RafraichissementFX;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author rozandq
 */
public class DidacticielController{
    public void start(Stage stage) throws IOException {
        Plateau p = new Plateau("plateaux_didacticiel/plateau_init_move");
        
        ArrayList<Joueur> joueurs = new ArrayList<>();
        joueurs.add(new JoueurHumainLocal("jean", Couleur.RougeFX, 0));
        joueurs.add(new JoueurIA1(Couleur.VioletFX, 1));
        
        Partie partie = new Partie(p, joueurs, true);
        
        ConfigurationPartie.getConfigurationPartie().setPartie(partie);
        ConfigurationPartie.getConfigurationPartie().getPartie().setNbPingouinParJoueur();
        
        FenetreJeuController f = new FenetreJeuController();
        f.creerFenetreJeu(stage);
    }
}
