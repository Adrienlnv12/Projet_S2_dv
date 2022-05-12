/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2.structure.forme;


import fr.insa.leneve.projet_s2.recup.Lire;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author adrie
 */
public class Point extends Forme{
    public static double RAYON_IN_DRAW = 5;

    protected double px;
    protected double py;
    protected boolean segmentSelected;
    
    public Point(double px, double py, int id) {
        this.id = id;
        this.px = px;
        this.py = py;
    }

    public Point(double px, double py) {
        this(px, py,-1);
    }

    public Point() {
        this(0, 0,-1);
    }
    
    /**
     * @return the px
     */
    public double getPx() {
        return px;
    }

    /**
     * @param px the px to set
     */
    public void setPx(double px) {
        this.px = px;
    }

    /**
     * @return the py
     */
    public double getPy() {
        return py;
    }

    /**
     * @param py the py to set
     */
    public void setPy(double py) {
        this.py = py;
    }


    public static Point demandePoint() {
        System.out.println("abscisse : ");
        double px = Lire.d();
        System.out.println("ordonnée : ");
        double py = Lire.d();
        return new Point(px, py);
    }    

    @Override
    public void dessine(GraphicsContext context) {
        if(selected){
            context.setFill(Forme.COULEUR_SELECTION);
            context.fillOval(this.px-RAYON_IN_DRAW, this.py-RAYON_IN_DRAW, 2*RAYON_IN_DRAW, 2*RAYON_IN_DRAW);
        }
        if(segmentSelected){
            context.setFill(Color.GREEN);
            context.setLineWidth(2);
            context.fillOval(this.px-RAYON_IN_DRAW, this.py-RAYON_IN_DRAW, 2*RAYON_IN_DRAW, 2*RAYON_IN_DRAW);
        }else{
            context.setFill(Color.BLACK);
            context.setLineWidth(2);
            context.fillOval(this.px-RAYON_IN_DRAW, this.py-RAYON_IN_DRAW, 2*RAYON_IN_DRAW, 2*RAYON_IN_DRAW);
        }
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id){
        this.id = id;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public ArrayList<String> getInfos(){
        String[] str = new String[]{"posX : " + px ,
                "posY : " + py,
                "selected : " + selected
        };
        return new ArrayList<>(Arrays.asList(str));
    }

    public void setSegmentSelected(boolean segmentSelected) {
        this.segmentSelected = segmentSelected;
    }
    
    public Point add(Point p){
        return new Point(p.px + px, p.py + py);
    }

    public Point substract(Point p){
        return new Point(- p.px + px, - p.py + py);
    }



}
