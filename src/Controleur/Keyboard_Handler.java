/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.Partie;
import Vue.InterfaceFX;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.swing.JButton;

/**
 *
 * @author liakopog
 */
public class Keyboard_Handler implements EventHandler<KeyEvent> {

    private AnnulerCoup ac;
    private Partie p;
    private Sauvegarde s;
    private InterfaceFX fx;

    public Keyboard_Handler(AnnulerCoup a, Partie p, InterfaceFX fx, Stage stage) {
	this.ac = a;
	this.p = p;
	this.fx = fx;
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
	    this.s = new Sauvegarde(p);
	    s.Save(1);

	} else if (event.isControlDown() && event.getCode() == KeyCode.L) {
	    this.s.Load(1);
	    System.out.println("sqdqd");
	    fx.continuer(null, p);
	    System.out.println("doone");

	}
    }
}
