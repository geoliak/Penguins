/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import Modele.MyImageView;
import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AccueilController implements Initializable {
    @FXML
    private ImageView retour, fermer, nom, locale, reseau, demo;
    
    @FXML
    private AnchorPane ap;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CloseButton c = new CloseButton(fermer);
        
        locale.setEffect(new DropShadow());
        reseau.setEffect(new DropShadow());
        demo.setEffect(new DropShadow());
        
        nom.setX((1200-nom.getFitWidth()) / 2);
        
        Settings.setSettings(ap);
        
        
        nom.setImage(new Image(new File("./ressources/img/img_menu/nom_du_jeu.png").toURI().toString()));
    }

    public void jouerLocal(MouseEvent e) throws IOException {
	Parent paramJeu = FXMLLoader.load(getClass().getResource("../Vue/ParamJeu.fxml"));
	Scene scene = new Scene(paramJeu);
	Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
	stage.setScene(scene);
	stage.show();
    }

    public void on(MouseEvent e) {
	((MyImageView) e.getSource()).setEffect(new InnerShadow());
    }

    public void out(MouseEvent e) {
	((MyImageView) e.getSource()).setEffect(new DropShadow());
    }

    public void reseau(MouseEvent e) throws IOException{
        Parent paramJeu = FXMLLoader.load(getClass().getResource("../Vue/JouerReseau.fxml"));
	Scene scene = new Scene(paramJeu);
	Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
	stage.setScene(scene);
	stage.show();
    }
    
    public void lancerDidacticiel(MouseEvent e) throws IOException{
        DidacticielController d = new DidacticielController();
        d.start((Stage) ((Node) e.getSource()).getScene().getWindow());
    }
}
