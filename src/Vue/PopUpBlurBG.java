/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vue;

/**
 *
 * @author boussedm
 */
import Modele.Partie;
import javafx.application.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PopUpBlurBG {

    private static final double BLUR_AMOUNT = 30;
    
    private static final Effect frostEffect =
        new BoxBlur(BLUR_AMOUNT, BLUR_AMOUNT, 3);

    private static final ImageView background = new ImageView();
    private static final StackPane layout = new StackPane();
    

    PopUpBlurBG(Stage stage, Partie partie) {
        layout.getChildren().setAll(background, createContent(partie));
        layout.setStyle("-fx-background-color: null");
        
        Scene oldScene = stage.getScene(); // Copie de la scene d'avant
        Scene scene = new Scene(
                layout,
                stage.getScene().getWidth(), stage.getScene().getHeight(),
                Color.TRANSPARENT
        );
        scene.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1)
                stage.setScene(oldScene);                    
                    //Platform.exit();
        });
        makeSmoke(stage);

        stage.setScene(scene);
        stage.show();

        background.setImage(copyBackground(stage));
        background.setEffect(frostEffect);
    }

    private Image copyBackground(Stage stage) {
        int gap = 25;
        final int X = (int) stage.getX();
        final int Y = (int) stage.getY()+gap;
        final int W = (int) stage.getWidth();
        final int H = (int) stage.getHeight()-gap;

        try {
            java.awt.Robot robot = new java.awt.Robot();
            java.awt.image.BufferedImage image = robot.createScreenCapture(new java.awt.Rectangle(X, Y, W, H));

            return SwingFXUtils.toFXImage(image, null);
        } catch (java.awt.AWTException e) {
            e.printStackTrace();
            return null;
        }
    }

    private BorderPane createContent(Partie partie) {
        BorderPane bp = new BorderPane();
        HBox ap = new HBox(); 
        ap.setAlignment(Pos.CENTER);
        ap.setStyle("-fx-font-size: 15px; -fx-text-fill: green;");
        Text texte = new Text();
        texte.setFont(new Font("Arial Black", 30));
        texte.setText("  "+partie.getJoueurGagnant().get(0).getNom()+" a gagné !!");
        
        ImageView ivGagnant = new ImageView(partie.getJoueurGagnant().get(0).getCouleur().getImage());        
        ap.getChildren().addAll(ivGagnant, texte);    
        bp.setCenter(ap);
        return bp;
    }

    private javafx.scene.shape.Rectangle makeSmoke(Stage stage) {
        return new javafx.scene.shape.Rectangle(
                stage.getWidth(),
                stage.getHeight(),
                Color.WHITESMOKE.deriveColor(
                        0, 1, 1, 0.08
                )
        );
    }

}  