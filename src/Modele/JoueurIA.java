/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import Modele.Case;
import Modele.Couleur;
import Modele.Joueur;
import Modele.Pinguin;
import Modele.Plateau;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;

/**
 *
 * @author novelm
 */
public class JoueurIA extends Joueur {

    private ArrayList<Case> chemin;

    public JoueurIA(Couleur couleur, String nom) {
        super(couleur);
        this.chemin = new ArrayList<>();
        this.setEstHumain(false);
        Random r = new Random();
        this.setNom(nom);
        this.setAge(r.nextInt(123)); // Jeanne Calment
    }
    
    public void attendreCoup(Partie partie){
        if(partie.isTourFini()){
            // Initialisation
            if (partie.estEnInitialisation()) {

                if(!partie.getJoueurCourant().getEstHumain()){                      
                    //Défini placement pingouin
                    partie.getJoueurCourant().ajouterPinguin(partie.getJoueurCourant().etablirCoup(partie.getPlateau()));
                    partie.getPlateau().setEstModifié(true);
                    partie.joueurSuivant();
                } 
            // Phase de jeu : Tour IA
            } else {
                if(!partie.getJoueurCourant().getEstHumain()){
                    System.out.println("TOUR IA =======================");
                    partie.getJoueurCourant().joueCoup(partie.getJoueurCourant().etablirCoup(partie.getPlateau()));
                    System.out.println("COUP IA " + partie.getJoueurCourant().getPinguinCourant().getPosition().getNumColonne() + " " + partie.getJoueurCourant().getPinguinCourant().getPosition().getNumLigne());
                    partie.getPlateau().setEstModifié(true);
                    for(Joueur j : partie.getJoueurs()){
                        for(Pinguin p : j.getPinguinsVivants()){
                            if (p.getPosition().getCasePossibles().size() == 0) {
                                p.coullePinguin();
                                partie.getPlateau().setEstModifié(true);
                            }
                        }
                    }
                    partie.joueurSuivant();
                    System.out.println("JOUEUR COURANT " + partie.getJoueurCourant());
                    System.out.println("Fin tour IA");
                }
            }
        }
    }

    @Override
    public void reset() {
        super.reset();
        this.chemin = new ArrayList<>();
    }

    /**
     * Cette methode prend un plateau en parametre. L'IA va ensuite l'utiliser
     * pour déterminer quelle pinguin jouer et ou. Avant de renvoyer la case
     * choisie, l'IA place le pinguin choisi en tant que pinguinCourant
     *
     * @param plateau : plateau de jeu
     * @return la case jouee par l'IA
     */
    @Override
    public Case etablirCoup(Plateau plateau) {
        if (!super.getPret()) {
            return this.phaseInitialisation(plateau);
        } else {
            return this.phaseJeu(plateau);
        }
    }

    /////////////////////////////////////////////////
    //Methode utilisee dans la phase d'initialisation
    /////////////////////////////////////////////////
    /**
     *
     * @param plateau
     * @return
     */
    public Case phaseInitialisation(Plateau plateau) {
        Case caseChoisie;
        Random r = new Random();
        int i, j;
        do {
            i = r.nextInt(8);
            j = r.nextInt(8);
            caseChoisie = plateau.getCases()[i][j];
        } while (caseChoisie.estCoulee() || caseChoisie.getPinguin() != null || caseChoisie.getNbPoissons() > 1);

        return caseChoisie;
    }

    /**
     *
     * @param plateau
     * @return
     */
    public Case phaseInitialisationGourmande(Plateau plateau) {
        Case caseChoisie = null;
        ArrayList<Case> casesAccessible = null;
        int nbPoissons = 3;
        Random r = new Random();
        int debutligne = r.nextInt(plateau.getNbLignes());
        int debutColonne = r.nextInt(plateau.getNbColonnes());

        while (nbPoissons > 0) {
            for (int i = debutligne; i < debutligne + plateau.getNbLignes(); i++) {
                for (int j = debutColonne; j < debutColonne + plateau.getNbColonnes(); j++) {
                    Case caseCourante = plateau.getCases()[i % plateau.getNbLignes()][j % plateau.getNbColonnes()];
                    if (caseCourante != null && !caseCourante.estCoulee() && caseCourante.getNbPoissons() == 1 && caseCourante.getPinguin() == null) {
                        casesAccessible = caseCourante.getCasePossibles();
                        for (Case c : casesAccessible) {
                            if (c.getNbPoissons() == nbPoissons) {
                                return caseCourante;
                            }
                        }
                    }
                }
            }
            nbPoissons--;
        }

        return caseChoisie;
    }

    public Case phaseInitialisationMaxPossibilitee(Plateau plateau) {
        Case caseCourante, CaseRes = null;
        int maxCasesAtteignable = -1;
        ArrayList<Case> casesAtteignable;
        for (int i = 0; i < plateau.getNbLignes(); i++) {
            for (int j = 0; j < plateau.getNbColonnes(); j++) {
                caseCourante = plateau.getCases()[i][j];
                if (caseCourante != null && !caseCourante.estCoulee() && caseCourante.getPinguin() == null && caseCourante.getNbPoissons() == 1) {
                    casesAtteignable = caseCourante.getCasePossibles();
                    if (casesAtteignable.size() > maxCasesAtteignable) {
                        maxCasesAtteignable = casesAtteignable.size();
                        CaseRes = caseCourante;
                    }
                }
            }
        }

        return CaseRes;
    }

    /////////////////////////////////////////////////
    //Methode utilisee dans la phase de jeu
    /////////////////////////////////////////////////
    public Case phaseJeu(Plateau plateau) {
        Random r = new Random();

        //Choix aléatoire d'un pinguin vivant
        Pinguin p = super.getPinguinsVivants().get(r.nextInt(super.getPinguinsVivants().size()));
        this.setPinguinCourant(p);

        //Choix aléatoire d'une case
        ArrayList<Case> casePossibles = p.getPosition().getCasePossibles();
        Case caseChoisie = casePossibles.get(r.nextInt(casePossibles.size()));

        return caseChoisie;
    }

    public Case phaseJeuElimination(Plateau plateau) {
        Case caseChoisie = this.chercherVictime(plateau);
        //Si elle ne peut tuer personne, alors elle joue aléatoirement
        if (caseChoisie == null) {
            Random r = new Random();

            //Choix aléatoire d'un pinguin vivant
            Pinguin p = super.getPinguinsVivants().get(r.nextInt(super.getPinguinsVivants().size()));

            this.setPinguinCourant(p);

            //Choix aléatoire d'une case
            ArrayList<Case> casePossibles = p.getPosition().getCasePossibles();
            caseChoisie = casePossibles.get(r.nextInt(casePossibles.size()));
        }
        return caseChoisie;
    }

    public Case phaseJeuGourmand(Plateau plateau) {
        Case caseChoisie = null;
        ArrayList<Case> casesAccessible = null;
        int nbPoissons = 3;
        while (nbPoissons > 0) {
            for (Pinguin p : super.getPinguinsVivants()) {
                casesAccessible = p.getPosition().getCasePossibles();
                for (Case caseCourante : casesAccessible) {
                    if (caseCourante.getNbPoissons() == nbPoissons) {
                        super.setPinguinCourant(p);
                        return caseCourante;
                    }
                }
            }
            nbPoissons--;
        }

        return caseChoisie;
    }

    public Case phaseJeuMaxPossibilitee(Plateau plateau) {
        Pinguin pCourant = null;
        ArrayList<Case> casesPossibles;
        Case caseChoisie = null;
        
        //Selection du pinguin qui a le moins de possibilitee de mouvement
        for (Pinguin p : super.getPinguinsVivants()) {
            int min = Integer.MAX_VALUE;
            casesPossibles = p.getPosition().getCasePossibles();
            if (casesPossibles.size() < min) {
                pCourant = p;
                min = casesPossibles.size();
            }
        }
        super.setPinguinCourant(pCourant);

        int max = -1;
        ArrayList<Case> tmp;
        for (Case c : pCourant.getPosition().getCasePossibles()) {
            tmp = c.getCasePossibles();
            if (tmp.size() > max) {
                max = tmp.size();
                caseChoisie = c;
            }
        }
        
        return caseChoisie;
    }

    public Case phaseJeuMeilleurChemin(Plateau plateau) {
        //Si il n'y a plus pinguin adverse sur l'iceberg
        //System.out.println("Sont seuls ?");
        this.setPinguinsSeuls(plateau);
        Boolean sontSeuls = this.pinguinsSontSeuls();

        Case caseChoisie = null;

        if (sontSeuls && !this.chemin.isEmpty()) {
            System.out.println("Taille pinguin vivants : " + super.getPinguinsVivants().size());
            System.out.println("chemin de longueur " + chemin.size() + " Pinnguin courant " + this.getPinguinCourant());
            this.afficherChemin();
            caseChoisie = this.chemin.remove(0);

        } else if (sontSeuls && this.chemin.isEmpty()) {
            Random r = new Random();

            Pinguin p = super.getPinguinsVivants().get(r.nextInt(super.getPinguinsVivants().size()));
            System.out.println("Taille pinguin vivants : " + super.getPinguinsVivants().size());

            /*DessinateurTexte dt = new DessinateurTexte();
             System.out.println(this.getCouleur() + this.getNom() + Couleur.ANSI_RESET);
             dt.visite(plateau);*/
            this.setPinguinCourant(p);
            System.out.println("seul " + p.getPosition().getNumLigne() + "," + p.getPosition().getNumColonne());

            ArrayList<Case> iceberg = plateau.getCasesIceberg(p.getPosition());
            int tailleMaximale = iceberg.size();
            for (Case c : iceberg) {
                if (c.getPinguin() != null) {
                    tailleMaximale--;
                }
            }

            this.setChemin(plateau.getMeilleurChemin(p.getPosition(), new ArrayList<>(), tailleMaximale - (int) (tailleMaximale * 0.25)));

            System.out.println("chemin de longueur " + chemin.size() + " Pinnguin courant " + this.getPinguinCourant() + "taille iceberg " + iceberg.size() + iceberg);
            this.afficherChemin();
            caseChoisie = this.chemin.remove(0);
        }

        return caseChoisie;
    }

    /**
     *
     * @param plateau : plateau de jeu
     * @return Une case permettant de tuer un pinguin adverse ou null si une
     * telle case n'existe pas
     */
    public Case chercherVictime(Plateau plateau) {
        Case caseCourante = null, caseResultat = null;
        ArrayList<Case> mouvementsPossibles;
        CaseCritique cc;
        Pinguin ennemi, PinguinResultat = null;

        //Pour toutes les cases du plateau
        for (int i = 0; i < plateau.getNbLignes(); i++) {
            for (int j = 0; j < plateau.getNbColonnes(); j++) {

                //Si la case contient un pinguin ennemi
                caseCourante = plateau.getCases()[i][j];
                if (!caseCourante.estCoulee() && caseCourante.getPinguin() != null && caseCourante.getPinguin().getGeneral() != this) {
                    ennemi = caseCourante.getPinguin();

                    //Si cette case n'a qu'un seul voisin
                    if (caseCourante.getNbVoisins() == 1) {
                        for (Pinguin p : this.getPinguinsVivants()) {
                            mouvementsPossibles = p.getPosition().getCasePossibles();
                            if (mouvementsPossibles.contains(ennemi.getPosition().getVoisinsEmerges().get(0))) {
                                PinguinResultat = p;
                                caseResultat = ennemi.getPosition().getVoisinsEmerges().get(0);
                            }
                        }

                        //Si on peut isoler un pinguin sur un ilot
                    } else if ((cc = this.estIlot(caseCourante, plateau)) != null) {
                        caseCourante.setCoulee(Boolean.TRUE);

                        int poidsIlot1 = plateau.getPoidsIceberg(cc.getIlot1()) / plateau.getNbJoueurIceberg(cc.getIlot1());
                        int poidsIlot2 = plateau.getPoidsIceberg(cc.getIlot2()) / plateau.getNbJoueurIceberg(cc.getIlot2());

                        if (poidsIlot1 > poidsIlot2 && cc.getIlot1().size() == 1) {
                            //for pour une valeur lol
                            for (Case c : cc.getIlot1()) {
                                if (!c.estCoulee() && c.getPinguin() != null && c.getPinguin().getGeneral() == this) {
                                    mouvementsPossibles = c.getCasePossibles();
                                    if (mouvementsPossibles.contains(c)) {
                                        PinguinResultat = c.getPinguin();
                                        caseResultat = c;
                                    }
                                }
                            }
                        } else if (poidsIlot1 < poidsIlot2 && cc.getIlot2().size() == 1) {
                            //for pour une valeur lol
                            for (Case c : cc.getIlot2()) {
                                if (!c.estCoulee() && c.getPinguin() != null && c.getPinguin().getGeneral() == this) {
                                    mouvementsPossibles = c.getCasePossibles();
                                    if (mouvementsPossibles.contains(c)) {
                                        PinguinResultat = c.getPinguin();
                                        caseResultat = c;
                                    }
                                }
                            }
                        }

                        caseCourante.setCoulee(Boolean.FALSE);
                    }
                }
            }
        }

        super.setPinguinCourant(PinguinResultat);
        return caseResultat;
    }

    /**
     * Parcours l'ensemble des pinguins vivants pour determiner si ils sont
     * finalement seuls Met la variable estSeul a True des pinguins dans ce cas
     *
     * @param plateau : plateau de jeu
     */
    public void setPinguinsSeuls(Plateau plateau) {
        for (Pinguin p : super.getPinguinsVivants()) {
            if (!p.estSeul() && p.estSeulIceberg(plateau)) {
                p.setEstSeul(true);
            }
        }
    }

    /**
     * Parcours les pinguins vivants pour determiner si ils sont tous seuls sur
     * leur iceberg
     *
     * @return True si les pinguins sont tous seuls
     */
    public Boolean pinguinsSontSeuls() {
        boolean sontSeuls = true;
        for (Pinguin p : super.getPinguinsVivants()) {
            sontSeuls = sontSeuls && p.estSeul();
        }
        return sontSeuls;
    }

    public Case chercheIlot(Plateau plateau) {
        ArrayList<CaseCritique> ilotsPossibles = new ArrayList<>();
        ArrayList<Case> casesAccessibles, iceberg;
        int poidsIceberg, maxPoidsIceberg = -1;
        this.getIlotsPossibles(plateau, ilotsPossibles);
        Case c, caseCourante = null;

        //Pour tous les pinguins du joueur
        for (Pinguin p : super.getPinguinsVivants()) {
            casesAccessibles = p.getPosition().getCasePossibles();
            //On regarde si il peut former un ilot
            for (CaseCritique cc : ilotsPossibles) {
                c = cc.getCassure();
                if (casesAccessibles.contains(c)) {
                    //poids de l'iceberg total
                    iceberg = plateau.getCasesIceberg(p.getPosition());
                    poidsIceberg = plateau.getPoidsIceberg(iceberg);

                    //Poids de l'iceberg sans l'ilot
                    p.getPosition().setCoulee(Boolean.TRUE);
                    iceberg = plateau.getCasesIceberg(p.getPosition());

                    //Poid de l'ilot
                    poidsIceberg -= plateau.getPoidsIceberg(iceberg);
                    p.getPosition().setCoulee(Boolean.FALSE);

                    if (maxPoidsIceberg < poidsIceberg) {
                        caseCourante = c;
                        maxPoidsIceberg = poidsIceberg;
                    }
                }
            }
        }

        return caseCourante;
    }

    /**
     *
     * @param plateau : plateau de jeu
     * @param ilotsPossibles : return par effet de bord
     */
    public void getIlotsPossibles(Plateau plateau, ArrayList<CaseCritique> ilotsPossibles) {
        Case caseCourante;

        for (int i = 0; i < plateau.getNbLignes(); i++) {
            for (int j = 0; j < plateau.getNbColonnes(); j++) {
                caseCourante = plateau.getCases()[i][j];
                CaseCritique c = this.estIlot(caseCourante, plateau);
                if (!caseCourante.estCoulee() && caseCourante.getPinguin() == null && c != null) {
                    ilotsPossibles.add(c);
                }
            }
        }
    }

    /**
     * Retourne true si la case est le seul lien entre deux banquises
     *
     * @param caseCourante : case étudiée
     * @param plateau : plateau de jeu
     * @return true si la suppression de cette case peut former un ilot
     */
    public CaseCritique estIlot(Case caseCourante, Plateau plateau) {
        Integer[][] dijkstra;
        CaseCritique caseCritique = null;

        caseCourante.setCoulee(true);
        if (caseCourante.getVoisinsEmerges().size() == 2) {
            dijkstra = plateau.Dijkstra(caseCourante.getVoisinsEmerges().get(0));
            if (dijkstra[caseCourante.getVoisinsEmerges().get(1).getNumLigne()][caseCourante.getVoisinsEmerges().get(1).getNumColonne()] < Integer.MAX_VALUE) {
                caseCritique = new CaseCritique(caseCourante, caseCourante.getVoisinsEmerges(), dijkstra);
            }

        } else if (caseCourante.getVoisinsEmerges().size() == 3) {
            dijkstra = plateau.Dijkstra(caseCourante.getVoisinsEmerges().get(0));
            if (dijkstra[caseCourante.getVoisinsEmerges().get(1).getNumLigne()][caseCourante.getVoisinsEmerges().get(1).getNumColonne()] == Integer.MAX_VALUE || dijkstra[caseCourante.getVoisinsEmerges().get(2).getNumLigne()][caseCourante.getVoisinsEmerges().get(2).getNumColonne()] == Integer.MAX_VALUE) {
                caseCritique = new CaseCritique(caseCourante, caseCourante.getVoisinsEmerges(), dijkstra);
            }

        } else if (caseCourante.getVoisinsEmerges().size() == 4) {
            dijkstra = plateau.Dijkstra(caseCourante.getVoisinsEmerges().get(0));
            if (dijkstra[caseCourante.getVoisinsEmerges().get(1).getNumLigne()][caseCourante.getVoisinsEmerges().get(1).getNumColonne()] < Integer.MAX_VALUE && dijkstra[caseCourante.getVoisinsEmerges().get(2).getNumLigne()][caseCourante.getVoisinsEmerges().get(2).getNumColonne()] == Integer.MAX_VALUE && dijkstra[caseCourante.getVoisinsEmerges().get(2).getNumLigne()][caseCourante.getVoisinsEmerges().get(2).getNumColonne()] == Integer.MAX_VALUE) {
                caseCritique = new CaseCritique(caseCourante, caseCourante.getVoisinsEmerges(), dijkstra);
            } else if (dijkstra[caseCourante.getVoisinsEmerges().get(1).getNumLigne()][caseCourante.getVoisinsEmerges().get(1).getNumColonne()] == Integer.MAX_VALUE && (dijkstra[caseCourante.getVoisinsEmerges().get(2).getNumLigne()][caseCourante.getVoisinsEmerges().get(2).getNumColonne()] < Integer.MAX_VALUE ^ dijkstra[caseCourante.getVoisinsEmerges().get(3).getNumLigne()][caseCourante.getVoisinsEmerges().get(3).getNumColonne()] < Integer.MAX_VALUE)) {
                caseCritique = new CaseCritique(caseCourante, caseCourante.getVoisinsEmerges(), dijkstra);
            }
        }

        caseCourante.setCoulee(false);
        return caseCritique;
    }

    public class CaseCritique {

        private ArrayList<Case> ilot1;
        private ArrayList<Case> ilot2;
        private Case cassure;

        public CaseCritique(Case cassure, ArrayList<Case> voisins, Integer[][] dijkstra) {
            this.cassure = cassure;
            this.ilot1 = new ArrayList<>();
            this.ilot2 = new ArrayList<>();
            this.init(voisins, dijkstra);
        }

        public void init(ArrayList<Case> voisins, Integer[][] dijkstra) {
            Case ancien = null;
            for (Case c : voisins) {
                if (ancien == null) {
                    this.ilot1.add(c);
                } else if (dijkstra[c.getNumLigne()][c.getNumColonne()] < Integer.MAX_VALUE) {
                    this.ilot1.add(c);
                } else {
                    this.ilot2.add(c);
                }
                ancien = c;
            }
        }

        public ArrayList<Case> getIlot1() {
            return ilot1;
        }

        public ArrayList<Case> getIlot2() {
            return ilot2;
        }

        public Case getCassure() {
            return cassure;
        }

    }

    public ArrayList<Case> getChemin() {
        return chemin;
    }

    public void setChemin(ArrayList<Case> chemin) {
        this.chemin = chemin;
    }

    public void afficherChemin() {
        System.out.println("Chemin de " + this.chemin.size());
        for (Case c : this.chemin) {
            System.out.println(c.getNumLigne() + "," + c.getNumColonne());
        }
    }

    /**
     * Marche pas (fork bombe lol)
     */
    public class MeilleurChemin extends Thread {

        private Plateau plateau;
        private Case source;
        private ArrayList<Case> file;

        public MeilleurChemin(Plateau plateau, Case source, ArrayList<Case> file) {
            this.plateau = plateau.clone();
            this.source = this.plateau.getCases()[source.getNumLigne()][source.getNumColonne()];
            this.file = file;
        }

        @Override
        public void run() {
            file = getMeilleurChemin(this.plateau, this.source, (ArrayList<Case>) file.clone());
        }

        public ArrayList<Case> getMeilleurChemin(Plateau plateau, Case source, ArrayList<Case> reponse) {
            ArrayList<Case> casesPossibles = source.getCasePossibles();

            //System.out.println("-\n" + source.getNumLigne() + "," + source.getNumColonne() + " -> " + file.size());
            if (casesPossibles.isEmpty()) {
                /*System.out.println("nnChemin de " + reponse.size());
                 for (Case c : reponse) {
                 System.out.println(c.getNumLigne() + "," + c.getNumColonne());
                 }*/
                return reponse;
            } else {
                source.setCoulee(true);
                ArrayList<MeilleurChemin> threads = new ArrayList<>();
                ArrayList<ArrayList<Case>> reponses = new ArrayList<>();

                for (Case c : casesPossibles) {
                    reponses.add((ArrayList<Case>) reponse.clone());
                    reponses.get(reponses.size() - 1).add(c);
                    threads.add(new MeilleurChemin(plateau, c, reponses.get(reponses.size() - 1)));
                    threads.get(threads.size() - 1).start();
                }
                for (MeilleurChemin mc : threads) {
                    try {
                        mc.join();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(JoueurIA.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                int max = Integer.MIN_VALUE;
                ArrayList<Case> branchementResultat = null;
                for (ArrayList<Case> cases : reponses) {
                    if (this.getPoidsChemin(cases) > max) {
                        max = this.getPoidsChemin(cases);
                        branchementResultat = cases;
                    } else if (this.getPoidsChemin(cases) == max && cases.size() > branchementResultat.size()) {
                        branchementResultat = cases;
                    }
                }

                source.setCoulee(false);
                reponse.addAll(branchementResultat);
                return reponse;
                //System.out.println(source.getNumLigne() + "," + source.getNumColonne() + " -> " + file.size() + "\n-");
            }
        }

        public int getPoidsChemin(ArrayList<Case> cases) {
            int res = 0;
            for (Case c : cases) {
                res += c.getNbPoissons();
            }
            return res;
        }

    }

}
