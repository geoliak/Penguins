/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author mariobap
 */
public class Menus extends Application{
    

    @Override
    public void start(Stage stage) throws IOException {
       Parent root = FXMLLoader.load(getClass().getResource("../Vue/Accueil.fxml"));
       stage.setTitle("Salut les pingoins");
       stage.setScene(new Scene(root, 1200, 900));
       stage.setResizable(false);
       stage.show();
       
    }
    
    public static void main(String[] args){
        launch(args);
    }
    
}
