/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2.structure.forme;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author adrie
 */
public abstract class Forme{
    
    private Treillis treillis;
    private Color couleur;
    public static Color COULEUR_SELECTION = Color.RED;
    protected boolean selected = false;
    protected int id;
    
    public Treillis getTreillis() {
        return treillis;
    }
    
    public void setTreillis(Treillis treillis) {
        this.treillis = treillis;
    }
    
    public abstract double maxX();

    public abstract double minX();

    public abstract double maxY();

    public abstract double minY();

    public abstract double distancePoint(Point p);
    
    public abstract ArrayList<String> getInfos();
    
    public abstract void dessine(GraphicsContext context);
    //public abstract void dessinProche(GraphicsContext context);
    public abstract void dessineSelection(GraphicsContext context);
    
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    /*public abstract void save(Writer w, Numeroteur<Figure> num) throws IOException;

    public void sauvegarde(File fout) throws IOException {
        Numeroteur<Figure> num = new Numeroteur<Figure>();
        try (BufferedWriter bout = new BufferedWriter(new FileWriter(fout))) {
            this.save(bout, num);
        }
    }

    public static Forme lecture(File fin) throws IOException {
        Numeroteur<Forme> num = new Numeroteur<>();
        Forme derniere = null;
        try (BufferedReader bin = new BufferedReader(new FileReader(fin))) {
            String line;
            while ((line = bin.readLine()) != null && line.length() != 0) {
                String[] bouts = line.split(";");
                if (bouts[0].equals("Point")) {
                    int id = Integer.parseInt(bouts[1]);
                    double px = Double.parseDouble(bouts[2]);
                    double py = Double.parseDouble(bouts[3]);
                    Color col = FigureSimple.parseColor(bouts[4], bouts[5], bouts[6]);
                    Point np = new Point(px, py, col);
                    num.associe(id, np);
                    derniere = np;
                } else if (bouts[0].equals("Segment")) {
                    int id = Integer.parseInt(bouts[1]);
                    int idP1 = Integer.parseInt(bouts[2]);
                    int idP2 = Integer.parseInt(bouts[3]);
                    Color col = FigureSimple.parseColor(bouts[4], bouts[5], bouts[6]);
                    Point p1 = (Point) num.getObj(idP1);
                    Point p2 = (Point) num.getObj(idP2);
                    Segment ns = new Segment(p1, p2, col);
                    num.associe(id, ns);
                    derniere = ns;
                } else if (bouts[0].equals("Groupe")) {
                    int id = Integer.parseInt(bouts[1]);
                    Treillis ng = new Treillis();
                    num.associe(id, ng);
                    for (int i = 2; i < bouts.length; i++) {
                        int idSous = Integer.parseInt(bouts[i]);
                        Forme fig = num.getObj(idSous);
                        ng.add(fig);
                    }
                    derniere = ng;
                }
            }

        }*/
}
