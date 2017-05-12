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
import Modele.Pinguin;
import Modele.Plateau;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public Boolean estDebutJeu(Plateau plateau) {
        int nbCasesCoulees = 0;
        for (int i = 0; i < plateau.getNbLignes(); i++) {
            for (int j = 0; j < plateau.getNbColonnes(); j++) {
                if (plateau.getCases()[i][j].estCoulee()) {
                    nbCasesCoulees++;
                }
            }
        }
        return nbCasesCoulees < 19; //Yolo
    }

    public Boolean estFinJeu(Plateau plateau) {
        return this.pinguinsSontSeuls();
    }

    @Override
    public void attendreCoup(Partie partie) {
        if (partie.isTourFini()) {
            // Initialisation
            if (partie.estEnInitialisation()) {

                if (!partie.getJoueurCourant().getEstHumain()) {
                    //Défini placement pingouin
                    partie.getJoueurCourant().ajouterPinguin(partie.getJoueurCourant().etablirCoup(partie.getPlateau()));
                    partie.getPlateau().setEstModifié(true);
                    partie.joueurSuivant();
                }
                // Phase de jeu : Tour IA
            } else {
                if (!partie.getJoueurCourant().getEstHumain()) {
                    System.out.println("TOUR IA =======================");
                    partie.getJoueurCourant().joueCoup(partie.getJoueurCourant().etablirCoup(partie.getPlateau()));
                    System.out.println("COUP IA " + partie.getJoueurCourant().getPinguinCourant().getPosition().getNumColonne() + " " + partie.getJoueurCourant().getPinguinCourant().getPosition().getNumLigne());
                    partie.getPlateau().setEstModifié(true);
                    for (Joueur j : partie.getJoueurs()) {
                        for (Pinguin p : j.getPinguinsVivants()) {
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
            return JoueurIA.phaseJeuStatic(this, plateau);
        }
    }

    /////////////////////////////////////////////////
    //Methode utilisee dans la phase d'initialisation
    /////////////////////////////////////////////////
    public Case phaseInitialisation(Plateau plateau) {
        return phaseInitialisationStatic(this, plateau);
    }

    /**
     *
     * @param joueurs
     * @param plateau
     * @return
     */
    public static Case phaseInitialisationStatic(JoueurIA joueurs, Plateau plateau) {
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

    public Case phaseInitialisationGourmande(Plateau plateau) {
        return phaseInitialisationGourmandeStatic(this, plateau);
    }

    /**
     *
     * @param joueurs
     * @param plateau
     * @return
     */
    public static Case phaseInitialisationGourmandeStatic(JoueurIA joueurs, Plateau plateau) {
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
        return phaseInitialisationMaxPossibiliteeStatic(this, plateau);
    }

    public static Case phaseInitialisationMaxPossibiliteeStatic(JoueurIA joueurs, Plateau plateau) {
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
        return JoueurIA.phaseJeuStatic(this, plateau);
    }

    public static Case phaseJeuStatic(JoueurIA joueur, Plateau plateau) {
        Random r = new Random();

        //Choix aléatoire d'un pinguin vivant
        Pinguin p = joueur.getPinguinsVivants().get(r.nextInt(joueur.getPinguinsVivants().size()));
        joueur.setPinguinCourant(p);

        //Choix aléatoire d'une case
        ArrayList<Case> casePossibles = p.getPosition().getCasePossibles();
        Case caseChoisie = casePossibles.get(r.nextInt(casePossibles.size()));

        return caseChoisie;
    }

    public Case phaseJeuElimination(Plateau plateau) {
        return JoueurIA.phaseJeuEliminationStatic(this, plateau);
    }

    public static Case phaseJeuEliminationStatic(JoueurIA joueur, Plateau plateau) {
        Case caseChoisie = joueur.chercherVictimeStatic(joueur, plateau);
        //Si elle ne peut tuer personne, alors elle joue aléatoirement
        if (caseChoisie == null) {
            Random r = new Random();

            //Choix aléatoire d'un pinguin vivant
            Pinguin p = joueur.getPinguinsVivants().get(r.nextInt(joueur.getPinguinsVivants().size()));

            joueur.setPinguinCourant(p);

            //Choix aléatoire d'une case
            ArrayList<Case> casePossibles = p.getPosition().getCasePossibles();
            caseChoisie = casePossibles.get(r.nextInt(casePossibles.size()));
        }
        return caseChoisie;
    }

    public Case phaseJeuGourmand(Plateau plateau) {
        return JoueurIA.phaseJeuGourmandStatic(this, plateau);
    }

    public static Case phaseJeuGourmandStatic(JoueurIA joueur, Plateau plateau) {
        Case caseChoisie = null;
        ArrayList<Case> casesAccessible = null;
        int nbPoissons = 3;
        while (nbPoissons > 0) {
            for (Pinguin p : joueur.getPinguinsVivants()) {
                casesAccessible = p.getPosition().getCasePossibles();
                for (Case caseCourante : casesAccessible) {
                    if (caseCourante.getNbPoissons() == nbPoissons) {
                        joueur.setPinguinCourant(p);
                        return caseCourante;
                    }
                }
            }
            nbPoissons--;
        }

        return caseChoisie;
    }

    public Case phaseJeuMaxPossibilitee(Plateau plateau) {
        return JoueurIA.phaseJeuMaxPossibiliteeStatic(this, plateau);
    }

    public static Case phaseJeuMaxPossibiliteeStatic(JoueurIA joueur, Plateau plateau) {
        Pinguin pCourant = null;
        ArrayList<Case> casesPossibles;
        Case caseChoisie = null;

        //Selection du pinguin qui a le moins de possibilitee de mouvement
        int min = Integer.MAX_VALUE;
        for (Pinguin p : joueur.getPinguinsVivants()) {
            casesPossibles = p.getPosition().getCasePossibles();
            if (casesPossibles.size() < min) {
                pCourant = p;
                min = casesPossibles.size();
            }
        }
        joueur.setPinguinCourant(pCourant);

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
        return JoueurIA.phaseJeuMeilleurCheminStatic(this, plateau);
    }

    public static Case phaseJeuMeilleurCheminStatic(JoueurIA joueur, Plateau plateau) {
        //Si il n'y a plus pinguin adverse sur l'iceberg
        //System.out.println("Sont seuls ?");
        joueur.setPinguinsSeuls(plateau);
        Boolean sontSeuls = joueur.pinguinsSontSeuls();

        Case caseChoisie = null;

        if (sontSeuls && !joueur.chemin.isEmpty()) {
            /*System.out.println("Taille pinguin vivants : " + super.getPinguinsVivants().size());
             System.out.println("chemin de longueur " + chemin.size() + " Pinnguin courant " + this.getPinguinCourant());
             this.afficherChemin();*/
            caseChoisie = joueur.chemin.remove(0);

        } else if (sontSeuls && joueur.chemin.isEmpty()) {
            Random r = new Random();

            Pinguin p = joueur.getPinguinsVivants().get(r.nextInt(joueur.getPinguinsVivants().size()));


            /*DessinateurTexte dt = new DessinateurTexte();
             System.out.println(this.getCouleur() + this.getNom() + Couleur.ANSI_RESET);
             dt.visite(plateau);*/
            joueur.setPinguinCourant(p);

            ArrayList<Case> iceberg = plateau.getCasesIceberg(p.getPosition());
            int tailleMaximale = iceberg.size();
            for (Case c : iceberg) {
                if (c.getPinguin() != null) {
                    tailleMaximale--;
                }
            }

            joueur.setChemin(plateau.getMeilleurChemin(p.getPosition(), new ArrayList<>(), tailleMaximale - (int) (tailleMaximale * 0.25)));

            /*System.out.println("Taille pinguin vivants : " + super.getPinguinsVivants().size());
             System.out.println("seul " + p.getPosition().getNumLigne() + "," + p.getPosition().getNumColonne());
             System.out.println("chemin de longueur " + chemin.size() + " Pinnguin courant " + this.getPinguinCourant() + "taille iceberg " + iceberg.size() + iceberg);
             this.afficherChemin();*/
            caseChoisie = joueur.getChemin().remove(0);
        }

        return caseChoisie;
    }

    public Case sauveQuiPeut(Plateau plateau) {
        return JoueurIA.sauveQuiPeutStatic(this, plateau);
    }

    public static Case sauveQuiPeutStatic(JoueurIA joueur, Plateau plateau) {
        Case caseChoisie = null;

        return caseChoisie;
    }

    public Case chercherVictime(Plateau plateau) {
        return JoueurIA.chercherVictimeStatic(this, plateau);
    }

    /**
     * Une case permettant de tuer un pinguin adverse ou null si une telle case
     * n'existe pas
     *
     * @param plateau : plateau de jeu
     * @return Case prochainement joue
     */
    public static Case chercherVictimeStatic(JoueurIA joueur, Plateau plateau) {
        Case caseCourante = null, caseResultat = null;
        ArrayList<Case> mouvementsPossibles;
        CaseCritique cc;
        Pinguin ennemi, PinguinResultat = null;

        //Pour toutes les cases du plateau
        for (int i = 0; i < plateau.getNbLignes(); i++) {
            for (int j = 0; j < plateau.getNbColonnes(); j++) {

                //Si la case contient un pinguin ennemi
                caseCourante = plateau.getCases()[i][j];
                if (!caseCourante.estCoulee() && caseCourante.getPinguin() != null && caseCourante.getPinguin().getGeneral() != joueur) {
                    ennemi = caseCourante.getPinguin();

                    //Si cette case n'a qu'un seul voisin
                    if (caseCourante.getNbVoisins() == 1) {
                        for (Pinguin p : joueur.getPinguinsVivants()) {
                            mouvementsPossibles = p.getPosition().getCasePossibles();
                            if (mouvementsPossibles.contains(ennemi.getPosition().getVoisinsEmerges().get(0))) {
                                PinguinResultat = p;
                                caseResultat = ennemi.getPosition().getVoisinsEmerges().get(0);
                            }
                        }

                        //Si on peut isoler un pinguin sur un ilot
                    } else if ((cc = joueur.estIlot(caseCourante, plateau)) != null) {
                        caseCourante.setCoulee(Boolean.TRUE);

                        int poidsIlot1 = plateau.getPoidsIceberg(cc.getIlot1()) / plateau.getNbJoueurIceberg(cc.getIlot1());
                        int poidsIlot2 = plateau.getPoidsIceberg(cc.getIlot2()) / plateau.getNbJoueurIceberg(cc.getIlot2());

                        if (poidsIlot1 > poidsIlot2 && cc.getIlot1().size() == 1) {
                            //for pour une valeur lol
                            for (Case c : cc.getIlot1()) {
                                if (!c.estCoulee() && c.getPinguin() != null && c.getPinguin().getGeneral() == joueur) {
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
                                if (!c.estCoulee() && c.getPinguin() != null && c.getPinguin().getGeneral() == joueur) {
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

        joueur.setPinguinCourant(PinguinResultat);
        return caseResultat;
    }

    public Case chercherVictimePremierDuNom(Plateau plateau) {
        return JoueurIA.chercherVictimePremierDuNomStatic(this, plateau);
    }

    /**
     *
     * @param plateau : plateau de jeu
     * @return Une case permettant de tuer un pinguin adverse ou null si une
     * telle case n'existe pas
     */
    public static Case chercherVictimePremierDuNomStatic(JoueurIA joueur, Plateau plateau) {
        Case caseCourante = null;
        ArrayList<Pinguin> pinguins = new ArrayList<>();

        //Recuperation de tous les pinguins adverse vivants qui n'ont qu'un seul mouvement possible
        for (int i = 0; i < plateau.getNbLignes(); i++) {
            for (int j = 0; j < plateau.getNbColonnes(); j++) {
                caseCourante = plateau.getCases()[i][j];
                if (!caseCourante.estCoulee() && caseCourante.getPinguin() != null && caseCourante.getNbVoisins() == 1 && caseCourante.getPinguin().getGeneral() != joueur) {
                    pinguins.add(caseCourante.getPinguin());
                }
            }
        }

        //On cherche une case permettant de bloquer un pinguin adverse
        ArrayList<Case> mouvementsPossibles;
        for (Pinguin p : joueur.getPinguinsVivants()) {
            mouvementsPossibles = p.getPosition().getCasePossibles();

            for (Pinguin ennemi : pinguins) {
                for (Case voisin : ennemi.getPosition().getVoisinsJouable()) {
                    if (mouvementsPossibles.contains(voisin)) {
                        joueur.setPinguinCourant(p);
                        return voisin;
                    }
                }
            }
        }

        return null;
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
        return JoueurIA.chercheIlotStatic(this, plateau);
    }

    public static Case chercheIlotStatic(JoueurIA joueur, Plateau plateau) {
        ArrayList<CaseCritique> ilotsPossibles = new ArrayList<>();
        ArrayList<Case> casesAccessibles, iceberg;
        int poidsIceberg, maxPoidsIceberg = -1;
        joueur.getIlotsPossibles(plateau, ilotsPossibles);
        Case c, caseCourante = null;

        //Pour tous les pinguins du joueur
        for (Pinguin p : joueur.getPinguinsVivants()) {
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
                        joueur.setPinguinCourant(p);
                        int poidsIlot1 = plateau.getPoidsIceberg(cc.getIlot1()) / plateau.getNbJoueurIceberg(cc.getIlot1());
                        int poidsIlot2 = plateau.getPoidsIceberg(cc.getIlot2()) / plateau.getNbJoueurIceberg(cc.getIlot2());

                        if (poidsIlot1 > poidsIlot2) {
                            joueur.getChemin().add(cc.getIlot1().get(0));
                        } else {
                            joueur.getChemin().add(cc.getIlot2().get(0));
                        }
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
    public static void getIlotsPossibles(Plateau plateau, ArrayList<CaseCritique> ilotsPossibles) {
        Case caseCourante;

        for (int i = 0; i < plateau.getNbLignes(); i++) {
            for (int j = 0; j < plateau.getNbColonnes(); j++) {
                caseCourante = plateau.getCases()[i][j];
                CaseCritique c = JoueurIA.estIlot(caseCourante, plateau);
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
    public static CaseCritique estIlot(Case caseCourante, Plateau plateau) {
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
