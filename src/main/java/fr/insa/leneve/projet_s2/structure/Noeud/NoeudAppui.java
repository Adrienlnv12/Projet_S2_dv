/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2.structure.Noeud;

import fr.insa.leneve.projet_s2.calcul.Maths;
import fr.insa.leneve.projet_s2.structure.Terrain.PointTerrain;
import fr.insa.leneve.projet_s2.structure.Terrain.SegmentTerrain;

import fr.insa.leneve.projet_s2.structure.Terrain.Triangle;
import fr.insa.leneve.projet_s2.structure.forme.Point;
import fr.insa.leneve.projet_s2.structure.forme.Treillis;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

/**
 *
 * @author adrie
 */
public class NoeudAppui extends Noeud {
    
    private final Triangle associatedTriangle;
    private final PointTerrain segmentPoint;
    private final SegmentTerrain segmentTerrain;
    private final double posSegment;
    protected Image image;

    public NoeudAppui(Triangle associatedTriangle, SegmentTerrain segmentTerrain, double posSegment, int id){
        super();
        this.associatedTriangle = associatedTriangle;
        this.id = id;
        this.segmentTerrain = segmentTerrain;
        this.segmentPoint = segmentTerrain.getDebut();
        this.posSegment = posSegment;

        this.px = segmentPoint.getPx()+(segmentTerrain.getFin().getPx() - segmentPoint.getPx()) * posSegment;
        this.py = segmentPoint.getPy()+(segmentTerrain.getFin().getPy() - segmentPoint.getPy()) * posSegment;
    }
    
    public static boolean DistestCreable(Treillis treillis, double px, double py) {//si un point est trop proche alors ca dit pas bon
        boolean creable = true;
        for (Noeud p : treillis.getNoeuds()) {
            if (Maths.distancePoint(p, new Point(px, py)) < 15) creable = false;
        }

        return creable;
    }
    
    public double getPosSegment() {
        return posSegment;
    }

    public PointTerrain getSegmentPoint() {
        return segmentPoint;
    }

    public Triangle getAssociatedTriangle() {
        return associatedTriangle;
    }

    public SegmentTerrain getSegmentTerrain() {
        return segmentTerrain;
    }

    public static SegmentTerrain isCreable(Triangle triangle ,double posX, double posY){
        for (SegmentTerrain s : triangle.getSegments()) {
            if(s.contain(posX, posY, 50) && s.asOneTriangle()){
                return s;
            }
        }
        return null;
    }

    @Override
    public String saveString()  {
        int segmentNbr = 0;
        for (int i = 0; i < associatedTriangle.getSegments().size(); i++) {
            if(associatedTriangle.getSegments().get(i).equals(segmentTerrain)){
                segmentNbr = i;
                break;
            }
        }
        return id + ";" + associatedTriangle.getId() + ";("  + segmentNbr + ");(" + posSegment+")";
    }
    
    @Override
    public ArrayList<String> getInfos(){ 
        String[] str = new String[]{"  posX : " + px+"  " ,
                "  posY : " + py+"  ",
        };
        ArrayList<String> output = new ArrayList<>(Arrays.asList(str));

        if(forceApplique != null){
            output.add("Forces :");
            output.addAll(forceApplique.getInfos());
        }

        return output;
    }

    //dessine l'image corrrespondante Ã  l'appui avec rotation
    @Override
    public void dessine(GraphicsContext context) {
        context.save();
        // angle de rotation et point pivot
        Point centerTriangle = associatedTriangle.getCenter();
        double angleCenter = (double) ((int) (Maths.angle(segmentTerrain.getFin(), segmentTerrain.getDebut(), centerTriangle) * 100)) / 100;
        double angle = Maths.angle(segmentTerrain.getFin(), segmentTerrain.getDebut()) * 360 / (2 * Math.PI);
        if(angleCenter > 0){
            angle = 180 + angle;
        }
        Rotate r = new Rotate(angle, px , py );
        context.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
        //context.drawImage(image, px - image.getWidth()/2 , py );
        context.restore();
    }
    
    @Override
    public void dessineSelection(GraphicsContext context) {
    }
    
    @Override
    public void dessinProche(GraphicsContext context) {
    }
    
    
    
    
}
