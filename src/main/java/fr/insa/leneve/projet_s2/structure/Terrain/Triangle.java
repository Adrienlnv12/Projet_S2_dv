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

    public Triangle(PointTerrain pt1,double x, int id,Terrain terrain){
       
        points[0] = pt1;
        PointTerrain pt2 = new PointTerrain(x,pt1.getPy());
        points[1] = pt2;
        double px=(pt1.getPx()+pt2.getPx())/2;
        double py=pt1.getPy()+50;
        PointTerrain pt3 = new PointTerrain(px,py);
        points[2] = pt3;
       


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
        return Math.max(this.points[0].maxX(), this.points[1].maxX());
    }

    @Override
    public double minX() {
        return Math.min(this.points[0].minX(), this.points[1].minX());
    }

    @Override
    public double maxY() {
        return Math.max(this.points[0].maxY(), this.points[2].maxY());
    }

    @Override
    public double minY() {
        return Math.min(this.points[0].minY(), this.points[2].minY());
    }

    @Override
    public double distancePoint(Point p) {
        double x1 = this.points[0].getPx();
        double y1 = this.points[0].getPy();
        double x2 = this.points[1].getPx();
        double y2 = this.points[1].getPy();
        double x3 = this.points[2].getPx();
        double y3 = this.points[2].getPy();
        double x = p.getPx();
        double y = p.getPy();
        double up1 = ((x - x1) * (x2 - x1) + (y - y1) * (y2 - y1))
                / (Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        double up2 = ((x - x2) * (x3 - x2) + (y - y2) * (y3 - y2))
                / (Math.pow(x3 - x2, 2) + Math.pow(y3 - y2, 2));
        double up3 = ((x - x3) * (x1 - x3) + (y - y3) * (y1 - y3))
                / (Math.pow(x1 - x3, 2) + Math.pow(y1 - y2, 2));
        if(contain(x,y)){  
            return p.distancePoint(p);
        }
        else if ((this.points[0].distancePoint(p)<this.points[2].distancePoint(p))&&(this.points[1].distancePoint(p)<this.points[2].distancePoint(p))){
        if (up1 < 0) {
            return this.points[0].distancePoint(p);
        } else if (up1 > 1) {
            return this.points[1].distancePoint(p);
        } else {
            Point p4 = new Point(x1 + up1 * (x2 - x1),
                    y1 + up1 * (y2 - y1));
            return p4.distancePoint(p);
           
        }
    } else if ((this.points[1].distancePoint(p)<this.points[0].distancePoint(p))&&(this.points[2].distancePoint(p)<this.points[0].distancePoint(p))){
        if (up2 < 0) {
            return this.points[1].distancePoint(p);
        } else if (up2 > 1) {
            return this.points[2].distancePoint(p);
        } else {
            Point p4 = new Point(x1 + up2 * (x2 - x1),
                    y1 + up2 * (y2 - y1));
            return p4.distancePoint(p);
           
        }
    }else{
         if (up3 < 0) {
            return this.points[0].distancePoint(p);
        } else if (up3 > 1) {
            return this.points[2].distancePoint(p);
        } else {
            Point p4 = new Point(x1 + up3 * (x2 - x1),
                    y1 + up3 * (y2 - y1));
            return p4.distancePoint(p);
           
        }
    }
    }
    
    @Override
    public void dessinProche(GraphicsContext context) {
    }

    @Override
    public void dessineSelection(GraphicsContext context) {
         double[] px = new double[3];
        double[] py = new double[3];
        for (int i = 0; i < points.length; i ++) {
            px[i] = points[i].getPx();
            py[i] = points[i].getPy();
        }
        context.setFill(Color.GRAY);
        context.setGlobalAlpha(0.5);
        context.fillPolygon(px, py, 3);
        context.setGlobalAlpha(1);

        context.setStroke(Color.RED);
        context.strokePolygon(px, py, 3);
    }

}
