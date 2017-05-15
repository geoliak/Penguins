/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.Joueur;
import Modele.Partie;
import Modele.Plateau;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author liakopog
 */
public class AnnulerCoup implements Serializable {

    Path dossier;
    private int coups = 1; //Initialisé à 1
    private Partie partie;

    public AnnulerCoup(Partie partie) {
	this.partie = partie;

	//creation du dossier de l'historique
	this.dossier = Paths.get("Historique");
	if (!isExecutable(dossier)) {
	    try {
		createDirectory(this.dossier);
	    } catch (IOException ex) {
		Logger.getLogger(AnnulerCoup.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
    }

    public void sauvegarderCoup() {
	ObjectOutputStream oos = null;
	try {
	    oos = new ObjectOutputStream(new FileOutputStream(dossier + "/" + Paths.get("Plateau" + coups).toFile()));
	    oos.writeObject(partie.getPlateau());
	    oos = new ObjectOutputStream(new FileOutputStream(dossier + "/" + Paths.get("Joueursenjeu" + coups).toFile()));
	    oos.writeObject(partie.getJoueursEnJeu());
	    oos = new ObjectOutputStream(new FileOutputStream(dossier + "/" + Paths.get("Joueurcourant" + coups).toFile()));
	    oos.writeObject(partie.getJoueurCourant());
	} catch (IOException ex) {
	    Logger.getLogger(AnnulerCoup.class.getName()).log(Level.SEVERE, null, ex);
	} finally {
	    try {
		oos.close();
	    } catch (IOException ex) {
		Logger.getLogger(AnnulerCoup.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
	coups++;
    }

    void annulerDernierCoup() throws FileNotFoundException {
	if (Files.exists(Paths.get(dossier.toString() + "/Plateau" + (coups - 1)))) {
	    System.out.println("annulation coup!");
	    coups--;

	    System.out.println(Paths.get(dossier.toString() + "/Plateau" + coups));

	    try {

		ObjectInputStream ois = null;

		ois = new ObjectInputStream(new FileInputStream(dossier + "/" + Paths.get("Plateau" + coups).toFile()));
		partie.setPlateau((Plateau) ois.readObject());
		Files.deleteIfExists(Paths.get(dossier.toString() + "/Plateau" + coups));

		ois = new ObjectInputStream(new FileInputStream(dossier + "/" + Paths.get("Joueursenjeu" + coups).toFile()));
		partie.setJoueursEnJeu((ArrayList<Joueur>) ois.readObject());
		Files.deleteIfExists(Paths.get(dossier.toString() + "/Joueursenjeu" + coups));

		ois = new ObjectInputStream(new FileInputStream(dossier + "/" + Paths.get("Joueurcourant" + coups).toFile()));
		partie.setJoueurCourant((Joueur) ois.readObject());
		Files.deleteIfExists(Paths.get(dossier.toString() + "/Joueurcourant" + coups));

	    } catch (FileNotFoundException ex) {
		Logger.getLogger(AnnulerCoup.class.getName()).log(Level.SEVERE, null, ex);

	    } catch (IOException ex) {
		Logger.getLogger(AnnulerCoup.class.getName()).log(Level.SEVERE, null, ex);

	    } catch (ClassNotFoundException ex) {
		Logger.getLogger(AnnulerCoup.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    partie.getPlateau().setEstModifié(true);

	} else {
	    System.out.println(Paths.get(dossier.toString() + "/Plateau" + coups));
	    System.out.println("Impossible d'annuler coup!!");

	}
    }
}
