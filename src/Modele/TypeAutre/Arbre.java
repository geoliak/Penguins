/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.TypeAutre;

import java.util.ArrayList;

/**
 *
 * @author novelm
 */
public class Arbre<T> {

    private ArrayList<T> fils;
    private Arbre<T> pere;
    private T t;
    private int poids;

    public Arbre(T t) {
        this.poids = 0;
        this.fils = new ArrayList<>();
        this.pere = null;
        this.t = t;
    }

    public Arbre(Arbre<T> pere, T t) {
        this.poids = 0;
        this.pere = pere;
        this.fils = new ArrayList<>();
        this.t = t;
    }

    public Arbre<T> getPere() {
        return pere;
    }

    public void setPere(Arbre<T> pere) {
        this.pere = pere;
    }

    public T getObjetCourante() {
        return t;
    }

    public void setObjetCourante(T t) {
        this.t = t;
    }

    public int getPoids() {
        return poids;
    }

    public void setPoids(int poids) {
        this.poids = poids;
    }

    public void ajouterFils(T fils) {
        this.fils.add(fils);
    }

    public ArrayList<T> getFils() {
        return fils;
    }

    public void setFils(ArrayList<T> fils) {
        this.fils = fils;
    }
}
