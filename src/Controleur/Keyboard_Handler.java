/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import java.awt.event.ActionListener;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javax.swing.JButton;

/**
 *
 * @author liakopog
 */
public class Keyboard_Handler implements EventHandler<KeyEvent> {

    private AnnulerCoup ac;

    public Keyboard_Handler(AnnulerCoup a) {
	this.ac = a;
    }

    @Override
    public void handle(KeyEvent event) {
	System.out.println("key pressed");
	if (event.isControlDown() && event.getCode() == KeyCode.Z) {
	    System.out.println("ctrl+z");
	    ac.annulerDernierCoup();
	}

    }
}
