/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.Historique;
import Modele.Sauvegarde;
import Modele.ConfigurationPartie;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author liakopog
 */
public class Keyboard_Handler implements EventHandler<KeyEvent> {

    private Historique ac;
    private Sauvegarde s;

    public Keyboard_Handler(Historique a) {
	this.ac = a;
    }

    @Override
    public void handle(KeyEvent event) {
	System.out.println("key pressed");
	if (event.isControlDown() && event.getCode() == KeyCode.Z) {
	    System.out.println("ctrl+z");
	    ac.annulerDernierCoup();
	} else if (event.isControlDown() && event.getCode() == KeyCode.S) {
	    this.s = new Sauvegarde();
	    s.Save("1");

	} else if (event.isControlDown() && event.getCode() == KeyCode.L) {
	    try {
		this.s = new Sauvegarde();

		ConfigurationPartie.getConfigurationPartie().setPartie(this.s.Load("1"));
		ConfigurationPartie.getConfigurationPartie().getPartie().setReloadPartie(true);
		ConfigurationPartie.getConfigurationPartie().getPartie().getPlateau().setEstModifié(true);

	    } catch (IOException ex) {
		Logger.getLogger(Keyboard_Handler.class.getName()).log(Level.SEVERE, null, ex);
	    } catch (ClassNotFoundException ex) {
		Logger.getLogger(Keyboard_Handler.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    System.out.println("load : " + ConfigurationPartie.getConfigurationPartie().getPartie());
	    System.out.println("load done");

	} else if (event.isControlDown() && event.getCode() == KeyCode.P) {
	    System.out.println("Bouton pas utilisé");
	}
    }
}