/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2.calcul;

import fr.insa.leneve.projet_s2.structure.forme.Point;
import javafx.scene.paint.Color;

/**
 *
 * @author adrie
 */
public class Maths {
    
    
    public static double distancePoint(Point a, Point b) {
        return Math.sqrt(Math.pow(a.getPx() - b.getPx(), 2) + Math.pow(a.getPy() - b.getPy(), 2));
    }

      
    public static double distancePoint(Point a, double x, double y) {
        return Math.sqrt(Math.pow(a.getPx() -x, 2) + Math.pow(a.getPy() - y, 2));
    }

    public static double max(double a, double b, double c){
        if(a > b && a > c){
            return a;
        }else if(b > a && b > c){
            return b;
        }else{
            return c;
        }
    }

    public static double min(double a, double b, double c){
        if(a < b && a < c){
            return a;
        }else if(b < a && b < c){
            return b;
        }else{
            return c;
        }
    }

    //formule pour trouver un angle orienté entre un vecteur et le vecteur horizontal (résultat entre -pi et pi)
    public static double angle(Point o, Point p){
        double op = o.distancePoint(p);

        return Math.acos((p.getPx() - o.getPx()) / op) * (p.getPy() - o.getPy()) / Math.abs((p.getPy() - o.getPy()));
    }

    //formule pour trouver un angle orienté entre deux vecteurs de même origine(résultat entre -pi et pi)
    public static double angle(Point o, Point p1, Point p2){
        double angle1 = angle(o, p1);
        double angle2 = angle(o, p2);

        return Math.PI - ((angle1 - angle2 + 2 * Math.PI) % (2 * Math.PI));
    }

    //retourne si un point est comprit entre deux points ou non
    public static boolean between(Point p, Point p1, Point p2){
        return (p.getPx() <= Math.max(p1.getPx(), p2.getPx())
                && p.getPx() >= Math.min(p1.getPx(), p2.getPx())
                && p.getPy() <= Math.max(p1.getPy(), p2.getPy())
                && p.getPy() >= Math.min(p1.getPy(), p2.getPy()));
    }

    //applique une rotation a un point par rapport a un point d'origine
    public static Point rotation(Point o, Point p, double angle){
        double px0 = p.getPx() - o.getPx();
        double py0 = p.getPy() - o.getPy();

        Point p2 = rotation(new Point(px0, py0), angle);

        return new Point(p2.getPx() + o.getPx(), p2.getPy() + o.getPy());
    }

    //applique une rotation a un point par rapport au point d'origine (0, 0)
    public static Point rotation(Point p, double angle){

        double px = p.getPx() * Math.cos(angle) + p.getPy() * Math.sin(angle);
        double py = p.getPx() * Math.sin(angle) + p.getPy() * Math.cos(angle);

        return new Point(px, py);
    }
    
    //transform a color to an hexadecimal string
    public static String colorToHexa(Color color){
        return Integer.toHexString(color.hashCode()).substring(0, 6);
    }

}
