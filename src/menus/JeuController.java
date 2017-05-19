/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menus;

import Modele.IA.JoueurIA;
import Modele.IA.JoueurIA5;
import Modele.Joueur;
import Modele.JoueurHumainLocal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author mariobap
 */
public class JeuController implements Initializable {

    @FXML
    private Group root;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
	/*try {
	 root = new Group();

	 //Scene scene = new Scene(root, 1200,900);

	 //root.getChildren().add(canvas);
	 /*scene.setFill(Color.AQUA);
	 Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
	 stage.setScene(scene);*/

	//Image img = new Image(f.toURI().toString());
	//plateau.getCases()[4][4].setPinguin(new Pinguin(plateau.getCases()[4][4], new JoueurHumainLocal("Quentin", Couleur.Rouge), img));
            /*Plateau plateau = new Plateau("ressources/plateaux/plateau4");
	 plateau.initCase();

	 JoueurHumainLocal joueurH1 = new JoueurHumainLocal("Jean", Couleur.JauneFX);
	 JoueurHumainLocal joueurH2 = new JoueurHumainLocal("Pierre", Couleur.RougeFX);

	 JoueurIA joueuria = new JoueurIA5(Couleur.RougeFX);

	 ArrayList<Joueur> joueurs = new ArrayList<>();
	 joueurs.add(joueurH1);
	 joueurs.add(joueuria);

	 System.out.println(joueurs.size());

	 Partie partie = new Partie(plateau, joueurs);
	 AnimationFX a = new AnimationFX();
	 DessinateurFX d = new  DessinateurFX(root, partie, a);

	 //plateau.accept(d);
	 RafraichissementFX r = new RafraichissementFX(d, partie);
	 r.start();
	 //stage.show();
	 } catch (IOException ex) {
	 Logger.getLogger(JeuController.class.getName()).log(Level.SEVERE, null, ex);
	 }*/
    }

    public void cache(MouseEvent e) {
	((Button) e.getSource()).setVisible(false);
    }

}
