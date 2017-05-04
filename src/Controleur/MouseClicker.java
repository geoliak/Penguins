/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Polygon;

/**
 *
 * @author liakopog
 */
public class MouseClicker implements EventHandler<MouseEvent> {

    ArrayList<GridPane> ag;
    GridPane g;
    Polygon p;
    int rowclic;
    int columnclic;

    public MouseClicker(Polygon p, GridPane g, ArrayList<GridPane> ag) {
	this.p = p;
	this.g = g;
	this.ag = ag;
    }

    @Override
    public void handle(MouseEvent event) {
	System.out.println("BLABLA");
	columnclic = GridPane.getColumnIndex(p);
	rowclic = ag.indexOf(g);
	System.out.println(rowclic + " " + columnclic);
    }
}
