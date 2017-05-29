/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.ClientJeu;
import Modele.ServeurJeu;
import Modele.Plateau;
import Modele.ConfigurationPartie;
import Modele.MyImageView;
import Vue.DessinateurFXReseau;
import java.io.File;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author mariobap
 */
public class JouerReseauController extends AnimationTimer implements Initializable {

    @FXML private TextField nom1;
    @FXML private TextField nom2;
    @FXML private TextField nbJoueurs;
    @FXML private TextField adresse;
    @FXML private ImageView nextTerrain;
    @FXML private ImageView terrain;
    @FXML private ImageView prevTerrain;
    @FXML private ImageView jouer1;
    @FXML private ImageView jouer2;
    private ListView listJoueurs;
    private ServeurJeu serveur;
    private ClientJeu client;
    private Stage stage;
    private int terrainCharge;
    
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        terrain.setImage(new Image(getClass().getClassLoader().getResourceAsStream("plateaux_jeu/img/plateau_1.png")));
        prevTerrain.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/left-arrow.png")));
        nextTerrain.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/right-arrow.png")));
        jouer1.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/img_menu/bouton_jouer_resize2.png")));
        jouer2.setImage(new Image(getClass().getClassLoader().getResourceAsStream("img/img_menu/bouton_jouer_resize2.png")));
        listJoueurs = new ListView();
        terrainCharge = 1;
        this.start();
    }    
    
    public void heberger(MouseEvent e) throws IOException, InterruptedException{
        Plateau plateau = new Plateau("plateaux_jeu/plateau" + terrainCharge);
        serveur = new ServeurJeu(Integer.valueOf(nbJoueurs.getText()), plateau);
        serveur.start();
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        lancerClient(nom1.getText(), null, stage);
    }
    
    public void rejoindre(MouseEvent e) throws InterruptedException{
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        
        lancerClient(nom2.getText(), adresse.getText(), stage);
        
    }
    
    public void lancerClient(String nom, String addr, Stage stage) throws InterruptedException{
        client = new ClientJeu(addr, nom, this);
        client.start();
        sleep(100);
        synchronized(client){
            System.out.println("controller");
            //fenetre salle d'attente
            BorderPane bp = new BorderPane();
            Scene scene = new Scene(bp, 1200, 900);

            Text text = new Text("Salle d'attente");
            bp.setTop(text);


            bp.setLeft(listJoueurs);

            ImageView terrain = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("plateaux_jeu/img/plateau_1.png")));
            bp.setCenter(terrain);

            ImageView boutonJouer = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("img/img_menu/bouton_jouer_resize2.png")));
            ImageView boutonRetour = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("img/img_menu/bouton_jouer_resize2.png")));
            boutonJouer.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    serveur.lancerPartie();
                }
            });
            System.out.println("Numero : " + client.getNumJoueur());
            if(client.getNumJoueur() > 0) boutonJouer.setVisible(false);

            BorderPane bas = new BorderPane();
            bas.setLeft(boutonRetour);
            bas.setRight(boutonJouer);

            bp.setBottom(bas);

            stage.setScene(scene);
            stage.show();
            
            System.out.println("sortie controller");
        }
    }

    public ListView getListJoueurs() {
        return listJoueurs;
    }

    public void setListJoueurs(ListView listJoueurs) {
        this.listJoueurs = listJoueurs;
    }
    
    public void ajouterNom(String nom){
        listJoueurs.getItems().add(nom);
    }

    @Override
    public void handle(long now) {
        if(client != null && client.getReady()){
            System.out.println("coucou");
            FenetreJeuControllerReseau f = new FenetreJeuControllerReseau();
            ConfigurationPartie.getConfigurationPartie().setPartie(client.getPartie());
            f.creerFenetreJeu(stage, client);
            client.setReady(false);
        }
        else if(client != null && client.isAddJoueur()) {
            ajouterNom(client.getNouveauJoueur());
            client.setAddJoueur(false);
        }
    }
    
    public void changerTerrain(MouseEvent e){
        if(e.getSource().equals(nextTerrain)){
            if(terrainCharge<3){
                terrainCharge ++;
            } else {
                terrainCharge = 1;
            }
        } else if(e.getSource().equals(prevTerrain)){
            if(terrainCharge>1){
                terrainCharge --;
            } else {
                terrainCharge = 3;
            }
        }
        String str = "plateaux_jeu/img/plateau_" + terrainCharge + ".png";
        terrain.setImage(new Image(getClass().getClassLoader().getResourceAsStream(str)));
    }
    
    
}
