/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controleur;

import java.io.Serializable;

/**
 *
 * @author mariobap
 */
public class ClasseTest implements Serializable{
    private int entier;
    private String chaine;
    private ClasseTest classeTest;
    
    public ClasseTest(int e, String c){
        entier = e;
        chaine = c;
    }

    public ClasseTest getClasseTest() {
        return classeTest;
    }

    public void setClasseTest(ClasseTest classeTest) {
        this.classeTest = classeTest;
    }
    
    @Override
    public String toString(){
       String s = entier + " - " + chaine + " - ";
       if(classeTest != null){
           s += classeTest.toString();
       }
       else {
           s += "null";
       }
       
       return s;
    }
}
