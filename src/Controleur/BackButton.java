/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author rozandq
 */
public class BackButton {
    ImageView closeiv;
    
    
    public BackButton(ImageView retour, String file) {
        Image close = new Image(getClass().getClassLoader().getResourceAsStream("img/img_menu/retour.png"));
        retour.setImage(close);
        retour.setFitWidth(40);
        retour.setX(1200 - retour.getFitWidth() - 60);
        retour.setY(10);
        retour.setEffect(new DropShadow());
        retour.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                retour.setEffect(new InnerShadow());
            }
        });
        
        retour.setOnMouseExited(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                retour.setEffect(new DropShadow());
            }
        });
        
        retour.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                try {
                    Parent paramJeu = FXMLLoader.load(getClass().getClassLoader().getResource("Vue/" + file + ".fxml"));
                    Scene scene = new Scene(paramJeu);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ex) {
                    Logger.getLogger(BackButton.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        this.closeiv = retour;
    }
    
}
