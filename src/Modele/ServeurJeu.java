/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import Controleur.FenetreJeuController;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.Stage;

/**
 *
 * @author mariobap
 */
public class ServeurJeu extends Thread {

    private Partie partie;
    private Socket[] sockets;
    private ObjectInputStream[] in;
    private ObjectOutputStream[] out;
    private String[] noms;
    private int nbJoueur;
    private int nbJoueurMax;
    private Plateau plateau;
    private boolean lancerPartie;

    public ServeurJeu(int n, Plateau p) {
	nbJoueur = 0;
	sockets = new Socket[n];
	in = new ObjectInputStream[n];
	out = new ObjectOutputStream[n];
	noms = new String[n];
	nbJoueurMax = n;
	plateau = p;
	lancerPartie = false;
    }

    @Override
    public void run() {
	try {
	    System.out.println("Serveur lancé :");
	    ServerSocket serverSocket = new ServerSocket(3333);
	    serverSocket.setSoTimeout(100);
	    while (!lancerPartie && nbJoueur < nbJoueurMax) {
		System.out.println("en attente...");
		while (!lancerPartie) {
		    try {
			sockets[nbJoueur] = serverSocket.accept();
			in[nbJoueur] = new ObjectInputStream(sockets[nbJoueur].getInputStream());
			out[nbJoueur] = new ObjectOutputStream(sockets[nbJoueur].getOutputStream());
			noms[nbJoueur] = in[nbJoueur].readUTF();
			out[nbJoueur].writeInt(nbJoueur);
			out[nbJoueur].flush();
			out[nbJoueur].writeObject(noms);
			out[nbJoueur].flush();
			for (int i = 0; i < nbJoueur; i++) {
			    out[i].writeBoolean(false);
			    System.out.println("boolen envoyé");
			    out[i].flush();
			    out[i].writeUTF(noms[nbJoueur]);
			    out[i].flush();
			}
			System.out.println(noms[nbJoueur] + " connecté.");
			nbJoueur++;
		    } catch (Exception e) {

		    }

		}

	    }

	    ArrayList<Joueur> joueurs = new ArrayList<>();
	    Couleur[] couleurs = {Couleur.RougeFX, Couleur.VioletFX, Couleur.JauneFX, Couleur.VertFX};
	    for (int i = 0; i < nbJoueur; i++) {
		joueurs.add(new JoueurHumainLocal(noms[i], couleurs[i], i));
	    }
	    partie = new Partie(plateau, joueurs);
	    System.out.println("Joueur courant : " + partie.getJoueurCourant().getNumero());

	    for (int i = 0; i < nbJoueur; i++) {
		out[i].writeBoolean(true);
		out[i].flush();
		out[i].writeObject(partie);
		out[i].flush();
	    }

	    while (true) {
		System.out.println("Serveur en attent de la partie de client " + partie.getJoueurCourant().getNumero());
		System.out.println("1ere aff " + partie + "" + partie.getJoueurCourant());
		partie = (Partie) in[partie.getJoueurCourant().getNumero()].readObject();
		partie.getPlateau().setEstModifié(true);
		System.out.println("Serveur partie reçue");
		int nbPingouins = 0;
		for (Joueur j : partie.getJoueurs()) {
		    nbPingouins += j.getPinguinsVivants().size();
		}
		System.out.println("Nombre de pingouins : " + nbPingouins);
		for (int i = 0; i < nbJoueur; i++) {
		    System.out.println("2me aff " + partie + " " + partie.getJoueurCourant());
		    out[i].writeObject(partie);
		}
	    }
	} catch (IOException ex) {
	    Logger.getLogger(ServeurJeu.class.getName()).log(Level.SEVERE, null, ex);
	} catch (ClassNotFoundException ex) {
	    Logger.getLogger(ServeurJeu.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    public void lancerPartie() {
	lancerPartie = true;
	System.out.println("lancer partie : " + lancerPartie);
    }
}
