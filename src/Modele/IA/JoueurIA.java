/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA;

import Modele.Case;
import Modele.Couleur;
import Modele.IA.Methodes.GetIlotsPossibles;
import Modele.IA.Methodes.GetNbCasesCoulees;
import Modele.IA.Methodes.PhaseInitialisationGourmande;
import Modele.IA.Methodes.PhaseInitialisationMaxPossibilitee;
import Modele.Joueur;
import Modele.Partie;
import Modele.Pinguin;
import Modele.Plateau;
import Modele.TypeAutre.MyPair;
import Vue.DessinateurTexte;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author novelm
 */
public class JoueurIA extends Joueur {

    private ArrayList<Case> chemin;

    public JoueurIA(Couleur couleur, String nom, int numero) {
        super(couleur, numero);
        this.chemin = new ArrayList<>();
        this.setEstHumain(false);
        Random r = new Random();
        this.setNom(nom);
        this.setAge(r.nextInt(123)); // Jeanne Calment
    }

    public int nbCasesRestantes(Partie partie) {
        GetNbCasesCoulees methode = new GetNbCasesCoulees();
        partie.getPlateau().appliquerSurCases(methode);
        return methode.getNbCasesCoulees();
    }

    public Boolean estFinJeu(Partie partie) {
        return this.pinguinsSontSeuls();
    }

    public ArrayList<Pinguin> getPinguinNonIsole() {
        ArrayList<Pinguin> pinguins = new ArrayList<>();

        for (Pinguin p : this.getPinguinsVivants()) {
            if (!p.estSeul()) {
                pinguins.add(p);
            }
        }

        return pinguins;
    }

    @Override
    public void attendreCoup(Partie partie) {
        if (partie.isTourFini()) {
            // Initialisation
            if (partie.estEnInitialisation()) {

                if (!partie.getJoueurCourant().getEstHumain()) {
                    //Défini placement pingouin
                    partie.getJoueurCourant().ajouterPinguin(partie.getJoueurCourant().etablirCoup(partie));
                    partie.getPlateau().setEstModifié(true);
                    partie.joueurSuivant();
                }
                // Phase de jeu : Tour IA
            } else {
                if (!partie.getJoueurCourant().getEstHumain()) {
                    //System.out.println("TOUR IA =======================");
                    partie.getJoueurCourant().joueCoup(partie.getJoueurCourant().etablirCoup(partie));
                    System.out.println("COUP IA " + partie.getJoueurCourant().getPinguinCourant().getPosition().getNumLigne() + " " + partie.getJoueurCourant().getPinguinCourant().getPosition().getNumColonne());
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
                    //System.out.println("JOUEUR COURANT " + partie.getJoueurCourant());
                    //System.out.println("Fin tour IA");
                }
            }
        }

        DessinateurTexte d = new DessinateurTexte();
        partie.getPlateau().accept(d);
        System.out.println("\n\n");
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
     * @param partie : partie en cours
     * @return la case jouee par l'IA
     */
    @Override
    public Case etablirCoup(Partie partie) {
        Case caseChoisie = null;
        if (!super.getPret()) {
            caseChoisie = this.phaseInitialisation(partie);
            if (caseChoisie == null) {
                System.out.println("");
            }

        } else if (!this.getChemin().isEmpty()) {
            //System.out.println("Suit Chemin");

            caseChoisie = this.getChemin().remove(0);
            if (caseChoisie == null) {
                System.out.println("");
            }

        } else if (this.setPinguinsSeuls(partie) && this.getPinguinsVivants().size() >= 1) {
            caseChoisie = this.phaseJeuMeilleurChemin(partie);
            if (caseChoisie == null) {
                System.out.println("");
            }

        } else {
            caseChoisie = this.phaseJeu(partie);
            if (caseChoisie == null) {
                System.out.println("");
            }
        }
        return caseChoisie;
    }

    /////////////////////////////////////////////////
    //Methode utilisee dans la phase d'initialisation
    /////////////////////////////////////////////////
    public Case phaseInitialisation(Partie partie) {
        return phaseInitialisationStatic(this, partie);
    }

    /**
     * Cette methode va placer un pinguin au hasard pour le joueur joueur
     *
     * @param joueur : joueur voulant faire ce coup
     * @param partie : partie en cours
     * @return Case ou un pinguin doit ce placer
     */
    public static Case phaseInitialisationStatic(JoueurIA joueur, Partie partie) {
        Case caseChoisie;
        Random r = new Random();
        int i, j;
        do {
            i = r.nextInt(8);
            j = r.nextInt(8);
            caseChoisie = partie.getPlateau().getCases()[i][j];
        } while (caseChoisie.estCoulee() || caseChoisie.getPinguin() != null || caseChoisie.getNbPoissons() > 1);

        return caseChoisie;
    }

    public Case phaseInitialisationGourmande(Partie partie) {
        return phaseInitialisationGourmandeStatic(this, partie);
    }

    //WOLOLO
    public static Case phaseInitialisationGourmandeStatic(JoueurIA joueur, Partie partie) {
        //System.out.println("phaseInitialisationGourmandeStatic");
        int nbPoissons = 3;
        Random r = new Random();
        int debutligne = r.nextInt(partie.getPlateau().getNbLignes());
        int debutColonne = r.nextInt(partie.getPlateau().getNbColonnes());

        PhaseInitialisationGourmande methode = new PhaseInitialisationGourmande(nbPoissons);

        while (methode.getCaseChoisie() == null && nbPoissons > 0) {
            while (methode.getCaseChoisie() == null) {
                partie.getPlateau().appliquerSurCasesAvecDebut(debutligne, debutColonne, methode);
            }
            nbPoissons--;
        }

        return methode.getCaseChoisie();
    }

    public Case phaseInitialisationMaxPossibilitee(Partie partie) {
        return phaseInitialisationMaxPossibiliteeStatic(this, partie);
    }

    //WOLOLO
    public static Case phaseInitialisationMaxPossibiliteeStatic(JoueurIA joueur, Partie partie) {
        //System.out.println("phaseInitialisationMaxPossibiliteeStatic");
        /*Case caseCourante, CaseRes = null;
         int maxCasesAtteignable = -1;
         ArrayList<Case> casesAtteignable;
         for (int i = 0; i < partie.getPlateau().getNbLignes(); i++) {
         for (int j = 0; j < partie.getPlateau().getNbColonnes(); j++) {

         caseCourante = partie.getPlateau().getCases()[i][j];
         if (caseCourante != null && !caseCourante.estCoulee() && caseCourante.getPinguin() == null && caseCourante.getNbPoissons() == 1) {
         casesAtteignable = caseCourante.getCasePossibles();
         if (casesAtteignable.size() > maxCasesAtteignable) {
         maxCasesAtteignable = casesAtteignable.size();
         CaseRes = caseCourante;
         }
         }
         }
         }*/

        PhaseInitialisationMaxPossibilitee methode = new PhaseInitialisationMaxPossibilitee();
        partie.getPlateau().appliquerSurCases(methode);

        return methode.getCaseChoisie();
    }

    /////////////////////////////////////////////////
    //Methode utilisee dans la phase de jeu
    /////////////////////////////////////////////////
    public Case phaseJeu(Partie partie) {
        return JoueurIA.phaseJeuStatic(this, partie);
    }

    //WOLOLO
    public static Case phaseJeuStatic(JoueurIA joueur, Partie partie) {
        //System.out.println("jeuAleatoire");
        Random r = new Random();

        //Choix aléatoire d'un pinguin vivant
        Pinguin p = joueur.getPinguinNonIsole().get(r.nextInt(joueur.getPinguinNonIsole().size()));
        joueur.setPinguinCourant(p);

        //Choix aléatoire d'une case
        ArrayList<Case> casePossibles = p.getPosition().getCasePossibles();
        Case caseChoisie = casePossibles.get(r.nextInt(casePossibles.size()));

        return caseChoisie;
    }

    public Case phaseJeuElimination(Partie partie) {
        return JoueurIA.phaseJeuEliminationStatic(this, partie);
    }

    //WOLOLO
    public static Case phaseJeuEliminationStatic(JoueurIA joueur, Partie partie) {
        //System.out.println("phaseJeuElimination");
        Case caseChoisie = joueur.chercherVictimeStatic(joueur, partie);
        //Si elle ne peut tuer personne, alors elle joue aléatoirement
        if (caseChoisie == null) {
            Random r = new Random();

            //Choix aléatoire d'un pinguin vivant
            Pinguin p = joueur.getPinguinNonIsole().get(r.nextInt(joueur.getPinguinNonIsole().size()));

            joueur.setPinguinCourant(p);

            //Choix aléatoire d'une case
            ArrayList<Case> casePossibles = p.getPosition().getCasePossibles();
            caseChoisie = casePossibles.get(r.nextInt(casePossibles.size()));
        }
        return caseChoisie;
    }

    public Case phaseJeuGourmand(Partie partie) {
        return JoueurIA.phaseJeuGourmandStatic(this, partie);
    }

    //WOLOLO
    public static Case phaseJeuGourmandStatic(JoueurIA joueur, Partie partie) {
        //System.out.println("Gourmand");
        Case caseChoisie = null;
        ArrayList<Case> casesAccessible = null;
        int nbPoissons = 3;
        while (nbPoissons > 0) {
            //Pour tous les pinguins du joueur
            for (Pinguin p : joueur.getPinguinNonIsole()) {
                casesAccessible = p.getPosition().getCasePossibles();

                //Pour toutes les cases accessibles du pinguin etudie
                for (Case caseCourante : casesAccessible) {
                    if (caseCourante.getNbPoissons() == nbPoissons) {
                        joueur.setPinguinCourant(p);
                        return caseCourante;
                    }
                }
            }
            nbPoissons--;
        }

        if (caseChoisie == null) {
            System.out.println("");
        }

        return caseChoisie;
    }

    public Case phaseJeuMaxPossibilitee(Partie partie) {
        return JoueurIA.phaseJeuMaxPossibiliteeStatic(this, partie);
    }

    //WOLOLO
    public static Case phaseJeuMaxPossibiliteeStatic(JoueurIA joueur, Partie partie) {
        //System.out.println("MaxPossibilitees");
        Pinguin pCourant = null;
        ArrayList<Case> casesPossibles;
        Case caseChoisie = null;

        //Selection du pinguin qui a le moins de possibilitee de mouvement
        int min = Integer.MAX_VALUE;
        for (Pinguin p : joueur.getPinguinNonIsole()) {
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

    public Case phaseJeuMeilleurChemin(Partie partie) {
        return JoueurIA.phaseJeuMeilleurCheminStatic(this, partie);
    }

    //WOLOLO
    public static Case phaseJeuMeilleurCheminStatic(JoueurIA joueur, Partie partie) {
        //Si il n'y a plus pinguin adverse sur l'iceberg
        //System.out.println("phaseJeuMeilleurCheminStatic");
        ArrayList<Case> iceberg;
        int tailleMaximale;
        Case caseChoisie;
        Random r = new Random();

        Pinguin p = joueur.getPinguinsVivants().get(r.nextInt(joueur.getPinguinsVivants().size()));

        joueur.setPinguinCourant(p);

        iceberg = partie.getPlateau().getCasesIceberg(p.getPosition());
        tailleMaximale = iceberg.size();
        for (Case c : iceberg) {
            if (c.getPinguin() != null) {
                tailleMaximale--;
            }
        }

        //Methode1 75%  du meilleur chemin
        joueur.setChemin(partie.getPlateau().getMeilleurChemin(p.getPosition(), new ArrayList<>(), (int) Math.round(tailleMaximale * 0.70)));
        //Methode2 100% à 3sec max
        /*EtablirMeilleurChemin meilleurChemin = new EtablirMeilleurChemin(p.getPosition(), tailleMaximale, joueur);
         meilleurChemin.start();

         long startTime;

         startTime = System.nanoTime();
         while (meilleurChemin.getContinuer() && System.nanoTime() - startTime < 1E9) {
         //System.out.println(System.nanoTime() - startTime + "   " + "taille iceberg : " + tailleMaximale + " <> " + joueur.getChemin().size() + "    " + meilleurChemin.getContinuer());
         }
         meilleurChemin.stopThread();
         try {
         meilleurChemin.join();
         System.out.println("Deces " + (System.nanoTime() - startTime));
         } catch (InterruptedException ex) {
         Logger.getLogger(JoueurIA.class.getName()).log(Level.SEVERE, null, ex);
         }*/

        caseChoisie = joueur.getChemin().remove(0);
        if (caseChoisie == null) {
            System.out.println(" ");
        }

        /*System.out.println("Taille pinguin vivants : " + super.getPinguinsVivants().size());
         System.out.println("seul " + p.getPosition().getNumLigne() + "," + p.getPosition().getNumColonne());
         System.out.println("chemin de longueur " + chemin.size() + " Pinnguin courant " + this.getPinguinCourant() + "taille iceberg " + iceberg.size() + iceberg);
         *//*joueur.afficherChemin();
         caseChoisie = joueur.getChemin().get(0);
         if (joueur.getChemin().size() != tailleMaximale) {
         joueur.getChemin().removeAll(joueur.getChemin());
         joueur.setPinguinCourant(null);
         }*/

        return caseChoisie;
    }

    public Case sauveQuiPeutBasique(Partie partie) {
        return JoueurIA.sauveQuiPeutBasiqueStatic(this, partie);
    }

    //WOLOLO
    public static Case sauveQuiPeutBasiqueStatic(Joueur joueur, Partie partie) {
        Case caseChoisie = null;
        boolean risque = false;

        /* Récupère les pingouins menacés */
        ArrayList<Pinguin> pingouinsMenace = new ArrayList<>();

        for (Pinguin p : joueur.getPinguinsVivants()) {
            if (p.getPosition().getCasePossibles().size() == 1) {
                Case c = p.getPosition().getCasePossibles().get(0);
                for (Joueur j : partie.getJoueurs()) {
                    for (Pinguin p2 : j.getPinguinsVivants()) {
                        if (p2.getPosition().getCasePossibles().contains(c)) {
                            pingouinsMenace.add(p);
                        }
                    }
                }
            }
        }

        /* Si plusieurs pingouins menacés, choix du pingouin à sauver */
        return caseChoisie;
    }

    public Case chercherVictime(Partie partie) {
        return JoueurIA.chercherVictimeStatic(this, partie);
    }

    //A DEBUGGER
    //WOLOLO
    public static Case chercherVictimeStatic(Joueur joueur, Partie partie) {
        //System.out.println("chercherVictimeStatic");
        Case caseCourante, caseResultat = null;
        ArrayList<Case> mouvementsPossibles;
        CaseCritique cc;
        Pinguin ennemi, PinguinResultat = null;

        //Pour toutes les cases du plateau
        for (int i = 0; i < partie.getPlateau().getNbLignes(); i++) {
            for (int j = 0; j < partie.getPlateau().getNbColonnes(); j++) {

                //Si la case contient un pinguin ennemi
                caseCourante = partie.getPlateau().getCases()[i][j];
                if (!caseCourante.estCoulee() && caseCourante.getPinguin() != null && caseCourante.getPinguin().getGeneral() != joueur) {
                    ennemi = caseCourante.getPinguin();

                    //Si cette case n'a qu'un seul voisin
                    if (caseCourante.getNbVoisins() == 1) {
                        for (Pinguin p : ((JoueurIA) joueur).getPinguinNonIsole()) {
                            mouvementsPossibles = p.getPosition().getCasePossibles();
                            if (mouvementsPossibles.contains(ennemi.getPosition().getVoisinsEmerges().get(0))) {
                                PinguinResultat = p;
                                caseResultat = ennemi.getPosition().getVoisinsEmerges().get(0);
                            }
                        }

                        //Si on peut isoler un pinguin sur un ilot
                    } else if ((cc = JoueurIA.estIlot(caseCourante, partie.getPlateau())) != null) {
                        caseCourante.setCoulee(Boolean.TRUE);

                        int poidsIlot1 = partie.getPlateau().getPoidsIceberg(cc.getIlot1()) / partie.getPlateau().getNbJoueurIceberg(cc.getIlot1());
                        int poidsIlot2 = partie.getPlateau().getPoidsIceberg(cc.getIlot2()) / partie.getPlateau().getNbJoueurIceberg(cc.getIlot2());

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

    public Case chercherVictimeSimple(Partie partie) {
        return JoueurIA.chercherVictimeSimpleStatic(this, partie);
    }

    //WOLOLO
    public static Case chercherVictimeSimpleStatic(Joueur joueur, Partie partie) {
        //System.out.println("chercherVictimeSimpleStatic");
        Case caseCourante = null;
        ArrayList<Pinguin> victimes = new ArrayList<>();

        //Recuperation de tous les pinguins adverse vivants qui n'ont qu'un seul mouvement possible
        for (Joueur adversaire : partie.getAutresJoueurs(joueur)) {
            for (Pinguin p : adversaire.getPinguinsVivants()) {
                if (p.getPosition().getCasePossibles().size() == 1) {
                    victimes.add(p);
                }
            }
        }

        //On cherche une case permettant de bloquer un pinguin adverse
        ArrayList<Case> mouvementsPossibles;
        for (Pinguin p : ((JoueurIA) joueur).getPinguinNonIsole()) {
            mouvementsPossibles = p.getPosition().getCasePossibles();

            for (Pinguin ennemi : victimes) {
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
     * @param partie
     * @return
     */
    public Boolean setPinguinsSeuls(Partie partie) {
        for (Pinguin p : super.getPinguinsVivants()) {
            if (!p.estSeul() && p.estSeulIceberg(partie.getPlateau())) {
                p.setEstSeul(true);
            }
        }
        return this.pinguinsSontSeuls();
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

    public Case chercheIlot(Partie partie) {
        return JoueurIA.chercheIlotStatic(this, partie);
    }

    //WOLOLO
    public static Case chercheIlotStatic(JoueurIA joueur, Partie partie) {
        //System.out.println("chercheIlotStatic");
        ArrayList<Case> casesAccessibles, iceberg;
        int poidsIceberg, maxPoidsIceberg = -1;
        ArrayList<CaseCritique> ilotsPossibles = JoueurIA.getIlotsPossibles(partie.getPlateau());
        Case c, caseCourante = null;

        //Pour tous les pinguins du joueur
        for (Pinguin p : joueur.getPinguinNonIsole()) {
            casesAccessibles = p.getPosition().getCasePossibles();

            //System.out.println(joueur + " pinguin " + p);
            //System.out.println(ilotsPossibles.size() + " Case critique possibles");
            //On regarde si il peut former un ilot
            for (CaseCritique cc : ilotsPossibles) {
                //System.out.println(cc);

                c = cc.getCassure();
                if (casesAccessibles.contains(c)) {
                    //poids de l'iceberg total
                    iceberg = partie.getPlateau().getCasesIceberg(p.getPosition());
                    poidsIceberg = partie.getPlateau().getPoidsIceberg(iceberg);

                    //Poids de l'iceberg sans l'ilot
                    c.setCoulee(Boolean.TRUE);
                    iceberg = partie.getPlateau().getCasesIceberg(p.getPosition());

                    //Poid de l'ilot
                    poidsIceberg -= partie.getPlateau().getPoidsIceberg(iceberg);

                    if (maxPoidsIceberg < poidsIceberg) {
                        joueur.setPinguinCourant(p);
                        int poidsDELICEBERG = partie.getPlateau().getPoidsIceberg(partie.getPlateau().getCasesIceberg(cc.getIlot1().get(0)));
                        int poidsIlot1 = poidsDELICEBERG / partie.getPlateau().getNbJoueurIceberg(partie.getPlateau().getCasesIceberg(cc.getIlot1().get(0)));
                        poidsDELICEBERG = partie.getPlateau().getPoidsIceberg(partie.getPlateau().getCasesIceberg(cc.getIlot2().get(0)));
                        int poidsIlot2 = poidsDELICEBERG / partie.getPlateau().getNbJoueurIceberg(partie.getPlateau().getCasesIceberg(cc.getIlot2().get(0)));
                        //System.out.println("poids ilot1 : " + poidsIlot1 + "    poids ilot2 : " + poidsIlot2);

                        if (poidsIlot1 > poidsIlot2) {
                            joueur.getChemin().add(cc.getIlot1().get(0));
                        } else {
                            joueur.getChemin().add(cc.getIlot2().get(0));
                        }
                        caseCourante = c;
                        maxPoidsIceberg = poidsIceberg;
                    }
                    c.setCoulee(Boolean.FALSE);

                }
            }
        }
        return caseCourante;
    }

    //WOLOLO
    public static ArrayList<CaseCritique> getIlotsPossibles(Plateau plateau) {
        GetIlotsPossibles methode = new GetIlotsPossibles(plateau);
        plateau.appliquerSurCases(methode);
        return methode.getIlotsPossibles();

    }

    /**
     * Retourne true si la case est le seul lien entre deux banquises
     *
     * @param caseCourante : case étudiée
     * @param plateau : plateau de jeu
     * @return true si la suppression de cette case peut former un ilot
     */
    public static CaseCritique estIlot(Case caseCourante, Plateau plateau) {
        CaseCritique caseCritique = null;
        if (!caseCourante.estCoulee()) {
            Integer[][] dijkstra;
            caseCourante.setCoulee(true);

            if (caseCourante.getVoisinsEmerges().size() == 2) {
                dijkstra = plateau.Dijkstra(caseCourante.getVoisinsEmerges().get(0));
                if (dijkstra[caseCourante.getVoisinsEmerges().get(1).getNumLigne()][caseCourante.getVoisinsEmerges().get(1).getNumColonne()] == Integer.MAX_VALUE) {
                    caseCritique = new CaseCritique(caseCourante, caseCourante.getVoisinsEmerges(), dijkstra);
                }

            } else if (caseCourante.getVoisinsEmerges().size() == 3) {
                dijkstra = plateau.Dijkstra(caseCourante.getVoisinsEmerges().get(0));
                if (!(dijkstra[caseCourante.getVoisinsEmerges().get(1).getNumLigne()][caseCourante.getVoisinsEmerges().get(1).getNumColonne()] < Integer.MAX_VALUE && dijkstra[caseCourante.getVoisinsEmerges().get(2).getNumLigne()][caseCourante.getVoisinsEmerges().get(2).getNumColonne()] < Integer.MAX_VALUE)) {
                    caseCritique = new CaseCritique(caseCourante, caseCourante.getVoisinsEmerges(), dijkstra);
                }

            } else if (caseCourante.getVoisinsEmerges().size() == 4) {
                dijkstra = plateau.Dijkstra(caseCourante.getVoisinsEmerges().get(0));
                if (!(dijkstra[caseCourante.getVoisinsEmerges().get(1).getNumLigne()][caseCourante.getVoisinsEmerges().get(1).getNumColonne()] < Integer.MAX_VALUE && dijkstra[caseCourante.getVoisinsEmerges().get(2).getNumLigne()][caseCourante.getVoisinsEmerges().get(2).getNumColonne()] < Integer.MAX_VALUE && dijkstra[caseCourante.getVoisinsEmerges().get(3).getNumLigne()][caseCourante.getVoisinsEmerges().get(3).getNumColonne()] < Integer.MAX_VALUE)) {
                    caseCritique = new CaseCritique(caseCourante, caseCourante.getVoisinsEmerges(), dijkstra);
                }
            }

            caseCourante.setCoulee(false);
        }

        return caseCritique;
    }

    //WOLOLO
    public static Case minimax(Joueur joueur, Partie partie, boolean elagage) {
        ArrayList<Joueur> joueurs;
        Joueur adversaire;
        Case caseRep = null;
        HashMap<Joueur, ArrayList<Pinguin>> pinguinDeJoueurs;
        ArrayList<Case> iceberg;
        int tailleIceberg, profondeur = 20;

        for (Pinguin p : ((JoueurIA) joueur).getPinguinNonIsole()) {
            iceberg = partie.getPlateau().getCasesIceberg(p.getPosition());
            tailleIceberg = iceberg.size();
            if (partie.getPlateau().getNbJoueurIceberg(iceberg) == 2) {
                if (tailleIceberg < 20) {
                    profondeur = -1;
                } else if (tailleIceberg < 25) {
                    profondeur = 8;
                }else if (tailleIceberg < 30) {
                    profondeur = 7;
                } else if (tailleIceberg < 40) {
                    profondeur = 5;
                } else {
                    profondeur = 4;
                }
                System.out.println("profondeur "+ profondeur);

                joueurs = partie.getPlateau().getJoueursIceberg(iceberg);
                joueurs.remove(joueur);
                adversaire = joueurs.get(0);

                pinguinDeJoueurs = partie.getPlateau().getPinguinsIceberg(iceberg);

                Minimax minimax = new Minimax(partie.getPlateau(), pinguinDeJoueurs.get(joueur), pinguinDeJoueurs.get(adversaire));
                MyPair<Case, Pinguin> rep = null;
                if (elagage) {
                    rep = minimax.executeNegamaxElagage(profondeur);
                } else {
                    rep = minimax.executeNegamaxMultiThread(profondeur);
                }

                joueur.setPinguinCourant(rep.getR());
                return rep.getL();
            }
        }

        return null;
    }

    /////////////////////////////
    // GETTER SETTER AFFICHAGE
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

}
