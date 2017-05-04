/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 *
 * @author liakopog
 */
public class MyPolygon extends Polygon {
    double sizeGlacon;
    double size;
    double width;
    double height;
    double proportion;
    double gap;
    int x;
    int y;

    public MyPolygon(int i, int j, double size) {
	super();
	this.x = i;
	this.y = j;
	this.sizeGlacon = size;
	proportion = 1;
	gap = 4;
	this.size = sizeGlacon * proportion;
	height = size * 2;
	width = height*Math.sqrt(3/2);
	double xorigin = ((i%2)*width/2) + (width*j) + j * gap + (i%2) * gap/2;
	double yorigin = (height*(3.0/4.0)) * i + i*gap;

	this.setPoints(this.size, Color.AQUA, xorigin, yorigin);
    }

    public int getX() {
	return x;
    }

    public int getY() {
	return y;
    }

    public void setPoints(double size, Color color, double i, double j) {
	this.setFill(color);

	double height = size * 2;
	double width = height*Math.sqrt(3/2);

	//p.setStroke(Color.BLACK);

	this.setLayoutX(i);
	this.setLayoutY(j);
	//point(50,0)
	this.getPoints().add(width/2);
	this.getPoints().add(0.0);
	//point(0, 50)
	this.getPoints().add(0.0);
	this.getPoints().add(height/4);
	//point(0,100)
	this.getPoints().add(0.0);
	this.getPoints().add(3*(height/4));
	//point(50,150)
	this.getPoints().add(width/2);
	this.getPoints().add(height);
	//point(100,100)
	this.getPoints().add(width);
	this.getPoints().add(3*(height/4));
	//point(100,50)
	this.getPoints().add(width);
	this.getPoints().add(height/4);
    }
}
