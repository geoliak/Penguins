/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vue;

import Modele.MyPolygon;
import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
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
    
    public Transition mouvementImage(MyPolygon p ,ImageView iv, double x, double y, double sizeGlacon, double proportion){
        double size = sizeGlacon * proportion;
        double height = size * 2;
	double width = height * Math.sqrt(3 / 2);
        
        double oldx = iv.getX();
        double oldy = iv.getY();
        
        double newx = p.getXorigine() + width / 2;
	double newy = p.getYorigine() + height / 2;
        
        //double d = Math.sqrt(Math.pow(newx-oldx, 2) + Math.pow(newy-oldy,2));
        
        TranslateTransition tt = new TranslateTransition(Duration.millis(400), iv);
        tt.setToX((newx - height/4) - oldx);
        tt.setToY((newy - iv.getFitHeight()*0.8) - oldy);
        
        tt.play();
        return tt;
    }
    
    public Transition efface(Node n){
        FadeTransition ft = new FadeTransition(Duration.millis(200), n);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.play();
        return ft;
    }
    
    
    
}
