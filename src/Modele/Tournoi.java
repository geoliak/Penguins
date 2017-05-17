/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import Modele.IA.JoueurIA;
import Controleur.CombatIA;
import Vue.DessinateurTexte;
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

    private HashMap<Joueur, Integer> nbPartieDeuxjoueurs;
    private HashMap<Joueur, Integer> nbPartieTroisjoueurs;
    private HashMap<Joueur, Integer> nbPartieQuatrejoueurs;

    private ArrayList<Composition> compoDeuxJoueurs;
    private ArrayList<Composition> compoTroisJoueurs;
    private ArrayList<Composition> compoQuatreJoueurs;

    private ArrayList<Joueur> afficheCroissantDeux;
    private ArrayList<Joueur> afficheCroissantTrois;
    private ArrayList<Joueur> afficheCroissantQuatre;

    public Tournoi(int nbMatch) {
        this.nbMatch = nbMatch;
        this.IA = new ArrayList<>();

        this.scoreDeuxjoueurs = new HashMap<>();
        this.scoreTroisJoueurs = new HashMap<>();
        this.scoreQuatrejoueurs = new HashMap<>();

        this.compoDeuxJoueurs = new ArrayList<>();
        this.compoTroisJoueurs = new ArrayList<>();
        this.compoQuatreJoueurs = new ArrayList<>();

        this.nbPartieDeuxjoueurs = new HashMap<>();
        this.nbPartieTroisjoueurs = new HashMap<>();
        this.nbPartieQuatrejoueurs = new HashMap<>();

        this.afficheCroissantDeux = new ArrayList<>();
        this.afficheCroissantTrois = new ArrayList<>();
        this.afficheCroissantQuatre = new ArrayList<>();
    }

    public void ajouterIA(Joueur ia) {
        this.IA.add(ia);
    }

    private void initToutesCompo() {
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

    private void initCompoDeuxJoueurs() {
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
            nbPartieDeuxjoueurs.put(ia, 0);
        }
    }

    private void initCompoTroisJoueurs() {
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
            nbPartieTroisjoueurs.put(ia, 0);
        }
    }

    private void initCompoQuatreJoueurs() {
        Composition c = new Composition();
        for (Joueur ia : this.IA) {
            for (Joueur ia2 : this.IA) {
                for (Joueur ia3 : this.IA) {
                    for (Joueur ia4 : this.IA) {
                        if (ia != ia2 && ia != ia3 && ia != ia4 && ia3 != ia2 && ia4 != ia2 && ia3 != ia2 && ia4 != ia3) {
                            c.ajouterJoueur(ia);
                            c.ajouterJoueur(ia2);
                            c.ajouterJoueur(ia3);
                            c.ajouterJoueur(ia4);
                            this.compoQuatreJoueurs.add(c);
                            c = new Composition();
                        }
                    }
                }
            }
            this.scoreQuatrejoueurs.put(ia, 0);
            nbPartieQuatrejoueurs.put(ia, 0);
        }
    }

    public void executerLesCombats(boolean deux, boolean trois, boolean quatre) {
        ArrayList<Composition> tousLesMatch = new ArrayList<>();
        if (deux) {
            this.initCompoDeuxJoueurs();
            tousLesMatch.addAll(this.compoDeuxJoueurs);
        }
        if (trois) {
            this.initCompoTroisJoueurs();
            tousLesMatch.addAll(this.compoTroisJoueurs);
        }
        if (quatre) {
            this.initCompoQuatreJoueurs();
            tousLesMatch.addAll(this.compoQuatreJoueurs);
        }
        int avancement = 0;
        int i = 0;
        System.out.println("Combats !");
        for (Composition c : tousLesMatch) {
            System.out.print("[");
            if (i >= tousLesMatch.size() / 10) {
                avancement++;
                i = 0;
            }
            for (int j = 0; j < avancement; j++) {
                System.out.print("#");
            }
            for (int j = avancement; j < 10; j++) {
                System.out.print(" ");
            }

            System.out.println("] " + (float) ((float) 100 * (avancement * (tousLesMatch.size() / 10) + i) / tousLesMatch.size()) + "%");

            for (Joueur j : c.getComposition()) {
                System.out.println(j);
            }
            System.out.println(" ");
            
            this.match(c);

            i++;
        }
    }

    public void match(Composition compo) {
        int nbPinguin = 0;
        HashMap<Joueur, Integer> score = null;
        HashMap<Joueur, Integer> nbPartie = null;
        ArrayList<Joueur> joueurs = compo.getComposition();
        switch (joueurs.size()) {
            case 2:
                score = this.scoreDeuxjoueurs;
                nbPartie = this.nbPartieDeuxjoueurs;
                nbPinguin = 4;
                break;
            case 3:
                score = this.scoreTroisJoueurs;
                nbPartie = this.nbPartieTroisjoueurs;
                nbPinguin = 3;
                break;
            case 4:
                score = this.scoreQuatrejoueurs;
                nbPartie = this.nbPartieQuatrejoueurs;
                nbPinguin = 2;
                break;
        }

        DessinateurTexte dt = new DessinateurTexte();

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

            //Placement des pinguins
            Boolean pinguinPlace;
            int numLigne, numColonne;
            Case caseChoisie;
            Joueur joueurCourant;

            for (int i = 0; i < nbPinguin * (joueurs.size()); i++) {
                joueurCourant = partie.getJoueurCourant();
                pinguinPlace = false;
                while (!pinguinPlace) {
                    caseChoisie = joueurCourant.etablirCoup(partie);
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
                        try {
                            joueurCourant.joueCoup(joueurCourant.etablirCoup(partie));
                        } catch (Exception e) {
                            plateau.accept(dt);
                            System.out.println("");
                        }
                        

                        aJoue = true;
                    } else {
                        aJoue = true;
                    }

                }
                partie.joueurSuivant();
            }

            compo.setVainceurs(partie.getJoueurGagnant());
            for (Joueur j : partie.getJoueurGagnant()) {

                score.put(j, score.get(j) + 1);

            }

            //partie.afficheResultats();
            for (Joueur j : joueurs) {
                nbPartie.put(j, nbPartie.get(j) + 1);
                j.reset();
            }
            partie.getJoueurCourant().reset();

            l++;
        }
    }

    public void afficheResultats(boolean deux, boolean trois, boolean quatre) {
        if (deux) {
            this.afficheResultatsDeuxJoueurs();
        }
        if (trois) {
            this.afficheResultatsTroisJoueurs();
        }
        if (quatre) {
            this.afficheResultatsQuatreJoueurs();
        }
    }

    private void afficheResultatsDeuxJoueurs() {
        Joueur joueurMaxDeux = null;

        while (afficheCroissantDeux.size() != this.IA.size()) {
            int maxDeux = Integer.MIN_VALUE;

            for (Joueur j : this.scoreDeuxjoueurs.keySet()) {
                if (!afficheCroissantDeux.contains(j) && this.scoreDeuxjoueurs.get(j) > maxDeux) {
                    maxDeux = this.scoreDeuxjoueurs.get(j);
                    joueurMaxDeux = j;
                }
            }
            afficheCroissantDeux.add(joueurMaxDeux);
        }

        System.out.println("\n===============================");
        System.out.println("============ 1 VS 1 ===========");
        System.out.println("===============================");
        for (Joueur j : afficheCroissantDeux) {
            System.out.println(j + " -> " + this.scoreDeuxjoueurs.get(j) + "\tvictoires (" + ((float) this.scoreDeuxjoueurs.get(j) / this.nbPartieDeuxjoueurs.get(j)) * 100 + " %)");
        }
    }

    public void afficheResultatsTroisJoueurs() {
        Joueur joueurMaxTrois = null;

        while (afficheCroissantTrois.size() != this.IA.size()) {
            int maxTrois = Integer.MIN_VALUE;

            for (Joueur j : this.scoreTroisJoueurs.keySet()) {
                if (!afficheCroissantTrois.contains(j) && this.scoreTroisJoueurs.get(j) > maxTrois) {
                    maxTrois = this.scoreTroisJoueurs.get(j);
                    joueurMaxTrois = j;
                }
            }
            afficheCroissantTrois.add(joueurMaxTrois);
        }
        System.out.println("\n===============================");
        System.out.println("========= 1 VS 1 VS 1 =========");
        System.out.println("===============================");
        for (Joueur j : afficheCroissantTrois) {
            System.out.println(j + " -> " + this.scoreTroisJoueurs.get(j) + "\tvictoires (" + ((float) this.scoreTroisJoueurs.get(j) / this.nbPartieTroisjoueurs.get(j)) * 100 + " %)");
        }
    }

    public void afficheResultatsQuatreJoueurs() {
        Joueur joueurMaxQuatre = null;

        while (afficheCroissantQuatre.size() != this.IA.size()) {
            int maxQuatre = Integer.MIN_VALUE;

            for (Joueur j : this.scoreQuatrejoueurs.keySet()) {
                if (!afficheCroissantQuatre.contains(j) && this.scoreQuatrejoueurs.get(j) > maxQuatre) {
                    maxQuatre = this.scoreQuatrejoueurs.get(j);
                    joueurMaxQuatre = j;
                }
            }
            afficheCroissantQuatre.add(joueurMaxQuatre);
        }
        System.out.println("\n===============================");
        System.out.println("======= 1 VS 1 VS 1 VS 1 ======");
        System.out.println("===============================");
        for (Joueur j : afficheCroissantQuatre) {
            System.out.println(j + " -> " + this.scoreQuatrejoueurs.get(j) + "\tvictoires (" + ((float) this.scoreQuatrejoueurs.get(j) / this.nbPartieQuatrejoueurs.get(j)) * 100 + " %)");
        }

    }

    public ArrayList<Joueur> getIA() {
        return IA;
    }

    public int getNbMatch() {
        return nbMatch;
    }

    public ArrayList<Joueur> getAfficheCroissantDeux() {
        return afficheCroissantDeux;
    }

    public ArrayList<Joueur> getAfficheCroissantTrois() {
        return afficheCroissantTrois;
    }

    public ArrayList<Joueur> getAfficheCroissantQuatre() {
        return afficheCroissantQuatre;
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

    public HashMap<Joueur, Integer> getNbPartieDeuxjoueurs() {
        return nbPartieDeuxjoueurs;
    }

    public void setNbPartieDeuxjoueurs(HashMap<Joueur, Integer> nbPartieDeuxjoueurs) {
        this.nbPartieDeuxjoueurs = nbPartieDeuxjoueurs;
    }

    public HashMap<Joueur, Integer> getNbPartieTroisjoueurs() {
        return nbPartieTroisjoueurs;
    }

    public void setNbPartieTroisjoueurs(HashMap<Joueur, Integer> nbPartieTroisjoueurs) {
        this.nbPartieTroisjoueurs = nbPartieTroisjoueurs;
    }

    public HashMap<Joueur, Integer> getNbPartieQuatrejoueurs() {
        return nbPartieQuatrejoueurs;
    }

    public void setNbPartieQuatrejoueurs(HashMap<Joueur, Integer> nbPartieQuatrejoueurs) {
        this.nbPartieQuatrejoueurs = nbPartieQuatrejoueurs;
    }

    public ArrayList<Composition> getCompoDeuxJoueurs() {
        return compoDeuxJoueurs;
    }

    public void setCompoDeuxJoueurs(ArrayList<Composition> compoDeuxJoueurs) {
        this.compoDeuxJoueurs = compoDeuxJoueurs;
    }

    public ArrayList<Composition> getCompoTroisJoueurs() {
        return compoTroisJoueurs;
    }

    public void setCompoTroisJoueurs(ArrayList<Composition> compoTroisJoueurs) {
        this.compoTroisJoueurs = compoTroisJoueurs;
    }

    public ArrayList<Composition> getCompoQuatreJoueurs() {
        return compoQuatreJoueurs;
    }

    public void setCompoQuatreJoueurs(ArrayList<Composition> compoQuatreJoueurs) {
        this.compoQuatreJoueurs = compoQuatreJoueurs;
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
