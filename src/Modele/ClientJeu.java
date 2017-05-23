/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import Controleur.JouerReseauController;
import Vue.DessinateurFXReseau;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author mariobap
 */
public class ClientJeu extends Thread{
    private Partie partie;
    private String addr;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String nomJoueur;
    private int numJoueur;
    private JouerReseauController controller;
    private boolean ready;
    private boolean finDeTour;
    private boolean addJoueur;
    private String nouveauJoueur;
    
    public ClientJeu(String ip, String nom, JouerReseauController j){
        addr = ip;
        nomJoueur = nom;
        controller = j;
        ready = false;
        finDeTour = false;
        addJoueur = false;
    }
    
    @Override
    public void run(){
        try {
            synchronized(this){
                System.err.println("Client - run");
                Socket socket = null;
                if(addr == null){
                    socket = new Socket(InetAddress.getLocalHost(), 3333);
                }
                else {
                    socket = new Socket(addr, 3333);
                }
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

                out.writeUTF(nomJoueur);
                out.flush();

                numJoueur = in.readInt();
                System.out.println("Entier reçu : " + numJoueur);
                String [] noms = (String []) in.readObject();

                ObservableList<String> items = controller.getListJoueurs().getItems();
                for(String nom : noms){
                    if(nom != null) controller.ajouterNom(nom);
                }
            }

            System.out.println("attente boolen");
            System.out.flush();
            boolean boucle = in.readBoolean();
            System.out.println("Boolean reçu : " + boucle);
            while(!boucle){
                try{
                    nouveauJoueur = in.readUTF();
//                    controller.ajouterNom(nouveauJoueur);
                    addJoueur = true;
                    boucle = in.readBoolean();
                }
                catch (Exception e){

                }

            }

//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
//                    // Update UI here.
//                }
//            });
            partie = (Partie) in.readObject();
            ready = true;
            
            ConfigurationPartie.getConfigurationPartie().setPartie(partie);
            
            while(true){
                int i = 0;
                if(ConfigurationPartie.getConfigurationPartie().getPartie().getJoueurCourant().getNumero() == numJoueur){
                    System.out.println("A moi de jouer");
                    while(!finDeTour) {
                        Thread.sleep(1000);
                        System.out.println("J'attend " + finDeTour);
                    }
                    System.out.println("Client " + numJoueur + " envoie la partie");
                    out.writeObject(partie);
                    out.flush();
//                    ready = false;
                }
                System.out.println("Client " + numJoueur + " attent la partie");
                partie = (Partie) in.readObject();
                System.out.println("Client " + numJoueur + " a reçu la partie");
                int nbPingouins = 0;
                for(Joueur j : partie.getJoueurs()){
                    nbPingouins += j.getPinguinsVivants().size();
                }
                System.out.println("Nombre de pingouins : " + nbPingouins);
                System.out.flush();
                ConfigurationPartie.getConfigurationPartie().setPartie(partie);
//                ready = true;
                ConfigurationPartie.getConfigurationPartie().getPartie().getPlateau().setEstModifié(true);
                finDeTour = false;
            }
            
        }
        catch (Exception ex) {
            Logger.getLogger(ClientJeu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void finDeTour(){
        finDeTour = true;
        System.out.println("Fin de tour");
    }

    public Partie getPartie() {
        return partie;
    }

    public void setPartie(Partie partie) {
        this.partie = partie;
    }

    public String getAddr() {
        return addr;
    }

    public boolean isAddJoueur() {
        return addJoueur;
    }

    public void setAddJoueur(boolean addJoueur) {
        this.addJoueur = addJoueur;
    }

    public String getNouveauJoueur() {
        return nouveauJoueur;
    }

    public void setNouveauJoueur(String nouveauJoueur) {
        this.nouveauJoueur = nouveauJoueur;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public void setIn(ObjectInputStream in) {
        this.in = in;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    public String getNomJoueur() {
        return nomJoueur;
    }

    public void setNomJoueur(String nomJoueur) {
        this.nomJoueur = nomJoueur;
    }

    public int getNumJoueur() {
        return numJoueur;
    }

    public void setNumJoueur(int numJoueur) {
        this.numJoueur = numJoueur;
    }

    public boolean getReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
    
    
}
