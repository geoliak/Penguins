/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author novelm
 */
public class Plateau implements Serializable {

    private Case[][] cases;
    public static final int LARGEUR = 8;
    public static final int LONGUEUR = 8;
    private boolean estModifié;

    public Plateau(String fichierPlateau) throws FileNotFoundException, IOException {
	this.cases = new Case[LARGEUR][LONGUEUR];
	this.lireFichier(fichierPlateau);
	this.initCase();
	estModifié = true;
    }

    private Plateau() {
	this.cases = new Case[LARGEUR][LONGUEUR];
    }

    /**
     * Place le nombre de poisson(s) aléatoirement sur les cases jusqu'a avoir
     * une configuration jouable
     */
    public void initCase() {
	int nbCaseUnPoissons = 0;
	for (int i = 0; i < this.LARGEUR; i++) {
	    for (int j = 0; j < this.LONGUEUR; j++) {
		this.cases[i][j].initVoisins(this);
		if (!this.cases[i][j].estCoulee() && this.cases[i][j].getNbPoissons() == 1) {
		    nbCaseUnPoissons++;
		}
	    }
	}
	while (nbCaseUnPoissons < 9) {
	    nbCaseUnPoissons = 0;
	    for (int i = 0; i < this.LARGEUR; i++) {
		for (int j = 0; j < this.LONGUEUR; j++) {
		    if (!this.cases[i][j].estCoulee()) {
			this.cases[i][j].genereNbPoissons();
			nbCaseUnPoissons++;
		    }
		}
	    }
	}
    }

    /**
     * Lit le fichier pour établir la configuration du plateau
     *
     * @param fichierPlateau : chaine désignant le fichier ou est stocke le
     * plateau
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void lireFichier(String fichierPlateau) throws FileNotFoundException, IOException {
	BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fichierPlateau))));
	String ligne;
	int numLigne = 0;
	char[] c;
	//Pour toutes les lignes du fichier
	while ((ligne = br.readLine()) != null) {
	    c = ligne.toCharArray();
	    for (int i = 0; i < LONGUEUR; i++) {
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

    /**
     * Surligne les cases désignées
     *
     * @param cases : Tableau contenant les cases à surligner
     */
    public void surligneCases(ArrayList<Case> cases) {
	for (Case c : cases) {
	    c.setAccessible(true);
	}
    }

    /**
     * Désurligne les cases désignées
     *
     * @param cases : : Tableau contenant les cases à désurligner
     */
    public void desurligneCases(ArrayList<Case> cases) {
	for (Case c : cases) {
	    c.setAccessible(false);
	}
    }

    /**
     * Renvoit libre si la case n'est pas coulee et si il n'y a pas deja un
     * pinguin dessus
     *
     * @param x : ligne de la case
     * @param y : colonne de la case
     * @return : Vrai si la cases est libre
     */
    public Boolean estCaseLibre(int x, int y) {
	return (!this.cases[x][y].estCoulee() && this.cases[x][y].getPinguin() == null);
    }

    public ArrayList<Case> getMeilleurChemin(Case source, ArrayList<Case> cheminCourant, int tailleMaximale) {
	if (cheminCourant.size() >= tailleMaximale) {
	    System.out.println(tailleMaximale + " - " + cheminCourant.size());
	    return cheminCourant;

	} else {
	    ArrayList<Case> casesPossible = source.getCasePossibles();
	    int max = 0;
	    boolean possible = false;
	    ArrayList<Case> branchementCourant, branchementResultat = null;

	    //Pour toutes les cases qui n'ont pas ete visitee
	    for (Case c : casesPossible) {
		if (!cheminCourant.contains(c)) {
		    possible = true;

		    branchementCourant = new ArrayList<>();
		    branchementCourant.addAll(cheminCourant);
		    branchementCourant.add(c);
		    branchementCourant = getMeilleurChemin(c, branchementCourant, tailleMaximale);

		    if (branchementCourant.size() >= tailleMaximale) {
			return branchementCourant;
		    } else if (this.getPoidsChemin(branchementCourant) > max) {
			branchementResultat = branchementCourant;
			max = this.getPoidsChemin(branchementCourant);
		    } else if (this.getPoidsChemin(branchementCourant) == max && branchementCourant.size() > branchementResultat.size()) {
			branchementResultat = branchementCourant;
		    }

		}
	    }

	    if (!possible) {
		return cheminCourant;
	    } else {
		return branchementResultat;
	    }
	}

    }

    public int getPoidsChemin(ArrayList<Case> cases) {
	int res = 0;
	for (Case c : cases) {
	    res += c.getNbPoissons();
	}
	return res;
    }

    public ArrayList<Case> getCasesIceberg(Case source) {
	ArrayList<Case> iceberg = new ArrayList<>();
	this.getCasesIcebergWorker(source, iceberg);
	for (Case c : iceberg) {
	    c.setCoulee(false);
	}
	return iceberg;
    }

    private void getCasesIcebergWorker(Case source, ArrayList<Case> iceberg) {
	if (!source.estCoulee()) {
	    iceberg.add(source);
	    source.setCoulee(true);
	    for (Case c : source.getVoisinsEmerges()) {
		this.getCasesIcebergWorker(c, iceberg);
	    }
	}
    }

    public Plateau clone() {
	Plateau clone = new Plateau();
	Case caseCourante;
	for (int i = 0; i < this.getNbLignes(); i++) {
	    for (int j = 0; j < this.getNbColonnes(); j++) {
		caseCourante = new Case(this.cases[i][j]);
		clone.getCases()[i][j] = caseCourante;
	    }
	}
	return clone;
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
	return LARGEUR;
    }

    public int getNbColonnes() {
	return LONGUEUR;
    }

    public boolean isEstModifié() {
	return estModifié;
    }

    public void setEstModifié(boolean estModifié) {
	this.estModifié = estModifié;
    }

}
