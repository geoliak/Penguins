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
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
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
    private RafraichissementFX r;

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
        BackgroundImage bg = new BackgroundImage(new Image(getClass().getClassLoader().getResourceAsStream("img/img_menu/banquise_fenetre_jeu.png")), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        b.setBackground(new Background(bg));

        stage.setScene(scene);
        ConfigurationPartie.getConfigurationPartie().setScene(scene);
        ConfigurationPartie.getConfigurationPartie().setRoot(root);
        ConfigurationPartie.getConfigurationPartie().setStage(stage);
        AnimationFX a = new AnimationFX();

        ImageView bulle = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("img/img_menu/bulle_ours.png")));
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

        r = new RafraichissementFX(d);
        r.start();

        /* TO DO */
        if (ConfigurationPartie.getConfigurationPartie().getPartie().getJoueurCourant().getEstHumain()) {
            Image img = new Image(getClass().getClassLoader().getResourceAsStream("img/img_menu/init_mess.png"));
            ImageView message = new ImageView(img);
            message.setId("message");
            message.setVisible(false);
            message.setLayoutY(300);

            Transition t = a.scaleFromZero(message, 1, 700);

            t.setOnFinished(new EventHandler<ActionEvent>() {
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
        }
        stage.show();
    }

    public void setBannieresJoueurs(Node v) {
        ArrayList<Joueur> joueurs = ConfigurationPartie.getConfigurationPartie().getPartie().getJoueurs();
        Image grey = new Image(getClass().getClassLoader().getResourceAsStream("img/grey_star.png"));
        Image yellow = new Image(getClass().getClassLoader().getResourceAsStream("img/yellow_star.png"));
        Image ping = new Image(getClass().getClassLoader().getResourceAsStream("img/pingouin_init.png"));

        ImageView[][] initpingoos = new ImageView[ConfigurationPartie.getConfigurationPartie().getPartie().getJoueurs().size()][ConfigurationPartie.getConfigurationPartie().getPartie().getNbPingouinParJoueur()];

        for (Joueur j : joueurs) {
            AnchorPane ap = new AnchorPane();

            Image imgBaniere = new Image(getClass().getClassLoader().getResourceAsStream("img/banniere_" + j.getCouleur().getNom() + ".png"));

            ImageView ivBanniere = new ImageView(imgBaniere);
            ivBanniere.setLayoutX(0);
            ivBanniere.setLayoutY(50);
            ivBanniere.setFitHeight(150);
            ivBanniere.setPreserveRatio(true);

            Label labelNom = new Label();
            labelNom.setLayoutX(150);
            labelNom.setLayoutY(115);
            labelNom.setText(j.getNom());
            System.out.println(j.getNom() + " " + labelNom.getText());

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
        ImageView home = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("img/img_menu/bouton_home.png")));
        home.setId("home");

        ImageView save = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("img/img_menu/save.png")));
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Sauvegarder la partie\n");
        Tooltip.install(save, tooltip);

        save.setOnMouseClicked(new EventHandler<MouseEvent>() {
            private Object dialog;
            @Override
            public void handle(MouseEvent event) {
                TextInputDialog dialog = new TextInputDialog("Sauvegarde");
                dialog.setTitle("Sauvegarde");
                dialog.setHeaderText("");
                dialog.setContentText("Nom de la sauvegarde :");

                // Traditional way to get the response value.
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()){
                    
                    
                    Sauvegarde s = new Sauvegarde();
                    s.Save(result.get());
                    
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setHeaderText("");
                    alert.setTitle("Confirmation");
                    alert.setContentText("La partie a bien été sauvegardée !");
                    
                    Optional<ButtonType> comfirm = alert.showAndWait();
                }
                
//                alert.setHeaderText("");
//                alert.setTitle("Confirmation");
//                alert.setContentText("Voulez vous sauvegarder cette partie ?");
//
//                Optional<ButtonType> result = alert.showAndWait();
//                Optional<String> name = alert.showAndWait();
//                
//                if (result.get() == ButtonType.OK) {
//                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-YY HH:mm:ss");
//                    Date date = new Date();
//                    Sauvegarde s = new Sauvegarde();
//                    System.out.println(dateFormat.format(date));
//                    s.Save(dateFormat.format(date));
//                } else {
//                    // ... user chose CANCEL or closed the dialog
//                }

            }
        });

        ImageView restart = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("img/img_menu/restart.png")));
        tooltip = new Tooltip();
        tooltip.setText("Recommencer la partie\n");
        Tooltip.install(restart, tooltip);
        restart.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!ConfigurationPartie.getConfigurationPartie().getHistorique().sauvegardeDebut()) {
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setHeaderText("");
                    alert.setTitle("Confirmation");
                    alert.setContentText("Voulez vous recommencer cette partie ?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        ConfigurationPartie.getConfigurationPartie().getHistorique().recommencer();
                        r.setResultatAffiches(false);
                    }
                }
            }
        });

        ImageView quit = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("img/img_menu/quit.png")));
        tooltip = new Tooltip();
        tooltip.setText("Quitter la partie\n");
        Tooltip.install(quit, tooltip);
        quit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setHeaderText("");
                    alert.setTitle("Confirmation");
                    alert.setContentText("Voulez vous quitter cette partie ?");
                    
                    ButtonType buttonTypeCancel = new ButtonType("Annuler", ButtonData.CANCEL_CLOSE);
                    ButtonType buttonTypeOne = new ButtonType("Quitter sans sauvegarder");
                    ButtonType buttonTypeTwo = new ButtonType("Sauvegarder et quiter");

                    alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == buttonTypeOne){
                        ConfigurationPartie.getConfigurationPartie().setPartie(null);
                        Parent paramJeu = FXMLLoader.load(getClass().getClassLoader().getResource("Vue/Accueil.fxml"));
                        Scene scene = new Scene(paramJeu);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();
                        ConfigurationPartie.getConfigurationPartie().setPartie(null);
                    } else if (result.get() == buttonTypeTwo) {
                        TextInputDialog dialog = new TextInputDialog("Sauvegarde");
                        dialog.setTitle("Sauvegarde");
                        dialog.setHeaderText("");
                        dialog.setContentText("Nom de la sauvegarde :");

                        // Traditional way to get the response value.
                        Optional<String> resultsave = dialog.showAndWait();
                        if (result.isPresent()){
                            Sauvegarde s = new Sauvegarde();
                            s.Save(resultsave.get());

                            Alert alertconfirm = new Alert(AlertType.CONFIRMATION);
                            alertconfirm.setHeaderText("");
                            alertconfirm.setTitle("Confirmation");
                            alertconfirm.setContentText("La partie a bien été sauvegardée !");

                            Optional<ButtonType> confirm = alertconfirm.showAndWait();
                            if (confirm.get() == ButtonType.OK) {
                                ConfigurationPartie.getConfigurationPartie().setPartie(null);
                                Parent paramJeu = FXMLLoader.load(getClass().getClassLoader().getResource("Vue/Accueil.fxml"));
                                Scene scene = new Scene(paramJeu);
                                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                stage.setScene(scene);
                                stage.show();
                                ConfigurationPartie.getConfigurationPartie().setPartie(null);
                            }
                            
                        }else {
                            
                        }
                    } else {
                        // ... user chose CANCEL or closed the dialog
                    }
                } catch (IOException ex) {
                    Logger.getLogger(FenetreJeuController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        double offset = 0.0;
        if(ConfigurationPartie.getConfigurationPartie().getPartie().getJoueurs().size() == 4){
            offset = -50.0;
        }

        home.setLayoutX(-140);
        quit.setLayoutX(-140 + 15);
        save.setLayoutX(-140 + 55);
        restart.setLayoutX(-140 + 95);

        home.setLayoutY(offset -120.0);
        quit.setLayoutY(offset -100.0);
        quit.toFront();
        save.setLayoutY(offset -95);
        save.toFront();
        restart.setLayoutY(offset -100);
        restart.toFront();

        home.setEffect(new DropShadow());

        ArrayList<ImageView> ivs = new ArrayList<>();
        ivs.add(save);
        ivs.add(home);
        ivs.add(quit);
        ivs.add(restart);

        setAnimHome(ivs);



        ImageView light = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("img/img_menu/ampoule.png")));
        tooltip = new Tooltip();
        tooltip.setText("Suggestion de coup\n");
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

        ImageView undo = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("img/img_menu/undo.png")));
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
                r.setResultatAffiches(false);
            }

        });

        ImageView redo = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("img/img_menu/redo.png")));
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

        redo.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                ConfigurationPartie.getConfigurationPartie().getHistorique().rejouerCoup();
            }

        });

        ap.getChildren().addAll(home, quit);
        if (ConfigurationPartie.getConfigurationPartie().getPartie().getDemo() == null) {
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
