/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA;

import Modele.Case;
import Modele.Couleur;
import Modele.Joueur;
import Modele.Plateau;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @author novelm
 */
public class GenereIA {

    private ArrayList<Function<Plateau, Case>> initialisation;

    private ArrayList<Function<Plateau, Case>> debutJeu;
    private ArrayList<Function<Plateau, Case>> debutJeuCertain;
    private ArrayList<Function<Plateau, Case>> debutJeuElimination;

    private ArrayList<Function<Plateau, Case>> millieuJeu;
    private ArrayList<Function<Plateau, Case>> millieuJeuCertain;
    private ArrayList<Function<Plateau, Case>> millieuJeuElimination;

    private ArrayList<JoueurIA> enumeration;

    public GenereIA(JoueurIA IA) {
        //Methode pouvant etre utilisées pour l'initialisation
        this.initialisation = new ArrayList<>();
        this.initialisation.add(IA::phaseInitialisation);
        this.initialisation.add(IA::phaseInitialisationGourmande);
        this.initialisation.add(IA::phaseInitialisationMaxPossibilitee);

        //Methode pouvant etre utilisées pour le debut de partie
        this.debutJeu = new ArrayList<>();
        this.debutJeuCertain = new ArrayList<>();
        this.debutJeuElimination = new ArrayList<>();
        //this.debutJeuCertain.add(IA::phaseJeu);
        this.debutJeuCertain.add(IA::phaseJeuGourmand);
        this.debutJeuCertain.add(IA::phaseJeuMaxPossibilitee);

        this.debutJeuElimination.add(IA::phaseJeuElimination);
        this.debutJeuElimination.add(IA::chercherVictimePremierDuNom);

        this.debutJeu.add(IA::chercheIlot);
        this.debutJeu.add(IA::phaseJeuElimination);
        this.debutJeu.add(IA::chercherVictimePremierDuNom);

        //Methode pouvant etre utilisées pour le millieu de partie
        this.millieuJeu = new ArrayList<>();
        this.millieuJeuCertain = new ArrayList<>();
        this.millieuJeuElimination = new ArrayList<>();
        //this.millieuJeuCertain.add(IA::phaseJeu);
        this.millieuJeuCertain.add(IA::phaseJeuGourmand);
        this.millieuJeuCertain.add(IA::phaseJeuMaxPossibilitee);

        this.millieuJeuElimination.add(IA::phaseJeuElimination);
        this.millieuJeuElimination.add(IA::chercherVictimePremierDuNom);

        this.millieuJeu.add(IA::chercheIlot);
        this.millieuJeu.add(IA::phaseJeuElimination);
        this.millieuJeu.add(IA::chercherVictimePremierDuNom);

        this.enumeration = new ArrayList<>();
    }

    public ArrayList<Joueur> genererGroupeIndividu(int tailleGroupe, int generation) {
        ArrayList<Joueur> groupe = new ArrayList<>();

        int i = 0;
        EnumIA individuCourant;
        Random r = new Random();
        while (i < tailleGroupe) {
            individuCourant = new EnumIA(Couleur.VALEURS[r.nextInt(Couleur.VALEURS.length)], generation);

            individuCourant.ajouterInitialisation(this.initialisation.get(r.nextInt(this.initialisation.size())));

            individuCourant.ajouterDebut(this.debutJeu.get(r.nextInt(this.debutJeu.size())));
            individuCourant.ajouterDebut(this.debutJeuCertain.get(r.nextInt(this.debutJeuCertain.size())));

            individuCourant.ajouterMilieu(this.millieuJeu.get(r.nextInt(this.millieuJeu.size())));
            individuCourant.ajouterMilieu(this.millieuJeuCertain.get(r.nextInt(this.millieuJeuCertain.size())));

            groupe.add(individuCourant);
            i++;
        }

        return groupe;
    }
    /*
     public void genererIA() {
     int i = 0;
     Random r = new Random();
     Couleur[] couleurs = {Couleur.Bleu, Couleur.Jaune, Couleur.Rouge, Couleur.Vert};
     EnumIA IAcourante = new EnumIA(couleurs[r.nextInt(4)],0);
     i++;
     for (Function init : this.initialisation) {
     IAcourante.ajouterInitialisation(init);

     for (Function debut : this.debutJeu) {
     for (Function debutElim : this.debutJeuElimination) {
     for (Function debutCertain : this.debutJeuCertain) {
     IAcourante.ajouterDebut(debut);
     IAcourante.ajouterDebut(debutElim);
     IAcourante.ajouterDebut(debutCertain);

     for (Function millieu : this.millieuJeu) {
     for (Function millieuElim : this.millieuJeuElimination) {
     for (Function millieuCertain : this.millieuJeuCertain) {
     IAcourante.ajouterMilieu(millieu);
     IAcourante.ajouterMilieu(millieuElim);
     IAcourante.ajouterMilieu(millieuCertain);

     this.enumeration.add(IAcourante);

     IAcourante = new EnumIA(couleurs[r.nextInt(4)], 0);
     i++;

     }
     }
     }

     }
     }
     }

     }
     }*/

    public ArrayList<JoueurIA> getEnumeration() {
        return enumeration;
    }

    public ArrayList<Function<Plateau, Case>> getInitialisation() {
        return initialisation;
    }

    public ArrayList<Function<Plateau, Case>> getDebutJeu() {
        return debutJeu;
    }

    public ArrayList<Function<Plateau, Case>> getDebutJeuCertain() {
        return debutJeuCertain;
    }

    public ArrayList<Function<Plateau, Case>> getDebutJeuElimination() {
        return debutJeuElimination;
    }

    public ArrayList<Function<Plateau, Case>> getMillieuJeu() {
        return millieuJeu;
    }

    public ArrayList<Function<Plateau, Case>> getMillieuJeuCertain() {
        return millieuJeuCertain;
    }

    public ArrayList<Function<Plateau, Case>> getMillieuJeuElimination() {
        return millieuJeuElimination;
    }

}
