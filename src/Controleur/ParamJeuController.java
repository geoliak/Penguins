/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.ConfigurationPartie;
import Modele.Couleur;
import Modele.IA.JoueurIA1;
import Modele.IA.JoueurIA2;
import Modele.IA.JoueurIA6;
import Modele.IA.JoueurIA8;
import Modele.IA.JoueurIA9;
import Modele.IA.JoueurIASauveQuiPeut;
import Modele.IA.JoueurMinimax;
import Modele.Joueur;
import Modele.JoueurHumainLocal;
import Modele.Partie;
import Modele.Plateau;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import Modele.MyImageView;
import javafx.scene.effect.InnerShadow;

import javafx.scene.image.ImageView;
import static javafx.scene.input.KeyCode.C;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author mariobap
 */
public class ParamJeuController implements Initializable {

    @FXML
    private MyImageView titre;

    @FXML
    private MyImageView rightJ1;
    @FXML
    private MyImageView rightJ2;
    @FXML
    private MyImageView rightJ3;
    @FXML
    private MyImageView rightJ4;

    @FXML
    private MyImageView leftJ1;
    @FXML
    private MyImageView leftJ2;
    @FXML
    private MyImageView leftJ3;
    @FXML
    private MyImageView leftJ4;

    @FXML
    private TextField nameJ1;
    @FXML
    private TextField nameJ2;
    @FXML
    private TextField nameJ3;
    @FXML
    private TextField nameJ4;

    @FXML
    private Group stars1;
    @FXML
    private Group stars2;
    @FXML
    private Group stars3;
    @FXML
    private Group stars4;

    @FXML
    private MyImageView star1_1;
    @FXML
    private MyImageView star1_2;
    @FXML
    private MyImageView star1_3;
    @FXML
    private MyImageView star2_1;
    @FXML
    private MyImageView star2_2;
    @FXML
    private MyImageView star2_3;
    @FXML
    private MyImageView star3_1;
    @FXML
    private MyImageView star3_2;
    @FXML
    private MyImageView star3_3;
    @FXML
    private MyImageView star4_1;
    @FXML
    private MyImageView star4_2;
    @FXML
    private MyImageView star4_3;

    @FXML
    private MyImageView joueur3;

    @FXML
    private MyImageView terrain;

    @FXML
    private MyImageView nextTerrain;

    @FXML
    private MyImageView prevTerrain;
    
    @FXML
    private MyImageView fermer, retour; 
    
    @FXML
    private MyImageView chargerpartie, jouer;
    
    @FXML
    private MyImageView joueur1, joueur2, joueur4;
    
    @FXML
    private AnchorPane ap;

    private int[] typesJoueurs = {0, 1, 2, 2};
    private int[] difficultesIA = {0, 2, 0, 0};
    private Image[] stars = {new Image(getClass().getClassLoader().getResourceAsStream("img/grey_star.png")), new Image(getClass().getClassLoader().getResourceAsStream("img/yellow_star.png"))};
    private int terrainCharge = 1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
	((ImageView) terrain).setImage(new Image(getClass().getClassLoader().getResourceAsStream("plateaux_jeu/img/plateau_1.png")));
        jouer.setEffect(new DropShadow());
        chargerpartie.setEffect(new DropShadow());
        
        chargerpartie.setX(10);
        chargerpartie.setY(10);
        CloseButton c = new CloseButton(fermer);
        BackButton b = new BackButton(retour, "Accueil");
        
        joueur1.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/choix_joueur_1.png")));
        leftJ1.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/left-arrow.png")));
        rightJ1.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/right-arrow.png")));
        star1_1.setImage(stars[1]);
        star1_2.setImage(stars[0]);
        star1_3.setImage(stars[0]);
        
        joueur2.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/choix_joueur_2_com.png")));
        leftJ2.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/left-arrow.png")));
        rightJ2.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/right-arrow.png")));
        star2_1.setImage(stars[1]);
        star2_2.setImage(stars[1]);
        star2_3.setImage(stars[1]);
        
        joueur3.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/choix_joueur_3_unable.png")));
        leftJ3.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/left-arrow.png")));
        rightJ3.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/right-arrow.png")));
        star3_1.setImage(stars[1]);
        star3_2.setImage(stars[0]);
        star3_3.setImage(stars[0]);
        
        joueur4.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/choix_joueur_4_unable.png")));
        leftJ4.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/left-arrow.png")));
        rightJ4.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/right-arrow.png")));
        star4_1.setImage(stars[1]);
        star4_2.setImage(stars[0]);
        star4_3.setImage(stars[0]);
        
        terrain.setImage(new Image(getClass().getClassLoader().getResourceAsStream("plateaux_jeu/img/plateau_1.png")));
        prevTerrain.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/left-arrow.png")));
        nextTerrain.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/right-arrow.png")));
        jouer.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/img_menu/bouton_jouer_resize2.png")));
        chargerpartie.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/img_menu/bouton_charger_partie_resize.png")));
        
        Settings.setSettings(ap);
    }

    public void arrowClick(MouseEvent e) {
	//Fleche Droite
	if (e.getSource().equals(rightJ1)) {
	    changeJoueur(0, 1);
	} else if (e.getSource().equals(rightJ2)) {
	    changeJoueur(1, 1);

	} else if (e.getSource().equals(rightJ3)) {
	    changeJoueur(2, 1);

	} else if (e.getSource().equals(rightJ4)) {
	    changeJoueur(3, 1);

	} //Fleche Gauche
	else if (e.getSource().equals(leftJ1)) {
	    changeJoueur(0, -1);

	} else if (e.getSource().equals(leftJ2)) {
	    changeJoueur(1, -1);

	} else if (e.getSource().equals(leftJ3)) {
	    changeJoueur(2, -1);

	} else if (e.getSource().equals(leftJ4)) {
	    changeJoueur(3, -1);

	}
    }

    public void starClick(MouseEvent e) {
	if (e.getSource().equals(star1_1)) {
	    star1_1.setImage(stars[1]);
	    star1_2.setImage(stars[0]);
	    star1_3.setImage(stars[0]);
	    difficultesIA[0] = 0;
	} else if (e.getSource().equals(star1_2)) {
	    star1_1.setImage(stars[1]);
	    star1_2.setImage(stars[1]);
	    star1_3.setImage(stars[0]);
	    difficultesIA[0] = 1;
	} else if (e.getSource().equals(star1_3)) {
	    star1_1.setImage(stars[1]);
	    star1_2.setImage(stars[1]);
	    star1_3.setImage(stars[1]);
	    difficultesIA[0] = 2;
	} else if (e.getSource().equals(star2_1)) {
	    star2_1.setImage(stars[1]);
	    star2_2.setImage(stars[0]);
	    star2_3.setImage(stars[0]);
	    difficultesIA[1] = 0;
	} else if (e.getSource().equals(star2_2)) {
	    star2_1.setImage(stars[1]);
	    star2_2.setImage(stars[1]);
	    star2_3.setImage(stars[0]);
	    difficultesIA[1] = 1;
	} else if (e.getSource().equals(star2_3)) {
	    star2_1.setImage(stars[1]);
	    star2_2.setImage(stars[1]);
	    star2_3.setImage(stars[1]);
	    difficultesIA[1] = 2;
	} else if (e.getSource().equals(star3_1)) {
	    star3_1.setImage(stars[1]);
	    star3_2.setImage(stars[0]);
	    star3_3.setImage(stars[0]);
	    difficultesIA[2] = 0;
	} else if (e.getSource().equals(star3_2)) {
	    star3_1.setImage(stars[1]);
	    star3_2.setImage(stars[1]);
	    star3_3.setImage(stars[0]);
	    difficultesIA[2] = 1;
	} else if (e.getSource().equals(star3_3)) {
	    star3_1.setImage(stars[1]);
	    star3_2.setImage(stars[1]);
	    star3_3.setImage(stars[1]);
	    difficultesIA[2] = 2;
	} else if (e.getSource().equals(star4_1)) {
	    star4_1.setImage(stars[1]);
	    star4_2.setImage(stars[0]);
	    star4_3.setImage(stars[0]);
	    difficultesIA[3] = 0;
	} else if (e.getSource().equals(star4_2)) {
	    star4_1.setImage(stars[1]);
	    star4_2.setImage(stars[1]);
	    star4_3.setImage(stars[0]);
	    difficultesIA[3] = 1;
	} else if (e.getSource().equals(star4_3)) {
	    star4_1.setImage(stars[1]);
	    star4_2.setImage(stars[1]);
	    star4_3.setImage(stars[1]);
	    difficultesIA[3] = 2;
	}
    }

    private void changeJoueur(int Joueur, int deplacement) {
	typesJoueurs[Joueur] = (typesJoueurs[Joueur] + deplacement) % 3;
	if (typesJoueurs[Joueur] < 0) {
	    typesJoueurs[Joueur] = 2;
	}

	//type du joueur
	switch (typesJoueurs[Joueur]) {
	    //joueur humain
	    case 0:
		//numéro du joueur
		switch (Joueur) {
		    case 0:
			nameJ1.setVisible(true);
			stars1.setVisible(false);
                        joueur1.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/choix_joueur_1.png")));
			break;

		    case 1:
			stars2.setVisible(false);
			nameJ2.setVisible(true);
                        joueur2.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/choix_joueur_2.png")));
			break;

		    case 2:
			stars3.setVisible(false);
			nameJ3.setVisible(true);
                        joueur3.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/choix_joueur_3.png")));
			break;

		    case 3:
			stars4.setVisible(false);
			nameJ4.setVisible(true);
                        joueur4.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/choix_joueur_4.png")));
			break;
		}
		break;

	    case 1:
		// joueur IA
		switch (Joueur) {
		    case 0:
			nameJ1.setVisible(false);
			stars1.setVisible(true);
                        joueur1.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/choix_joueur_1_com.png")));
			break;

		    case 1:
			stars2.setVisible(true);
			nameJ2.setVisible(false);
                        joueur2.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/choix_joueur_2_com.png")));
			break;

		    case 2:
			stars3.setVisible(true);
			nameJ3.setVisible(false);
                        joueur3.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/choix_joueur_3_com.png")));
			break;

		    case 3:
			stars4.setVisible(true);
			nameJ4.setVisible(false);
                        joueur4.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/choix_joueur_4_com.png")));
			break;
		}
		break;

	    //pas de joueur
	    case 2:
		//numéro du joueur
		switch (Joueur) {
		    case 0:
			nameJ1.setVisible(false);
			stars1.setVisible(false);
                        joueur1.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/choix_joueur_1_unable.png")));
			break;

		    case 1:
			stars2.setVisible(false);
			nameJ2.setVisible(false);
                        joueur2.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/choix_joueur_2_unable.png")));
			break;

		    case 2:
			stars3.setVisible(false);
			nameJ3.setVisible(false);
                        joueur3.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/choix_joueur_3_unable.png")));
			break;

		    case 3:
			stars4.setVisible(false);
			nameJ4.setVisible(false);
                        joueur4.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/choix_joueur_4_unable.png")));
			break;
		}
		break;
	}
    }

    public void jouer(MouseEvent e) throws IOException {

	Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
	Partie partie = creationPartie();
	ConfigurationPartie.getConfigurationPartie().setPartie(partie);
        ConfigurationPartie.getConfigurationPartie().setEnableHelp(true);

	FenetreJeuController fenetre = new FenetreJeuController();
	fenetre.creerFenetreJeu(stage);

    }

    public void on(MouseEvent e) {
	((MyImageView) e.getSource()).setEffect(new InnerShadow());
    }

    public void out(MouseEvent e) {
	((MyImageView) e.getSource()).setEffect(new DropShadow());
    }

    public void chargerPartie(MouseEvent e) throws IOException {
	Parent chargerJeu = FXMLLoader.load(getClass().getClassLoader().getResource("Vue/ChargerJeu.fxml"));
	Scene scene = new Scene(chargerJeu);
	Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
	stage.setScene(scene);
	stage.show();
    }

    public Partie creationPartie() throws IOException {
        System.out.println(terrainCharge);
        
	Plateau plateau = new Plateau("plateaux_jeu/plateau" + terrainCharge);
        //System.out.println("PARTIE PLATEAU 3");
	//plateau.initCase();

	String[] names = new String[4];
	Couleur[] couleurs = {Couleur.RougeFX, Couleur.VioletFX, Couleur.JauneFX, Couleur.VertFX};

	if (nameJ1.getText().equals("")) {
	    names[0] = "Joueur 1";
	} else {
	    names[0] = nameJ1.getText();
	}

	if (nameJ2.getText().equals("")) {
	    names[1] = "Joueur 2";
	} else {
	    names[1] = nameJ2.getText();
	}

	if (nameJ3.getText().equals("")) {
	    names[2] = "Joueur 3";
	} else {
	    names[2] = nameJ3.getText();
	}

	if (nameJ4.getText().equals("")) {
	    names[3] = "Joueur 4";
	} else {
	    names[3] = nameJ4.getText();
	}

	ArrayList<Joueur> joueurs = new ArrayList<>();
	for (int i = 0; i < 4; i++) {
	    System.out.print("Joueur " + i + " : ");
	    if (typesJoueurs[i] == 0) {
		System.out.println("humain");
		joueurs.add(new JoueurHumainLocal(names[i], couleurs[i], joueurs.size()));
	    } else if (typesJoueurs[i] == 1) {
		if (difficultesIA[i] == 0) {
		    System.out.println("IA facile");
		    Joueur j = new JoueurIA1(couleurs[i], joueurs.size());
		    j.setDifficulte(1);
		    joueurs.add(j);
		} else if (difficultesIA[i] == 1) {
		    System.out.println("IA moyenne");
		    Joueur j = new JoueurIA8(couleurs[i], joueurs.size());
		    j.setDifficulte(2);
		    joueurs.add(j);
		} else {
		    System.out.println("IA difficile");
		    Joueur j = new JoueurIA9(couleurs[i], joueurs.size());
		    j.setDifficulte(3);
		    joueurs.add(j);
		}
	    } else {
		System.out.println("pas de joueur");
//                joueurs.add(null);
	    }
	}
        
	Partie partie = new Partie(plateau, joueurs);

	return partie;
    }
    
    public void changerTerrain(MouseEvent e){
        if(e.getSource().equals(nextTerrain)){
            if(terrainCharge<3){
                terrainCharge ++;
            } else {
                terrainCharge = 1;
            }
        } else if(e.getSource().equals(prevTerrain)){
            if(terrainCharge>1){
                terrainCharge --;
            } else {
                terrainCharge = 3;
            }
        }
        String str = "plateaux_jeu/img/plateau_" + terrainCharge + ".png";
        ((ImageView) terrain).setImage(new Image(getClass().getClassLoader().getResourceAsStream(str)));
    }
}
