/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.io.Serializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author liakopog
 */
public class MyImageView extends ImageView implements Serializable {

    public MyImageView() {
	super();
    }

    public MyImageView(Image image) {
	super(image);
    }

    public MyImageView(String url) {
	super(url);
    }

}
