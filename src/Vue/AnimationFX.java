/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vue;

import Modele.MyPolygon;
import javafx.animation.PathTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

/**
 *
 * @author rozandq
 */
public class AnimationFX {
    
    public void mouvementImage(MyPolygon p ,ImageView iv, double x, double y){
        double oldx = iv.getX();
        double oldy = iv.getY();
        System.out.println("POINT ORIGINE PINGOUIN ANIMATION : " + oldx + " " + oldy);
        
        double newx = p.getXorigine() + 75 / 2;
	double newy = p.getYorigine() + 100 / 2;
        
        //double d = Math.sqrt(Math.pow(newx-oldx, 2) + Math.pow(newy-oldy,2));
        
        TranslateTransition tt = new TranslateTransition(Duration.millis(500), iv);
        tt.setToX((newx - 75/4) - oldx);
        tt.setToY((newy - iv.getFitHeight()*0.8) - oldy);
        
        tt.play();
    }
    
    public void effaceGlacon(){
        
    }
    
}
