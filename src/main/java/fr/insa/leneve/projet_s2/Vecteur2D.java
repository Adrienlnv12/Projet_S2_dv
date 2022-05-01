/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2;

/**
 *
 * @author adrie
 */
public class Vecteur2D {

    private double vx;
    private double vy;
    
    public Vecteur2D(double vx, double vy) {
        this.vx = vx;
        this.vy = vy;
    }

    /**
     *
     */
    public Vecteur2D() {
        this(0,0);
    }

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    @Override
    public String toString() {
        return "Vecteur2D{" + "vx=" + vx + ", vy=" + vy + '}';
    }
    
}