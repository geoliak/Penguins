/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import javafx.scene.Group;

/**
 *
 * @author mariobap
 */
public class ConfigurationPartie {
    private static ConfigurationPartie configurationPartie;
    
    private Partie partie;
    private Group root;
    
    private ConfigurationPartie(){
        root = new Group();
    }

    public static ConfigurationPartie getConfigurationPartie() {
        if(configurationPartie == null){
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

    public Group getRoot() {
        return root;
    }

    public void setRoot(Group root) {
        this.root = root;
    }
    
    
    
}