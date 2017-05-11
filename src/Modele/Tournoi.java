/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import Controleur.CombatIA;
import Modele.IA.JoueurIA;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author novelm
 */
public class Tournoi {

    private int nbMatch;
    private ArrayList<Joueur> IA;

    private HashMap<Joueur, Integer> scoreDeuxjoueurs;
    private HashMap<Joueur, Integer> scoreTroisJoueurs;
    private HashMap<Joueur, Integer> scoreQuatrejoueurs;

    private ArrayList<Composition> compoDeuxJoueurs;
    private ArrayList<Composition> compoTroisJoueurs;
    private ArrayList<Composition> compoQuatreJoueurs;

    private HashMap<Joueur, Joueur> deuxJoueurs;
    private HashMap<Joueur, HashMap<Joueur, Joueur>> troisJoueurs;
    private HashMap<Joueur, HashMap<Joueur, HashMap<Joueur, Joueur>>> quatreJoueurs;

    public Tournoi(int nbMatch) {
        this.nbMatch = nbMatch;
        this.IA = new ArrayList<>();

        this.scoreDeuxjoueurs = new HashMap<>();
        this.scoreTroisJoueurs = new HashMap<>();
        this.scoreQuatrejoueurs = new HashMap<>();

        this.compoDeuxJoueurs = new ArrayList<>();
        this.compoTroisJoueurs = new ArrayList<>();
        this.compoQuatreJoueurs = new ArrayList<>();

        this.deuxJoueurs = new HashMap<>();
        this.troisJoueurs = new HashMap<>();
        this.quatreJoueurs = new HashMap<>();
    }

    public void ajouterIA(JoueurIA ia) {
        this.IA.add(ia);
    }

    public void initCompo() {
        if (this.IA.size() >= 2) {
            this.initCompoDeuxJoueurs();
            if (this.IA.size() >= 3) {
                this.initCompoTroisJoueurs();
                if (this.IA.size() >= 4) {
                    this.initCompoQuatreJoueurs();
                }
            }
        }
    }

    public void initCompoDeuxJoueurs() {
        Composition c = new Composition();
        for (Joueur ia : this.IA) {
            for (Joueur ia2 : this.IA) {
                if (ia != ia2) {
                    c.ajouterJoueur(ia);
                    c.ajouterJoueur(ia2);
                    this.compoDeuxJoueurs.add(c);
                    c = new Composition();
                }
            }
            this.scoreDeuxjoueurs.put(ia, 0);
        }
    }

    public void initCompoTroisJoueurs() {
        Composition c = new Composition();
        for (Joueur ia : this.IA) {
            for (Joueur ia2 : this.IA) {
                for (Joueur ia3 : this.IA) {
                    if (ia != ia2 && ia != ia3 && ia3 != ia2) {
                        c.ajouterJoueur(ia);
                        c.ajouterJoueur(ia2);
                        c.ajouterJoueur(ia3);
                        this.compoTroisJoueurs.add(c);
                        c = new Composition();
                    }
                }
            }
            this.scoreTroisJoueurs.put(ia, 0);
        }
    }

    public void initCompoQuatreJoueurs() {
        Composition c = new Composition();
        for (Joueur ia : this.IA) {
            for (Joueur ia2 : this.IA) {
                for (Joueur ia3 : this.IA) {
                    for (Joueur ia4 : this.IA) {
                        if (ia != ia2 && ia != ia3 && ia != ia4 && ia3 != ia2 && ia4 != ia2 && ia3 != ia2) {
                            c.ajouterJoueur(ia);
                            c.ajouterJoueur(ia2);
                            c.ajouterJoueur(ia3);
                            c.ajouterJoueur(ia4);
                            this.compoTroisJoueurs.add(c);
                            c = new Composition();
                        }
                    }
                }
            }
            this.scoreQuatrejoueurs.put(ia, 0);
        }
    }

    public void pourLAntartique() {
        ArrayList<Joueur> joueurs;
        ArrayList<Composition> tousLesMatch = new ArrayList<>();
        tousLesMatch.addAll(this.compoDeuxJoueurs);
        tousLesMatch.addAll(this.compoTroisJoueurs);
        tousLesMatch.addAll(this.compoQuatreJoueurs);
        for (Composition c : tousLesMatch) {
            this.match(c);
        }
    }

    public void match(Composition compo) {
        int nbPinguin = 0;
        HashMap<Joueur, Integer> score = null;
        ArrayList<Joueur> joueurs = compo.getComposition();
        switch (joueurs.size()) {
            case 2:
                score = this.scoreDeuxjoueurs;
                nbPinguin = 4;
                break;
            case 3:
                score = this.scoreTroisJoueurs;
                nbPinguin = 3;
                break;
            case 4:
                score = this.scoreQuatrejoueurs;
                nbPinguin = 2;
                break;
        }

        int l = 0;
        while (l < this.nbMatch) {
            Plateau plateau = null;
            try {
                plateau = new Plateau("ressources/plateaux/plateau1");
            } catch (IOException ex) {
                Logger.getLogger(CombatIA.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(-1);
            }

            Partie partie = new Partie(plateau, joueurs);

            //plateau.accept(dt);
            //Placement des pinguins
            Boolean pinguinPlace;
            int numLigne, numColonne;
            Case caseChoisie;
            Joueur joueurCourant;

            for (int i = 0; i < nbPinguin * (joueurs.size() + 1); i++) {
                joueurCourant = partie.getJoueurCourant();
                pinguinPlace = false;
                while (!pinguinPlace) {
                    caseChoisie = joueurCourant.etablirCoup(plateau);
                    numLigne = caseChoisie.getNumLigne();
                    numColonne = caseChoisie.getNumColonne();

                    if (plateau.estCaseLibre(numLigne, numColonne) && plateau.getCases()[numLigne][numColonne].getNbPoissons() == 1) {
                        joueurCourant.ajouterPinguin(plateau.getCases()[numLigne][numColonne]);
                        pinguinPlace = true;
                    } else {
                        System.out.println("Cette case est occupée ou coulé ou n'a pas un poisson");
                    }
                }
                partie.joueurSuivant();
            }
            partie.getJoueurCourant().setPret(true);
            for (Joueur j : joueurs) {
                j.setPret(true);
            }
            partie.setInitialisation(false);

            //Jeu
            boolean aJoue;
            ArrayList<Case> casesPossibles;
            while (!partie.estTerminee()) {
                aJoue = false;
                while (!aJoue) {
                    joueurCourant = partie.getJoueurCourant();

                    //On tue les pinguins qui n'ont plus de cases accessibles
                    for (Pinguin p : joueurCourant.getPinguinsVivants()) {
                        casesPossibles = p.getPosition().getCasePossibles();
                        if (casesPossibles.isEmpty()) {
                            p.coullePinguin();
                        }
                    }

                    //Si le joueur n'est pas elimine
                    if (joueurCourant.estEnJeu()) {
                        joueurCourant.joueCoup(joueurCourant.etablirCoup(plateau));
                        aJoue = true;
                    } else {
                        aJoue = true;
                    }

                }
                partie.joueurSuivant();
            }

            compo.setVainceurs(partie.getJoueurGagnant());
            for (Joueur j : partie.getJoueurGagnant()) {
                switch (joueurs.size()) {
                    case 2:
                        score.put(j, this.scoreDeuxjoueurs.get(j) + 1);
                        break;
                    case 3:
                        this.scoreTroisJoueurs.put(j, this.scoreTroisJoueurs.get(j) + 1);
                        break;
                    case 4:
                        this.scoreQuatrejoueurs.put(j, this.scoreQuatrejoueurs.get(j) + 1);
                        break;
                }
            }
            
            partie.afficheResultats();

            for (Joueur j : joueurs) {
                j.reset();
            }
            partie.getJoueurCourant().reset();

            l++;
        }
    }

    public void afficheResultats() {
        ArrayList<Joueur> afficheCroissantDeux = new ArrayList<>();
        ArrayList<Joueur> afficheCroissantTrois = new ArrayList<>();
        ArrayList<Joueur> afficheCroissantQuatre = new ArrayList<>();
        Joueur joueurMaxDeux = null;
        Joueur joueurMaxTrois = null;
        Joueur joueurMaxQuatre = null;

        while (afficheCroissantDeux.size() != this.IA.size()) {
            int maxDeux = Integer.MIN_VALUE;
            int maxTrois = Integer.MIN_VALUE;
            int maxQuatre = Integer.MIN_VALUE;
            for (Joueur j : this.scoreDeuxjoueurs.keySet()) {
                if (!afficheCroissantDeux.contains(j) && this.scoreTroisJoueurs.get(j) > maxDeux) {
                    maxDeux = this.scoreTroisJoueurs.get(j);
                    joueurMaxDeux = j;
                }
                if (!afficheCroissantTrois.contains(j) && this.scoreTroisJoueurs.get(j) > maxTrois) {
                    maxTrois = this.scoreTroisJoueurs.get(j);
                    joueurMaxTrois = j;
                }
                if (!afficheCroissantQuatre.contains(j) && this.scoreQuatrejoueurs.get(j) > maxQuatre) {
                    maxQuatre = this.scoreQuatrejoueurs.get(j);
                    joueurMaxQuatre = j;
                }
            }
            afficheCroissantDeux.add(joueurMaxDeux);
            afficheCroissantTrois.add(joueurMaxTrois);
            afficheCroissantQuatre.add(joueurMaxQuatre);
        }

        System.out.println("\n===============================");
        System.out.println("============ 1 VS 1 ===========");
        System.out.println("===============================");
        for (Joueur j : afficheCroissantDeux) {
            System.out.println(j + " -> " + this.scoreDeuxjoueurs.get(j) + "\tvictoires ");
        }
        System.out.println("\n===============================");
        System.out.println("========= 1 VS 1 VS 1 =========");
        System.out.println("===============================");
        for (Joueur j : afficheCroissantTrois) {
            System.out.println(j + " -> " + this.scoreTroisJoueurs.get(j) + "\tvictoires ");
        }
        System.out.println("\n===============================");
        System.out.println("======= 1 VS 1 VS 1 VS 1 ======");
        System.out.println("===============================");
        for (Joueur j : afficheCroissantQuatre) {
            System.out.println(j + " -> " + this.scoreQuatrejoueurs.get(j) + "\tvictoires ");
        }

    }

    public ArrayList<Joueur> getIA() {
        return IA;
    }

    public HashMap<Joueur, Joueur> getDeuxJoueurs() {
        return deuxJoueurs;
    }

    public HashMap<Joueur, HashMap<Joueur, Joueur>> getTroisJoueurs() {
        return troisJoueurs;
    }

    public HashMap<Joueur, HashMap<Joueur, HashMap<Joueur, Joueur>>> getQuatreJoueurs() {
        return quatreJoueurs;
    }

    public int getNbMatch() {
        return nbMatch;
    }

    public HashMap<Joueur, Integer> getScoreDeuxjoueurs() {
        return scoreDeuxjoueurs;
    }

    public HashMap<Joueur, Integer> getScoreTroisJoueurs() {
        return scoreTroisJoueurs;
    }

    public HashMap<Joueur, Integer> getScoreQuatrejoueurs() {
        return scoreQuatrejoueurs;
    }

    public class Composition {

        private ArrayList<Joueur> composition;
        private ArrayList<Joueur> vainceurs;

        public Composition() {
            this.composition = new ArrayList<>();
            this.vainceurs = new ArrayList<>();
        }

        public void ajouterJoueur(Joueur j) {
            composition.add(j);
        }

        public ArrayList<Joueur> getComposition() {
            return composition;
        }

        public void setComposition(ArrayList<Joueur> composition) {
            this.composition = composition;
        }

        public ArrayList<Joueur> getVainceurs() {
            return vainceurs;
        }

        public void setVainceurs(ArrayList<Joueur> vainceurs) {
            this.vainceurs = vainceurs;
        }
    }

}
