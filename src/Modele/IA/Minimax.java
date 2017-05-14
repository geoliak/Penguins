/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA;

import java.util.ArrayList;

/**
 *
 * @author novelm
 */
public class Minimax {
    
    
    public class Arbre<T> {
        private ArrayList<T> fils;
        private T pere;
        private int poids;

        public Arbre() {
            this.poids = 0;
            this.fils = new ArrayList<>();
            this.pere = null;
        }
        
        public Arbre(T pere) {
            this.poids = 0;
            this.pere = pere;
            this.fils = new ArrayList<>();
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

        public T getPere() {
            return pere;
        }

        public void setPere(T pere) {
            this.pere = pere;
        }
        
        
    }
}
