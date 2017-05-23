/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 *
 * @author mariobap
 */
public class ConfigurationPartie {

    private static ConfigurationPartie configurationPartie;

    private Partie partie;
    private Historique historique;
    private Node root;
    private Scene scene;
    private Label labelScores[] = new Label[4];
    private Stage stage;
    private ImageView[][] initpingoos;

    private ConfigurationPartie() {
	root = new Group();
    }

    public static ConfigurationPartie getConfigurationPartie() {
	if (configurationPartie == null) {
	    configurationPartie = new ConfigurationPartie();
	}
	return configurationPartie;
    }

    public Partie getPartie() {
	return partie;
    }

    public void setPartie(Partie partie) {
	this.partie = partie;
    }

    public Node getRoot() {
	return root;
    }

    public void setRoot(Node root) {
	this.root = root;
    }

    public Scene getScene() {
	return scene;
    }

    public void setScene(Scene scene) {
	this.scene = scene;
    }

    public Label[] getLabelScores() {
	return labelScores;
    }

    public void setLabelScore(Label labelScore, int i) {
	labelScores[i] = labelScore;
    }

    public Stage getStage() {
	return stage;
    }

    public void setStage(Stage stage) {
	this.stage = stage;
    }

    public ImageView[][] getInitpingoos() {
	return initpingoos;
    }

    public void setInitpingoos(ImageView[][] initpingoos) {
	this.initpingoos = initpingoos;
    }

    public Historique getHistorique() {
	return historique;
    }

    public void setHistorique(Historique historique) {
	this.historique = historique;
    }

}
