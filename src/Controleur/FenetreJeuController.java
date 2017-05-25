/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.ConfigurationPartie;
import Modele.Joueur;
import Modele.Sauvegarde;
import Vue.AnimationFX;
import Vue.DessinateurFX;
import Vue.DessinateurTexte;
import Vue.RafraichissementFX;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author mariobap
 */
public class FenetreJeuController {

    public void creerFenetreJeu(Stage stage) {
	System.out.println(ConfigurationPartie.getConfigurationPartie().getPartie());
	BorderPane b = new BorderPane();

	VBox scores = new VBox();
	scores.setSpacing(30);
	scores.setAlignment(Pos.CENTER);

	HBox bas = new HBox();

	AnchorPane root = new AnchorPane();

	b.setCenter(root);
	b.setRight(scores);
	b.setBottom(bas);

	setBannieresJoueurs(scores);
	setBottom(bas);

	Scene scene = new Scene(b, 1200, 900);
	BackgroundImage bg = new BackgroundImage(new Image(new File("ressources/img/img_menu/banquise_fenetre_jeu.png").toURI().toString()), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
	b.setBackground(new Background(bg));

	stage.setScene(scene);
	ConfigurationPartie.getConfigurationPartie().setScene(scene);
	ConfigurationPartie.getConfigurationPartie().setRoot(root);
	ConfigurationPartie.getConfigurationPartie().setStage(stage);
	AnimationFX a = new AnimationFX();

	File f = new File("ressources/img/img_menu/bulle_ours.png");
	ImageView bulle = new ImageView(new Image(f.toURI().toString()));
	bulle.setEffect(new DropShadow());

	if (ConfigurationPartie.getConfigurationPartie().getPartie().getDemo() != null) {
	    root.getChildren().add(bulle);
	}

	DessinateurFX d = new DessinateurFX(root, a);
	ConfigurationPartie.getConfigurationPartie().getPartie().getPlateau().accept(d);

	System.out.println("PARTIE CHARGEE");
	DessinateurTexte dt = new DessinateurTexte();
	ConfigurationPartie.getConfigurationPartie().getPartie().getPlateau().accept(dt);

	EventHandler<KeyEvent> keypresser = new Keyboard_Handler();
	scene.setOnKeyPressed(keypresser);

	RafraichissementFX r = new RafraichissementFX(d);
	r.start();
        
	/* TO DO */
        File fmess = new File("ressources/img/img_menu/init_mess.png");
        Image img = new Image(fmess.toURI().toString());
        ImageView message = new ImageView(img);
        message.setId("message");
        message.setVisible(false);
        message.setLayoutY(300);

        Transition t = a.scaleFromZero(message, 1, 700);
        
        t.setOnFinished(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        a.scaleToZero(message, 700);
                    }
                }, 2500);
                
            }
            
        });
        
        root.getChildren().add(message);

	stage.show();
    }

    public void setBannieresJoueurs(Node v) {
	ArrayList<Joueur> joueurs = ConfigurationPartie.getConfigurationPartie().getPartie().getJoueurs();
	Image grey = new Image(new File("./ressources/img/grey_star.png").toURI().toString());
	Image yellow = new Image(new File("./ressources/img/yellow_star.png").toURI().toString());
	Image ping = new Image(new File("ressources/img/pingouin_init.png").toURI().toString());

	ImageView[][] initpingoos = new ImageView[ConfigurationPartie.getConfigurationPartie().getPartie().getJoueurs().size()][ConfigurationPartie.getConfigurationPartie().getPartie().getNbPingouinParJoueur()];

	for (Joueur j : joueurs) {
	    System.out.println(j.getNumero());
	    AnchorPane ap = new AnchorPane();

	    File f = new File("ressources/img/banniere_" + j.getCouleur().getNom() + ".png");
	    Image imgBaniere = new Image(f.toURI().toString());

	    ImageView ivBanniere = new ImageView(imgBaniere);
	    ivBanniere.setLayoutX(0);
	    ivBanniere.setLayoutY(50);
	    ivBanniere.setFitHeight(150);
	    ivBanniere.setPreserveRatio(true);

	    Label labelNom = new Label();
	    labelNom.setLayoutX(150);
	    labelNom.setLayoutY(115);
	    labelNom.setText(j.getNom());

	    Label labelScore = new Label();
	    labelScore.setLayoutX(50);
	    labelScore.setLayoutY(115);
	    labelScore.setText(Integer.toString(j.getScorePoissons()));
	    labelScore.setTextFill(Color.WHITE);

	    ap.getChildren().addAll(ivBanniere, labelNom, labelScore);

	    if (j.getDifficulte() != 0) {
		//étoiles
		ImageView etoile1 = new ImageView(yellow);
		etoile1.setLayoutX(130);
		etoile1.setLayoutY(155);
		ImageView etoile2 = new ImageView(grey);
		etoile2.setLayoutX(160);
		etoile2.setLayoutY(155);
		ImageView etoile3 = new ImageView(grey);
		etoile3.setLayoutX(190);
		etoile3.setLayoutY(155);

		if (j.getDifficulte() > 1) {
		    etoile2.setImage(yellow);
		}

		if (j.getDifficulte() > 2) {
		    etoile3.setImage(yellow);
		}

		ap.getChildren().addAll(etoile1, etoile2, etoile3);
	    }

	    ImageView[] init = new ImageView[ConfigurationPartie.getConfigurationPartie().getPartie().getNbPingouinParJoueur()];

	    ImageView pInit;
	    for (int i = 0; i < ConfigurationPartie.getConfigurationPartie().getPartie().getNbPingouinParJoueur(); i++) {
		System.out.println("init");
		pInit = new ImageView(ping);
		pInit.setLayoutX(130 + 30 * i);
		pInit.setLayoutY(65);
		pInit.setPreserveRatio(true);
		pInit.setFitHeight(30);
		ap.getChildren().add(pInit);
		init[i] = pInit;
	    }
	    initpingoos[j.getNumero()] = init;

	    ConfigurationPartie.getConfigurationPartie().setInitpingoos(initpingoos);

	    ConfigurationPartie.getConfigurationPartie().setLabelScore(labelScore, j.getNumero());

	    ((VBox) v).getChildren().add(ap);
	}
    }

    public void setBottom(Node n) {
	AnimationFX a = new AnimationFX();
	AnchorPane ap = new AnchorPane();
	HBox h = new HBox();
	File f = new File("ressources/img/img_menu/bouton_home.png");
	ImageView home = new ImageView(new Image(f.toURI().toString()));
	home.setId("home");

	File fsave = new File("ressources/img/img_menu/save.png");
	ImageView save = new ImageView(new Image(fsave.toURI().toString()));
	Tooltip tooltip = new Tooltip();
	tooltip.setText("Sauvegarder la partie\n");
	Tooltip.install(save, tooltip);

	save.setOnMouseClicked(new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent event) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setHeaderText("");
                alert.setTitle("Comfirmation");
                alert.setContentText("Voulez vous sauvegarder cette partie ?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-YY HH:mm:ss");
                    Date date = new Date();
                    Sauvegarde s = new Sauvegarde();
                    System.out.println(dateFormat.format(date));
                    s.Save(dateFormat.format(date));
                } else {
                    // ... user chose CANCEL or closed the dialog
                }
		
	    }
	});

	File frestart = new File("ressources/img/img_menu/restart.png");
	ImageView restart = new ImageView(new Image(frestart.toURI().toString()));
	tooltip = new Tooltip();
	tooltip.setText("Recommencer la partie\n");
	Tooltip.install(restart, tooltip);
	restart.setOnMouseClicked(new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent event) {
		if (!ConfigurationPartie.getConfigurationPartie().getHistorique().sauvegardeDebut()) {
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setHeaderText("");
                    alert.setTitle("Comfirmation");
                    alert.setContentText("Voulez vous recommencer cette partie ?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK){
                        ConfigurationPartie.getConfigurationPartie().getHistorique().recommencer();
                    }
		}
	    }
	});

	File fquit = new File("ressources/img/img_menu/quit.png");
	ImageView quit = new ImageView(new Image(fquit.toURI().toString()));
	tooltip = new Tooltip();
	tooltip.setText("Quitter la partie\n");
	Tooltip.install(quit, tooltip);
	quit.setOnMouseClicked(new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent event) {
		try {
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setHeaderText("");
                    alert.setTitle("Comfirmation");
                    alert.setContentText("Voulez vous quitter cette partie sans sauvegarder ?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK){
                        Parent paramJeu = FXMLLoader.load(getClass().getResource("../Vue/Accueil.fxml"));
                        Scene scene = new Scene(paramJeu);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();
                        ConfigurationPartie.getConfigurationPartie().setPartie(null);
                    }
		} catch (IOException ex) {
		    Logger.getLogger(FenetreJeuController.class.getName()).log(Level.SEVERE, null, ex);
		}
	    }
	});

	home.setLayoutX(-140);
	quit.setLayoutX(-140 + 15);
	save.setLayoutX(-140 + 55);
	restart.setLayoutX(-140 + 95);

	home.setLayoutY(-120);
	quit.setLayoutY(-100);
	quit.toFront();
	save.setLayoutY(-95);
	save.toFront();
	restart.setLayoutY(-100);
	restart.toFront();

	home.setLayoutX(-140);
	quit.setLayoutX(-140 + 15);
	save.setLayoutX(-140 + 55);
	restart.setLayoutX(-140 + 95);

	home.setLayoutY(-120);
	quit.setLayoutY(-100);
	quit.toFront();
	save.setLayoutY(-95);
	save.toFront();
	restart.setLayoutY(-100);
	restart.toFront();

	home.setEffect(new DropShadow());

	ArrayList<ImageView> ivs = new ArrayList<>();
        ivs.add(save);
        ivs.add(home);
        ivs.add(quit);
        ivs.add(restart);
        
        setAnimHome(ivs);
        
        File flight = new File("ressources/img/img_menu/ampoule.png");

	ImageView light = new ImageView(new Image(flight.toURI().toString()));
	tooltip = new Tooltip();
	tooltip.setText("Suggéstion de coup\n");
	Tooltip.install(light, tooltip);

	light.setId("light");
	light.setLayoutX(250);
	light.setLayoutY(-150);
	light.setOnMouseClicked(new hintClicEvent(light));
	light.setOnMouseEntered(new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent event) {
		a.scale(light, 1.15, 200);
	    }
	});

	light.setOnMouseExited(new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent event) {
		a.scale(light, 1, 200);
	    }
	});

	File fundo = new File("ressources/img/img_menu/undo.png");
	ImageView undo = new ImageView(new Image(fundo.toURI().toString()));
	tooltip = new Tooltip();
	tooltip.setText("Annuler un coup\n");
	Tooltip.install(undo, tooltip);

	undo.setId("undo");
	undo.setLayoutX(400);
	undo.setLayoutY(-150);
	undo.setEffect(new DropShadow());

	undo.setOnMouseEntered(new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent event) {
		undo.setEffect(new InnerShadow());
	    }
	});

	undo.setOnMouseExited(new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent event) {
		undo.setEffect(new DropShadow());
	    }
	});
	undo.setOnMouseClicked(new EventHandler<MouseEvent>() {

	    @Override
	    public void handle(MouseEvent event) {
		ConfigurationPartie.getConfigurationPartie().getHistorique().annulerDernierCoup();
	    }

	});

	File fredo = new File("ressources/img/img_menu/redo.png");
	ImageView redo = new ImageView(new Image(fredo.toURI().toString()));
	tooltip = new Tooltip();
	tooltip.setText("Rejouer un coup\n");
	Tooltip.install(redo, tooltip);

	redo.setLayoutX(600);
	redo.setLayoutY(-150);
	undo.setEffect(new DropShadow());

	redo.setOnMouseEntered(new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent event) {
		redo.setEffect(new InnerShadow());
	    }
	});

	redo.setOnMouseExited(new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent event) {
		redo.setEffect(new DropShadow());

	    }
	});

	redo.setOnMouseClicked(new EventHandler<MouseEvent>() {

	    @Override
	    public void handle(MouseEvent event) {
		ConfigurationPartie.getConfigurationPartie().getHistorique().rejouerCoup();
	    }

	});
        
        redo.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {
                ConfigurationPartie.getConfigurationPartie().getHistorique().rejouerCoup();
            }
            
        });
        
	ap.getChildren().addAll(home, quit);
        if(ConfigurationPartie.getConfigurationPartie().getPartie().getDemo() == null) {
            ap.getChildren().addAll(save, restart, light, undo, redo);
        }
        
        ((HBox) n).getChildren().add(ap);
	((HBox) n).setAlignment(Pos.TOP_LEFT);
	((HBox) n).setPadding(new Insets(0, 0, 20, 0));
        
        Settings.setSettings(ap);
    }

    public void setAnimHome(ArrayList<ImageView> ivs) {
	AnimationFX a = new AnimationFX();
	for (ImageView iv : ivs) {
	    iv.setOnMouseEntered(new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
		    for (ImageView i : ivs) {
			a.moveIV(140, 0, i);
			if (i == iv && i.getId() == null) {
			    a.scale(i, 1.2, 100);
			}
		    }
		}
	    });

	    iv.setOnMouseExited(new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {
		    for (ImageView i : ivs) {
			a.moveIV(0, 0, i);
			if (i == iv && i.getId() == null) {
			    a.scale(i, 1.0, 100);
			}
		    }
		}

	    });
	}
    }
}
