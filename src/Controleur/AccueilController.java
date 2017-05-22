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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class AccueilController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void jouerLocal(MouseEvent e) throws IOException {
	Parent paramJeu = FXMLLoader.load(getClass().getResource("../Vue/ParamJeu.fxml"));
	Scene scene = new Scene(paramJeu);
	Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
	stage.setScene(scene);
	stage.show();
    }

    public void on(MouseEvent e) {
	((MyImageView) e.getSource()).setEffect(new DropShadow());
    }

    public void out(MouseEvent e) {
	((MyImageView) e.getSource()).setEffect(null);
    }
    
    public void lancerDidacticiel(MouseEvent e) throws IOException{
        DidacticielController d = new DidacticielController();
        d.start((Stage) ((Node) e.getSource()).getScene().getWindow());
    }
}
