/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA;

import Modele.Case;
import Modele.Couleur;
import Modele.IA.Methodes.GetEvaluationPlateau;
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
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public Boolean estSeul(Partie partie) {
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
        //System.out.print("etablirCoup ");
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
        //System.out.println("- Ok");
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

    public Case phaseJeuGourmand(Partie partie) {
        return JoueurIA.phaseJeuGourmandStatic(this, partie);
    }

    //WOLOLO
    public static Case phaseJeuGourmandStatic(JoueurIA joueur, Partie partie) {
        //System.out.println("Gourmand");
        Case caseChoisie = null;
        ArrayList<Case> casesAccessible = null;
        int nbPoissons = 3;
        int i = 0;
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
                    i++;
                }
            }

            nbPoissons--;
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
        //System.out.print("phaseJeuMeilleurCheminStatic ");
        ArrayList<Case> iceberg;
        int tailleMaximale;
        Case caseChoisie = null;
        Random r = new Random();

        Pinguin p = joueur.getPinguinsVivants().get(r.nextInt(joueur.getPinguinsVivants().size()));

        joueur.setPinguinCourant(p);

        iceberg = partie.getPlateau().getCasesIcebergSansCassures(p.getPosition());
        tailleMaximale = iceberg.size();
        for (Case c : iceberg) {
            if (c.getPinguin() != null) {
                tailleMaximale--;
            }
        }

        //Methode1 70%  du meilleur chemin
        joueur.setChemin(partie.getPlateau().getMeilleurChemin(p.getPosition(), new ArrayList<>(), (int) Math.round(tailleMaximale * 0.10) + 1));

//Methode2 100% à 3sec max
        /*EtablirMeilleurChemin meilleurChemin = new EtablirMeilleurChemin(p.getPosition(), tailleMaximale, joueur);
         meilleurChemin.start();

         long startTime;

         startTime = System.nanoTime();
         while (meilleurChemin.getContinuer() && System.nanoTime() - startTime < 1E7) {
         //System.out.println(System.nanoTime() - startTime + "   " + "taille iceberg : " + tailleMaximale + " <> " + joueur.getChemin().size() + "    " + meilleurChemin.getContinuer());
         }
         meilleurChemin.stopThread();
         try {
         meilleurChemin.join();
         //System.out.println("Deces " + (System.nanoTime() - startTime));
         } catch (InterruptedException ex) {
         Logger.getLogger(JoueurIA.class.getName()).log(Level.SEVERE, null, ex);
         }*/
        try {
            caseChoisie = joueur.getChemin().remove(0);
        } catch (Exception e) {
            DessinateurTexte dt = new DessinateurTexte();
            partie.getPlateau().accept(dt);
            System.out.println("");
        }
        /*
         if (joueur.getChemin().size() != tailleMaximale - 1) {
         joueur.getChemin().removeAll(joueur.getChemin());
         }
         */

        /*System.out.println("Taille pinguin vivants : " + super.getPinguinsVivants().size());
         System.out.println("seul " + p.getPosition().getNumLigne() + "," + p.getPosition().getNumColonne());
         System.out.println("chemin de longueur " + chemin.size() + " Pinnguin courant " + this.getPinguinCourant() + "taille iceberg " + iceberg.size() + iceberg);
         *//*joueur.afficherChemin();
         caseChoisie = joueur.getChemin().get(0);
         if (joueur.getChemin().size() != tailleMaximale) {
         joueur.getChemin().removeAll(joueur.getChemin());
         joueur.setPinguinCourant(null);
         }*/

        //System.out.println(" - Ok");
        return caseChoisie;
    }

    public Case sauveQuiPeutBasique(Partie partie) {
        return JoueurIA.sauveQuiPeutBasiqueStatic(this, partie);
    }

    //WOLOLO
    public static Case sauveQuiPeutBasiqueStatic(Joueur joueur, Partie partie) {
        //System.out.println("sauveQuiPeutBasiqueStatic");
        Case caseChoisie = null;
        boolean risque = false;

        /* Récupère les pingouins menacés */
        ArrayList<Pinguin> pingouinsMenace = new ArrayList<>();

        for (Pinguin p : joueur.getPinguinNonIsole()) {
            if (p.getPosition().getNbVoisins() == 1) {
                Case c = p.getPosition().getCasePossibles().get(0);

                for (Joueur adversaire : partie.getAutresJoueurs(joueur)) {

                    for (Pinguin p2 : adversaire.getPinguinNonIsole()) {
                        if (p2.getPosition().getCasePossibles().contains(c)) {
                            pingouinsMenace.add(p);
                        }
                    }
                }
            }
        }

        if (!pingouinsMenace.isEmpty()) {
            /* Si plusieurs pingouins menacés, choix du pingouin à sauver */
            Pinguin pinguinRes = null;
            int max = Integer.MIN_VALUE;
            int poidsCourant;
            for (Pinguin p : pingouinsMenace) {
                poidsCourant = partie.getPlateau().getPoidsIceberg(partie.getPlateau().getCasesIceberg(p.getPosition())) / partie.getPlateau().getNbJoueurIceberg(partie.getPlateau().getCasesIceberg(p.getPosition()));
                if (poidsCourant > max) {
                    max = poidsCourant;
                    pinguinRes = p;
                }
            }

            max = Integer.MIN_VALUE;
            for (Case c : pinguinRes.getPosition().getCasePossibles()) {
                if (c.getNbVoisins() > max) {
                    caseChoisie = c;
                } else if (c.getNbVoisins() == max && c.getNbPoissons() > caseChoisie.getNbPoissons()) {
                    caseChoisie = c;
                }
            }
            joueur.setPinguinCourant(pinguinRes);
        }

        return caseChoisie;
    }

    public Case chercherVictimeIlot(Partie partie) {
        return JoueurIA.chercherVictimeIlotStatic(this, partie);
    }

    //A DEBUGGER
    //WOLOLO
    public static Case chercherVictimeIlotStatic(Joueur joueur, Partie partie) {
        //System.out.println("chercherVictimeIlotStatic");
        Case caseCourante = null;
        CaseCritique cc;

        for (Joueur j : partie.getAutresJoueurs(joueur)) {
            for (Pinguin ennemi : j.getPinguinNonIsole()) {

                //Si on peut isoler un pinguin sur un ilot
                if ((cc = JoueurIA.estIlot(ennemi.getPosition(), partie.getPlateau())) != null) {
                    ennemi.getPosition().setCoulee(Boolean.TRUE);

                    int poidsIlot1 = partie.getPlateau().getPoidsIceberg(partie.getPlateau().getCasesIceberg(cc.getIlot1().get(0))) / partie.getPlateau().getNbJoueurIceberg(partie.getPlateau().getCasesIceberg(cc.getIlot1().get(0)));
                    int poidsIlot2 = partie.getPlateau().getPoidsIceberg(partie.getPlateau().getCasesIceberg(cc.getIlot2().get(0))) / partie.getPlateau().getNbJoueurIceberg(partie.getPlateau().getCasesIceberg(cc.getIlot2().get(0)));

                    if (poidsIlot1 > poidsIlot2 && cc.getIlot1().size() == 1) {
                        Case c = cc.getIlot1().get(0);
                        if (c.getNbVoisins() >= 2) {
                            for (Pinguin p : joueur.getPinguinNonIsole()) {
                                if (p.getPosition().getCasePossibles().contains(c)) {
                                    joueur.setPinguinCourant(p);
                                    ennemi.getPosition().setCoulee(Boolean.FALSE);
                                    return c;
                                }
                            }
                        }

                    } else if (poidsIlot1 < poidsIlot2 && cc.getIlot2().size() == 1) {
                        Case c = cc.getIlot2().get(0);
                        if (c.getNbVoisins() >= 2) {
                            for (Pinguin p : joueur.getPinguinNonIsole()) {
                                if (p.getPosition().getCasePossibles().contains(c)) {
                                    joueur.setPinguinCourant(p);
                                    ennemi.getPosition().setCoulee(Boolean.FALSE);
                                    return c;
                                }
                            }
                        }
                    }

                    ennemi.getPosition().setCoulee(Boolean.FALSE);
                }

            }
        }

        return null;
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
        //System.out.print("setPinguinsSeuls");
        for (Pinguin p : super.getPinguinsVivants()) {
            if (!p.estSeul() && partie.getPlateau().getNbJoueurIceberg(partie.getPlateau().getCasesIceberg(p.getPosition())) == 1) {
                p.setEstSeul(true);
            }
        }
        //System.out.println(" - OK");
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
                        int poidsIlot1 = Plateau.getPoidsIceberg(Plateau.getCasesIceberg(cc.getIlot1().get(0))) / Plateau.getNbJoueurIceberg(Plateau.getCasesIceberg(cc.getIlot1().get(0)));
                        int poidsIlot2 = Plateau.getPoidsIceberg(Plateau.getCasesIceberg(cc.getIlot2().get(0))) / Plateau.getNbJoueurIceberg(Plateau.getCasesIceberg(cc.getIlot2().get(0)));
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
            caseCourante.setCoulee(true);

            if (caseCourante.getVoisinsEmerges().size() == 2) {
                if (!plateau.existeChemin(caseCourante.getVoisinsEmerges().get(0), caseCourante.getVoisinsEmerges().get(1), 2)) {
                    caseCritique = new CaseCritique(caseCourante, caseCourante.getVoisinsEmerges(), plateau);
                }

            } else if (caseCourante.getVoisinsEmerges().size() == 3) {
                if (!(plateau.existeChemin(caseCourante.getVoisinsEmerges().get(0), caseCourante.getVoisinsEmerges().get(1), 2) && plateau.existeChemin(caseCourante.getVoisinsEmerges().get(0), caseCourante.getVoisinsEmerges().get(2), 2))) {
                    caseCritique = new CaseCritique(caseCourante, caseCourante.getVoisinsEmerges(), plateau);
                }

            } else if (caseCourante.getVoisinsEmerges().size() == 4) {
                if (!(plateau.existeChemin(caseCourante.getVoisinsEmerges().get(0), caseCourante.getVoisinsEmerges().get(1), 2) && plateau.existeChemin(caseCourante.getVoisinsEmerges().get(0), caseCourante.getVoisinsEmerges().get(2), 2) && plateau.existeChemin(caseCourante.getVoisinsEmerges().get(0), caseCourante.getVoisinsEmerges().get(3), 2))) {
                    caseCritique = new CaseCritique(caseCourante, caseCourante.getVoisinsEmerges(), plateau);
                }
            }

            caseCourante.setCoulee(false);
        }

        return caseCritique;
    }

    /**
     * Retourne true si la case est le seul lien entre deux banquises
     *
     * @param caseCourante : case étudiée
     * @param plateau : plateau de jeu
     * @return true si la suppression de cette case peut former un ilot
     */
    /*public static CaseCritique estIlotObsolete(Case caseCourante, Plateau plateau) {
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
     }*/
    public static int evaluationEtat(Joueur joueur, Plateau plateau) {
        GetEvaluationPlateau methode = new GetEvaluationPlateau(joueur);
        plateau.appliquerSurCases(methode);
        return methode.getEvaluation();
    }

    public static int evaluationEtatV2(ArrayList<Pinguin> pinguinsJoueur, Plateau plateau) {
        int eval = 0;
        ArrayList<Case> iceberg;
        for (Pinguin p : pinguinsJoueur) {
            iceberg = Plateau.getCasesIceberg(p.getPosition());
            if (Plateau.getNbJoueurIceberg(iceberg) == 1) {
                eval += Plateau.getPoidsIceberg(iceberg) / Plateau.getNbPinguinIceberg(iceberg);
            } else {
                eval += Plateau.getPoidsIceberg(iceberg) / (Plateau.getNbPinguinIceberg(iceberg));
            }
        }
        return eval;
    }

    //WOLOLO
    public static Case minimax(Joueur joueur, Partie partie) {
        //System.out.println("Minimax ");
        ArrayList<Joueur> joueurs;
        Joueur adversaire;
        HashMap<Joueur, ArrayList<Pinguin>> pinguinDeJoueurs;
        ArrayList<Case> iceberg;
        int tailleIceberg, profondeur;

        for (Pinguin p : ((JoueurIA) joueur).getPinguinNonIsole()) {
            iceberg = partie.getPlateau().getCasesIceberg(p.getPosition());
            tailleIceberg = iceberg.size();

            if (Plateau.getNbJoueurIceberg(iceberg) == 2) {
                if (tailleIceberg <= 17) {
                    profondeur = 17;
                } else if (tailleIceberg < 25) {
                    profondeur = 6;
                } else if (tailleIceberg < 30) {
                    profondeur = 5;
                } else if (tailleIceberg < 40) {
                    profondeur = 4;
                } else {
                    profondeur = 3;
                }
                //System.out.print("profondeur " + profondeur);

                joueurs = Plateau.getJoueursIceberg(iceberg);
                joueurs.remove(joueur);
                adversaire = joueurs.get(0);

                pinguinDeJoueurs = Plateau.getPinguinsIceberg(iceberg);

                Minimax minimax = new Minimax(partie, pinguinDeJoueurs.get(joueur), pinguinDeJoueurs.get(adversaire));
                MyPair<Case, Pinguin> rep = minimax.executeNegamaxMultiThread(profondeur);

                joueur.setPinguinCourant(rep.getR());
                //System.out.println(" - OK");
                return rep.getL();
            }
        }
//System.out.println(" - OK");
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

    @Override
    public String getSpecialitees() {
        return this.getNom() + "Joueur IA";
    }

}
