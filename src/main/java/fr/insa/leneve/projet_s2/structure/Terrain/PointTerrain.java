package fr.insa.leneve.projet_s2.structure.Terrain;


import fr.insa.leneve.projet_s2.structure.forme.Forme;
import fr.insa.leneve.projet_s2.structure.forme.Point;
import static fr.insa.leneve.projet_s2.structure.forme.Point.RAYON_IN_DRAW;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * 
 */
public class PointTerrain extends Point {

    private final ArrayList<SegmentTerrain> segments = new ArrayList<>();
    private final ArrayList<Triangle> triangles = new ArrayList<>();

    public PointTerrain(Point p) {
        super(p.getPx(), p.getPy());
    }

    public PointTerrain(double px, double py){
        super(px, py);
    }

    public PointTerrain(double px, double py, int id){
        super(px, py,id);
    }
    
    @Override
    public void dessine(GraphicsContext context) {
        if(selected){
            context.setFill(Forme.COULEUR_SELECTION);
            context.fillOval(this.px-RAYON_IN_DRAW, this.py-RAYON_IN_DRAW, 2*RAYON_IN_DRAW, 2*RAYON_IN_DRAW);
        }if(segmentSelected){
            context.setGlobalAlpha(0.5);
            context.setFill(Color.GREEN);
            context.fillOval(this.px-RAYON_IN_DRAW, this.py-RAYON_IN_DRAW, 2*RAYON_IN_DRAW, 2*RAYON_IN_DRAW);
            context.setGlobalAlpha(0.5);
        }else{
            context.setFill(Color.BROWN);
            context.fillOval(this.px-RAYON_IN_DRAW, this.py-RAYON_IN_DRAW, 2*RAYON_IN_DRAW, 2*RAYON_IN_DRAW);
        }
    }



    public boolean asTriangle(){
        return !triangles.isEmpty();
    }

    public void addTriangle(Triangle triangle){
        triangles.add(triangle);
    }

    public void addSegments(SegmentTerrain segmentTerrain){
        segments.add(segmentTerrain);
    }

    public ArrayList<Triangle> getTriangles() {
        return triangles;
    }

    public ArrayList<SegmentTerrain> getSegments() {
        return segments;
    }


    public boolean EstPoint(PointTerrain pt){
        return this.px == pt.px && this.py == py;
    }
}