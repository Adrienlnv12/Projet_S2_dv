/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2.structure.forme;


import fr.insa.leneve.projet_s2.recup.Lire;
import fr.insa.leneve.projet_s2.structure.Terrain.Triangle;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author adrie
 */
public class Point extends Forme{
    
    public static double RAYON_IN_DRAW = 4;

    public double px;
    public double py;
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
    
    @Override
    public void dessine(GraphicsContext context) {
        if(segmentSelected){
            context.setStroke(Color.GREEN);
            context.setLineWidth(2);
            context.strokeOval(this.px-RAYON_IN_DRAW, this.py-RAYON_IN_DRAW, 2*RAYON_IN_DRAW, 2*RAYON_IN_DRAW);
        }else{
            context.setStroke(Color.BLACK);
            context.setLineWidth(2);
            context.strokeOval(this.px-RAYON_IN_DRAW, this.py-RAYON_IN_DRAW, 2*RAYON_IN_DRAW, 2*RAYON_IN_DRAW);
        }
    }
    @Override
    public void dessineSelection(GraphicsContext context) {
        context.setFill(Forme.COULEUR_SELECTION);
        context.fillOval(this.px-RAYON_IN_DRAW, this.py-RAYON_IN_DRAW, 2*RAYON_IN_DRAW, 2*RAYON_IN_DRAW);
    }
    
    @Override
    public void dessinProche(GraphicsContext context) {
        context.setFill(Color.BLUE);
        context.fillOval(this.px-RAYON_IN_DRAW, this.py-RAYON_IN_DRAW, 2*RAYON_IN_DRAW, 2*RAYON_IN_DRAW);
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
        String[] str = new String[]{"  posX : " + px + "  ",
                "  posY : " + py+"  ",
                
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
    public double distancePoint(Point p) {
        double dx = this.px - p.px;
        double dy = this.py - p.py;
        return Math.sqrt(dx*dx+dy*dy);

    }
    
    


}
