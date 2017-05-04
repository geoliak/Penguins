/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author novelm
 */
public class Plateau {

    private Case[][] cases;
    private final int nbLignes = 8;
    private final int nbColonnes = 8;

    public Plateau(String fichierPlateau) throws FileNotFoundException, IOException {
        this.cases = new Case[nbLignes][nbColonnes];
        this.lireFichier(fichierPlateau);
        this.initCase();
    }

    public void initCase() {
        for (int i = 0; i < this.nbLignes; i++) {
            for (int j = 0; j < this.nbColonnes; j++) {
                this.cases[i][j].initVoisins(this);
            }
        }
    }

    public void lireFichier(String fichierPlateau) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fichierPlateau))));
        String ligne;
        int numLigne = 0;
        char[] c;
        //Pour toutes les lignes du fichier
        while ((ligne = br.readLine()) != null) {
            c = ligne.toCharArray();
            for (int i = 0; i < nbColonnes; i++) {
                if (c.length != i && c[i] == '1') {
                    this.cases[numLigne][i] = new Case(numLigne, i);
                } else {
                    this.cases[numLigne][i] = new Case(numLigne, i);
                    this.cases[numLigne][i].setCoulee(true);
                }
            }
            numLigne++;
        }
    }
    
    public Boolean estCaseLibre(int x, int y) {
        return (!this.cases[x][y].estCoulee() && this.cases[x][y].getPinguin() == null);
    }

    public void accept(Visiteur v) {
        v.visite(this);
    }

    public Case[][] getCases() {
        return cases;
    }

    public void setCases(Case[][] cases) {
        this.cases = cases;
    }

    public int getNbLignes() {
        return nbLignes;
    }

    public int getNbColonnes() {
        return nbColonnes;
    }

}
