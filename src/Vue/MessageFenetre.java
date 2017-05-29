/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vue;

import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
/**
 *
 * @author boussedm
 */
public class MessageFenetre {
    private long delai;
    private Scene oldScene;
    private Stage stage;
    private ImageView imageMessage;
    
    
    
    
    public void MessageFenetre(Stage stage, ImageView imageMessage, long delai){
        this.imageMessage = imageMessage;
        this.stage = stage;
        this.delai = delai;
        this.oldScene = stage.getScene(); // Copie de la scene d'avant
        
        StackPane sp = new StackPane();
        Scene newScene = new Scene(sp, oldScene.getWidth(), oldScene.getHeight(), Color.TRANSPARENT);
        
        
    } 
}
