/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

//import static EssaiFX.drawGaufre;
//import static EssaiFX.drawrect;
import Models.Case;
import Models.Plateau;
import static essaifx.EssaiFX.arrondirPourCases;
import static essaifx.EssaiFX.drawGaufre;
import static essaifx.EssaiFX.drawrect;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

/**
 *
 * @author novelm
 */
public class DessinateurFX extends Application {
    static double xcasecliquee;
    static double ycasecliquee;
    private Plateau plateau;
    GraphicsContext gc;
    Group root;

    public DessinateurFX(Plateau plateau) {
        this.plateau = plateau;
    }
    
    @Override
    public void start(Stage stage) {
	stage.setTitle("Gaufre");
	Group root = new Group();

	//Parent parent = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
	//root.getChildren().add(parent);
	Canvas canvas = new Canvas(800, 600);
	GraphicsContext gc = canvas.getGraphicsContext2D();
        this.gc = gc;
	int xgaufre = 500 / 5;
	int ygaufre = 300 / 3;

	drawGaufre(gc, root);

	Scene scene = new Scene(root);
	root.getChildren().add(canvas);
	stage.setScene(scene);

	stage.show();

	//Mouse handler
	scene.setOnMousePressed(new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent event) {
		System.out.println("mouse click detected! " + event.getSource());
		double x = event.getX();
		double y = event.getY();
		System.out.println(x + " " + y);


		drawrect(((int) (x / xgaufre) * xgaufre), ((int) (y / ygaufre) * ygaufre), gc);
		arrondirPourCases(x, xgaufre, y, ygaufre);
		System.out.println(xcasecliquee + " cassseees " + ycasecliquee);

	    }
	});
    }
    
    public static void main(String[] args) {
	launch(args);
    }
    
    

    public void drawGaufre(GraphicsContext gc, Group root) {
	Image gaufre = new Image("/gaufre.jpg");
        ImageView iv = new ImageView();
        iv.setImage(gaufre);
        
        int largeur = plateau.getLargeur();
        int longueur = plateau.getLongueur();
        Case[][] cases = plateau.getCases();
        
        TilePane tile = new TilePane();
        //tile.setHgap(8);
        tile.setPrefColumns(longueur);
        for (int i = 0; i < largeur; i++) {
            tile.getChildren().add(iv);
        }
        
        /*
        for(int i = 0; i < longueur; i++){
            for(int j = 0; j < largeur; j++){
                Polygon p = new Polygon();

                p.setLayoutX(0);
                p.setLayoutY(0);

                //point(10,10)
                p.getPoints().add(10.0);
                p.getPoints().add(10.0);
                //point(510,10)
                p.getPoints().add(510.0);
                p.getPoints().add(10.0);
                //point(510,310)
                p.getPoints().add(510.0);
                p.getPoints().add(310.0);
                //point(10,310)
                p.getPoints().add(10.0);
                p.getPoints().add(310.0);

                p.setFill(new ImagePattern(gaufre, 0, 0, 1, 1, true));

                root.getChildren().add(p);
            }
        }*/

	/*
	 gc.drawImage(image1, 10, 10);
	 gc.setFill(Color.BROWN);
	 gc.setStroke(Color.BLUE);
	 gc.setLineWidth(5);
	 gc.fillRect(100, 100, 600, 400);
	 */
	

    }
}