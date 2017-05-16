/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author mariobap
 */
public class ChargerJeuController implements Initializable {

    @FXML private ListView<String> listView; 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> items = listView.getItems();
        items.add("One");
        items.add("Two");
        items.add("Three");
        items.add("Four");
        items.add("Five");
        
        listView.getSelectionModel().select(0);
        listView.getFocusModel().focus(0);
    }    
    
    public void creerPartie(MouseEvent e) throws IOException{
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
