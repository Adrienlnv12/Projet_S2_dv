/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2;

import java.awt.Color;
/**
 *
 * @author adrie
 */
public class Barre {
        
    private int id; /*g*/
    private Noeud Noeuddepart;
    private Noeud Noeudarrivee;
    private double traction;
    private double compression;
    private double prix;
    
    public Barre(Noeud Noeuddepart, Noeud Noeudarrivee/*, double traction,double compression,double prix, int id*/) {
        
        this.Noeuddepart = Noeuddepart;
        this.Noeudarrivee = Noeudarrivee;
        /*this.id=id
        this.traction = traction;
        this.compression = compression;
        this.prix = prix;*/
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Noeud getNoeuddepart() {
        return Noeuddepart;
    }

    public void setNoeuddepart(Noeud Noeuddepart) {
        this.Noeuddepart = Noeuddepart;
    }

    public Noeud getNoeudarrivee() {
        return Noeudarrivee;
    }

    public void setNoeudarrivee(Noeud Noeudarrivee) {
        this.Noeudarrivee = Noeudarrivee;
    }

    public double getTraction() {
        return traction;
    }

    public void setTraction(double traction) {
        this.traction = traction;
    }

    public double getCompression() {
        return compression;
    }

    public void setCompression(double compression) {
        this.compression = compression;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    @Override
    public String toString() {
        return "Barre{" + "id=" + id + ", Noeuddepart=" + Noeuddepart + ", Noeudarrivee=" + Noeudarrivee + ", traction=" + traction + ", compression=" + compression + ", prix=" + prix + '}';
    }

}
