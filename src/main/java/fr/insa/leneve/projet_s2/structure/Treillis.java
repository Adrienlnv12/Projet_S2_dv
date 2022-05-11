package fr.insa.leneve.projet_s2.structure;



import fr.insa.leneve.projet_s2.Numerateur;
import fr.insa.leneve.projet_s2.structure.Noeud.*;
import fr.insa.leneve.projet_s2.structure.Terrain.SegmentTerrain;
import fr.insa.leneve.projet_s2.structure.Terrain.Terrain;
import fr.insa.leneve.projet_s2.structure.Terrain.Triangle;
import fr.insa.leneve.projet_s2.structure.forme.Forme;
import java.awt.Graphics;
import java.util.ArrayList;


public class Treillis {

    private Terrain terrain;
    private final ArrayList<Noeud> noeuds;
    private final ArrayList<Barre> barres;
    private final ArrayList<TypedeBarre> catalogue;
    private final Numerateur numerateur;


    public Treillis() {
        numerateur = new Numerateur();
        noeuds = new ArrayList<>();
        barres = new ArrayList<>();
        catalogue = new ArrayList<>();
        terrain = new Terrain();
    }

    public Treillis(Terrain terrain, ArrayList<Noeud> noeuds, ArrayList<Barre> barres, ArrayList<TypedeBarre> catalogue, Numerateur numerateur) {
        this.terrain = terrain;
        this.noeuds = noeuds;
        this.barres = barres;
        this.numerateur = numerateur;
        this.catalogue = catalogue;
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

    public void setTerrain(Terrain terrain){
        this.terrain = terrain;
    }

    public ArrayList<Noeud> getNoeuds() {
        return noeuds;
    }

    public ArrayList<Barre> getBarres() {
        return barres;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public Numerateur getNumerateur() {
        return numerateur;
    }

    public ArrayList<TypedeBarre> getCatalogue() {
        return catalogue;
    }

    public NoeudSimple createNoeudSimple(double posX, double posY) {
        NoeudSimple node = new NoeudSimple(posX, posY, numerateur.getNewNoeudId());
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
        return appui;
    }

    public void addType(TypedeBarre type){
        catalogue.add(type);
    }

    public void updateTerrain(double x1, double y1, double x2, double y2){
        this.terrain.setBorder(x1,y1, x2, y2);
    }

    public void createBarre(Noeud pA, Noeud pB, TypedeBarre type){
        if(type == null) {
            System.err.println("TYPE NULL");
            return;
        }
        Barre b = new Barre(pA, pB, type, numerateur.getNewBarreId());
        barres.add(b);
        pA.addBarres(b);
        pB.addBarres(b);
    }

    public void removeElement(Forme f){
        if(f instanceof Noeud) removeNoeuds((Noeud) f);
        else if(f instanceof Barre) removeBarres((Barre) f);
    }

    public void updateNoeuds(Graphics graphics){
        ArrayList<Noeud> toRemove = new ArrayList<>();
        for (Noeud noeud : noeuds) {
            if(!terrain.contain(noeud.getPx(), noeud.getPy())){
                toRemove.add(noeud);
                continue;
            }
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
            noeuds.remove(noeud);
            graphics.remove(noeud);
        }
    }
}