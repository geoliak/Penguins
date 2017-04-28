/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author rozandq
 */
public class Case {
    private int x, y;
    private boolean jouable;

    public Case(int x, int y, boolean jouable) {
        this.x = x;
        this.y = y;
        this.jouable = jouable;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isJouable() {
        return jouable;
    }

    public void setJouable(boolean jouable) {
        this.jouable = jouable;
    }
}
