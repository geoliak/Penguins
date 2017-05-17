/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;



public class AccueilController implements Initializable {
    
    @FXML private ImageView locale;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    public void jouerLocal(MouseEvent e) throws IOException{
        Parent paramJeu = FXMLLoader.load(getClass().getResource("../Vue/ParamJeu.fxml"));
        Scene scene = new Scene(paramJeu);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    
    public void on(MouseEvent e){
        ((ImageView) e.getSource()).setEffect(new DropShadow());
    }
    
    public void out(MouseEvent e){
        ((ImageView) e.getSource()).setEffect(null);
    }
    
}
