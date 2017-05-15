/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.Plateau;
import Vue.InterfaceFX;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author novelm
 */
public class TestFX {

    public static void main(String[] args) {
	try {

	    Plateau plateau = new Plateau("ressources/plateaux/plateau4");

	    System.out.println(plateau);
	    InterfaceFX i = new InterfaceFX();
	    i.creer(args, plateau);
	} catch (IOException ex) {
	    System.out.println("Erreur d'ouverture du fichier");
	    Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

}
