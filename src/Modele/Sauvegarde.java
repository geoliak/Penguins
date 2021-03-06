/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.io.FileInputStream;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;

public class Sauvegarde {

    private Partie partie;
    private Path savepath;

    public Sauvegarde() {
	this.partie = ConfigurationPartie.getConfigurationPartie().getPartie();
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

    public void Save(String nomFichier) {
	try {
	    Path savefilepath = Paths.get(savepath + "/S_" + nomFichier);
	    Path histfilepath = Paths.get(savepath + "/H_" + nomFichier);
	    deleteIfExists(savefilepath);
	    Files.createFile(savefilepath);
	    deleteIfExists(histfilepath);
	    Files.createFile(histfilepath);

	    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(savefilepath.toFile()));
	    oos.writeObject(partie);
	    oos = new ObjectOutputStream(new FileOutputStream(histfilepath.toFile()));
	    oos.writeObject(ConfigurationPartie.getConfigurationPartie().getHistorique());

	    //Creation d'image de sauvegarde
            WritableImage wi = new WritableImage(875, 620);
            WritableImage snapshot = ConfigurationPartie.getConfigurationPartie().getScene().snapshot(wi);
            Path filepathim = Paths.get(savepath + "/I_" + nomFichier);
	    deleteIfExists(filepathim);
	    Files.createFile(filepathim);
	    ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", filepathim.toFile());
            
            
//	    Image screenie = ConfigurationPartie.getConfigurationPartie().getRoot().snapshot(null, null);
//	    Path filepathim = Paths.get(savepath + "/I_" + nomFichier);
//	    deleteIfExists(filepathim);
//	    Files.createFile(filepathim);
//	    ImageIO.write(SwingFXUtils.fromFXImage(screenie, null), "png", filepathim.toFile());

	} catch (IOException ex) {
	    Logger.getLogger(Sauvegarde.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    public Partie Load(String nomFichier) throws IOException, ClassNotFoundException {
	Path savefilepath = Paths.get(savepath + "/S_" + nomFichier);
	Path histfilepath = Paths.get(savepath + "/H_" + nomFichier);

	ObjectInputStream ois = null;

	ois = new ObjectInputStream(new FileInputStream(savefilepath.toFile()));
	Partie partieacharger = (Partie) ois.readObject();

	ois = new ObjectInputStream(new FileInputStream(histfilepath.toFile()));
	ConfigurationPartie.getConfigurationPartie().setHistorique((Historique) ois.readObject());

	return partieacharger;
    }

    private void makeIvNull(ArrayList<Joueur> joueurs, Group root) {
	for (Iterator<Joueur> it = joueurs.iterator(); it.hasNext();) {
	    Joueur joueurCourant = it.next();
	    for (Iterator<Pinguin> itpin = joueurCourant.getPinguins().iterator(); itpin.hasNext();) {
		Pinguin pinguin = itpin.next();
		root.getChildren().remove(pinguin.getIv());
		pinguin.setIv(null);
	    }
	}
    }
}
