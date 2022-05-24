/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2.structure.Terrain;



import fr.insa.leneve.projet_s2.calcul.Maths;
import fr.insa.leneve.projet_s2.structure.forme.Forme;
import fr.insa.leneve.projet_s2.structure.forme.Point;
import fr.insa.leneve.projet_s2.structure.forme.Segment;
import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import javafx.scene.paint.Color;

/**
 *
 * @author adrie
 */
public class SegmentTerrain extends Segment {

    private final ArrayList<Forme> temps = new ArrayList<>();
    private final ArrayList<Triangle> triangles = new ArrayList<>();
    private final double angle;

    public SegmentTerrain(PointTerrain pA, PointTerrain pB) {
        super(pA, pB);
        angle = Maths.angle(pA, pB);
    }
    
    @Override
    public void dessine(GraphicsContext context) {
        if(selected){
            context.setStroke(Forme.COULEUR_SELECTION);
        }else {
            context.setStroke(Color.BROWN);
        }
        context.strokeLine(this.debut.getPx(), this.debut.getPy(), this.fin.getPx(), this.fin.getPy());
    }


    public ArrayList<Triangle> getTriangles() {
        return triangles;
    }

    @Override
    public PointTerrain getDebut() {
        return (PointTerrain) super.getDebut();
    }
    
    @Override
    public PointTerrain getFin() {
        return (PointTerrain) super.getFin();
    }

    public void addTriangle(Triangle triangle) {
        triangles.add(triangle);
    }

    public boolean isSegment(SegmentTerrain s){
        return s.debut == debut && s.fin == fin || s.debut == fin && s.fin == fin;
    }

    // test si un point est sur le segment avec une certaine tolérance
    public boolean contain(double x, double y, int tolerance){
        Point p = new Point(x, y);

        return distancePoint(p) < tolerance && distancePoint(p) != -1;
    }

    @Override
    public ArrayList<String> getInfos() {
        ArrayList<String> infos = super.getInfos();
        infos.add("triangles : " + triangles.size());
        return infos;
    }
    
    @Override
    public String toString() {
        return "[" + this.debut + "," + this.fin + ']';
    }
    
    public void removeS(SegmentTerrain t){
        for (Triangle triangle : (t.getTriangles())) {
            temps.add(triangle);
        }
        for (Forme f : temps) {
            if(f instanceof Triangle){
                triangles.remove(f);
            }
        }
        temps.clear();
    }
    
    
    @Override
    public double distancePoint(Point p) {
        double x1 = this.debut.getPx();
        double y1 = this.debut.getPy();
        double x2 = this.fin.getPx();
        double y2 = this.fin.getPy();
        double x3 = p.getPx();
        double y3 = p.getPy();
        double up = ((x3 - x1) * (x2 - x1) + (y3 - y1) * (y2 - y1))
                / (Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        if (up < 0) {
            return this.debut.distancePoint(p);
        } else if (up > 1) {
            return this.fin.distancePoint(p);
        } else {
            Point p4 = new Point(x1 + up * (x2 - x1),
                    y1 + up * (y2 - y1));
            return p4.distancePoint(p);
        }
    }

    public boolean asOneTriangle() {
        return triangles.size() == 1;
    }


}