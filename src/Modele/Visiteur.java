/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import Modele.Case;
import Modele.Plateau;

/**
 *
 * @author novelm
 */
public abstract class Visiteur {
    
    public abstract void visite(Plateau plateau);
    
    public abstract void visite(Case c);
    
    public abstract void visite(Pinguin p);
    
}
