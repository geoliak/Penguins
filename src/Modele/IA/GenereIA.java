/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA;

import Modele.Case;
import Modele.Couleur;
import Modele.Joueur;
import Modele.Partie;
import Modele.Plateau;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 *
 * @author novelm
 */
public class GenereIA {

    private ArrayList<BiFunction<JoueurIA, Partie, Case>> initialisation;

    private ArrayList<BiFunction<JoueurIA, Partie, Case>> debutJeu;
    private ArrayList<BiFunction<JoueurIA, Partie, Case>> debutJeuCertain;
    private ArrayList<BiFunction<JoueurIA, Partie, Case>> debutJeuElimination;

    private ArrayList<BiFunction<JoueurIA, Partie, Case>> millieuJeu;
    private ArrayList<BiFunction<JoueurIA, Partie, Case>> millieuJeuCertain;
    private ArrayList<BiFunction<JoueurIA, Partie, Case>> millieuJeuElimination;

    private ArrayList<JoueurIA> enumeration;

    public GenereIA(JoueurIA IA) {
        //Methode pouvant etre utilisées pour l'initialisation
        this.initialisation = new ArrayList<>();
        this.initialisation.add(JoueurIA::phaseInitialisationStatic);
        this.initialisation.add(JoueurIA::phaseInitialisationGourmandeStatic);
        this.initialisation.add(JoueurIA::phaseInitialisationMaxPossibiliteeStatic);

        //Methode pouvant etre utilisées pour le debut de partie
        this.debutJeu = new ArrayList<>();
        this.debutJeuCertain = new ArrayList<>();
        this.debutJeuElimination = new ArrayList<>();
        //this.debutJeuCertain.add(IA::phaseJeu);
        this.debutJeuCertain.add(JoueurIA::phaseJeuGourmand);
        this.debutJeuCertain.add(JoueurIA::phaseJeuMaxPossibilitee);

        this.debutJeuElimination.add(JoueurIA::phaseJeuElimination);
        this.debutJeuElimination.add(JoueurIA::chercherVictimeSimple);

        this.debutJeu.add(JoueurIA::chercheIlot);
        this.debutJeu.add(JoueurIA::phaseJeuElimination);
        this.debutJeu.add(JoueurIA::chercherVictimeSimple);

        //Methode pouvant etre utilisées pour le millieu de partie
        this.millieuJeu = new ArrayList<>();
        this.millieuJeuCertain = new ArrayList<>();
        this.millieuJeuElimination = new ArrayList<>();
        //this.millieuJeuCertain.add(IA::phaseJeu);
        this.millieuJeuCertain.add(JoueurIA::phaseJeuGourmand);
        this.millieuJeuCertain.add(JoueurIA::phaseJeuMaxPossibilitee);

        this.millieuJeuElimination.add(JoueurIA::phaseJeuElimination);
        this.millieuJeuElimination.add(JoueurIA::chercherVictimeSimple);

        this.millieuJeu.add(JoueurIA::chercheIlot);
        this.millieuJeu.add(JoueurIA::phaseJeuElimination);
        this.millieuJeu.add(JoueurIA::chercherVictimeSimple);

        this.enumeration = new ArrayList<>();
    }

    public ArrayList<Joueur> genererGroupeIndividu(int tailleGroupe, int generation, int idDebut) {
        ArrayList<Joueur> groupe = new ArrayList<>();

        int i = idDebut;
        EnumIA individuCourant;
        Random r = new Random();
        while (i < tailleGroupe) {
            individuCourant = new EnumIA(Couleur.VALEURS[r.nextInt(Couleur.VALEURS.length)], generation, generation + "<" + i + ">");

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

    public ArrayList<BiFunction<JoueurIA, Partie, Case>> getInitialisation() {
        return initialisation;
    }

    public ArrayList<BiFunction<JoueurIA, Partie, Case>> getDebutJeu() {
        return debutJeu;
    }

    public ArrayList<BiFunction<JoueurIA, Partie, Case>> getDebutJeuCertain() {
        return debutJeuCertain;
    }

    public ArrayList<BiFunction<JoueurIA, Partie, Case>> getDebutJeuElimination() {
        return debutJeuElimination;
    }

    public ArrayList<BiFunction<JoueurIA, Partie, Case>> getMillieuJeu() {
        return millieuJeu;
    }

    public ArrayList<BiFunction<JoueurIA, Partie, Case>> getMillieuJeuCertain() {
        return millieuJeuCertain;
    }

    public ArrayList<BiFunction<JoueurIA, Partie, Case>> getMillieuJeuElimination() {
        return millieuJeuElimination;
    }

}
