/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.ConfigurationPartie;
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
import Modele.MyImageView;
import Modele.Partie;
import Modele.Sauvegarde;
import java.io.File;
import java.util.ArrayList;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author mariobap
 */
public class ChargerJeuController implements Initializable {

    @FXML
    private ListView<String> listView;
    private File[] files;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        ArrayList<String> results = new ArrayList<>();
	ObservableList<String> items = listView.getItems();


        files = new File("./Savefiles").listFiles();

        for (File file : files) {
            if (file.isFile()) {
                items.add(file.getName());
            }
        }
	

	listView.getSelectionModel().select(0);
	listView.getFocusModel().focus(0);
    }

    public void creerPartie(MouseEvent e) throws IOException {
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
    
    public void lancerPartie(MouseEvent e) throws IOException, ClassNotFoundException{
        Partie partie = new Sauvegarde().Load(1);
        ConfigurationPartie.getConfigurationPartie().setPartie(partie);
	Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        FenetreJeuController fenetre = new FenetreJeuController();
        fenetre.creerFenetreJeu(stage);
    }

}
