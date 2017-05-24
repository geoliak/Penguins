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
import Vue.DessinateurTexte;
import java.io.File;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
    @FXML
    private ImageView terrain, fermer, retour;
    
    @FXML
    private AnchorPane ap;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
	CloseButton c = new CloseButton(fermer);
	BackButton b = new BackButton(retour, "ParamJeu");

	ObservableList<String> items = listView.getItems();

	files = new File("./Savefiles").listFiles();

	for (File file : files) {
	    if (file.isFile()) {
		String[] name = file.getName().split("_");
		if (name[0].equals("S")) {
		    items.add(name[1]);
		}

	    }
	}

	listView.getSelectionModel().select(0);
	listView.getFocusModel().focus(0);
	saveClick(null);
        
        Settings.setSettings(ap);
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

    public void lancerPartie(MouseEvent e) throws IOException, ClassNotFoundException {
	if (listView.getSelectionModel().getSelectedItems().size() != 0) {
	    String filename = listView.getSelectionModel().getSelectedItems().get(0).toString();

	    Partie partie = new Sauvegarde().Load(filename);
	    ConfigurationPartie.getConfigurationPartie().setPartie(partie);

	    Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
	    FenetreJeuController fenetre = new FenetreJeuController();
	    fenetre.creerFenetreJeu(stage);

	    ConfigurationPartie.getConfigurationPartie().getPartie().setReloadPartie(true);
	    ConfigurationPartie.getConfigurationPartie().getPartie().getPlateau().setEstModifié(true);
            
            System.out.println("PARTIE CHARGEE");
            DessinateurTexte d = new DessinateurTexte();
            ConfigurationPartie.getConfigurationPartie().getPartie().getPlateau().accept(d);
	}
    }

    public void saveClick(MouseEvent e) {
	if (listView.getSelectionModel().getSelectedItems().size() != 0) {
	    String filename = "./Savefiles/I_" + listView.getSelectionModel().getSelectedItems().get(0).toString();
	    terrain.setImage(new Image(new File(filename).toURI().toString()));
	} else {
	    terrain.setImage(null);
	}

    }

    public void deleteSave(MouseEvent e) {
	if (listView.getItems().size() != 0) {
	    String filename = listView.getSelectionModel().getSelectedItems().get(0).toString();

	    int i = listView.getSelectionModel().getSelectedIndices().get(0);

	    File f = new File("./Savefiles/S_" + filename);
	    f.delete();

	    f = new File("./Savefiles/I_" + filename);
	    f.delete();

	    f = new File("./Savefiles/H_" + filename);
	    f.delete();

	    List<String> items = listView.getItems();
	    items.removeAll(items);

	    files = new File("./Savefiles").listFiles();

	    for (File file : files) {
		if (file.isFile()) {
		    String[] name = file.getName().split("_");
		    if (name[0].equals("S")) {
			items.add(name[1]);
		    }

		}
	    }

	    if (listView.getItems().size() != i) {
		listView.getSelectionModel().select(i);
		listView.getFocusModel().focus(i);
	    } else {
		listView.getSelectionModel().select(i - 1);
		listView.getFocusModel().focus(i - 1);
	    }

	    saveClick(null);
	}

    }

}
