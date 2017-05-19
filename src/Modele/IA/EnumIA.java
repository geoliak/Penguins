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

    private ArrayList<BiFunction<JoueurIA, Partie, Case>> initialisation;
    private ArrayList<BiFunction<JoueurIA, Partie, Case>> debutJeu;
    private ArrayList<BiFunction<JoueurIA, Partie, Case>> millieuJeu;

    private int generation;
    public static final double TAUX_DE_MUTATION = 0.05;

    public EnumIA(Couleur couleur, int generation, String nom) {
	super(couleur, nom, 1);
	this.initialisation = new ArrayList<>();
	this.debutJeu = new ArrayList<>();
	this.millieuJeu = new ArrayList<>();
	this.generation = generation;
    }

    @Override
    public Case etablirCoup(Partie partie) {
	Random r = new Random();
	Case caseChoisie = null;
	BiFunction<JoueurIA, Partie, Case> fonction;
	int i = 0;

	if (!super.getPret()) {
	    while (caseChoisie == null) {
		if (r.nextFloat() < 0.75) {
		    caseChoisie = this.initialisation.get(i % this.initialisation.size()).apply(this, partie); //pour l'instant
		}
		i++;
	    }

	} else {

	    /*System.out.println(this);
	     DessinateurTexte dt = new DessinateurTexte();
	     partie.getPlateau().accept(dt);*/
	    while (caseChoisie == null) {
		if (!this.estFinJeu(partie)) {
		    //saute l'allele 50% de chance
		    if (r.nextBoolean()) {
			if (this.nbCasesRestantes(partie) < 19) {
			    fonction = this.debutJeu.get(i % this.debutJeu.size());
			    caseChoisie = fonction.apply(this, partie);
			    //System.out.println("debutJeu <" + i % this.debutJeu.size() + ">");
			} else {
			    fonction = this.millieuJeu.get(i % this.millieuJeu.size());
			    caseChoisie = fonction.apply(this, partie);
			    //System.out.println("millieuJeu <" + i % this.millieuJeu.size() + ">");
			}
		    }
		    i++;
		} else {
		    caseChoisie = this.phaseJeuMeilleurChemin(partie);
		}

	    }
	    /*
	     System.out.println("----");
	     System.out.println(this.getPinguinCourant() + " -> "+caseChoisie);
	     System.out.println("======================== Fin etablir coup\n");*/
	}

	return caseChoisie;
    }

    public void mutation(GenereIA genereIA) {
	Random r = new Random();
	//25% de chance de modifier l'initialisation
	if (r.nextFloat() < 0.25) {
	    //Ajout d'un nouveau gene
	    if (r.nextFloat() < 0.25) {
		this.initialisation.add(r.nextInt(this.initialisation.size()), genereIA.getInitialisation().get(r.nextInt(genereIA.getInitialisation().size())));
		//Modification d'un gene
	    } else {
		this.initialisation.set(r.nextInt(this.initialisation.size()), genereIA.getInitialisation().get(r.nextInt(genereIA.getInitialisation().size())));
	    }

	    //Sinon 50% de chance de modifier le comportement de debut de partie
	} else if (r.nextBoolean()) {
	    //Ajout d'un nouveau gene
	    if (r.nextFloat() < 0.25) {
		this.debutJeu.add(r.nextInt(this.debutJeu.size()), genereIA.getDebutJeu().get(r.nextInt(genereIA.getDebutJeu().size())));
		//Modification d'un gene
	    } else {
		this.debutJeu.set(r.nextInt(this.debutJeu.size()), genereIA.getDebutJeu().get(r.nextInt(genereIA.getDebutJeu().size())));
	    }

	    // et 50% de chance de modifier le comportement de millieu de partie
	} else {
	    //Ajout d'un nouveau gene
	    if (r.nextFloat() < 0.25) {
		this.millieuJeu.add(r.nextInt(this.millieuJeu.size()), genereIA.getMillieuJeu().get(r.nextInt(genereIA.getMillieuJeu().size())));
		//Modification d'un gene
	    } else {
		this.millieuJeu.set(r.nextInt(this.millieuJeu.size()), genereIA.getMillieuJeu().get(r.nextInt(genereIA.getMillieuJeu().size())));
	    }
	}
    }

    public EnumIA reproduction(EnumIA parent, int id) {
	Couleur[] couleurs = {Couleur.Bleu, Couleur.Jaune, Couleur.Rouge, Couleur.Vert};
	EnumIA enfant;
	Random r = new Random();

	if (this.getCouleur() == parent.getCouleur()) {
	    enfant = new EnumIA(parent.getCouleur(), parent.generation + 1, Integer.toString(parent.generation + 1) + "<" + id + ">");
	} else {
	    enfant = new EnumIA(couleurs[r.nextInt(couleurs.length)], parent.generation + 1, Integer.toString(parent.generation + 1) + "<" + id + ">");
	}

	enfant.setInitialisation(fusionGenes(this.initialisation, parent.getInitialisation()));
	enfant.setDebutJeu(fusionGenes(this.debutJeu, parent.getDebutJeu()));
	enfant.setMillieuJeu(fusionGenes(this.millieuJeu, parent.getMillieuJeu()));

	return enfant;
    }

    private ArrayList<BiFunction<JoueurIA, Partie, Case>> fusionGenes(ArrayList<BiFunction<JoueurIA, Partie, Case>> yin, ArrayList<BiFunction<JoueurIA, Partie, Case>> yang) {
	int i = 0;
	Random r = new Random();
	ArrayList<BiFunction<JoueurIA, Partie, Case>> rep = new ArrayList<>();
	//Pour tous les genes existants chez les deux parents
	while (i < yin.size()) {
	    //Si les parent ont le meme
	    if (yin.get(i) == yang.get(i)) {
		rep.add(yang.get(i));
	    } else if (r.nextBoolean() && yang.size() > i) {
		rep.add(yang.get(i));
	    } else {
		rep.add(yin.get(i));
	    }
	    i++;
	}
	while (i < yang.size()) {
	    rep.add(yang.get(i));
	    i++;
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

    public void ajouterInitialisation(BiFunction<JoueurIA, Partie, Case> init) {
	this.initialisation.add(init);
    }

    public void ajouterDebut(BiFunction<JoueurIA, Partie, Case> debut) {
	this.debutJeu.add(debut);
    }

    public void ajouterMilieu(BiFunction<JoueurIA, Partie, Case> millieu) {
	this.millieuJeu.add(millieu);
    }

    public ArrayList<BiFunction<JoueurIA, Partie, Case>> getInitialisation() {
	return initialisation;
    }

    public void setInitialisation(ArrayList<BiFunction<JoueurIA, Partie, Case>> initialisation) {
	this.initialisation = initialisation;
    }

    public ArrayList<BiFunction<JoueurIA, Partie, Case>> getDebutJeu() {
	return debutJeu;
    }

    public void setDebutJeu(ArrayList<BiFunction<JoueurIA, Partie, Case>> debutJeu) {
	this.debutJeu = debutJeu;
    }

    public ArrayList<BiFunction<JoueurIA, Partie, Case>> getMillieuJeu() {
	return millieuJeu;
    }

    public void setMillieuJeu(ArrayList<BiFunction<JoueurIA, Partie, Case>> millieuJeu) {
	this.millieuJeu = millieuJeu;
    }

}
