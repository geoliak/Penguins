/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA;

import Modele.Case;
import Modele.Joueur;
import Modele.Pinguin;
import Modele.Plateau;
import Modele.TypeAutre.MyPair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author novelm
 */
public class Minimax {

    private Joueur joueurCourant;
    private Joueur joueurAdverse;

    private Plateau plateau;

    private ArrayList<Pinguin> pinguinsJoueur;
    private ArrayList<Pinguin> pinguinsAdverses;

    public Minimax(Plateau plateau, ArrayList<Pinguin> pinguinsJoueur, ArrayList<Pinguin> pinguinsAdverses) {
	this.joueurCourant = pinguinsJoueur.get(0).getGeneral();
	this.joueurAdverse = pinguinsAdverses.get(0).getGeneral();

	this.plateau = plateau;

	this.pinguinsJoueur = pinguinsJoueur;
	this.pinguinsAdverses = pinguinsAdverses;
    }

    /*
     minimax(p) = f(p) si p est une feuille de l’arbre où f est une fonction d’évaluation de la position du jeu
     minimax(p) = MAX(minimax( O 1 {\displaystyle O_{1}} O_{1}), …, minimax( O n {\displaystyle O_{n}} O_{n})) si p est un nœud Joueur avec fils O 1 {\displaystyle O_{1}} O_{1}, …, O n {\displaystyle O_{n}} O_{n}
     minimax(p) = MIN(minimax( O 1 {\displaystyle O_{1}} O_{1}), …, minimax( O n {\displaystyle O_{n}} O_{n})) si p est un nœud Opposant avec fils O 1 {\displaystyle O_{1}} O_{1}, …, O n {\displaystyle O_{n}} O_{n}
     */
    public MyPair<Case, Pinguin> execute(int profondeur) {
	Case caseRes = null;
	Pinguin pinguinRep = null;
	int poidsCourant;
	Case anciennePositionPinguin = null;
	int max = Integer.MIN_VALUE;
	ArrayList<Case> movementPossibles;
	HashMap<Pinguin, Integer> debug = new HashMap<>();

	for (Pinguin p : pinguinsJoueur) {
	    debug.put(p, 0);
	    movementPossibles = p.getPosition().getCasePossibles();
	    anciennePositionPinguin = p.getPosition();
	    anciennePositionPinguin.setPinguin(null);
	    anciennePositionPinguin.setCoulee(true);

	    for (Case c : movementPossibles) {
		debug.put(p, debug.get(p) + 1);
		p.setPosition(c);
		c.setPinguin(p);

		poidsCourant = minimaxWorker(1, (ArrayList<Pinguin>) pinguinsJoueur.clone(), (ArrayList<Pinguin>) pinguinsAdverses.clone(), profondeur) + c.getNbPoissons();

		if (poidsCourant > max) {
		    max = poidsCourant;
		    pinguinRep = p;
		    caseRes = c;
		}

		c.setPinguin(null);

	    }
	    p.setPosition(anciennePositionPinguin);
	    anciennePositionPinguin.setCoulee(false);
	    anciennePositionPinguin.setPinguin(p);
	}

	MyPair<Case, Pinguin> rep = new MyPair(caseRes, pinguinRep);

	return rep;
    }

    public void affichePositionPinguin() {
	for (Case[] cs : this.plateau.getCases()) {
	    for (Case c : cs) {
		if (c.getPinguin() != null) {
		    System.out.println(c.getPinguin());
		}
	    }
	}
    }

    public MyPair<Case, Pinguin> executeMultiThread(int profondeur) {
	Case caseRes = null;
	Pinguin pinguinRep = null;
	Case anciennePositionPinguin = null;
	int max = -1;
	CalculBranche cb;
	ArrayList<CalculBranche> cbs = new ArrayList<>();
	ArrayList<Case> movementPossibles;
	for (Pinguin p : pinguinsJoueur) {
	    movementPossibles = p.getPosition().getCasePossibles();
	    anciennePositionPinguin = p.getPosition();
	    anciennePositionPinguin.setPinguin(null);
	    anciennePositionPinguin.setCoulee(true);

	    for (Case c : movementPossibles) {
		p.setPosition(c);
		c.setPinguin(p);

		Plateau plateauClone = plateau.clone();

		ArrayList<Pinguin> joueur1 = new ArrayList<>();
		for (Pinguin pj1 : this.pinguinsJoueur) {
		    joueur1.add(pj1.myClone(plateauClone));
		}

		ArrayList<Pinguin> joueur2 = new ArrayList<>();
		for (Pinguin pj2 : this.pinguinsAdverses) {
		    joueur2.add(pj2.myClone(plateauClone));
		}

		cb = new CalculBranche(joueur1, joueur2, c, plateauClone, p, profondeur);
		cbs.add(cb);
		cbs.get(cbs.size() - 1).start();

		c.setPinguin(null);
	    }
	    p.setPosition(anciennePositionPinguin);
	    anciennePositionPinguin.setPinguin(p);
	    anciennePositionPinguin.setCoulee(false);
	}

	for (CalculBranche calcul : cbs) {
	    try {
		calcul.join();
	    } catch (InterruptedException ex) {
		Logger.getLogger(Minimax.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
	for (CalculBranche calcul : cbs) {
	    if (calcul.getRes() > max) {
		max = calcul.getRes();
		pinguinRep = calcul.getPinguin();
		caseRes = calcul.getCasePrecedente();
	    }
	}

	MyPair<Case, Pinguin> rep = new MyPair(caseRes, pinguinRep);

	return rep;
    }

    public int minimaxWorker(int tour, ArrayList<Pinguin> pinguinsJoueur, ArrayList<Pinguin> pinguinsAdverses, int profondeur) {
	int bonusJoueur = 0;
	int bonusAdversaire = 0;

	ArrayList<Pinguin> potence = new ArrayList<>();

	for (Pinguin p : pinguinsJoueur) {
	    if (this.plateau.getNbJoueurIceberg(plateau.getCasesIceberg(p.getPosition())) == 1) {
		potence.add(p);
		bonusJoueur += this.plateau.getPoidsIceberg(this.plateau.getCasesIceberg(p.getPosition())) / this.plateau.getNbPinguinIceberg(this.plateau.getCasesIceberg(p.getPosition()));
		/*if (this.plateau.getCasesIceberg(p.getPosition()).isEmpty() || this.plateau.getNbPinguinIceberg(this.plateau.getCasesIceberg(p.getPosition())) == 0) {
		 System.out.println("=================\nTaille iceberg : " + this.plateau.getCasesIceberg(p.getPosition()) + "\nNb Pinguins : " + this.plateau.getNbPinguinIceberg(this.plateau.getCasesIceberg(p.getPosition())));
		 }*/
	    } else if (p.getPosition().getCasePossibles().isEmpty()) {
		bonusJoueur += p.getPosition().getNbPoissons();
		potence.add(p);
	    }
	}

	for (Pinguin p : pinguinsAdverses) {
	    if (this.plateau.getNbJoueurIceberg(plateau.getCasesIceberg(p.getPosition())) == 1) {
		potence.add(p);
		bonusAdversaire += this.plateau.getPoidsIceberg(this.plateau.getCasesIceberg(p.getPosition())) / this.plateau.getNbPinguinIceberg(this.plateau.getCasesIceberg(p.getPosition()));
		/*if (this.plateau.getCasesIceberg(p.getPosition()).isEmpty() || this.plateau.getNbPinguinIceberg(this.plateau.getCasesIceberg(p.getPosition())) == 0) {
		 System.out.println("=================\nTaille iceberg : " + this.plateau.getCasesIceberg(p.getPosition()) + "\nNb Pinguins : " + this.plateau.getNbPinguinIceberg(this.plateau.getCasesIceberg(p.getPosition())));
		 }*/
	    } else if (p.getPosition().getCasePossibles().isEmpty()) {
		bonusAdversaire += p.getPosition().getNbPoissons();
		potence.add(p);
	    }
	}

	pinguinsJoueur.removeAll(potence);
	pinguinsAdverses.removeAll(potence);
	int poidsCourant;
	Case anciennePositionPinguin = null;
	ArrayList<Case> movementPossibles;

	//Tour de l'IA
	if (tour % 2 == 0) {
	    if (pinguinsAdverses.isEmpty() || profondeur == 0) {
		return bonusJoueur;
	    } else if (profondeur == 0) {

	    }

	    int max = -1;

	    for (Pinguin p : pinguinsJoueur) {
		movementPossibles = p.getPosition().getCasePossibles();
		anciennePositionPinguin = p.getPosition();
		anciennePositionPinguin.setPinguin(null);
		anciennePositionPinguin.setCoulee(true);

		for (Case c : movementPossibles) {

		    p.setPosition(c);
		    c.setPinguin(p);

		    if ((poidsCourant = minimaxWorker(tour + 1, (ArrayList<Pinguin>) pinguinsJoueur.clone(), (ArrayList<Pinguin>) pinguinsAdverses.clone(), profondeur - 1) + c.getNbPoissons()) > max) {
			max = poidsCourant;
		    }

		    c.setPinguin(null);
		}

		p.setPosition(anciennePositionPinguin);
		anciennePositionPinguin.setPinguin(p);
		anciennePositionPinguin.setCoulee(false);
	    }

	    return max;

	    //Tour de l'adversaire
	} else {
	    if (pinguinsJoueur.isEmpty() || profondeur == 0) {
		return bonusAdversaire;
	    }
	    int min = 300;

	    for (Pinguin p : pinguinsAdverses) {
		movementPossibles = p.getPosition().getCasePossibles();
		anciennePositionPinguin = p.getPosition();
		anciennePositionPinguin.setPinguin(null);
		anciennePositionPinguin.setCoulee(true);

		for (Case c : movementPossibles) {

		    p.setPosition(c);
		    c.setPinguin(p);

		    if ((poidsCourant = minimaxWorker(tour + 1, (ArrayList<Pinguin>) pinguinsJoueur.clone(), (ArrayList<Pinguin>) pinguinsAdverses.clone(), profondeur - 1) + c.getNbPoissons()) < min) {
			min = poidsCourant;
		    }

		    c.setPinguin(null);

		}

		p.setPosition(anciennePositionPinguin);
		anciennePositionPinguin.setPinguin(p);
		anciennePositionPinguin.setCoulee(false);
	    }

	    return min;

	}
    }

    public MyPair<Case, Pinguin> executeNegamax(int profondeur) {
	Case caseRes = null;
	Pinguin pinguinRep = null;
	int poidsCourant;
	Case anciennePositionPinguin;
	int meilleurPoids = Integer.MIN_VALUE;
	ArrayList<Case> movementPossibles;
	HashMap<Pinguin, Integer> debug = new HashMap<>();

	for (Pinguin p : pinguinsJoueur) {
	    debug.put(p, Integer.MIN_VALUE);
	    movementPossibles = p.getPosition().getCasePossibles();
	    anciennePositionPinguin = p.getPosition();
	    anciennePositionPinguin.setPinguin(null);
	    anciennePositionPinguin.setCoulee(true);

	    for (Case c : movementPossibles) {
		p.setPosition(c);
		c.setPinguin(p);

		poidsCourant = NegaMax(plateau, (ArrayList<Pinguin>) this.pinguinsAdverses.clone(), (ArrayList<Pinguin>) this.pinguinsJoueur.clone(), profondeur - 1, 0, c.getNbPoissons());
		if (poidsCourant > meilleurPoids) {
		    meilleurPoids = poidsCourant;
		    pinguinRep = p;
		    caseRes = c;
		}

		c.setPinguin(null);

	    }
	    p.setPosition(anciennePositionPinguin);
	    anciennePositionPinguin.setCoulee(false);
	    anciennePositionPinguin.setPinguin(p);
	    debug.put(p, meilleurPoids);
	}

	MyPair<Case, Pinguin> rep = new MyPair(caseRes, pinguinRep);

	return rep;
    }

    public static int NegaMax(Plateau plateau, ArrayList<Pinguin> pinguinsJoueur1, ArrayList<Pinguin> pinguinsJoueur2, int profondeur, int poidsChemin1, int poidsChemin2) {
	int meilleurPoids = Integer.MIN_VALUE;
	int poidsCourant, poidsFeuille = 0;
	ArrayList<Case> mouvementsPossibles;
	Case anciennePositionPinguin;

	//On stop l'enumeration
	if (profondeur == 0) {
	    return poidsChemin1 + JoueurIA.evaluationEtatV2(pinguinsJoueur1, plateau);

	} else {
	    //Supprime les pinguins inutiles
	    for (int i = 0; i < pinguinsJoueur1.size(); i++) {
		//Si un pinguin ne peut plus bouger
		if (pinguinsJoueur1.get(i).getPosition().getVoisinsJouable().isEmpty()) {
		    poidsFeuille += pinguinsJoueur1.get(i).getPosition().getNbPoissons();
		    pinguinsJoueur1.remove(i);
		    i--;

		    //Si le joueur est seul sur l'iceberg alors on considere la configuration comme une feuille et on retournera le poids de l'iceberg
		} else if (Plateau.getNbJoueurIceberg(Plateau.getCasesIceberg(pinguinsJoueur1.get(i).getPosition())) == 1) {
		    poidsFeuille += Plateau.getPoidsIceberg(Plateau.getCasesIceberg(pinguinsJoueur1.get(i).getPosition())) / Plateau.getNbPinguinIceberg(Plateau.getCasesIceberg(pinguinsJoueur1.get(i).getPosition()));
		    pinguinsJoueur1.remove(i);
		    i--;
		}
	    }

	    //Feuille
	    if (pinguinsJoueur1.isEmpty()) {
		return poidsChemin1 + poidsFeuille;
	    }

	    //Pour tous les pinguins du joueur courant
	    for (Pinguin p : pinguinsJoueur1) {
		mouvementsPossibles = p.getPosition().getCasePossibles();
		//sauvegarde de l'etat courant
		anciennePositionPinguin = p.getPosition();
		anciennePositionPinguin.setPinguin(null);
		anciennePositionPinguin.setCoulee(true);

		for (Case c : mouvementsPossibles) {
		    //Deplacement du pinguin
		    p.setPosition(c);
		    c.setPinguin(p);

		    poidsCourant = -NegaMax(plateau, (ArrayList<Pinguin>) pinguinsJoueur2.clone(), (ArrayList<Pinguin>) pinguinsJoueur1.clone(), profondeur - 1, poidsChemin2, c.getNbPoissons() + poidsChemin1 + poidsFeuille);
		    meilleurPoids = Integer.max(poidsCourant, meilleurPoids);

		    //undo coup
		    c.setPinguin(null);
		}
		//undo coup
		p.setPosition(anciennePositionPinguin);
		anciennePositionPinguin.setPinguin(p);
		anciennePositionPinguin.setCoulee(false);

	    }

	    return meilleurPoids;
	}
    }

    public MyPair<Case, Pinguin> executeNegamaxElagage(int profondeur) {
	int alpha = Integer.MIN_VALUE, beta = Integer.MAX_VALUE;
	Case caseRes = null;
	Pinguin pinguinRep = null;
	int poidsCourant;
	Case anciennePositionPinguin;
	int meilleurPoids = Integer.MIN_VALUE;
	ArrayList<Case> movementPossibles;
	HashMap<Pinguin, Integer> debug = new HashMap<>();

	for (Pinguin p : pinguinsJoueur) {
	    debug.put(p, Integer.MIN_VALUE);
	    movementPossibles = p.getPosition().getCasePossibles();
	    anciennePositionPinguin = p.getPosition();
	    anciennePositionPinguin.setPinguin(null);
	    anciennePositionPinguin.setCoulee(true);

	    for (Case c : movementPossibles) {
		p.setPosition(c);
		c.setPinguin(p);

		poidsCourant = -NegaMaxElagage(plateau, (ArrayList<Pinguin>) pinguinsJoueur.clone(), (ArrayList<Pinguin>) pinguinsAdverses.clone(), profondeur - 1, -beta, -alpha, 0, c.getNbPoissons());

		if (poidsCourant > meilleurPoids) {
		    meilleurPoids = poidsCourant;
		    caseRes = c;
		    pinguinRep = p;
		    if (meilleurPoids > alpha) {
			alpha = meilleurPoids;
			if (alpha > beta) {
			    p.setPosition(anciennePositionPinguin);
			    anciennePositionPinguin.setPinguin(p);
			    anciennePositionPinguin.setCoulee(false);
			    return new MyPair<>(caseRes, pinguinRep);
			}
		    }
		}

		c.setPinguin(null);

	    }

	    p.setPosition(anciennePositionPinguin);
	    anciennePositionPinguin.setCoulee(false);
	    anciennePositionPinguin.setPinguin(p);
	    debug.put(p, meilleurPoids);

	}

	MyPair<Case, Pinguin> rep = new MyPair(caseRes, pinguinRep);

	return rep;
    }

    public MyPair<Case, Pinguin> executeNegamaxMultiThread(int profondeur) {
	Case caseRes = null;
	Pinguin pinguinRep = null;
	Case anciennePositionPinguin;
	int meilleurPoids = Integer.MIN_VALUE;
	ArrayList<Case> movementPossibles;
	CalculBrancheNegamax cb;
	ArrayList<CalculBrancheNegamax> cbs = new ArrayList<>();

	for (Pinguin p : pinguinsJoueur) {
	    movementPossibles = p.getPosition().getCasePossibles();
	    anciennePositionPinguin = p.getPosition();
	    anciennePositionPinguin.setPinguin(null);
	    anciennePositionPinguin.setCoulee(true);

	    for (Case c : movementPossibles) {
		p.setPosition(c);
		c.setPinguin(p);

		Plateau plateauClone = plateau.clone();

		ArrayList<Pinguin> joueur1 = new ArrayList<>();
		for (Pinguin pj1 : this.pinguinsJoueur) {
		    joueur1.add(pj1.myClone(plateauClone));
		}

		ArrayList<Pinguin> joueur2 = new ArrayList<>();
		for (Pinguin pj2 : this.pinguinsAdverses) {
		    joueur2.add(pj2.myClone(plateauClone));
		}

		cb = new CalculBrancheNegamax(plateauClone, joueur1, joueur2, profondeur - 1, c, p);
		cbs.add(cb);
		cbs.get(cbs.size() - 1).start();

		c.setPinguin(null);

	    }
	    p.setPosition(anciennePositionPinguin);
	    anciennePositionPinguin.setCoulee(false);
	    anciennePositionPinguin.setPinguin(p);
	}

	for (CalculBrancheNegamax calcul : cbs) {
	    try {
		calcul.join();
		if (calcul.getRes() > meilleurPoids) {
		    meilleurPoids = calcul.getRes();
		    pinguinRep = calcul.getPinguinEtudie();
		    caseRes = calcul.getCaseEtudiee();
		}
	    } catch (InterruptedException ex) {
		Logger.getLogger(Minimax.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}

	MyPair<Case, Pinguin> rep = new MyPair(caseRes, pinguinRep);

	return rep;
    }

    public class CalculBrancheNegamax extends Thread {

	private Plateau plateau;
	private ArrayList<Pinguin> pinguinsJoueur1;
	private ArrayList<Pinguin> pinguinsJoueur2;
	private int profondeur;
	private int res;
	private Case caseEtudiee;
	private Pinguin pinguinEtudie;

	public CalculBrancheNegamax(Plateau plateau, ArrayList<Pinguin> pinguinsJoueur1, ArrayList<Pinguin> pinguinsJoueur2, int profondeur, Case caseEtudiee, Pinguin pinguinEtudie) {
	    this.plateau = plateau;
	    this.pinguinsJoueur1 = pinguinsJoueur1;
	    this.pinguinsJoueur2 = pinguinsJoueur2;
	    this.profondeur = profondeur;
	    this.caseEtudiee = caseEtudiee;
	    this.pinguinEtudie = pinguinEtudie;
	}

	public int getRes() {
	    return res;
	}

	public Case getCaseEtudiee() {
	    return caseEtudiee;
	}

	public Pinguin getPinguinEtudie() {
	    return pinguinEtudie;
	}

	@Override
	public void run() {
	    //this.res = Minimax.NegaMaxElagage(plateau, pinguinsJoueur2, pinguinsJoueur1, profondeur, Integer.MAX_VALUE, Integer.MIN_VALUE, 0, this.caseEtudiee.getNbPoissons());
	    this.res = Minimax.NegaMax(plateau, pinguinsJoueur2, pinguinsJoueur1, profondeur, 0, this.caseEtudiee.getNbPoissons());
	}
    }

    /**
     * a faire
     *
     * @param plateau
     * @param pinguinsJoueur1
     * @param pinguinsJoueur2
     * @param profondeur
     * @param alpha
     * @param beta
     * @param poidsChemin1
     * @param poidsChemin2
     * @return
     */
    public static int NegaMaxElagage(Plateau plateau, ArrayList<Pinguin> pinguinsJoueur1, ArrayList<Pinguin> pinguinsJoueur2, int profondeur, int alpha, int beta, int poidsChemin1, int poidsChemin2) {
	int meilleurPoids = Integer.MIN_VALUE;
	int poidsCourant, poidsFeuille = 0;
	ArrayList<Case> mouvementsPossibles;
	Case anciennePositionPinguin;

	//On stop l'enumeration
	if (profondeur == 0) {
	    return poidsChemin1 + JoueurIA.evaluationEtatV2(pinguinsJoueur1, plateau);

	} else {
	    //On retire les pinguins qui sont dans un etat final
	    for (int i = 0; i < pinguinsJoueur1.size(); i++) {
		//Ceux qui n'ont plus de mouvements possible
		if (pinguinsJoueur1.get(i).getPosition().getVoisinsJouable().isEmpty()) {
		    poidsFeuille += pinguinsJoueur1.get(i).getPosition().getNbPoissons();
		    pinguinsJoueur1.remove(i);
		    i--;

		    //Si le joueur est seul sur l'iceberg alors on considere que le joueur gagne les points de cet iceberg
		} else if (Plateau.getNbJoueurIceberg(Plateau.getCasesIceberg(pinguinsJoueur1.get(i).getPosition())) == 1) {
		    poidsFeuille += Plateau.getPoidsIceberg(Plateau.getCasesIceberg(pinguinsJoueur1.get(i).getPosition())) / Plateau.getNbPinguinIceberg(Plateau.getCasesIceberg(pinguinsJoueur1.get(i).getPosition()));
		    pinguinsJoueur1.remove(i);
		    i--;
		}
	    }

	    //Feuille
	    if (pinguinsJoueur1.isEmpty()) {
		return poidsChemin1 + poidsFeuille;
	    }

	    //Pour tous les pinguins du joueur courant
	    for (Pinguin p : pinguinsJoueur1) {
		mouvementsPossibles = p.getPosition().getCasePossibles();
		//sauvegarde de l'etat courant
		anciennePositionPinguin = p.getPosition();
		anciennePositionPinguin.setPinguin(null);
		anciennePositionPinguin.setCoulee(true);

		for (Case c : mouvementsPossibles) {
		    //Deplacement du pinguin
		    p.setPosition(c);
		    c.setPinguin(p);

		    poidsCourant = -NegaMaxElagage(plateau, (ArrayList<Pinguin>) pinguinsJoueur2.clone(), (ArrayList<Pinguin>) pinguinsJoueur1.clone(), profondeur - 1, -beta, -alpha, poidsChemin2, c.getNbPoissons() + poidsChemin1 + poidsFeuille);
		    if (poidsCourant > meilleurPoids) {
			meilleurPoids = poidsCourant;
			if (meilleurPoids > alpha) {
			    alpha = meilleurPoids;
			    if (alpha > beta) {
				//undo coup
				p.setPosition(anciennePositionPinguin);
				anciennePositionPinguin.setPinguin(p);
				anciennePositionPinguin.setCoulee(false);
				c.setPinguin(null);
				return meilleurPoids;
			    }
			}
		    }

		    //undo coup
		    c.setPinguin(null);
		}
		//undo coup
		p.setPosition(anciennePositionPinguin);
		anciennePositionPinguin.setPinguin(p);
		anciennePositionPinguin.setCoulee(false);
	    }
	}

	return meilleurPoids;
    }

    public class CalculBranche extends Thread {

	private int res;
	private Case casePrecedente;
	private ArrayList<Pinguin> pinguinsJoueur;
	private ArrayList<Pinguin> pinguinsAdverses;
	private Plateau plateau;
	private Pinguin pinguin;
	private int profondeur;

	public CalculBranche(ArrayList<Pinguin> pinguinsJoueur, ArrayList<Pinguin> pinguinsAdverses, Case casePrecedente, Plateau plateau, Pinguin pinguin, int profondeur) {
	    this.res = 0;
	    this.pinguin = pinguin;
	    this.casePrecedente = casePrecedente;

	    this.profondeur = profondeur;

	    this.plateau = plateau;

	    this.pinguinsJoueur = pinguinsJoueur;

	    this.pinguinsAdverses = pinguinsAdverses;
	}

	public int getRes() {
	    return res + this.casePrecedente.getNbPoissons();
	}

	public Pinguin getPinguin() {
	    return this.pinguin;
	}

	public Case getCasePrecedente() {
	    return casePrecedente;
	}

	@Override
	public void run() {
	    this.res = this.minimaxWorker(1, this.pinguinsJoueur, this.pinguinsAdverses, this.profondeur, this.casePrecedente.getNbPoissons(), 0);
	}

	public int minimaxWorker(int tour, ArrayList<Pinguin> pinguinsJoueur, ArrayList<Pinguin> pinguinsAdverses, int profondeur, int poidsJoueur1, int poidsJoueur2) {
	    int bonusJoueur = 0;
	    int bonusAdversaire = 0;

	    ArrayList<Pinguin> potence = new ArrayList<>();

	    for (Pinguin p : pinguinsJoueur) {
		if (this.plateau.getNbJoueurIceberg(plateau.getCasesIceberg(p.getPosition())) == 1) {
		    potence.add(p);
		    int bonus = this.plateau.getPoidsIceberg(this.plateau.getCasesIceberg(p.getPosition())) / this.plateau.getNbPinguinIceberg(this.plateau.getCasesIceberg(p.getPosition()));
		    bonusJoueur += bonus;
		} else if (p.getPosition().getCasePossibles().isEmpty()) {
		    int bonus = p.getPosition().getNbPoissons();
		    bonusJoueur += bonus;
		    potence.add(p);
		}
	    }

	    for (Pinguin p : pinguinsAdverses) {
		if (this.plateau.getNbJoueurIceberg(plateau.getCasesIceberg(p.getPosition())) == 1) {
		    potence.add(p);
		    bonusAdversaire += this.plateau.getPoidsIceberg(this.plateau.getCasesIceberg(p.getPosition())) / this.plateau.getNbPinguinIceberg(this.plateau.getCasesIceberg(p.getPosition()));
		    /*if (this.plateau.getCasesIceberg(p.getPosition()).isEmpty() || this.plateau.getNbPinguinIceberg(this.plateau.getCasesIceberg(p.getPosition())) == 0) {
		     System.out.println("=================\nTaille iceberg : " + this.plateau.getCasesIceberg(p.getPosition()) + "\nNb Pinguins : " + this.plateau.getNbPinguinIceberg(this.plateau.getCasesIceberg(p.getPosition())));
		     }*/
		} else if (p.getPosition().getCasePossibles().isEmpty()) {
		    bonusAdversaire += p.getPosition().getNbPoissons();
		    potence.add(p);
		}
	    }

	    pinguinsJoueur.removeAll(potence);
	    pinguinsAdverses.removeAll(potence);
	    int poidsCourant;
	    Case anciennePositionPinguin = null;
	    ArrayList<Case> movementPossibles;

	    //Tour de l'IA
	    if (tour % 2 == 0) {
		if (pinguinsAdverses.isEmpty() || profondeur == 0) {
		    return bonusJoueur + poidsJoueur1;
		}
		int max = -1;

		for (Pinguin p : pinguinsJoueur) {
		    movementPossibles = p.getPosition().getCasePossibles();
		    anciennePositionPinguin = p.getPosition();
		    anciennePositionPinguin.setPinguin(null);
		    anciennePositionPinguin.setCoulee(true);

		    for (Case c : movementPossibles) {

			p.setPosition(c);
			c.setPinguin(p);

			if ((poidsCourant = minimaxWorker(tour + 1, (ArrayList<Pinguin>) pinguinsJoueur.clone(), (ArrayList<Pinguin>) pinguinsAdverses.clone(), profondeur - 1, poidsJoueur1 + bonusJoueur + c.getNbPoissons(), poidsJoueur2 + bonusAdversaire)) > max) {
			    max = poidsCourant;
			}

			c.setPinguin(null);
		    }

		    p.setPosition(anciennePositionPinguin);
		    anciennePositionPinguin.setPinguin(p);
		    anciennePositionPinguin.setCoulee(false);
		}

		return max;

		//Tour de l'adversaire
	    } else {
		if (pinguinsJoueur.isEmpty() || profondeur == 0) {
		    return bonusAdversaire + poidsJoueur2;
		}
		int min = 300;

		for (Pinguin p : pinguinsAdverses) {
		    movementPossibles = p.getPosition().getCasePossibles();
		    anciennePositionPinguin = p.getPosition();
		    anciennePositionPinguin.setPinguin(null);
		    anciennePositionPinguin.setCoulee(true);

		    for (Case c : movementPossibles) {

			p.setPosition(c);
			c.setPinguin(p);

			if ((poidsCourant = minimaxWorker(tour + 1, (ArrayList<Pinguin>) pinguinsJoueur.clone(), (ArrayList<Pinguin>) pinguinsAdverses.clone(), profondeur - 1, poidsJoueur1 + bonusJoueur, poidsJoueur2 + bonusAdversaire + c.getNbPoissons())) < min) {
			    min = poidsCourant;
			}

			c.setPinguin(null);

		    }

		    p.setPosition(anciennePositionPinguin);
		    anciennePositionPinguin.setPinguin(p);
		    anciennePositionPinguin.setCoulee(false);
		}

		return min;

	    }

	}

    }

    public MyPair<Case, Pinguin> executeElagage(int profondeur) {
	int poidsCourant, min = Integer.MIN_VALUE;
	int alpha = -Integer.MIN_VALUE, beta = -Integer.MAX_VALUE;

	Case caseRes = null;
	Pinguin pinguinRep = null;

	Case anciennePositionPinguin = null;
	ArrayList<Case> movementPossibles;

	for (Pinguin p : pinguinsJoueur) {

	    movementPossibles = p.getPosition().getCasePossibles();
	    anciennePositionPinguin = p.getPosition();
	    anciennePositionPinguin.setPinguin(null);
	    anciennePositionPinguin.setCoulee(true);

	    for (Case c : movementPossibles) {
		p.setPosition(c);
		c.setPinguin(p);

		//////////
		poidsCourant = -elagageWorker(1, (ArrayList<Pinguin>) this.pinguinsJoueur.clone(), (ArrayList<Pinguin>) this.pinguinsAdverses.clone(), -beta, -alpha, profondeur);
		if (poidsCourant > min) {
		    min = poidsCourant;
		    pinguinRep = p;
		    caseRes = c;
		    if (min > alpha) {
			alpha = min;
			if (alpha >= beta) {
			    c.setPinguin(null);
			    p.setPosition(anciennePositionPinguin);
			    anciennePositionPinguin.setCoulee(false);
			    anciennePositionPinguin.setPinguin(p);
			    return new MyPair<>(caseRes, pinguinRep);
			}
		    }
		}

		c.setPinguin(null);

	    }
	    p.setPosition(anciennePositionPinguin);
	    anciennePositionPinguin.setCoulee(false);
	    anciennePositionPinguin.setPinguin(p);
	}

	MyPair<Case, Pinguin> rep = new MyPair(caseRes, pinguinRep);

	return rep;
    }

    public int elagageWorker(int tour, ArrayList<Pinguin> pinguinsJoueur, ArrayList<Pinguin> pinguinsAdverses, int alpha, int beta, int profondeur) {
	int bonusJoueur = 0;
	int bonusAdversaire = 0;

	ArrayList<Pinguin> potence = new ArrayList<>();

	for (Pinguin p : pinguinsJoueur) {
	    if (this.plateau.getNbJoueurIceberg(plateau.getCasesIceberg(p.getPosition())) == 1) {
		potence.add(p);
		bonusJoueur += this.plateau.getPoidsIceberg(this.plateau.getCasesIceberg(p.getPosition())) / this.plateau.getNbPinguinIceberg(this.plateau.getCasesIceberg(p.getPosition()));
	    } else if (p.getPosition().getCasePossibles().isEmpty()) {
		bonusJoueur += p.getPosition().getNbPoissons();
		potence.add(p);
	    }
	}

	for (Pinguin p : pinguinsAdverses) {
	    if (this.plateau.getNbJoueurIceberg(plateau.getCasesIceberg(p.getPosition())) == 1) {
		potence.add(p);
		bonusAdversaire += this.plateau.getPoidsIceberg(this.plateau.getCasesIceberg(p.getPosition())) / this.plateau.getNbPinguinIceberg(this.plateau.getCasesIceberg(p.getPosition()));
	    } else if (p.getPosition().getCasePossibles().isEmpty()) {
		bonusAdversaire += p.getPosition().getNbPoissons();
		potence.add(p);
	    }
	}

	pinguinsJoueur.removeAll(potence);
	pinguinsAdverses.removeAll(potence);
	int poidsCourant;
	Case anciennePositionPinguin = null;
	ArrayList<Case> movementPossibles;
	int min = Integer.MIN_VALUE;

	//Tour de l'IA
	if (tour % 2 == 0) {
	    if (pinguinsAdverses.isEmpty() || profondeur == 0) {
		return bonusJoueur;
	    }

	    for (Pinguin p : pinguinsJoueur) {
		movementPossibles = p.getPosition().getCasePossibles();
		anciennePositionPinguin = p.getPosition();
		anciennePositionPinguin.setPinguin(null);
		anciennePositionPinguin.setCoulee(true);

		for (Case c : movementPossibles) {

		    p.setPosition(c);
		    c.setPinguin(p);

		    poidsCourant = -elagageWorker(tour + 1, (ArrayList<Pinguin>) this.pinguinsJoueur.clone(), (ArrayList<Pinguin>) this.pinguinsAdverses.clone(), -beta, -alpha, profondeur - 1);
		    if (poidsCourant > min) {
			min = poidsCourant;
			if (min > alpha) {
			    alpha = min;
			    if (alpha >= beta) {
				c.setPinguin(null);
				p.setPosition(anciennePositionPinguin);
				anciennePositionPinguin.setPinguin(p);
				anciennePositionPinguin.setCoulee(false);
				return min;
			    }
			}
		    }

		    c.setPinguin(null);
		}

		p.setPosition(anciennePositionPinguin);
		anciennePositionPinguin.setPinguin(p);
		anciennePositionPinguin.setCoulee(false);
	    }

	    //Tour de l'adversaire
	} else {
	    if (pinguinsJoueur.isEmpty() || profondeur == 0) {
		return bonusAdversaire;
	    }

	    for (Pinguin p : pinguinsAdverses) {
		movementPossibles = p.getPosition().getCasePossibles();
		anciennePositionPinguin = p.getPosition();
		anciennePositionPinguin.setPinguin(null);
		anciennePositionPinguin.setCoulee(true);

		for (Case c : movementPossibles) {

		    p.setPosition(c);
		    c.setPinguin(p);

		    poidsCourant = -elagageWorker(tour + 1, (ArrayList<Pinguin>) this.pinguinsJoueur.clone(), (ArrayList<Pinguin>) this.pinguinsAdverses.clone(), -beta, -alpha, profondeur - 1);
		    if (poidsCourant > min) {
			min = poidsCourant;
			if (min > alpha) {
			    alpha = min;
			    if (alpha >= beta) {
				c.setPinguin(null);
				p.setPosition(anciennePositionPinguin);
				anciennePositionPinguin.setPinguin(p);
				anciennePositionPinguin.setCoulee(false);
				return min;
			    }
			}
		    }

		    c.setPinguin(null);

		}
		p.setPosition(anciennePositionPinguin);
		anciennePositionPinguin.setPinguin(p);
		anciennePositionPinguin.setCoulee(false);
	    }

	}
	return min;
    }

    public MyPair<Case, Pinguin> executeElagageMultiThread(int profondeur) {
	int poidsCourant, min = Integer.MIN_VALUE;
	int alpha = -Integer.MIN_VALUE, beta = -Integer.MAX_VALUE;

	CalculBrancheElagage cb;
	ArrayList<CalculBrancheElagage> cbs = new ArrayList<>();

	Case caseRes = null;
	Pinguin pinguinRep = null;

	Case anciennePositionPinguin = null;
	ArrayList<Case> movementPossibles;

	for (Pinguin p : pinguinsJoueur) {

	    movementPossibles = p.getPosition().getCasePossibles();
	    anciennePositionPinguin = p.getPosition();
	    anciennePositionPinguin.setPinguin(null);
	    anciennePositionPinguin.setCoulee(true);

	    for (Case c : movementPossibles) {
		p.setPosition(c);
		c.setPinguin(p);

		Plateau plateauClone = plateau.clone();

		ArrayList<Pinguin> joueur1 = new ArrayList<>();
		for (Pinguin pj1 : this.pinguinsJoueur) {
		    joueur1.add(pj1.myClone(plateauClone));
		}

		ArrayList<Pinguin> joueur2 = new ArrayList<>();
		for (Pinguin pj2 : this.pinguinsAdverses) {
		    joueur2.add(pj2.myClone(plateauClone));
		}

		cb = new CalculBrancheElagage(joueur1, joueur2, c, plateauClone, p, profondeur, -beta, -alpha);
		cbs.add(cb);
		cbs.get(cbs.size() - 1).start();

		c.setPinguin(null);

	    }
	    p.setPosition(anciennePositionPinguin);
	    anciennePositionPinguin.setCoulee(false);
	    anciennePositionPinguin.setPinguin(p);
	}

	for (CalculBrancheElagage calcul : cbs) {
	    try {
		calcul.join();
	    } catch (InterruptedException ex) {
		Logger.getLogger(Minimax.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}

	for (CalculBrancheElagage calcul : cbs) {
	    if (calcul.getRes() > min) {
		min = calcul.getRes();
		pinguinRep = calcul.getPinguin();
		caseRes = calcul.getCasePrecedente();
		if (min > alpha) {
		    alpha = min;
		    if (alpha >= beta) {
			return new MyPair<>(caseRes, pinguinRep);
		    }
		}
	    }
	}

	MyPair<Case, Pinguin> rep = new MyPair(caseRes, pinguinRep);

	return rep;
    }

    public class CalculBrancheElagage extends Thread {

	private int res;
	private Case casePrecedente;
	private ArrayList<Pinguin> pinguinsJoueur;
	private ArrayList<Pinguin> pinguinsAdverses;
	private Plateau plateau;
	private Pinguin pinguin;
	private int profondeur;
	private int alpha;
	private int beta;

	public CalculBrancheElagage(ArrayList<Pinguin> pinguinsJoueur, ArrayList<Pinguin> pinguinsAdverses, Case casePrecedente, Plateau plateau, Pinguin pinguin, int profondeur, int alpha, int beta) {
	    this.res = 0;
	    this.alpha = alpha;
	    this.beta = beta;
	    this.pinguin = pinguin;
	    this.casePrecedente = casePrecedente;

	    this.profondeur = profondeur;

	    this.plateau = plateau;

	    this.pinguinsJoueur = pinguinsJoueur;

	    this.pinguinsAdverses = pinguinsAdverses;
	}

	public int getRes() {
	    return res + this.casePrecedente.getNbPoissons();
	}

	public Pinguin getPinguin() {
	    return this.pinguin;
	}

	public Case getCasePrecedente() {
	    return casePrecedente;
	}

	@Override
	public void run() {
	    this.res = -elagageWorker(1, (ArrayList<Pinguin>) this.pinguinsJoueur.clone(), (ArrayList<Pinguin>) this.pinguinsAdverses.clone(), -beta, -alpha, profondeur);
	}

	public int elagageWorker(int tour, ArrayList<Pinguin> pinguinsJoueur, ArrayList<Pinguin> pinguinsAdverses, int alpha, int beta, int profondeur) {
	    int bonusJoueur = 0;
	    int bonusAdversaire = 0;

	    ArrayList<Pinguin> potence = new ArrayList<>();

	    for (Pinguin p : pinguinsJoueur) {
		if (this.plateau.getNbJoueurIceberg(plateau.getCasesIceberg(p.getPosition())) == 1) {
		    potence.add(p);
		    bonusJoueur += this.plateau.getPoidsIceberg(this.plateau.getCasesIceberg(p.getPosition())) / this.plateau.getNbPinguinIceberg(this.plateau.getCasesIceberg(p.getPosition()));
		} else if (p.getPosition().getCasePossibles().isEmpty()) {
		    bonusJoueur += p.getPosition().getNbPoissons();
		    potence.add(p);
		}
	    }

	    for (Pinguin p : pinguinsAdverses) {
		if (this.plateau.getNbJoueurIceberg(plateau.getCasesIceberg(p.getPosition())) == 1) {
		    potence.add(p);
		    bonusAdversaire += this.plateau.getPoidsIceberg(this.plateau.getCasesIceberg(p.getPosition())) / this.plateau.getNbPinguinIceberg(this.plateau.getCasesIceberg(p.getPosition()));
		} else if (p.getPosition().getCasePossibles().isEmpty()) {
		    bonusAdversaire += p.getPosition().getNbPoissons();
		    potence.add(p);
		}
	    }

	    pinguinsJoueur.removeAll(potence);
	    pinguinsAdverses.removeAll(potence);
	    int poidsCourant;
	    Case anciennePositionPinguin = null;
	    ArrayList<Case> movementPossibles;
	    int min = Integer.MIN_VALUE;

	    //Tour de l'IA
	    if (tour % 2 == 0) {
		if (pinguinsAdverses.isEmpty() || profondeur == 0) {
		    return bonusJoueur;
		}

		for (Pinguin p : pinguinsJoueur) {
		    movementPossibles = p.getPosition().getCasePossibles();
		    anciennePositionPinguin = p.getPosition();
		    anciennePositionPinguin.setPinguin(null);
		    anciennePositionPinguin.setCoulee(true);

		    for (Case c : movementPossibles) {

			p.setPosition(c);
			c.setPinguin(p);

			poidsCourant = -elagageWorker(tour + 1, (ArrayList<Pinguin>) this.pinguinsJoueur.clone(), (ArrayList<Pinguin>) this.pinguinsAdverses.clone(), -beta, -alpha, profondeur - 1);
			if (poidsCourant > min) {
			    min = poidsCourant;
			    if (min > alpha) {
				alpha = min;
				if (alpha >= beta) {
				    c.setPinguin(null);
				    p.setPosition(anciennePositionPinguin);
				    anciennePositionPinguin.setPinguin(p);
				    anciennePositionPinguin.setCoulee(false);
				    return min;
				}
			    }
			}

			c.setPinguin(null);
		    }

		    p.setPosition(anciennePositionPinguin);
		    anciennePositionPinguin.setPinguin(p);
		    anciennePositionPinguin.setCoulee(false);
		}

		//Tour de l'adversaire
	    } else {
		if (pinguinsJoueur.isEmpty() || profondeur == 0) {
		    return bonusAdversaire;
		}

		for (Pinguin p : pinguinsAdverses) {
		    movementPossibles = p.getPosition().getCasePossibles();
		    anciennePositionPinguin = p.getPosition();
		    anciennePositionPinguin.setPinguin(null);
		    anciennePositionPinguin.setCoulee(true);

		    for (Case c : movementPossibles) {

			p.setPosition(c);
			c.setPinguin(p);

			poidsCourant = -elagageWorker(tour + 1, (ArrayList<Pinguin>) this.pinguinsJoueur.clone(), (ArrayList<Pinguin>) this.pinguinsAdverses.clone(), -beta, -alpha, profondeur - 1);
			if (poidsCourant > min) {
			    min = poidsCourant;
			    if (min > alpha) {
				alpha = min;
				if (alpha >= beta) {
				    c.setPinguin(null);
				    p.setPosition(anciennePositionPinguin);
				    anciennePositionPinguin.setPinguin(p);
				    anciennePositionPinguin.setCoulee(false);
				    return min;
				}
			    }
			}

			c.setPinguin(null);

		    }
		    p.setPosition(anciennePositionPinguin);
		    anciennePositionPinguin.setPinguin(p);
		    anciennePositionPinguin.setCoulee(false);
		}

	    }
	    return min;
	}

    }

}
