/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import Modele.IA.JoueurIA;
import Modele.IA.Methodes.Methode;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fichierPlateau))));
        String ligne;
        ligne = br.readLine();
        if (ligne.equals("jeu")) {
            this.lireFichierJeu(fichierPlateau, br);
            this.initCase();
        } else {
            this.lireFichierTest(fichierPlateau, br);
        }

        setCasesValidesInit();

        estModifié = true;
    }

    private Plateau() {
        this.cases = new Case[LARGEUR][LONGUEUR];
    }

    public void setCasesValidesInit() {
        for (Case[] cases : cases) {
            for (Case c : cases) {
                if (c.getNbPoissons() == 1) {
                    c.setAccessible(true);
                }
            }
        }
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
    public void lireFichierJeu(String fichierPlateau, BufferedReader br) throws FileNotFoundException, IOException {
        String ligne;
        int numLigne = 0;
        char[] c;
        //Pour toutes les lignes du fichier
        while ((ligne = br.readLine()) != null) {
            c = ligne.toCharArray();
            for (int i = 0; i < LONGUEUR; i++) {
                if (c.length != i && c[i] == '?') {
                    this.cases[numLigne][i] = new Case(numLigne, i);
                } else {
                    this.cases[numLigne][i] = new Case(numLigne, i);
                    this.cases[numLigne][i].setCoulee(true);
                }
            }
            numLigne++;
        }
    }

    public void lireFichierTest(String fichierPlateau, BufferedReader br) throws FileNotFoundException, IOException {
        //System.out.println("test");
        String ligne;
        int numLigne = 0;
        char[] c;
        //Pour toutes les lignes du fichier
        while ((ligne = br.readLine()) != null) {
            c = ligne.toCharArray();
            for (int i = 0; i < LONGUEUR; i++) {
                //System.out.println(numLigne + " " + i);
                if (c.length != i && c[i] == '?') {
                    this.cases[numLigne][i] = new Case(numLigne, i);
                } else if (c.length != i && c[i] == '1') {
                    this.cases[numLigne][i] = new Case(numLigne, i, 1);
                } else if (c.length != i && c[i] == '2') {
                    this.cases[numLigne][i] = new Case(numLigne, i, 2);
                } else if (c.length != i && c[i] == '3') {
                    this.cases[numLigne][i] = new Case(numLigne, i, 3);
                } else {
                    this.cases[numLigne][i] = new Case(numLigne, i);
                    this.cases[numLigne][i].setCoulee(true);
                }
            }
            numLigne++;
        }

        for (Case[] cases : this.cases) {
            for (Case ca : cases) {
                ca.initVoisins(this);
            }
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

    public void appliquerSurCasesAvecDebut(int x, int y, Methode methode) {
        for (int i = x; i < x + this.getNbLignes(); i++) {
            for (int j = y; j < y + this.getNbColonnes(); j++) {
                methode.execute(this.cases[i % this.getNbLignes()][j % this.getNbColonnes()]);
            }
        }
    }

    public void appliquerSurCases(Methode methode) {
        this.appliquerSurCasesAvecDebut(0, 0, methode);
    }

    public ArrayList<Case> getMeilleurChemin(Case source, ArrayList<Case> cheminCourant, int tailleMaximale) {
        if (cheminCourant.size() >= tailleMaximale) {
            //System.out.println(tailleMaximale + " - " + cheminCourant.size());
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
                    source.setCoulee(true);
                    branchementCourant = getMeilleurChemin(c, branchementCourant, tailleMaximale);
                    source.setCoulee(false);

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

    public ArrayList<Case> getCasesIcebergSansCassures(Case source) {
        ArrayList<Case> iceberg = new ArrayList<>();
        iceberg.add(source);
        source.setCoulee(true);
        for (Case voisin : source.getVoisinsEmerges()) {
            this.getCasesIcebergSansCassuresWorker(voisin, iceberg);
        }
        
        for (Case c : iceberg) {
            c.setCoulee(false);
        }
        return iceberg;
    }

    private void getCasesIcebergSansCassuresWorker(Case source, ArrayList<Case> iceberg) {
        if (!source.estCoulee() && !(JoueurIA.estIlot(source, this) != null && source.getPinguin() != null)) {
            iceberg.add(source);
            source.setCoulee(true);
            for (Case c : source.getVoisinsEmerges()) {
                this.getCasesIcebergWorker(c, iceberg);
            }
        }
    }

    public int getPoidsIceberg(ArrayList<Case> iceberg) {
        int rep = 0;

        for (Case c : iceberg) {
            rep += c.getNbPoissons();
        }

        return rep;
    }

    public int getNbPinguinIceberg(ArrayList<Case> iceberg) {
        int rep = 0;

        for (Case c : iceberg) {
            if (c.getPinguin() != null) {
                rep += c.getNbPoissons();
            }
        }

        return rep;
    }

    public HashMap<Joueur, ArrayList<Pinguin>> getPinguinsIceberg(ArrayList<Case> iceberg) {
        HashMap<Joueur, ArrayList<Pinguin>> rep = new HashMap<>();

        for (Case c : iceberg) {

            if (c.getPinguin() != null && rep.get(c.getPinguin().getGeneral()) == null) {
                rep.put(c.getPinguin().getGeneral(), new ArrayList<>());
                rep.get(c.getPinguin().getGeneral()).add(c.getPinguin());
            } else if (c.getPinguin() != null) {
                rep.get(c.getPinguin().getGeneral()).add(c.getPinguin());
            }

        }

        return rep;
    }

    public int getNbJoueurIceberg(ArrayList<Case> iceberg) {
        HashSet<Joueur> joueurs = new HashSet<>();

        for (Case c : iceberg) {
            if (c.getPinguin() != null) {
                joueurs.add(c.getPinguin().getGeneral());
            }
        }

        if (joueurs.isEmpty()) {
            return 1;
        } else {
            return joueurs.size();
        }
    }

    public ArrayList<Joueur> getJoueursIceberg(ArrayList<Case> iceberg) {
        ArrayList<Joueur> joueurs = new ArrayList<>();

        for (Case c : iceberg) {
            if (c.getPinguin() != null && !joueurs.contains(c.getPinguin().getGeneral())) {
                joueurs.add(c.getPinguin().getGeneral());
            }
        }

        return joueurs;
    }

    @Override
    public Plateau clone() {
        Plateau clone = new Plateau();
        for (int i = 0; i < this.getNbLignes(); i++) {
            for (int j = 0; j < this.getNbColonnes(); j++) {
                clone.getCases()[i][j] = new Case(this.cases[i][j]);
            }
        }

        for (int i = 0; i < this.getNbLignes(); i++) {
            for (int j = 0; j < this.getNbColonnes(); j++) {
                clone.getCases()[i][j].initVoisins(clone);
            }
        }

        return clone;
    }

    /**
     * Algorithme de Dijkstra
     *
     * @param origine : la case d'origine
     * @return la matrice d'incidence du graphe
     */
    public Integer[][] Dijkstra(Case origine) {
        Integer[][] D = new Integer[this.getNbLignes()][this.getNbColonnes()];
        ArrayList<Case> Q = new ArrayList<>();

        for (int m = 0; m < this.getNbLignes(); m++) {
            for (int n = 0; n < this.getNbColonnes(); n++) {
                D[m][n] = Integer.MAX_VALUE;
            }
        }

        Q.add(origine);
        D[origine.getNumLigne()][origine.getNumColonne()] = 0;

        Case u;
        while (!Q.isEmpty()) {
            //System.out.println(Q.size());
            u = Q.get(0);
            for (Case c : Q) {
                if (D[c.getNumLigne()][c.getNumColonne()] < D[u.getNumLigne()][u.getNumColonne()]) {
                    u = c;
                }
            }
            Q.remove(u);

            for (Case v : u.getVoisinsEmerges()) {
                if (D[u.getNumLigne()][u.getNumColonne()] + 1 < D[v.getNumLigne()][v.getNumColonne()]) {
                    D[v.getNumLigne()][v.getNumColonne()] = D[u.getNumLigne()][u.getNumColonne()] + 1;
                    Q.add(v);
                }
            }

        }

        return D;
    }

    public Boolean existeChemin(Case source, Case destination, int profondeur) {
        if (profondeur == 0) {
            return false;
            
        } else if (source == destination) {
            return true;
            
        } else {
            source.setCoulee(true);
            boolean rep = false;

            for (Case voisins : source.getVoisinsJouable()) {
                rep = rep || this.existeChemin(voisins, destination, profondeur - 1);
            }

            source.setCoulee(false);
            return rep;
        }
    }

    //GETTER ET SETTER
    public void accept(Visiteur v) {
        v.visite(this);
    }

    public Case[][] getCases() {
        return cases;
    }

    public void setCases(Case[][] cases) {
        this.cases = cases;
    }

    public void setCases() {
        for (Case[] cases : this.cases) {
            for (Case c : cases) {
                if (c.getPolygon() != null) {
                    c.getPolygon().setImage(c.getNbPoissons());
                }
            }
        }
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
