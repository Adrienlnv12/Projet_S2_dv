/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2;

import javafx.scene.Group;

/**
 *
 * @author adrie
 */
public abstract class Figure {

    /**
     * @param contenuDans the contenuDans to set
     */
    public void setContenuDans(Groupe contenuDans) {
        this.contenuDans = contenuDans;
    }

    /**
     * @return the contenuDans
     */
    public Groupe getContenuDans() {
        return contenuDans;
    }
    
    /**
     * groupe dans lequel est inclue la figure.
     * null si aucun contenant
     */
    private Groupe contenuDans;
    
    public Figure() {
        this.contenuDans = null;
    }
    
    public abstract double maxX() ;
   public  double minX() {
       // TODO
       return 0;
   }
   
   public double largeur() {
       return this.maxX() - this.minX();
   }
   
   public abstract Group dessine() ;
    
}
