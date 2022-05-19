/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2.structure.Noeud;

import fr.insa.leneve.projet_s2.structure.Terrain.SegmentTerrain;
import fr.insa.leneve.projet_s2.structure.Terrain.Triangle;
import fr.insa.leneve.projet_s2.structure.forme.Forme;
import static fr.insa.leneve.projet_s2.structure.forme.Point.RAYON_IN_DRAW;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;


/**
 *
 * @author adrie
 */
public class NoeudAppuiDouble extends NoeudAppui {
    
    public NoeudAppuiDouble(Triangle associatedTriangle, SegmentTerrain segmentTerrain, double posSegment, int id) {
        super(associatedTriangle, segmentTerrain, posSegment, id);
        this.image = new Image("projet_s2/interfa/imageinfo/NoeudAppuiDouble.png", 28, 19, true, true);
    }

    @Override
    public String saveString() {
        return "AppuiDouble;" + super.saveString();
    }

 
    @Override
    public void dessine(GraphicsContext context) {
        if(selected){
            context.setFill(Forme.COULEUR_SELECTION);
            context.fillOval(this.px-RAYON_IN_DRAW, this.py-RAYON_IN_DRAW, 2*RAYON_IN_DRAW, 2*RAYON_IN_DRAW);
        }
        if(segmentSelected){
            context.setStroke(Color.GREEN);
            context.setLineWidth(2);
            context.strokeOval(this.px-RAYON_IN_DRAW, this.py-RAYON_IN_DRAW, 2*RAYON_IN_DRAW, 2*RAYON_IN_DRAW);
        }else{
            context.setStroke(Color.BLACK);
            context.setLineWidth(2);
            context.strokeOval(this.px-RAYON_IN_DRAW, this.py-RAYON_IN_DRAW, 2*RAYON_IN_DRAW, 2*RAYON_IN_DRAW);
        }
        super.dessine(context);
    }

    

}
