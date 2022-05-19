/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2.structure.Noeud;

import fr.insa.leneve.projet_s2.Numeroteur;
import fr.insa.leneve.projet_s2.calcul.Maths;
import fr.insa.leneve.projet_s2.structure.Terrain.Terrain;
import fr.insa.leneve.projet_s2.structure.Terrain.Triangle;
import fr.insa.leneve.projet_s2.structure.forme.Treillis;
import fr.insa.leneve.projet_s2.structure.forme.Forme;
import fr.insa.leneve.projet_s2.structure.forme.Point;
import static fr.insa.leneve.projet_s2.structure.forme.Point.RAYON_IN_DRAW;
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
public class NoeudSimple extends Noeud{
    
    // Voici les constructeurs --> comment initialiser un point 
    public NoeudSimple(double px,double py, int id){ // constructeur général qui contient tous les attributs
        super(px,py, id);   
    }
    public NoeudSimple(Point pos, int id) {
        super();
        this.px = pos.getPx();
        this.py = pos.getPy();
        this.id = id;
    }
    
    /**
     *
     * @param px
     * @param py
     */
    public NoeudSimple(double px, double py) {
        super();
        this.px = px;
        this.py = py;
    }
   
    
    public static boolean DistestCreable(Treillis treillis, double px, double py) {//si un point est trop proche alors ca dit pas bon
        boolean creable = true;
        for (Noeud p : treillis.getNoeuds()) {
            if (Maths.distancePoint(p, new Point(px, py)) < 15) creable = false;
        }

        return creable;
    }
    
    public static boolean TriangleestCreable(Terrain terrain, double px, double py){//si un point se creer dans un triangle il dit que c'est pas bon
        boolean creable = true;
        for (Triangle triangle : terrain.getTriangles()) {
            if (triangle.contain(px, py)) creable = false;
        }
        return creable;
    }
    
    public void dessine(GraphicsContext context) {
        if(segmentSelected){
            context.setFill(Color.GREEN);
            context.setLineWidth(2);
            context.fillOval(this.px-RAYON_IN_DRAW, this.py-RAYON_IN_DRAW, 2*RAYON_IN_DRAW, 2*RAYON_IN_DRAW);
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
    public ArrayList<String> getInfos(){
        String[] str = new String[]{"posX : " + px ,
                "posY : " + py,
                "selected : " + selected
        };
        ArrayList<String> output = new ArrayList<>(Arrays.asList(str));

        if(forceApplique != null){
            output.add("Forces :");
            output.addAll(forceApplique.getInfos());
        }

        return output;
    }

    @Override
    public String saveString() {
        return "NoeudSimple;"+ id +";(" + px + "," + py + ")";
    }

}
