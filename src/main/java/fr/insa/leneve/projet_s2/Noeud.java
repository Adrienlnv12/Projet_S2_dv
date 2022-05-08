/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2;


import fr.insa.leneve.projet_s2.recup.Lire;
import java.io.IOException;
import java.io.Writer;
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author adrie
 */
public class Noeud extends FigureSimple {
    
    public static double RAYON_IN_DRAW = 5;
    double px;
    double py;
    public  int id; // identificateur pour repérer le n° du noeud 
     
    /*public class Poids {
        
        public Poids(){
            this.px = 0;
            this.py = 0;
        }
    }
     
    public Poids Poids(Barre B){
        Poids poids  = new Poids();
        double angle = B.getAngleBarre(B);
        if(B.Noeud1 instanceof NoeudSimple){
            
        }
        return(poids);
    }*/
     
    public Noeud(double px, double py, Color couleur) {
        super(couleur);
        this.px = px;
        this.py = py;
    }
    
    public Noeud(double px, double py) {
        this(px, py, Color.BLACK);
    }

    public Noeud() {
        this(0, 0);
    }

    /**
     * initialise comme une copie du point
     * @param modele
     */
    public Noeud(Noeud modele) {
        this(modele.px,modele.px,modele.getCouleur());
    }
    
    public static Noeud demandeNoeud() {
        System.out.println("abscisse : ");
        double px = Lire.d();
        System.out.println("ordonnée : ");
        double py = Lire.d();
        return new Noeud(px, py);
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
    
     @Override
    public double maxX() {
        return this.px;
    }

    @Override
    public double minX() {
        return this.px;
    }

    @Override
    public double maxY() {
        return this.py;
    }

    @Override
    public double minY() {
        return this.py;
    }
    
    
    @Override
    public double distanceNoeud(Noeud p) {
        double dx = this.px - p.px;
        double dy = this.py - p.py;
        return Math.sqrt(dx*dx+dy*dy);

    }
    
    @Override
    public void dessine(GraphicsContext context) {
        context.setFill(this.getCouleur());
        context.fillOval(this.px-RAYON_IN_DRAW, this.py-RAYON_IN_DRAW, 2*RAYON_IN_DRAW, 2*RAYON_IN_DRAW);
    }
    
    @Override
    public void dessineSelection(GraphicsContext context) {
        context.setFill(Figure.COULEUR_SELECTION);
        context.fillOval(this.px-RAYON_IN_DRAW, this.py-RAYON_IN_DRAW, 2*RAYON_IN_DRAW, 2*RAYON_IN_DRAW);
    }
    
    @Override
     public void save(Writer w, Numeroteur<Figure> num) throws IOException {
        if(! num.objExist(this)) {
            id = num.creeID(this);
            w.append("Noeud;"+id+";"+this.px+";"+this.py+
                    ";" + FigureSimple.saveColor(this.getCouleur()) + "\n");
        }
    }
 
}
