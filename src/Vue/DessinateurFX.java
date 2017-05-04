/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vue;

import Controleur.MouseClicker;
import Modele.Case;
import Modele.MyPolygon;
import Modele.Pinguin;
import Modele.Plateau;
import Modele.Visiteur;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 *
 * @author liakopog
 */
public class DessinateurFX extends Visiteur {

    private GraphicsContext gc;
    private Group root;
    private double sizeGlacon;
    private double proportion;
    private int gap;
    private double size ;
    private double height;
    private double width;

    public DessinateurFX(GraphicsContext gc, Group root) {
        this.gc = gc;
        this.root = root;
        sizeGlacon = 50.0;
        proportion = 1;
        gap = 4;
        size = sizeGlacon * proportion;
        height = size * 2;
        width = height * Math.sqrt(3 / 2);
    }

    @Override
    public void visite(Plateau plateau) {
        ArrayList<GridPane> ag = new ArrayList<>();

        int rows = plateau.getNbLignes();
        int columns = plateau.getNbColonnes();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                plateau.getCases()[i][j].accept(this);
            }
        }
    }

    @Override
    public void visite(Case c) {
        MyPolygon p = new MyPolygon(c.getNumLigne(), c.getNumColonne(), sizeGlacon);
        EventHandler<? super MouseEvent> clicSourisFX = new MouseClicker(p);
        p.setOnMouseClicked(clicSourisFX);

        root.getChildren().add(p);
    }

    @Override
    public void visite(Pinguin p) {
        double x = p.getPosition().getPolygon().getXorigine() + width/2;
        double y = p.getPosition().getPolygon().getYorigine() + height/2;
        
        gc.setFill(p.getGeneral().getCouleur());
        gc.fillOval(x, y, 5, 5);
    }
}
