/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.Sauvegarde;
import Modele.ConfigurationPartie;
import Modele.SuggestionCoup;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    private Sauvegarde s;

    public Keyboard_Handler() {
    }

    @Override
    public void handle(KeyEvent event) {
	System.out.println("key pressed");
	//Annuler coup
	if (event.isControlDown() && event.getCode() == KeyCode.Z) {
	    System.out.println("ctrl+z");
	    ConfigurationPartie.getConfigurationPartie().getHistorique().annulerDernierCoup();
	} //Rejouer coup
	else if (event.isControlDown() && event.getCode() == KeyCode.R) {
	    ConfigurationPartie.getConfigurationPartie().getHistorique().rejouerCoup();

	}//Sauvegarder
	else if (event.isControlDown() && event.getCode() == KeyCode.S) {
	    DateFormat dateFormat = new SimpleDateFormat("dd-MM-YY HH:mm:ss");
	    Date date = new Date();
	    this.s = new Sauvegarde();
	    System.out.println(dateFormat.format(date));
	    s.Save(dateFormat.format(date));

	}//Charger
	else if (event.isControlDown() && event.getCode() == KeyCode.L) {
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

	}//Peut etre utilise pour des tests, pas assigne
	else if (event.isControlDown() && event.getCode() == KeyCode.P) {
	    System.out.println("bouton pas utilise");

	}//Suggestion du prochain coup
	else if (event.isControlDown() && event.getCode() == KeyCode.C) {

	    try {
		SuggestionCoup.suggestionCoup();
	    } catch (InterruptedException ex) {
		Logger.getLogger(Keyboard_Handler.class.getName()).log(Level.SEVERE, null, ex);
	    }

	}
    }
}
