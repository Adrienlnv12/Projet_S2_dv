/*
Copyright 2000- Francois de Bertrand de Beuvron

This file is part of CoursBeuvron.

CoursBeuvron is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

CoursBeuvron is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with CoursBeuvron.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.insa.leneve.projet_s2.structure.forme;

import fr.insa.leneve.projet_s2.Numeroteur;
import fr.insa.leneve.projet_s2.calcul.Maths;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author francois
 */
public class Segment extends Forme {

    protected Point debut,fin;
    
    
    public Segment() {
    }

    public Segment(Point d, Point f,int id) {
        this(d, f);
        this.id = id;
    }
    
     public Segment(Point debut, Point fin) {
        this.debut=debut;
        this.fin=fin;
    }

     
    @Override
    public ArrayList<String> getInfos() {
        ArrayList<String> infos = new ArrayList<>();
        infos.add("Premiere extrémité : ");
        infos.addAll(debut.getInfos());
        infos.add("Deuxieme extrémité : ");
        infos.addAll(fin.getInfos());
        /*infos.add("Longueur : " + lengthInfo());*/

        return infos;

    }
    
    public double lengthInfo(){
        return (double) ((int)(Maths.distancePoint(debut, fin) * 100)) / 100;
    }
    
    public double length(){
        return Maths.distancePoint(debut, fin);
    }
    
    public Point getCenter(){
        double px  = (debut.getPx() + fin.getPx()) / 2;
        double py = (debut.getPy() + fin.getPy()) / 2;
        return new Point(px , py);
    }
    
    public Point getDebut() {
        return debut;
    }

    public Point getFin() {
        return fin;
    }
    
    /**
     * @param debut the debut to set
     */
    public void setDebut(Point debut) {
        this.debut = debut;
    }

    /**
     * @param fin the fin to set
     */
    public void setFin(Point fin) {
        this.fin = fin;
    }

    public static Segment demandeSegment() {
        System.out.println("point début : ");
        Point deb = Point.demandePoint();
        System.out.println("point fin : ");
        Point fin = Point.demandePoint();
        return new Segment(deb, fin);
    }

    @Override
    public void dessine(GraphicsContext context) {
        if(selected){
            context.setStroke(Forme.COULEUR_SELECTION);
        } else {
            context.setStroke(Color.BLUE);
        }
        context.strokeLine(this.debut.getPx(), this.debut.getPy(), this.fin.getPx(), this.fin.getPy());
    }


}
