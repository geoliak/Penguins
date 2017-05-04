/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.MyPolygon;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 *
 * @author liakopog
 */
public class MouseClicker implements EventHandler<MouseEvent> {

    ArrayList<GridPane> ag;
    GridPane g;
    MyPolygon p;
    int rowclic;
    int columnclic;

    int size;

    public MouseClicker(MyPolygon p) {
	this.p = p;
    }


    @Override
    public void handle(MouseEvent event) {
	System.out.println(p.getX() + " " + p.getY());
	/*
	columnclic = GridPane.getColumnIndex(p);
	rowclic = ag.indexOf(g);
	System.out.println(rowclic + " " + columnclic);
	*/
    }


}
