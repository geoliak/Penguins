/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.io.File;
import java.io.Serializable;
import javafx.scene.image.Image;
import Modele.MyImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 *
 * @author liakopog
 *
 * TO DO:
 *
 * La classe MyPolygon ne sert plus qu'a stocker l'image view des cases.
 * Supprimer le "extends polygon" et la création des polygon.
 *
 */
public class MyPolygon extends Polygon implements Serializable {

    private int x;
    private int y;

    private double xorigine;
    private double yorigine;

    private double sizeGlacon;
    private MyImageView image;

    public MyPolygon(int i, int j, double sizeGlacon, double proportion, double gap, Color color, int nbPoisson) {
	super();
	this.x = i;
	this.y = j;
	this.sizeGlacon = sizeGlacon;
	this.setOrigine(gap, proportion);

	this.setImage(nbPoisson);
	//this.setPoints(gap, proportion, color, xorigine, yorigine);

	/*
	 File f = new File("ressources/img/glacon_mini.png");
	 this.image = new MyImageView(new Image(f.toURI().toString()));

	 this.image.setX(xorigine);
	 this.image.setY(yorigine);
	 */
    }

    public void setOrigine(double gap, double proportion) {
	double size = sizeGlacon * proportion;
	double height = sizeGlacon * 2;
	double width = height * Math.sqrt(3 / 2);

	this.xorigine = ((this.y % 2) * width / 2) + (width * this.x) + this.x * gap * 0.8 + (this.y % 2) * gap * 0.8 / 2;
	this.yorigine = (height * (3.0 / 4.0)) * this.y + this.y * gap * 4 - this.y * size / 2;
    }

    public int getX() {
	return x;
    }

    public int getY() {
	return y;
    }

    public double getXorigine() {
	return xorigine;
    }

    public double getYorigine() {
	return yorigine;
    }

    public double getSizeGlacon() {
	return sizeGlacon;
    }

    public MyImageView getImage() {
	return image;
    }

    public void setImage(int nbPoisson) {
	File f = new File("ressources/img/glacon_poisson" + nbPoisson + "_mini.png");

	this.image = new MyImageView(new Image(f.toURI().toString()));

	this.image.setX(xorigine);
	this.image.setY(yorigine);
    }

    public void setPoints(double gap, double proportion, Color color, double i, double j) {
	double size = sizeGlacon * proportion;
	double height = size * 2;
	double width = height * Math.sqrt(3 / 2);

	this.setFill(color);

	//p.setStroke(Color.BLACK);
	this.setLayoutX(i);
	this.setLayoutY(j);
	//point(50,0)
	this.getPoints().add(width / 2);
	this.getPoints().add(0.0);
	//point(0, 50)
	this.getPoints().add(0.0);
	this.getPoints().add(height / 4);
	//point(0,100)
	this.getPoints().add(0.0);
	this.getPoints().add(3 * (height / 4));
	//point(50,150)
	this.getPoints().add(width / 2);
	this.getPoints().add(height);
	//point(100,100)
	this.getPoints().add(width);
	this.getPoints().add(3 * (height / 4));
	//point(100,50)
	this.getPoints().add(width);
	this.getPoints().add(height / 4);
    }
}
