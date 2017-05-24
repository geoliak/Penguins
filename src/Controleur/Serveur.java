/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import Modele.ClientJeu;
import Modele.Plateau;
import Modele.ServeurJeu;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author mariobap
 */
public class Serveur {
    
    public static void main(String[] args) throws IOException, ClassNotFoundException{
        ServeurJeu s = new ServeurJeu(1, new Plateau("ressources/plateaux/plateau1"));
        s.start();
        
    }
}
