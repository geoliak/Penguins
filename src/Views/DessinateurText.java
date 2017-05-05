/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;
import Models.*;

/**
 *
 * @author rozandq
 */
public class DessinateurText {
    
    public void dessinePlateau(Plateau plateau){
        System.out.print("  ");
        for(int j = 0; j < plateau.getLongueur(); j++){
            System.out.print(j + " ");
        }
        System.out.println();
        
        for(int i = 0; i < plateau.getLargeur(); i++){
            System.out.print(i + " ");
            for(int j = 0; j < plateau.getLongueur(); j++){
                if(i == 0 && j == 0){
                    System.out.print("X ");
                } else if(plateau.getCases()[i][j].isJouable()){
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }   
}
