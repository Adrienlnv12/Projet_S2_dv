/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2.structure.Terrain;

/**
 *
 * @author adrie
 */



import fr.insa.leneve.projet_s2.calcul.Maths;
import fr.insa.leneve.projet_s2.structure.forme.Forme;
import fr.insa.leneve.projet_s2.structure.forme.Point;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * 
 */
public class Triangle extends Forme {

    private final int id;
    private final PointTerrain[] points = new PointTerrain[3];
    private final SegmentTerrain[] segments = new SegmentTerrain[3];

    public Triangle(PointTerrain pt1, PointTerrain pt2, PointTerrain pt3, int id,Terrain terrain){

        double max = Maths.max(pt1.getPx(), pt2.getPx(), pt3.getPx());
        if(max == pt1.getPx()){
            points[0] = pt1;
        }else if(max == pt2.getPx()){
            points[0] = pt2;
        }else{
            points[0] = pt3;
        }

        double min = Maths.min(pt1.getPx(), pt2.getPx(), pt3.getPx());
        if(min == pt1.getPx()){
            points[2] = pt1;
        }else if(min == pt2.getPx()){
            points[2] = pt2;
        }else{
            points[2] = pt3;
        }

        if(pt1 != points[0] && pt1 != points[2]){
            points[1] = pt1;
        }else if(pt2 != points[0] && pt2 != points[2]){
            points[1] = pt2;
        }else{
            points[1] = pt3;
        }


        for (int i = 0; i < points.length; i++) {
            SegmentTerrain s = new SegmentTerrain(points[i], points[(i + 1) % 3]);
            SegmentTerrain tempSegment;
            if((tempSegment = terrain.asSegment(s)) != null) s = tempSegment;
            else terrain.addSegment(s);

            points[i].addSegments(s);
            points[(i + 1) % 3].addSegments(s);

            segments[i] = s;
        }

        for (PointTerrain point : points) {
            point.addTriangle(this);
            point.setSelected(false);
        }

        for (SegmentTerrain segment : segments) {
            segment.addTriangle(this);
        }

        System.out.println(id);
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }
    
     @Override
    public void dessine(GraphicsContext context) {
        double[] px = new double[3];
        double[] py = new double[3];
        for (int i = 0; i < points.length; i ++) {
            px[i] = points[i].getPx();
            py[i] = points[i].getPy();
        }
        context.setFill(Color.YELLOW);
        context.setGlobalAlpha(0.5);
        context.fillPolygon(px, py, 3);
        context.setGlobalAlpha(1);

        context.setStroke(Color.YELLOW);
        context.strokePolygon(px, py, 3);
    }
    

   

    @Override
    public ArrayList<String> getInfos() {
        ArrayList<String> infos = new ArrayList<>();
        infos.add("premier sommet : ");
        infos.addAll(points[0].getInfos());
        infos.add("second sommet : ");
        infos.addAll(points[1].getInfos());
        infos.add("troisieme sommet : ");
        infos.addAll(points[2].getInfos());

        return infos;
    }

    public String saveString() {
        StringBuilder save = new StringBuilder("Triangle;" + id);
        for (PointTerrain point: points) {
            save.append(";(").append(point.getPx()).append(",").append(point.getPy()).append(")");
        }
        return save.toString();
    }


    public PointTerrain[] getPoints() {
        return points;
    }

    public SegmentTerrain[] getSegments() {
        return segments;
    }

    public Point getCenter(){
        double px = 0;
        double py = 0;

        for (PointTerrain point : points) {
            px += point.getPx();
            py += point.getPy();
        }
        return new Point(px, py);

    }

    public boolean contain(double posX, double posY){
        char[] pos = new char[3];
        Point point = new Point(posX, posY);
        boolean near = false;
        for (int i = 0; i < pos.length; i++) {
            double angle = (double) ((int) (Maths.angle(points[i], points[(i + 1) % 3], point) * 100)) / 100;
            if(angle % Math.PI == 0.0){
                pos[i] = 'c';
            }else if(angle > 0){
                pos[i] = 'p';
            }else{
                pos[i] = 'n';
            }


            double distS = segments[i].distanceTo(point);
            if(distS != -1) near |= distS < 10;
        }
        boolean colineaire = false;
        for (int i = 0; i < pos.length; i++) {
            colineaire = colineaire || pos[0] == 'c' && Maths.between(point, points[i], points[(i + 1) % 3]);
        }

        return pos[0] == pos[1] && pos[1] == pos[2] || colineaire || near;
    }

    public boolean asPoint(PointTerrain point){
        for (PointTerrain p : points) {
            if(point.EstPoint(p)){
                return true;
            }
        }
        return false;
    }

    public boolean isTriangle(Triangle t){
        PointTerrain[] tPoints = t.getPoints();
        return asPoint(tPoints[0]) && asPoint(tPoints[1]) && asPoint(tPoints[2]);
    }

    @Override
    public double maxX() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public double minX() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public double maxY() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public double minY() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public double distancePoint(Point p) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void dessinProche(GraphicsContext context) {
    }

}
