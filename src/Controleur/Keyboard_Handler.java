/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Vue.DessinateurTexte;
import Vue.InterfaceFX;
import java.io.FileNotFoundException;
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

    private AnnulerCoup ac;
    private Sauvegarde s;

    public Keyboard_Handler(AnnulerCoup a) {
	this.ac = a;
    }

    @Override
    public void handle(KeyEvent event) {
	System.out.println("key pressed");
	if (event.isControlDown() && event.getCode() == KeyCode.Z) {
	    try {
		System.out.println("ctrl+z");
		ac.annulerDernierCoup();
	    } catch (FileNotFoundException ex) {
		Logger.getLogger(Keyboard_Handler.class.getName()).log(Level.SEVERE, null, ex);
	    }
	} else if (event.isControlDown() && event.getCode() == KeyCode.S) {
	    this.s = new Sauvegarde(InterfaceFX.getPartie());
	    s.Save(1);

	} else if (event.isControlDown() && event.getCode() == KeyCode.L) {
	    try {
		this.s = new Sauvegarde(InterfaceFX.getPartie());

		DessinateurTexte d = new DessinateurTexte();

//		InterfaceFX.getPartie().getPlateau().accept(d);
		InterfaceFX.setPartie(this.s.Load(1));
//		InterfaceFX.getPartie().getPlateau().accept(d);
		InterfaceFX.getPartie().setReloadPartie(true);
		InterfaceFX.getPartie().getPlateau().setEstModifié(true);

	    } catch (IOException ex) {
		Logger.getLogger(Keyboard_Handler.class.getName()).log(Level.SEVERE, null, ex);
	    } catch (ClassNotFoundException ex) {
		Logger.getLogger(Keyboard_Handler.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    System.out.println("load : " + InterfaceFX.getPartie());
	    System.out.println("load done");

	} else if (event.isControlDown() && event.getCode() == KeyCode.P) {
	    System.out.println("Bouton pas utilisé");
	}
    }
}
