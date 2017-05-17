/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import static java.nio.file.Files.createDirectory;
import static java.nio.file.Files.isExecutable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author liakopog
 */
public class Historique implements Serializable {

    private String dossierNom;
    private int indice = 0;
    private static LinkedList<Coup> historiqueCoups;

    public Historique() {
	Historique.historiqueCoups = new LinkedList<>();
    }

    public void rejouerCoup() {
	if (historiqueCoups.size() > indice) {
	    if (ConfigurationPartie.getConfigurationPartie().getPartie().getJoueurCourant().equals(historiqueCoups.get(indice))) {

	    }
	}
    }

    public void sauvegarderCoup() {
	historiqueCoups.add(new Coup());
	indice++;

    }

    public void annulerDernierCoup() throws FileNotFoundException {

    }
}
