package fr.insa.leneve.projet_s2.structure.Terrain;



import fr.insa.leneve.projet_s2.calcul.Maths;
import fr.insa.leneve.projet_s2.structure.forme.Forme;
import fr.insa.leneve.projet_s2.structure.forme.Point;
import fr.insa.leneve.projet_s2.structure.forme.Segment;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import javafx.scene.paint.Color;

public class SegmentTerrain extends Segment {

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

        return distanceTo(p) < tolerance && distanceTo(p) != -1;
    }

    @Override
    public ArrayList<String> getInfos() {
        ArrayList<String> infos = super.getInfos();
        infos.add("triangles : " + triangles.size());
        return infos;
    }

    public double distanceTo(Point p){
        Point p2 = Maths.rotation(debut, p, angle);

        if(Maths.distancePoint(getCenter(), p) > length() / 2){
            return -1;
        }
        return Math.sqrt(Math.pow(Maths.distancePoint(debut, p), 2) - Math.pow(p2.getPx() - debut.getPx(), 2));
    }

    public boolean asOneTriangle() {
        return triangles.size() == 1;
    }


}