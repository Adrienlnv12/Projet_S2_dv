/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2;

import fr.insa.leneve.projet_s2.recup.Lire;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.scene.shape.Ellipse;
/**
 *
 * @author adrie
 */
public class Noeud {
    
    public static final double TAILLE_POINT = 5;
    
    double px;
    double py;
    private Color couleur;
    public  int id; // identificateur pour repérer le n° du noeud 
    

    
      public class Poids {
        double px;
        double py;
        
        public Poids(){
            this.px = 0;
            this.py = 0;
        }
    }
     
    /*public Poids Poids(Barre B){
        Poids poids  = new Poids();
        double angle = B.getAngleBarre(B);
        if(B.Noeud1 instanceof NoeudSimple){
            
        }
        return(poids);
    }*/
     
    public Noeud(double px, double py, Color couleur, Numeroteur<Noeud> N) {
        this.couleur = couleur;
        id = N.genererIdLibre();// constructeur de la super classe pour initialiser id 
        this.px = px;
        this.py = py;
    }
    
    public String toString() {
        return "Noeud{" + "id=" + id + '}';
    }
     public int getIdNoeud(){
        return(this.id);
    }
     
    public Noeud(double px, double py,Numeroteur<Noeud> N) {
        this(px,py,Color.BLACK,N);
        
    }
    public Noeud(Numeroteur<Noeud> N) {
        this(0,0,N);
    }

    public double getPx() {
        return px;
    }

    public void setPx(double px) {
        this.px = px;
    }

    public double getPy() {
        return py;
    }

    public void setPy(double py) {
        this.py = py;
    }
    
    public Color getCouleur() {
        return couleur;
    }

    public void setCouleur(Color couleur) {
        this.couleur = couleur;
    }
    
    /*public Noeud entreeNoeud(){
        int Rep=3;
        System.out.println("Donnez l'abscisse de votre noeud");
        double Px=Lire.d();
        System.out.println("Donnez l'ordonnée de votre noeud");
        double Py=Lire.d();
        System.out.println("Donnez l'identifiant de votre noeud");
        while (Rep==3){
            System.out.println("Donnez son type");
            System.out.println("Tapez 0 pour un noeud simple");
            System.out.println("Tapez 1 pour un appui simple");
            System.out.println("Tapez 2 pour un appui double");
            Rep=Lire.i();
            if((Rep!=1)&&(Rep!=2)&&(Rep!=0)){
                System.out.println("Donnez un nombre compris entre 0 et 2");
                Rep=3;
            }
        }
        switch (Rep) {
            case 0 -> {
                System.out.println("Le type de noeud est un noeud simple");
                return new NoeudSimple(Px,Py,Color.BLACK);
            }
            case 1 -> {
                System.out.println("Le type de noeud est un appui simple");
                return new NoeudAppuiSimple(Px,Py,Color.BLACK);
            }
            case 2 -> {
                System.out.println("Le type de noeud est un appui double");
                return new NoeudAppuiDouble(Px,Py,Color.BLACK);
            }
            default -> {
            }
        }
        return null;    
    }*/
   /* public int nbrInconnues(){
        
        return couleur;
    }*/
    
      public Group dessine() {
        Ellipse res = new Ellipse(this.px, this.py,TAILLE_POINT, TAILLE_POINT);
        Group g = new Group(res);
        return g;
    }
    public static void main(String[]args){
     
       Numeroteur<Noeud> num = new Numeroteur();
       Noeud N = new Noeud(num);
       String type_objet;
       type_objet = N.getClass().getSimpleName();
       System.out.println("ceci est le type de cet objet "+type_objet);
       System.out.println("saisir une valeur de a ");
       
      
   }
 
}
