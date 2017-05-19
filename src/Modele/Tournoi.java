/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import Modele.IA.JoueurIA;
import Controleur.CombatIA;
import Vue.DessinateurTexte;
import com.panayotis.gnuplot.JavaPlot;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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

            c.getComposition().stream().forEach((j) -> {
                System.out.println(j);
            });
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
            Joueur joueurCourant = null;

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
                //System.out.print(joueurCourant + " -> ");
                aJoue = false;
                while (!aJoue) {
                    //System.out.println(" vas Jouer");
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
                        //try {
                        joueurCourant.joueCoup(joueurCourant.etablirCoup(partie));
                        /* } catch (Exception e) {
                         System.out.println(joueurCourant);
                         plateau.accept(dt);      
                         }*/

                        aJoue = true;
                    } else {
                        aJoue = true;
                    }

                }
                //System.out.print("Je change de joueur " + joueurCourant);
                partie.joueurSuivant();
                //System.out.println(" - OK");
            }

            for (Joueur j : partie.getJoueurGagnant()) {
                compo.ajouterVictoire(j);
                score.put(j, score.get(j) + 1);

            }

            partie.afficheResultats();
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

    public void sortirResultat() {
        String dossier = "IA";
        if (!new File("IA").exists() && !new File("IA").mkdir()) {
            System.out.println("Erreur de creation du fichier");
            System.exit(0);
        }

        if (!this.compoDeuxJoueurs.isEmpty()) {
            reunirCompositions(this.compoDeuxJoueurs);
        }

        if (!this.compoTroisJoueurs.isEmpty()) {
            reunirCompositions(this.compoTroisJoueurs);
        }

        if (!this.compoQuatreJoueurs.isEmpty()) {
            reunirCompositions(this.compoQuatreJoueurs);
        }

    }

    private void reunirCompositions(ArrayList<Composition> compositions) {
        ArrayList<ArrayList<Composition>> compoReuni = new ArrayList<>();
        boolean present;
        for (Composition compo : compositions) {
            if (compo.getNbCouplage() < compo.composition.size()) {
                for (Composition compo2 : compositions) {
                    //Si la composition n'a pas deja ete reuni
                    if (compo.getComposition().containsAll(compo2.getComposition())) {
                        for (Joueur j : compo2.getComposition()) {
                            compo.getScores().put(j, compo.getScores().get(j) + compo2.getScores().get(j));
                        }
                        compo.effectueCouplage();
                        compo2.effectueCouplage();
                    }
                }
            }
        }
    }

    private void sortirCompositions(String dossier, ArrayList<Composition> compositions) throws IOException {
        dossier = dossier + "\\" + compositions.get(0).getComposition().size() + "-Joueurs";
        File resultats = new File(dossier);
        if (resultats.exists()) {
            if (!resultats.delete()) {
                System.out.println("Erreur de la suppression du fichier IA\\" + compositions.get(0).getComposition().size() + "-Joueurs");
                System.exit(0);
            }
        }
        if (!resultats.mkdirs()) {
            System.out.println("Erreur de la creation du fichier IA\\" + compositions.get(0).getComposition().size() + "-Joueurs");
            System.exit(0);
        }

        StringBuilder sb = new StringBuilder();
        File fichierJoueur;
        BufferedWriter out;
        for (Joueur joueur : this.IA) {
            fichierJoueur = new File(dossier + "/" + joueur.getNom());
            if (!fichierJoueur.createNewFile()) {
                System.out.println("Erreur de creation du fichier " + dossier + "/" + joueur.getNom());
                System.exit(0);
            }
            out = new BufferedWriter(new FileWriter(fichierJoueur));
            
            sb.append(joueur.getNom()).append("\n");
            
            for (Composition compo : compositions) {
                 if (compo.getComposition().contains(joueur)) {
                     
                     for (Joueur joueurCompo : compo.getComposition()) {
                         if (joueur != joueurCompo) {
                             sb.append(compo.getScores().get(joueurCompo)).append("\t-> ").append(joueurCompo).append("\n");
                         }
                     }
                 }
            }
            out.write(sb.toString(), 0, sb.length());            
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
        private int nbMatch;
        private HashMap<Joueur, Integer> scores;
        private int nbCouplage;

        public Composition() {
            this.nbCouplage = 0;
            this.composition = new ArrayList<>();
            this.scores = new HashMap<>();
        }

        public void ajouterVictoire(Joueur j) {
            if (!this.scores.containsKey(j)) {
                this.scores.put(j, 0);
            }
            this.scores.put(j, this.scores.get(j));
            this.nbMatch++;
        }

        public void ajouterJoueur(Joueur j) {
            composition.add(j);
        }

        public void effectueCouplage() {
            this.nbCouplage++;
        }

        public int getNbCouplage() {
            return this.nbCouplage;
        }

        public int getNbMatch() {
            return nbMatch;
        }

        public HashMap<Joueur, Integer> getScores() {
            return scores;
        }

        public ArrayList<Joueur> getComposition() {
            return composition;
        }

        public void setComposition(ArrayList<Joueur> composition) {
            this.composition = composition;
        }
    }

}
