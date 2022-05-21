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
import fr.insa.leneve.projet_s2.structure.Noeud.Noeud;
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
    private final ArrayList<PointTerrain> points = new ArrayList<>();
    private final ArrayList<SegmentTerrain> segments = new ArrayList<>();
    private final ArrayList<Forme> temps = new ArrayList<>();

    public Triangle(PointTerrain pt1,double x, int id){
       
        points.add(pt1);
        PointTerrain pt2 = new PointTerrain(x,pt1.getPy());
        points.add(pt2);
        double px=(pt1.getPx()+pt2.getPx())/2;
        double py=pt1.getPy()+100;
        PointTerrain pt3 = new PointTerrain(px,py);
        points.add(pt3);
        
        
        int j=0;
        for (int i = 0; i < points.size(); i++) {
            SegmentTerrain s = new SegmentTerrain(points.get(i),points.get((i + 1) % 3));


            points.get(i).addSegments(s);
            points.get((i + 1) % 3).addSegments(s);
            
            segments.add(s);
            if(segments.contains(s)){
                j=j+1;
            }
         System.out.println("s: "+j);
        }

        for (PointTerrain point : points) {
            System.out.println(point.toString());
            point.addTriangle(this);
            point.setSelected(false);
        }

        for (SegmentTerrain segment : segments) {
            System.out.println(segment.toString());
            segment.addTriangle(this);
        }

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
        for (int i = 0; i < points.size(); i ++) {
            px[i] = points.get(i).getPx();
            py[i] = points.get(i).getPy();
        }
        context.setFill(Color.GRAY);
        context.setGlobalAlpha(0.5);
        context.fillPolygon(px, py, 3);
        context.setGlobalAlpha(1);

        context.setStroke(Color.GRAY);
        context.strokePolygon(px, py, 3);
    }
    

   

    @Override
    public ArrayList<String> getInfos() {
        ArrayList<String> infos = new ArrayList<>();
        infos.add("premier sommet : ");
        infos.addAll(points.get(0).getInfos());
        infos.add("second sommet : ");
        infos.addAll(points.get(1).getInfos());
        infos.add("troisieme sommet : ");
        infos.addAll(points.get(2).getInfos());

        return infos;
    }

    public String saveString() {
        StringBuilder save = new StringBuilder("Triangle;" + id);
        for (PointTerrain point: points) {
            save.append(";(").append(point.getPx()).append(",").append(point.getPy()).append(")");
        }
        return save.toString();
    }

     public void removeT(Triangle t){
        for (SegmentTerrain segment : (t.getSegments())) {
            temps.add(segment);
        }
        for (PointTerrain point : (t.getPoints())) {
            temps.add(point);
        }
        for (Forme f : temps) {
            if(f instanceof PointTerrain point){
                points.remove(point);
            }
            if(f instanceof SegmentTerrain segment){
                segments.remove(segment);
            }
        }
        temps.clear();
    }
    
    public ArrayList<PointTerrain> getPoints() {
        return points;
    }

    public ArrayList<SegmentTerrain> getSegments() {
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
            double angle = (double) ((int) (Maths.angle(points.get(i), points.get((i + 1) % 3), point) * 100)) / 100;
            if(angle % Math.PI == 0.0){
                pos[i] = 'c';
            }else if(angle > 0){
                pos[i] = 'p';
            }else{
                pos[i] = 'n';
            }


            double distS = segments.get(i).distancePoint(point);
            if(distS != -1) near |= distS < 10;
        }
        boolean colineaire = false;
        for (int i = 0; i < pos.length; i++) {
            colineaire = colineaire || pos[0] == 'c' && Maths.between(point, points.get(i), points.get((i + 1) % 3));
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
        ArrayList<PointTerrain> tPoints = t.getPoints();
        return asPoint(tPoints.get(0)) && asPoint(tPoints.get(1)) && asPoint(tPoints.get(2));
    }

    @Override
    public double maxX() {
        return Math.max(this.points.get(0).maxX(), this.points.get(2).maxX());
    }

    @Override
    public double minX() {
        return Math.min(this.points.get(0).minX(), this.points.get(2).minX());
    }

    @Override
    public double maxY() {
        return Math.max(this.points.get(0).maxY(), this.points.get(2).maxY());
    }

    @Override
    public double minY() {
        return Math.min(this.points.get(0).minY(), this.points.get(2).minY());
    }

    @Override
    public double distancePoint(Point p) {
        double x1 = this.points.get(0).getPx();
        double y1 = this.points.get(0).getPy();
        double x2 = this.points.get(1).getPx();
        double y2 = this.points.get(1).getPy();
        double x3 = this.points.get(2).getPx();
        double y3 = this.points.get(2).getPy();
        double x = p.getPx();
        double y = p.getPy();
        double up1 = ((x - x1) * (x2 - x1) + (y - y1) * (y2 - y1))
                / (Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        double up2 = ((x - x2) * (x3 - x2) + (y - y2) * (y3 - y2))
                / (Math.pow(x3 - x2, 2) + Math.pow(y3 - y2, 2));
        double up3 = ((x - x3) * (x1 - x3) + (y - y3) * (y1 - y3))
                / (Math.pow(x1 - x3, 2) + Math.pow(y1 - y3, 2));
        if(contain(x,y)){  
            return p.distancePoint(p);
        }
        else if ((this.points.get(2).distancePoint(p)<this.points.get(1).distancePoint(p))&&(this.points.get(0).distancePoint(p)<this.points.get(1).distancePoint(p))){
        if (up1 < 0) {
            return this.points.get(1).distancePoint(p);
        } else if (up1 > 1) {
            return this.points.get(0).distancePoint(p);
        } else {
            System.out.println("bonjour");
            Point p4 = new Point(x1 + up1 * (x2 - x1),
                    y1 + up1 * (y2 - y1));
            return p4.distancePoint(p);
           
        }
    } else if ((this.points.get(1).distancePoint(p)<this.points.get(0).distancePoint(p))&&(this.points.get(2).distancePoint(p)<this.points.get(0).distancePoint(p))){
        if (up2 < 0) {
            return this.points.get(1).distancePoint(p);
        } else if (up2 > 1) {
            return this.points.get(2).distancePoint(p);
        } else {
            System.out.println("bonjour1");
            Point p4 = new Point(x2 + up2 * (x3 - x2),
                    y2 + up2 * (y3 - y2));
            return p4.distancePoint(p);
           
        }
    }else{
         if (up3 < 0) {
            return this.points.get(2).distancePoint(p);
        } else if (up3 > 1) {
            return this.points.get(0).distancePoint(p);
        } else {
            System.out.println("bonjour2");
            Point p4 = new Point(x3 + up3 * (x1 - x3),
                    y3 + up3 * (y1 - y3));
            return p4.distancePoint(p);
           
        }
    }
    }

    @Override
    public void dessineSelection(GraphicsContext context) {
         double[] px = new double[3];
        double[] py = new double[3];
        for (int i = 0; i < points.size(); i ++) {
            px[i] = points.get(i).getPx();
            py[i] = points.get(i).getPy();
        }
        context.setFill(Color.GRAY);
        context.setGlobalAlpha(0.5);
        context.fillPolygon(px, py, 3);
        context.setGlobalAlpha(1);

        context.setStroke(Color.RED);
        context.strokePolygon(px, py, 3);
    }

}
