/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2;

import java.io.IOException;
import java.io.Writer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author adrie
 */
public class Barre extends FigureSimple {
        
    public TypedeBarre typebarre; // chaque barre a un type 
    public int id;
    public Noeud debut;
    public Noeud fin;

    /*private double traction;
    private double compression;
    private double prix;
    
    /**
     * @param debut the debut to set
     */
    public void setDebut(Noeud debut) {
        this.debut = debut;
    }

    /**
     * @param fin the fin to set
     */
    public void setFin(Noeud fin) {
        this.fin = fin;
    }

    public Barre(/*TypedeBarre Tb*/ Noeud debut, Noeud fin,Color couleur) {
       /* this.typebarre = Tb;*/
        this.debut = debut;
        this.fin = fin;
    }
    
    public Barre(/*TypedeBarre Tb*/Noeud debut, Noeud fin) {
        this(/*Tb*/debut, fin, Color.BLUE);
    }
    
    public TypedeBarre gettypebarre(){
        return(this.typebarre);
    }
    
    public Noeud getDebut() {
        return debut;
    }

    public Noeud getFin() {
        return fin;
    }
    public void setIdBarre(int id) {
        this.id = id;
    }
    
    public int getIdBarre() {
        return (this.id);
    }
    
   
    public void settypebarre(TypedeBarre T){
        this.typebarre=T;
    }
    
    public static Barre demandeBarre() {
        /*System.out.println("Donnez le type de la barre");
        TypedeBarre Tb = TypedeBarre.demandeTypedeBarre();*/
        System.out.println("point début : ");
        Noeud deb = Noeud.demandeNoeud();
        System.out.println("point fin : ");
        Noeud fin = Noeud.demandeNoeud();
        return new Barre(/*Tb*/deb, fin);
    }
    
    @Override
    public String toString() {
        return "barre{" + "typebarre=" + typebarre + ", id=" + id + ", noeud1=" + debut + ", noeud2=" + fin + '}';
    }
    
    public double Longueur_barre(Barre B){
        double longueur_barre;
      
        // on va utiliser la formule mathématique de la norme 
      
        longueur_barre = Math.sqrt(Math.pow(B.fin.getPx()-B.debut.getPx(),2)+Math.pow(B.fin.getPy()-B.debut.getPy(),2));
      
        return(longueur_barre);
    }
    //cette méthode devrait permettre de recup l'angle entre la barre et Ox
    
    public double AngleBarre(Barre B){
            double angle = 0;            
            angle= Math.acos(Math.abs(B.fin.getPx()-B.debut.getPx())/Longueur_barre(B));
            return(angle);  
        /*cette partie commentaire va expliquer la méthode angle 
        cette méthode permet de recupérer en théorie l'angle entre une barre et l'axe des abscisses
        Cet angle est utilisé dans les équations d'équilibre statique. Une fois que l'on a l'angle, dans les cas d'un sytème isostasique 
        on peut résoudre le système. 
        Donc une condition de résolution du système un treillis isostasique */
            
    }
    
    public double getAngleBarre(Barre B){
        return(AngleBarre(B));
    }
    
    @Override
    public double maxX() {
        return Math.max(this.debut.maxX(), this.fin.maxX());
    }

    @Override
    public double minX() {
        return Math.min(this.debut.minX(), this.fin.minX());
    }

    @Override
    public double maxY() {
        return Math.max(this.debut.maxY(), this.fin.maxY());
    }

    @Override
    public double minY() {
        return Math.min(this.debut.minY(), this.fin.minY());
    }
    
    @Override
    public double distanceNoeud(Noeud p) {
        double x1 = this.debut.getPx();
        double y1 = this.debut.getPy();
        double x2 = this.fin.getPx();
        double y2 = this.fin.getPy();
        double x3 = p.getPx();
        double y3 = p.getPy();
        double up = ((x3 - x1) * (x2 - x1) + (y3 - y1) * (y2 - y1))
                / (Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        if (up < 0) {
            return this.debut.distanceNoeud(p);
        } else if (up > 1) {
            return this.fin.distanceNoeud(p);
        } else {
            Noeud p4 = new Noeud(x1 + up * (x2 - x1),
                    y1 + up * (y2 - y1));
            return p4.distanceNoeud(p);
        }
    }
    
    @Override
    public void dessine(GraphicsContext context) {
        context.setStroke(this.getCouleur());
        context.strokeLine(this.debut.getPx(), this.debut.getPy(), this.fin.getPx(), this.fin.getPy());
    }

    @Override
    public void dessineSelection(GraphicsContext context) {
        context.setStroke(Figure.COULEUR_SELECTION);
        context.strokeLine(this.debut.getPx(), this.debut.getPy(), this.fin.getPx(), this.fin.getPy());
    }

    @Override
    public void save(Writer w, Numeroteur<Figure> num) throws IOException {
        if (!num.objExist(this)) {
            id = num.creeID(this);
            this.debut.save(w, num);
            this.fin.save(w, num);
            w.append("Barre;" + id + ";" +
                    num.getID(this.debut) + ";" + num.getID(this.fin) +
                    ";" + FigureSimple.saveColor(this.getCouleur())+"\n");
        }
    }
}
