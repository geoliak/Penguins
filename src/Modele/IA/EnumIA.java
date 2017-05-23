/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA;

import Modele.Case;
import Modele.Couleur;
import Modele.Partie;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.BiFunction;

/**
 *
 * @author Mathias
 */
public class EnumIA extends JoueurIA {

    private double[] initialisation;

    private double[] debutJeu;

    private double[] millieuJeu;

    private double[] finJeu;

    private double[] jeuCertain;

    private int generation;
    public static final double TAUX_DE_MUTATION = 0.05;

    private int indiceDeFinDeJeu;

    private GenereIA genereIA;

    public EnumIA(Couleur couleur, int generation, int id, GenereIA genereIA) {
        super(couleur, generation + "<" + id + ">", 1);
        Random r = new Random();
        this.indiceDeFinDeJeu = r.nextInt(25) + 5;

        this.initialisation = this.initGene(genereIA.getInitialisation());
        this.debutJeu = this.initGene(genereIA.getDebutJeu());
        this.millieuJeu = this.initGene(genereIA.getMillieuJeu());
        this.finJeu = this.initGene(genereIA.getFinJeu());
        this.jeuCertain = this.initGene(genereIA.getJeuCertain());

        this.genereIA = genereIA;
        this.generation = generation;

    }

    private double[] initGene(ArrayList<BiFunction<JoueurIA, Partie, Case>> methodes) {
        double[] gene = new double[methodes.size()];
        double correction = 0;
        Random r = new Random();

        for (int i = 0; i < methodes.size(); i++) {
            gene[i] = r.nextDouble();
            correction += gene[i];
        }
        correction = 1 / correction;
        for (int i = 0; i < methodes.size(); i++) {
            gene[i] = gene[i] * correction;
        }

        return gene;
    }

    public EnumIA(int generation, int id, GenereIA genereIA, EnumIA mere, EnumIA pere) {
        super(mere.getCouleur(), generation + "<" + id + ">", 1);
        this.indiceDeFinDeJeu = (mere.getIndiceDeFinDeJeu() + pere.getIndiceDeFinDeJeu()) / 2;

        this.initialisation = this.fusionGenes(mere.getInitialisation(), pere.getInitialisation());
        this.debutJeu = this.fusionGenes(mere.getDebutJeu(), pere.getDebutJeu());
        this.millieuJeu = this.fusionGenes(mere.getMillieuJeu(), pere.getMillieuJeu());
        this.finJeu = this.fusionGenes(mere.getFinJeu(), pere.getFinJeu());
        this.jeuCertain = this.fusionGenes(mere.getJeuCertain(), pere.getJeuCertain());

        this.genereIA = genereIA;
        this.generation = generation;
    }

    public Case getCaseCertaineFrom(double[] proba, Partie partie, ArrayList<BiFunction<JoueurIA, Partie, Case>> methodes) {
        //System.out.print("getCaseCertaineFrom ");
        Case rep = null;
        Random r = new Random();
        double choix, probaCourante;

        int i = 0;
        choix = r.nextDouble();
        probaCourante = proba[i];

        while (probaCourante < choix) {
            i++;
            probaCourante += proba[i];
        }
        rep = methodes.get(i).apply(this, partie);
        //System.out.println("- OK");
        return rep;
    }

    public Case getCaseIncertaineFrom(double[] proba, Partie partie, ArrayList<BiFunction<JoueurIA, Partie, Case>> methodes) {
        //System.out.print("getCaseIncertaineFrom ");
        Case rep = null;
        Random r = new Random();
        double choix, probaCourante;
        double[] etatCourant = proba.clone();

        int i;
        int j = 0;

        do {
            choix = r.nextDouble();
            i = 0;
            probaCourante = etatCourant[i];
            //Recherche de la methode a utiliser
            while (probaCourante < choix) {
                i++;
                probaCourante += etatCourant[i];
                /*if (probaCourante > 1 || i > proba.length) {
                    System.out.println("==================\nDebug\n");
                    System.out.println("i " + i + "\t probaCourante " + probaCourante);
                    for (int l = 0; l < etatCourant.length; l++) {
                        System.out.println("etatCourant["+l+"] = " + etatCourant[l]);
                    }
                    System.out.println("");
                }*/
            }
            rep = methodes.get(i).apply(this, partie);
            //Si cette methode n'a pas donnee de case alors on la retire et on recommence sur les methode restantes
            if (rep == null) {
                double bonus = 1 / (1 - etatCourant[i]);
                etatCourant[i] = 0;
                for (int k = 0; k < etatCourant.length; k++) {
                    if (etatCourant[k] > 0) {
                        etatCourant[k] = etatCourant[k] * bonus;
                    }
                }
            }
            j++;

        } while (rep == null && j < proba.length);

        //Si aucun coup n'a donné de case
        if (rep == null) {
            rep = this.getCaseCertaineFrom(this.jeuCertain, partie, this.genereIA.getJeuCertain());
        }
        //System.out.println("- OK");
        return rep;
    }

    @Override
    public Case etablirCoup(Partie partie) {
        Case caseChoisie = null;
        //System.out.print("etablirCoupEnumIA ");
        if (!super.getPret()) {
            caseChoisie = this.getCaseCertaineFrom(this.initialisation, partie, this.genereIA.getInitialisation());

        } else {
            //Si tous les pinguins du joueur sont seuls sur leurs icebergs
            if (this.estSeul(partie)) {
                caseChoisie = this.phaseJeuMeilleurChemin(partie);

            } else {

                //Si on est en début de jeu
                if (this.nbCasesRestantes(partie) > this.indiceDeFinDeJeu) {
                    caseChoisie = this.getCaseIncertaineFrom(this.debutJeu, partie, this.genereIA.getDebutJeu());

                    //Si on est en millieu de jeu
                } else {
                    caseChoisie = this.getCaseIncertaineFrom(this.millieuJeu, partie, this.genereIA.getMillieuJeu());

                }
            }
        }
        //System.out.println("- OK");
        return caseChoisie;
    }

    private void muteGene(double[] gene) {
        Random r = new Random();
        int geneConcerne;

        geneConcerne = r.nextInt(this.initialisation.length);

        this.initialisation[geneConcerne] = r.nextDouble();
        for (int k = 0; k < this.initialisation.length; k++) {
            if (k != geneConcerne) {
                this.initialisation[k] = (1 - this.initialisation[geneConcerne]) * this.initialisation[k];
            }
        }
    }

    public void mutation(GenereIA genereIA) {
        Random r = new Random();
        double choix = r.nextDouble();

        //1 chance sur 5 de modifier l'initialisation
        if (choix < 1 / 5) {
            this.muteGene(this.initialisation);

            //1 chance sur 5 de modifier le comportement de debut de partie
        } else if (choix < 2 / 5) {
            this.muteGene(this.debutJeu);

            //1 chance sur 5 de modifier le comportement de millieu de partie
        } else if (choix < 3 / 5) {
            this.muteGene(this.millieuJeu);

            //1 chance sur 5 de modifier le comportement de jeu certain
        } else if (choix < 4 / 5) {
            this.muteGene(this.jeuCertain);

            //1 chance sur 5 de modifier le comportement de fin de partie
        } else {
            this.muteGene(this.finJeu);
        }
    }

    private double[] fusionGenes(double[] yin, double[] yang) {
        int i = 0;
        Random r = new Random();
        double[] rep = new double[yin.length];

        for (int k = 0; k < yang.length; k++) {
            rep[k] = (yin[k] + yang[k]) / 2;
        }

        return rep;
    }

    @Override
    public String toString() {
        /*StringBuilder sb = new StringBuilder();
         sb.append("============================\n");
         sb.append(super.toString()).append(" -> ");
         sb.append(this.initialisation).append(" _-_ ");
         sb.append(this.initialisation).append(" _-_ ");
         sb.append(this.initialisation);
         return sb.toString();
         */
        return super.toString();
    }

    public int getGeneration() {
        return generation;
    }

    public double[] getInitialisation() {
        return initialisation;
    }

    public void setInitialisation(double[] initialisation) {
        this.initialisation = initialisation;
    }

    public double[] getDebutJeu() {
        return debutJeu;
    }

    public void setDebutJeu(double[] debutJeu) {
        this.debutJeu = debutJeu;
    }

    public double[] getMillieuJeu() {
        return millieuJeu;
    }

    public void setMillieuJeu(double[] millieuJeu) {
        this.millieuJeu = millieuJeu;
    }

    public double[] getFinJeu() {
        return finJeu;
    }

    public void setFinJeu(double[] finJeu) {
        this.finJeu = finJeu;
    }

    public double[] getJeuCertain() {
        return jeuCertain;
    }

    public void setJeuCertain(double[] jeuCertain) {
        this.jeuCertain = jeuCertain;
    }

    public int getIndiceDeFinDeJeu() {
        return indiceDeFinDeJeu;
    }

    public void setIndiceDeFinDeJeu(int indiceDeFinDeJeu) {
        this.indiceDeFinDeJeu = indiceDeFinDeJeu;
    }

    public GenereIA getGenereIA() {
        return genereIA;
    }

    public void setGenereIA(GenereIA genereIA) {
        this.genereIA = genereIA;
    }

    @Override
    public String getSpecialitees() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getNom()).append(" Joueur IA enumeree");
        sb.append("|| Phase d'initialisation : \n");
        sb.append("|Au hasard\t\t-> " + this.initialisation[0] + "\n");
        sb.append("|Gourmande\t\t\t-> " + this.initialisation[1] + "\n");
        sb.append("|Max mouvements\t\t\t-> " + this.initialisation[2] + "\n");

        sb.append("|| Phase debutJeu : \n");
        sb.append("|Minimax\t\t\t-> " + this.debutJeu[0] + "\n");
        sb.append("|Sauve qui peut\t\t\t-> " + this.debutJeu[1] + "\n");
        sb.append("|Cherche ilot\t\t\t-> " + this.debutJeu[2] + "\n");
        sb.append("|Victime simple\t\t\t-> " + this.debutJeu[3] + "\n");
        sb.append("|Victime ilot\t\t\t-> " + this.debutJeu[4] + "\n");
        sb.append("|Gourmande\t\t\t-> " + this.debutJeu[5] + "\n");
        sb.append("|Max mouvements\t\t\t-> " + this.debutJeu[6] + "\n");

        sb.append("|| Phase millieuJeu : \n");
        sb.append("|Minimax\t\t\t-> " + this.millieuJeu[0] + "\n");
        sb.append("|Sauve qui peut\t\t\t-> " + this.millieuJeu[1] + "\n");
        sb.append("|Cherche ilot\t\t\t-> " + this.millieuJeu[2] + "\n");
        sb.append("|Victime simple\t\t\t-> " + this.millieuJeu[3] + "\n");
        sb.append("|Victime ilot\t\t\t-> " + this.millieuJeu[4] + "\n");
        sb.append("|Gourmande\t\t\t-> " + this.millieuJeu[5] + "\n");
        sb.append("|Max mouvements\t\t\t-> " + this.millieuJeu[6] + "\n");

        sb.append("|| Phase jeu Certain : \n");
        sb.append("|Gourmande\t\t\t-> " + this.jeuCertain[0] + "\n");
        sb.append("|Max mouvements\t\t\t-> " + this.jeuCertain[1] + "\n");

        return sb.toString();
    }

}
