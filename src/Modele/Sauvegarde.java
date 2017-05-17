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
import javafx.scene.image.Image;
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

    public void Save(int filenum) {
	try {
	    Path filepath = Paths.get(savepath + "/save" + filenum);
	    deleteIfExists(filepath);
	    Files.createFile(filepath);

	    partie.setReloadPartie(true);
	    partie.getPlateau().setEstModifié(true);

	    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filepath.toFile()));
	    oos.writeObject(partie);

	    //Creation d'image de sauvegarde
	    Image screenie = ConfigurationPartie.getConfigurationPartie().getScene().snapshot(null);
	    Path filepathim = Paths.get(savepath + "/savescr" + filenum);
	    deleteIfExists(filepathim);
	    Files.createFile(filepathim);
	    ImageIO.write(SwingFXUtils.fromFXImage(screenie, null), "png", filepathim.toFile());

	} catch (IOException ex) {
	    Logger.getLogger(Sauvegarde.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    public Partie Load(int filenum) throws IOException, ClassNotFoundException {
	Path filepath = Paths.get(savepath + "/save" + filenum);

	ObjectInputStream ois = null;
	ois = new ObjectInputStream(new FileInputStream(filepath.toFile()));

	Partie partieacharger = (Partie) ois.readObject();

	partieacharger.setReloadPartie(true);
	partieacharger.getPlateau().setEstModifié(true);

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
