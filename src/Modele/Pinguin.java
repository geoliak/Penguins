/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import javafx.scene.image.Image;

/**
 *
 * @author novelm
 */
public class Pinguin {

    private Boolean vivant;
    private Case position;
    private Joueur general;
    private Image image;

    public Pinguin(Case position, Joueur maitre, Image image) {
        this.vivant = true;
        this.position = position;
        this.general = maitre;
        this.image = image;
    }

    public void deplace(Case c) {
        this.position.setPinguin(null);
        this.position.setCoulee(true);

        this.general.setScoreGlacons(this.general.getScoreGlacons() + 1);
        this.general.setScorePoissons(this.general.getScorePoissons() + position.getNbPoissons());

        this.position = c;
        c.setPinguin(this);
    }

    public void coullePinguin() {
        this.position.setPinguin(null);
        this.position.setCoulee(true);

        this.general.setScoreGlacons(this.general.getScoreGlacons() + 1);
        this.general.setScorePoissons(this.general.getScorePoissons() + position.getNbPoissons());

        this.vivant = false;
        System.out.println("coullé " + this);
    }

    /**
     * A l'air de marcher
     *
     * @param plateau
     * @param source
     * @return True si le pinguin est seul ou avec un de ses collègues sur
     * l'iceberg
     */
    public Boolean estSeulIceberg(Case source) {
        Boolean res = true;

        //Si la case courante n'est pas coulee
        if (!source.estCoulee()) {

            //Si la case courante contient un adversaire
            if (source.getPinguin() != null && source.getPinguin().getGeneral() != this.general) {
                return false;

            } else {
                for (Case c : source.getVoisinsEmerges()) {
                    source.setCoulee(true);
                    res = res && this.estSeulIceberg(c);
                    source.setCoulee(false);
                }
            }
        }

        return res;
    }

    public void accept(Visiteur v) {
        v.visite(this);
    }

    public Boolean estVivant() {
        return vivant;
    }

    public void setVivant(Boolean vivant) {
        this.vivant = vivant;
    }

    public Case getPosition() {
        return position;
    }

    public void setPosition(Case position) {
        this.position = position;
    }

    public Joueur getGeneral() {
        return general;
    }

    public Image getImage() {
        return image;
    }

    @Override
    public String toString() {
        return this.getGeneral().getCouleur() + "(" + this.position.getNumLigne() + "," + this.position.getNumColonne() + ")" + Couleur.ANSI_RESET;

    }
}
