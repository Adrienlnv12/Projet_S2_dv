/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2;

import javafx.scene.paint.Color;


/**
 *
 * @author adrie
 */
public class NoeudAppuiSimple extends NoeudAppui {
    public NoeudAppuiSimple(double px, double py,Color c) {
        super(px,py,c);
    }
    @Override
    public String toString() {
        return "Noeud{" + "px=" + getPx() + ", py=" + getPy() + ", couleur=" + getCouleur() + '}';
    }
}