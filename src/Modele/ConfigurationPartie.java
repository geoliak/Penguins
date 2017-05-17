/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;

/**
 *
 * @author mariobap
 */
public class ConfigurationPartie {

    private static ConfigurationPartie configurationPartie;

    private Partie partie;
    private Node root;
    private Scene scene;

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

}
