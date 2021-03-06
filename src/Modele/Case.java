/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 *
 * @author novelm
 */
public class Case implements Serializable {

    private int nbPoissons;
    private int numLigne;
    private int numColonne;
    private Case[] voisins;
    private Boolean coulee;
    private Pinguin pinguin;
    private Boolean accessible;
    private MyPolygon polygon;

    public Case(Case c) {
        this.coulee = c.estCoulee();
        this.pinguin = c.getPinguin();
        this.accessible = c.getAccessible();
        this.voisins = new Case[6];
        this.numLigne = c.numLigne;
        this.numColonne = c.numColonne;
        this.nbPoissons = c.getNbPoissons();
    }

    public Case(int numLigne, int numColonne) {
        this.coulee = false;
        this.pinguin = null;
        this.accessible = false;
        this.voisins = new Case[6];
        this.numLigne = numLigne;
        this.numColonne = numColonne;
        this.genereNbPoissons();
    }

    public Case(int numLigne, int numColonne, int nbPoisson) {
        this.coulee = false;
        this.pinguin = null;
        this.accessible = false;
        this.voisins = new Case[6];
        this.numLigne = numLigne;
        this.numColonne = numColonne;
        this.nbPoissons = nbPoisson;
    }

    public void genereNbPoissons() {
        Random r = new Random();
        this.nbPoissons = r.nextInt(6);
        if (this.nbPoissons < 3) {
            this.nbPoissons = 1;
        } else if (this.nbPoissons < 5) {
            this.nbPoissons = 2;
        } else {
            this.nbPoissons = 3;
        }
    }

    public void initVoisins(Plateau plateau) {
        if (!this.coulee) {
            //Pas tout a gauche
            if (this.numColonne != 0) {
                this.voisins[4] = (plateau.getCases()[this.numLigne][this.numColonne - 1]);
            }
            //Pas tout a droite
            if (this.numColonne != plateau.getNbColonnes() - 1) {
                this.voisins[1] = (plateau.getCases()[this.numLigne][this.numColonne + 1]);
            }

            //Pas tout a gauche et tout en haut
            if ((this.numColonne != 0 || this.numLigne % 2 == 1) && this.numLigne != 0) {
                this.voisins[5] = (plateau.getCases()[this.numLigne - 1][this.numColonne - ((this.numLigne + 1) % 2)]);
            }
            //Pas tout a gauche et tout en bas
            if ((this.numColonne != 0 || (this.numColonne == 0 && this.numLigne % 2 == 1)) && this.numLigne != plateau.getNbLignes() - 1) {
                this.voisins[3] = (plateau.getCases()[this.numLigne + 1][this.numColonne - ((this.numLigne + 1) % 2)]);
            }

            //Pas tout a droite et tout en haut
            if (this.numColonne != plateau.getNbColonnes() - 1 && this.numLigne != 0) {
                this.voisins[0] = (plateau.getCases()[this.numLigne - 1][this.numColonne + (this.numLigne % 2)]);
            }
            //Pas tout a droite et tout en bas
            if ((this.numColonne != plateau.getNbColonnes() - 1 || this.numLigne % 2 == 1) && this.numLigne != plateau.getNbLignes() - 1) {
                this.voisins[2] = (plateau.getCases()[this.numLigne + 1][this.numColonne + (this.numLigne % 2)]);
            }
        }
    }

    public ArrayList<Case> getCasePossibles() {
        ArrayList<Case> res = new ArrayList<>();

        if (!coulee) {
            for (int i = 0; i < 6; i++) {
                parcoursLigne(res, this.voisins[i], i);
            }
        }

        res.sort((Case o1, Case o2) -> o1.compareTo(o2));

        if (this.pinguin != null) {
            ArrayList<Case> clone = (ArrayList<Case>) res.clone();
            clone.removeAll(this.pinguin.getCasesInterdites());
            
            if (!res.isEmpty() && !clone.isEmpty()) {
                return clone;
            }
        }
        
        return res;
    }

    public int compareTo(Case c2) {
        return this.nbPoissons - c2.getNbPoissons();
    }

    public void parcoursLigne(ArrayList<Case> res, Case courante, int direction) {
        if (courante != null && !courante.estCoulee() && courante.getPinguin() == null) {
            res.add(courante);
            parcoursLigne(res, courante.getVoisins()[direction], direction);
        }
    }

    public Case[] getVoisins() {
        return voisins;
    }

    public int getNbPoissons() {
        return nbPoissons;
    }

    public int getNumLigne() {
        return numLigne;
    }

    public int getNumColonne() {
        return numColonne;
    }

    public Boolean estCoulee() {
        return coulee;
    }

    public void setCoulee(Boolean coulee) {
        this.coulee = coulee;
    }

    public Pinguin getPinguin() {
        return pinguin;
    }

    public void setPinguin(Pinguin pinguin) {
        this.pinguin = pinguin;
    }

    public void accept(Visiteur v) {
        v.visite(this);
    }

    public Boolean getAccessible() {
        return accessible;
    }

    public void setAccessible(Boolean accessible) {
        this.accessible = accessible;
    }

    public void setPolygon(MyPolygon polygon) {
        this.polygon = polygon;
    }

    public MyPolygon getPolygon() {
        return polygon;
    }

    public int getNbVoisins() {
        int nbVoisins = 0;

        for (int i = 0; i < 6; i++) {
            if (this.voisins[i] != null && !this.voisins[i].estCoulee() && this.voisins[i].getPinguin() == null) {
                nbVoisins++;
            }
        }

        return nbVoisins;
    }

    public ArrayList<Case> getVoisinsJouable() {
        ArrayList<Case> voisins = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            if (this.voisins[i] != null && !this.voisins[i].estCoulee() && this.voisins[i].getPinguin() == null) {
                voisins.add(this.voisins[i]);
            }
        }

        return voisins;
    }

    public ArrayList<Case> getVoisinsEmerges() {
        ArrayList<Case> voisins = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            if (this.voisins[i] != null && !this.voisins[i].estCoulee()) {
                voisins.add(this.voisins[i]);
            }
        }

        return voisins;
    }

    public boolean estCaseLibre() {
        if (!this.coulee && this.pinguin == null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean estCaseValideInit() {
        return estCaseLibre() && nbPoissons == 1;
    }

    @Override
    public String toString() {
        return "(" + this.numLigne + "," + this.numColonne + ")";
    }

}
