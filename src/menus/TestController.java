/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menus;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author mariobap
 */
public class TestController implements Initializable {

    @FXML private Button button_incrementer;
    @FXML private Label nombre;
    
    private Nombre n;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        n = new Nombre();
    }    
    
    public void incrementer(){
        n.incrementer();
        nombre.setText("" + n.getNombre());
    }
    
}
