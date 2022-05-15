package fr.insa.leneve.projet_s2.structure.Terrain;



import fr.insa.leneve.projet_s2.structure.forme.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Terrain {

    private boolean selected = false;

    private HashMap<Integer, Triangle> triangles = new HashMap<>();

    private final ArrayList<PointTerrain> points = new ArrayList<>();
    private final ArrayList<SegmentTerrain> segments = new ArrayList<>();

    public Terrain() {
    }

    //verifie si un point est dans la zone constructible mais pas dans les triangles
    public boolean containOutTriangle(double x, double y){
        for (Triangle triangle : triangles.values()) {
            if(triangle.contain(x, y)) return false;
        }
        return true;
    }

    //get et set
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Collection<Triangle> getTriangles() {
        return triangles.values();
    }

    public ArrayList<PointTerrain> getPoints() {
        return points;
    }

    public ArrayList<SegmentTerrain> getSegments() {
        return segments;
    }

    public Triangle getTriangle(int id){
        return triangles.get(id);
    }

    public void addTriangle(Triangle t){
        for (Triangle triangle: triangles.values()) {
            if (triangle.isTriangle(t)) {
                return;
            }
        }
        triangles.put(t.getId(), t);
    }

    public PointTerrain addPoint(double x, double y){
        PointTerrain pt = new PointTerrain(x, y);
        points.add(pt);
        return pt;
    }

    public void addPoint(PointTerrain pt){
        if(points.contains(pt)) return;
        points.add(pt);
    }

    public void addSegment(SegmentTerrain s){
        if(segments.contains(s)) return;
        segments.add(s);
    }

    public boolean isSelected() {
        return selected;
    }

    public SegmentTerrain asSegment(SegmentTerrain s) {
        for (SegmentTerrain segment : segments) {
            if(s.isSegment(segment)) return segment;
        }
        return null;
    }

    public void update(){
        ArrayList<PointTerrain> pointTerrain = new ArrayList<>();
        for (PointTerrain point : points) {
            if (!point.asTriangle()) pointTerrain.add(point);
        }
        for (PointTerrain pt : pointTerrain) {
            remove(pt, false);
        }
    }

    public void remove(Forme f, boolean last){
        if(f instanceof Triangle){
            triangles.remove(f.getId());
            if(last) return;
            for (SegmentTerrain segment : ((Triangle) f).getSegments()) {
                remove(segment, true);
            }
            for (PointTerrain point : ((Triangle) f).getPoints()) {
                remove(point, true);
            }
        }else if(f instanceof PointTerrain){
            points.remove((PointTerrain) f);
            if (last) return;
            ((PointTerrain) f).getSegments().forEach(s -> remove(s, true));
            ((PointTerrain) f).getTriangles().forEach(t -> remove(t, true));
        }else if(f instanceof SegmentTerrain){
            segments.remove(f);
            if(last) return;
            ((SegmentTerrain) f).getTriangles().forEach(t -> remove(t, true));
        }
    }

    // fonction de dessin
    public void draw(GraphicsContext gc, boolean drawBorder, Point origin){
        if(!triangles.isEmpty()) triangles.values().forEach(t -> t.dessine(gc));
        if(!points.isEmpty()) points.forEach(p -> p.dessine(gc));
        if(!segments.isEmpty()) segments.forEach(s -> s.dessine(gc));
    }

}