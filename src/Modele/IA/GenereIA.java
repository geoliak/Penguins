/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA;

import Modele.Case;
import Modele.Couleur;
import static Modele.IA.JoueurIA.phaseInitialisationStatic;
import Modele.Joueur;
import Modele.Partie;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.BiFunction;

/**
 *
 * @author novelm
 */
public class GenereIA {

    private ArrayList<BiFunction<JoueurIA, Partie, Case>> initialisation;

    private ArrayList<BiFunction<JoueurIA, Partie, Case>> debutJeu;

    private ArrayList<BiFunction<JoueurIA, Partie, Case>> millieuJeu;

    private ArrayList<BiFunction<JoueurIA, Partie, Case>> finJeu;

    private ArrayList<BiFunction<JoueurIA, Partie, Case>> jeuCertain;

    private ArrayList<JoueurIA> groupe;

    public GenereIA(JoueurIA IA) {

        //Methode pouvant etre utilisées pour l'initialisation
        this.initialisation = new ArrayList<>();
        this.initialisation.add(JoueurIA::phaseInitialisationStatic);
        this.initialisation.add(JoueurIA::phaseInitialisationGourmandeStatic);
        this.initialisation.add(JoueurIA::phaseInitialisationMaxPossibiliteeStatic);

        //Methode pouvant etre utilisées pour le debut de partie
        this.debutJeu = new ArrayList<>();
        this.debutJeu.add(JoueurIA::minimax);
        this.debutJeu.add(JoueurIA::sauveQuiPeutBasiqueStatic);
        this.debutJeu.add(JoueurIA::chercheIlotStatic);
        this.debutJeu.add(JoueurIA::chercherVictimeSimple);
        this.debutJeu.add(JoueurIA::chercherVictimeIlotStatic);
        this.debutJeu.add(JoueurIA::phaseJeuGourmandStatic);
        this.debutJeu.add(JoueurIA::phaseJeuMaxPossibiliteeStatic);

        //Methode pouvant etre utilisées pour le millieu de partie
        this.millieuJeu = new ArrayList<>();
        //this.millieuJeuCertain.add(IA::phaseJeu);
        this.millieuJeu.add(JoueurIA::minimax);
        this.millieuJeu.add(JoueurIA::sauveQuiPeutBasiqueStatic);
        this.millieuJeu.add(JoueurIA::chercheIlotStatic);
        this.millieuJeu.add(JoueurIA::chercherVictimeSimple);
        this.millieuJeu.add(JoueurIA::chercherVictimeIlotStatic);
        this.millieuJeu.add(JoueurIA::phaseJeuGourmandStatic);
        this.millieuJeu.add(JoueurIA::phaseJeuMaxPossibiliteeStatic);

        //Methode retournant forcement une case
        this.jeuCertain = new ArrayList<>();
        //this.jeuCertain.add(JoueurIA::phaseJeuStatic);
        this.jeuCertain.add(JoueurIA::phaseJeuGourmandStatic);
        this.jeuCertain.add(JoueurIA::phaseJeuMaxPossibiliteeStatic);

        //Methode de fin de partie (peu de case ou seul)
        this.finJeu = new ArrayList<>();
        this.finJeu.add(JoueurIA::minimax);
        this.finJeu.add(JoueurIA::phaseJeuMeilleurCheminStatic);
    }

    public ArrayList<Joueur> genererGroupeIndividu(int tailleGroupe, int generation, int idDebut) {
        ArrayList<Joueur> groupe = new ArrayList<>();

        int i = idDebut;
        EnumIA individuCourant;
        Random r = new Random();
        while (i < tailleGroupe) {
            individuCourant = new EnumIA(Couleur.VALEURS[r.nextInt(Couleur.VALEURS.length)], generation, i, this);

            groupe.add(individuCourant);
            i++;
        }

        return groupe;
    }

    /**
     *
     * @param generationPrecedente : Generation precedente triee par nombre de
     * victoires decroissante
     * @param tailleGroupe : Taille d'individu dans le groupe
     * @param generation : Age de la nouvelle generation
     * @return Nouvelle generation
     */
    public ArrayList<Joueur> nouvelleGeneration(ArrayList<Joueur> generationPrecedente, int tailleGroupe, int generation) {
        ArrayList<Joueur> nouvelleGeneration = new ArrayList<>();
        EnumIA enfant;
        Random r = new Random();

        //On conserve les 5% meilleurs de la generation precedente
        nouvelleGeneration.addAll(generationPrecedente.subList(0, (int) (tailleGroupe * 0.05)));

        int id;
        //On couple les 5% meilleurs 4 fois chacun, avec des individus choisi au hasard
        for (id = 0; id  < (int) (tailleGroupe * 0.05); id++) {
            for (int j = 0; j < 4; j++) {
                enfant = new EnumIA(generation, id, this, (EnumIA) generationPrecedente.get(id), (EnumIA) generationPrecedente.get(r.nextInt(generationPrecedente.size())));
                nouvelleGeneration.add(enfant);
                
            }
            id++;
        }
        //On couple les 5%-10% meilleurs 3 fois chacun, avec des individus choisi au hasard
        for (; id  < (int) (tailleGroupe * 0.1); id++) {
            for (int j = 0; j < 3; j++) {
                enfant = new EnumIA(generation, id, this, (EnumIA) generationPrecedente.get(id), (EnumIA) generationPrecedente.get(r.nextInt(generationPrecedente.size())));
                nouvelleGeneration.add(enfant);
                
            }
            id++;
        }
        //On couple les 10%-20% meilleurs 2 fois chacun, avec des individus choisi au hasard
        for (; id  < (int) (tailleGroupe * 0.2); id++) {
            for (int j = 0; j < 2; j++) {
                enfant = new EnumIA(generation, id, this, (EnumIA) generationPrecedente.get(id), (EnumIA) generationPrecedente.get(r.nextInt(generationPrecedente.size())));
                nouvelleGeneration.add(enfant);
                
            }
            id++;
        }
        //On genere les 40% restants
        for (; id < tailleGroupe; id++) {
            enfant = new EnumIA(Couleur.VALEURS[r.nextInt(Couleur.VALEURS.length)], generation, id, this);
            nouvelleGeneration.add(enfant);
        }

        return nouvelleGeneration;
    }

    public ArrayList<BiFunction<JoueurIA, Partie, Case>> getInitialisation() {
        return initialisation;
    }

    public ArrayList<BiFunction<JoueurIA, Partie, Case>> getDebutJeu() {
        return debutJeu;
    }

    public ArrayList<BiFunction<JoueurIA, Partie, Case>> getMillieuJeu() {
        return millieuJeu;
    }

    public ArrayList<BiFunction<JoueurIA, Partie, Case>> getFinJeu() {
        return finJeu;
    }

    public void setFinJeu(ArrayList<BiFunction<JoueurIA, Partie, Case>> finJeu) {
        this.finJeu = finJeu;
    }

    public ArrayList<BiFunction<JoueurIA, Partie, Case>> getJeuCertain() {
        return jeuCertain;
    }

    public void setJeuCertain(ArrayList<BiFunction<JoueurIA, Partie, Case>> jeuCertain) {
        this.jeuCertain = jeuCertain;
    }

    public ArrayList<JoueurIA> getGroupe() {
        return groupe;
    }

    public void setGroupe(ArrayList<JoueurIA> groupe) {
        this.groupe = groupe;
    }
}
