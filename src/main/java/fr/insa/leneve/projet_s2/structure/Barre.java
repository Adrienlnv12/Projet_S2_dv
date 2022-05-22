/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2.structure;
//a finir
import fr.insa.leneve.projet_s2.structure.forme.Segment;
import fr.insa.leneve.projet_s2.structure.Noeud.Noeud;
import fr.insa.leneve.projet_s2.structure.forme.Forme;
import static fr.insa.leneve.projet_s2.structure.forme.Point.RAYON_IN_DRAW;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author adrie
 */
public class Barre extends Segment {
    
    public Barre(Noeud debut, Noeud fin, int id) {
        super(debut, fin);
        this.id = id;
    }
    
    @Override
    public ArrayList<String> getInfos() {
        ArrayList<String> infos = super.getInfos();
        infos.add(0,"  Barre n°"+id);

        return infos;

    }
    
     public String saveString() {

        return "Barre;" + id + ";" + debut.getId() + ";" + fin.getId();
    }
     
    @Override
    public void dessine(GraphicsContext context) {
        if(selected){
            context.setStroke(Color.RED);
        }else {
            context.setStroke(Color.BLACK);
        }
        context.setLineWidth(3);
        context.strokeLine(debut.getPx(), debut.getPy(),fin.getPx(), fin.getPy());
    }

    
    @Override
    public String toString() {
        return "barre{" +  ", id=" + id + ", noeud1=" + debut + ", noeud2=" + fin + '}';
    }
    
    public double Longueur_barre(Barre B){
        double longueur_barre;
      
        // on va utiliser la formule mathématique de la norme 
      
        longueur_barre = Math.sqrt(Math.pow(B.fin.getPx()-B.debut.getPx(),2)+Math.pow(B.fin.getPy()-B.debut.getPy(),2));
      
        return(longueur_barre);
    }
    //cette méthode devrait permettre de recup l'angle entre la barre et Ox
    
    public double AngleBarre(Barre B){
            double angle = 0;            
            angle= Math.acos(Math.abs(B.fin.getPx()-B.debut.getPx())/Longueur_barre(B));
            return(angle);  
        /*cette partie commentaire va expliquer la méthode angle 
        cette méthode permet de recupérer en théorie l'angle entre une barre et l'axe des abscisses
        Cet angle est utilisé dans les équations d'équilibre statique. Une fois que l'on a l'angle, dans les cas d'un sytème isostasique 
        on peut résoudre le système. 
        Donc une condition de résolution du système un treillis isostasique */
            
    }
    
    public double getAngleBarre(Barre B){
        return(AngleBarre(B));
    }
    
    
}
