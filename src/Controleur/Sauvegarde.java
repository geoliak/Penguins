/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.Partie;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import static java.nio.file.Files.createDirectory;
import static java.nio.file.Files.deleteIfExists;
import static java.nio.file.Files.isExecutable;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Sauvegarde {

    private Partie partie;
    private Path savepath;

    public Sauvegarde(Partie p) {
	this.partie = p;
	this.savepath = Paths.get("Savefiles");

	//creation du dossier de sauvegarde
	if (!isExecutable(savepath)) {
	    try {
		createDirectory(this.savepath);
	    } catch (IOException ex) {
		Logger.getLogger(Sauvegarde.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
    }

    public void Save(int filenum) {
	try {
	    Path filepath = Paths.get(savepath + "/save" + filenum);
	    deleteIfExists(filepath);
	    Files.createFile(filepath);

	    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filepath.toFile()));
	    oos.writeObject(partie);

	    System.out.println("Partie Sauvegardee a: " + filepath.toString());
	} catch (IOException ex) {
	    Logger.getLogger(Sauvegarde.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    public void Load(int filenum) {
	ObjectInputStream ois = null;
	try {
	    Path filepath = Paths.get(savepath + "/save" + filenum);
	    ois = new ObjectInputStream(new FileInputStream(filepath.toFile()));
	    Partie partieacharger = (Partie) ois.readObject();
	    partie.setPlateau(partieacharger.getPlateau());
	    partie.setJoueursEnJeu(partieacharger.getJoueursEnJeu());
	    partie.setJoueurs(partieacharger.getJoueurs());
	    partie.setJoueurCourant(partieacharger.getJoueurCourant());
	    partie.setNbPingouinParJoueur();
	    partie.setInitialisation(partieacharger.getInitialisation());
	} catch (IOException | ClassNotFoundException ex) {
	    Logger.getLogger(Sauvegarde.class.getName()).log(Level.SEVERE, null, ex);
	} finally {
	    try {
		ois.close();
	    } catch (IOException ex) {
		Logger.getLogger(Sauvegarde.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
    }
}
