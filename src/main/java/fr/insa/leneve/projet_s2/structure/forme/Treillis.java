package fr.insa.leneve.projet_s2.structure.forme;



import fr.insa.leneve.projet_s2.Numerateur;
import fr.insa.leneve.projet_s2.interfa.MainPanel;
import fr.insa.leneve.projet_s2.structure.Barre;
import fr.insa.leneve.projet_s2.structure.Noeud.*;
import fr.insa.leneve.projet_s2.structure.Terrain.PointTerrain;
import fr.insa.leneve.projet_s2.structure.Terrain.SegmentTerrain;
import fr.insa.leneve.projet_s2.structure.Terrain.Terrain;
import fr.insa.leneve.projet_s2.structure.Terrain.Triangle;
import fr.insa.leneve.projet_s2.structure.forme.Forme;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Treillis extends Forme{
    
    private ArrayList<Forme> contient;
    private Terrain terrain;
    private final ArrayList<Noeud> noeuds;
    private final ArrayList<Barre> barres;
    private final Numerateur numerateur;


    public Treillis() {
        this.contient = new ArrayList<>();
        this.numerateur = new Numerateur();
        this.noeuds = new ArrayList<>();
        this.barres = new ArrayList<>();
        this.terrain = new Terrain();
    }

    public void add(Forme f) {
        if (f.getTreillis() != this) {
            if (f.getTreillis() != null) {
                throw new Error("figure déja dans un autre groupe");
            }
            else if (f instanceof Noeud noeud){
                this.noeuds.add(noeud);
            }
            else if(f instanceof Barre barre){
                this.barres.add(barre);
            }
            this.contient.add(f);
            f.setTreillis(this);
        }
    }

    public ArrayList<Forme> getContient() {
        return contient;
    }
    
    public void setTerrain(Terrain terrain){
        this.terrain = terrain;
    }
    
    public Terrain getTerrain() {
        return terrain;
    }
    
    public void addBarres(Barre barre){
        barres.add(barre);
    }

    
    public void removeBarres (Barre barre){
        barres.remove(barre);
    }
    
    public void addNoeuds(Noeud noeud){
        noeuds.add(noeud);
    }
    
    public void removeNoeuds (Noeud noeud){
        noeuds.remove(noeud);
    }

    public ArrayList<Noeud> getNoeuds() {
        return noeuds;
    }

    public ArrayList<Barre> getBarres() {
        return barres;
    }

    public Numerateur getNumerateur() {
        return numerateur;
    }

    public NoeudSimple createNoeudSimple(double posX, double posY) {
        NoeudSimple node = new NoeudSimple(posX, posY, numerateur.getNewNoeudId());
        contient.add(node);
        noeuds.add(node);
        return node;
    }

    public NoeudAppui createAppui(boolean simple, Triangle associatedTriangle, SegmentTerrain segmentTerrain, double posSegment){
        NoeudAppui appui;
        if(simple){
            appui =  new NoeudAppuiSimple(associatedTriangle, segmentTerrain, posSegment, numerateur.getNewNoeudId());
        }else{
            appui = new NoeudAppuiDouble(associatedTriangle, segmentTerrain, posSegment, numerateur.getNewNoeudId());
        }
        noeuds.add(appui);
        contient.add(appui);
        return appui;
    }

    public void createBarre(Noeud pA, Noeud pB){
        Barre b = new Barre(pA, pB, numerateur.getNewBarreId());
        barres.add(b);
        contient.add(b);
        pA.addBarres(b);
        pB.addBarres(b);
    }
    
    public void createTriangle(PointTerrain pt1, double x,Terrain terrain){
        Triangle b = new Triangle(pt1, x,numerateur.getNewTriangleId(),terrain);
        contient.add(b);
    }


    
    public void removeAll(ArrayList<Forme> lf){
        for (Forme f : lf) {
            if(f instanceof Noeud noeud) removeNoeuds(noeud);
            else if(f instanceof Barre barre) removeBarres(barre);
            this.contient.remove(f);
        }
    }
    

    /**
     *
     */
    public void updateNoeuds(){
        ArrayList<Noeud> toRemove = new ArrayList<>();
        for (Noeud noeud : noeuds) {
            if(noeud instanceof NoeudSimple) {
                for (Triangle triangle : terrain.getTriangles()) {
                    if (triangle.contain(noeud.getPx(), noeud.getPy())) {
                        toRemove.add(noeud);
                        break;
                    }
                }
            }else{
                if(!((NoeudAppui) noeud).getSegmentTerrain().asOneTriangle()){
                    toRemove.add(noeud);
                }
            }
        }
        for (Noeud noeud : toRemove) {
            contient.remove(noeud);
            noeuds.remove(noeud);
        }
    }
    
    
    /**
     * retourne la figure contenue dans le groupe la plus proche du point et au
     * maximum à distMax du point; retourne null si aucune figure n'est à une
     * distance plus faible que distMax;
     * @param p
     */
    public Forme plusProche(Point p, double distMax) {
        if (this.contient.isEmpty()) {
            return null;
        } else {
            Forme fmin = this.contient.get(0);
            double min = fmin.distancePoint(p);
            for (int i = 1; i < this.contient.size(); i++) {
                Forme fcur = this.contient.get(i);
                double cur = fcur.distancePoint(p);
                if (cur < min) {
                    min = cur;
                    fmin = fcur;
                }
            }
            if (min <= distMax) {
                return fmin;
            } else {
                return null;
            }
        }
    }
    public int size() {
        return this.contient.size();
    }
    
    @Override
    public double distancePoint(Point p) {
        if (this.contient.isEmpty()) {
            return new Point(0, 0).distancePoint(p);
        } else {
            double dist = this.contient.get(0).distancePoint(p);
            for (int i = 1; i < this.contient.size(); i++) {
                double cur = this.contient.get(i).distancePoint(p);
                if (cur < dist) {
                    dist = cur;
                }
            }
            return dist;
        }
    }
    
    /**
     * abscice maximale d'un groupe de figures.0 si le groupe est vide.
     * @return 
     */
    @Override
    public double maxX() {
        if (this.contient.isEmpty()) {
            return 0;
        } else {
            double max = this.contient.get(0).maxX();
            for (int i = 1; i < this.contient.size(); i++) {
                double cur = this.contient.get(i).maxX();
                if (cur > max) {
                    max = cur;
                }
            }
            return max;
        }
    }

    /**
     * abscice minimale d'un groupe de figures. 0 si le groupe est vide.
     */
    @Override
    public double minX() {
        if (this.contient.isEmpty()) {
            return 0;
        } else {
            double min = this.contient.get(0).minX();
            for (int i = 1; i < this.contient.size(); i++) {
                double cur = this.contient.get(i).minX();
                if (cur < min) {
                    min = cur;
                }
            }
            return min;
        }
    }
    
   
    
    /**
     * ordonnee maximale d'un groupe de figures. 0 si le groupe est vide.
     */
    @Override
    public double maxY() {
        if (this.contient.isEmpty()) {
            return 0;
        } else {
            double max = this.contient.get(0).maxY();
            for (int i = 1; i < this.contient.size(); i++) {
                double cur = this.contient.get(i).maxY();
                if (cur > max) {
                    max = cur;
                }
            }
            return max;
        }
    }

    /**
     * ordonnee minimale d'un groupe de figures. 0 si le groupe est vide.
     */
    @Override
    public double minY() {
        if (this.contient.isEmpty()) {
            return 0;
        } else {
            double min = this.contient.get(0).minY();
            for (int i = 1; i < this.contient.size(); i++) {
                double cur = this.contient.get(i).minY();
                if (cur < min) {
                    min = cur;
                }
            }
            return min;
        }
    }
    
    @Override
    public void dessine(GraphicsContext context) {
        for (Forme f : this.contient) {
            f.dessine(context);
        }
    }
    
    @Override
    public void dessinProche(GraphicsContext context) {
        for (Forme f : this.contient) {
            f.dessinProche(context);
        }  
    }
    
    
    @Override
    public void dessineSelection(GraphicsContext context) {
        for (Forme f : this.contient) {
            f.dessineSelection(context);
        }
    }
    
    public static Treillis LastTreillis() {
        Point p1 = new Point(10, 10);
        Point p2 = new Point(100, 10);
        Point p3 = new Point(100, 100);
        Point p4 = new Point(10, 100);
        Point p5 = new Point(50, 50);
        Point p6 = new Point(500, 500);
        Segment s1 = new Segment(p1, p2);
        Segment s2 = new Segment(p2, p3);
        Segment s3 = new Segment(p3, p1);
        Segment s4 = new Segment(p1, p4);
        Treillis triangle = new Treillis();
        triangle.add(s1);
        triangle.add(s2);
        triangle.add(s3);
        Treillis res = new Treillis();
        res.add(p5);
        res.add(p6);
        res.add(s4);
        res.add(triangle);
        return res;
    }
    
    @Override
    public ArrayList<String> getInfos() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
}