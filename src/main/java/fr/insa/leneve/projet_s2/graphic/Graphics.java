package fr.insa.leneve.projet_s2.graphic;


import fr.insa.leneve.projet_s2.calcul.Maths;
import fr.insa.leneve.projet_s2.interfa.*;

import fr.insa.leneve.projet_s2.structure.Barre;
import fr.insa.leneve.projet_s2.structure.Noeud.*;

import fr.insa.leneve.projet_s2.structure.Terrain.SegmentTerrain;
import fr.insa.leneve.projet_s2.structure.Terrain.Terrain;
import fr.insa.leneve.projet_s2.structure.Treillis;
import fr.insa.leneve.projet_s2.structure.forme.Forme;
import fr.insa.leneve.projet_s2.structure.forme.Point;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

//classe s'occupant principalement de tout ce qui se réfère au dessin sur le canvas
public class Graphics {

    private GraphicsContext gc;
    private MainPanel mainpanel;
    private ArrayList<Forme> formes = new ArrayList<>();
    private Controleur controleur;
    private final Point origin = new Point(0,0);
    private final Point lastOrigin = new Point(origin.getPx(), origin.getPy());
    private double scale = 1;

    private FenetreInfo fenetreinfo;

    public Graphics() {

    }

    //méthode d'initialisation, nécessaire de ne pas la mettre dans le constructeur car sinon bug
    //(besoin d'une instance de canvas pour créer cette instance et de cette instace pour créer l'instance de canvas)
    public void init(MainPanel mainpanel, GraphicsContext graphicsContext, Controleur controleur){
        this.mainpanel = mainpanel;
        this.gc = graphicsContext;
        this.controleur = controleur;

        fenetreinfo = mainpanel.getInfos();
    }

    public void removeInfos()
    {
        fenetreinfo.removeInfos();
    }

    public void drawInfosMultiplePoint(int nbNoeud,int nbAppuiDouble, int nbAppuiSimple,int nbBarre) {
        fenetreinfo.drawInfosMultiplePoint(nbNoeud, nbAppuiDouble, nbAppuiSimple, nbBarre);
    }

    public void drawInfos(Forme nearest){
        fenetreinfo.drawInfos(nearest);
    }

    public void drawInfos(Terrain terrain){
        fenetreinfo.drawInfos(terrain);
    }

    public void resetFormes(){
        formes = new ArrayList<>();
    }

    public void updateFormes(Treillis treillis){
        ArrayList<Noeud> noeuds = treillis.getNoeuds();

        for (Noeud n: noeuds) {
            if(!formes.contains(n)){
                formes.add(n);
            }
        }

        ArrayList<Barre> barres = treillis.getBarres();

        for (Barre b: barres) {
            if(!formes.contains(b)){
                formes.add(b);
            }
        }
    }

    private void drawNear(){
        Forme nearest = controleur.getNearest();
        if(nearest != null){
            nearest.drawNear(gc, origin);
        }
    }

    //fonction de dessin principale
    public void draw(int selectedButton, boolean inDrawing) {
        Point mousePoint = new Point(controleur.getMouseX(), controleur.getMouseY());

        DessinCanvas canvas = mainpanel.getcDessin();

        //fond
        gc.setFill(Color.LIGHTCYAN);
        gc.fillRect(
                0,
                0,
                canvas.getWidth(),
                canvas.getHeight());

        //dessin de l'echelle
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);
        gc.setFill(Color.BLACK);
        gc.fillText("1 m", canvas.getWidth() - 30 - controleur.getEchelle() / 2, canvas.getHeight() - 5);
        gc.strokeLine(canvas.getWidth() - 20 - controleur.getEchelle(), canvas.getHeight() - 20, canvas.getWidth() - 20, canvas.getHeight() - 20);



        //dessin du terrain
        Terrain terrain = controleur.getTreillis().getTerrain();
        if(terrain != null) terrain.draw(gc, inDrawing, origin);

        //dessin des noeud et des barres
        for (Forme f: formes) {
            if(selectedButton != 0) {
                f.setSelected(false);
            }
            if(selectedButton != 20 && selectedButton != 40 && f instanceof Point){
                ((Point) f).setSegmentSelected(false);
            }
            f.draw(gc, origin);
        }

        //dessin des noeuds et barres selectionné + des infos associées
        if(controleur.isInMultSelect()){
            ArrayList<Forme> multipleSelect = controleur.getMultipleSelect();
            int nbNoeud = 0;
            int nbAppuiSimple = 0;
            int nbAppuiDouble = 0;
            int nbBarre = 0;
            for (Forme f: multipleSelect) {
                if(f instanceof NoeudSimple) nbNoeud ++;
                else if(f instanceof Barre) nbBarre ++;
                else if(f instanceof NoeudAppuiDouble) nbAppuiDouble ++;
                else if(f instanceof NoeudAppuiSimple) nbAppuiSimple ++;
            }
            drawInfosMultiplePoint(nbNoeud, nbAppuiDouble, nbAppuiSimple, nbBarre);
        }

        //dessin de la zone de selection
        if (controleur.isDrag()) {
            double mouseX = controleur.getMouseX(), mouseY = controleur.getMouseY();
            double dragMouseX = controleur.getDragMouseX(), dragMouseY = controleur.getDragMouseY();
            gc.setFill(Color.BLUE);
            gc.setStroke(Color.DARKBLUE);
            gc.setGlobalAlpha(0.5);
            gc.fillRect(
                    Math.min(mouseX, dragMouseX),
                    Math.min(mouseY, dragMouseY),
                    Math.abs(dragMouseX - mouseX),
                    Math.abs(dragMouseY - mouseY));
            gc.strokeRect(
                    Math.min(mouseX, dragMouseX),
                    Math.min(mouseY, dragMouseY),
                    Math.abs(dragMouseX - mouseX),
                    Math.abs(dragMouseY - mouseY));
        }else if(selectedButton == 0 || selectedButton == 20 || selectedButton == 40){
            drawNear();
        }
        gc.setGlobalAlpha(1);

        if(controleur.isInDrawing()) {
            //dessin le fantome de la zone constructible
            if (selectedButton == 30 && controleur.getCurrentClick() == 1) {
                drawTerrainZone(controleur.getMouseX(), controleur.getMouseY(), controleur.getTerrainX() + origin.getPx(), controleur.getTerrainY() + origin.getPy());
            }

            //dessin le fantome des triangles
            if (selectedButton == 40) {
                if (controleur.getCurrentClick() >= 1 && controleur.getFirstSegmentPoint() != null) {
                    SegmentTerrain.drawGhost(gc, origin, controleur.getFirstSegmentPoint(), mousePoint);
                }
                if (controleur.getCurrentClick() == 2 && controleur.getSecondSegmentPoint() != null && v.getFirstSegmentPoint() != null) {
                    SegmentTerrain.drawGhost(gc, origin, controleur.getFirstSegmentPoint(), new Point(
                            controleur.getSecondSegmentPoint().getPx() + origin.getPx(),
                            controleur.getSecondSegmentPoint().getPy() + origin.getPy()));
                    SegmentTerrain.drawGhost(gc, origin, controleur.getSecondSegmentPoint(), mousePoint);
                }
            }

            //dessin le fantome des barres
            if (selectedButton == 20) {
                Point firstPoint = controleur.getFirstSegmentPoint();
                if (controleur.getCurrentClick() >= 1 && firstPoint != null) {
                    if (Maths.dist(firstPoint, mousePoint.substract(origin)) > controleur.getBarreType().getlMin() * controleur.getEchelle()) {
                        Point point = mousePoint;

                        double lMax = controleur.getBarreType().getlMax() * ac.getEchelle();
                        if (Maths.distancePoint(firstPoint, mousePoint.substract(origin)) > lMax) {
                            double angle = Maths.angle(firstPoint, mousePoint.substract(origin));
                            point = new Point(firstPoint.getPx() + lMax * Math.cos(angle) + origin.getPx(), firstPoint.getPy() + lMax * Math.sin(angle) + origin.getPy());
                        }


                        assert terrain != null;
                        SegmentTerrain segment = NoeudAppui.isCreable(terrain, mousePoint.getPx() - origin.getPx(), mousePoint.getPy() - origin.getPy());
                        if (segment != null) {
                            Point point1 = NoeudAppui.drawGhost(gc, origin, segment, Maths.distancePoint(segment.getDebut(), mousePoint.getPx() - origin.getPx(), mousePoint.getPy() - origin.getPy()) / segment.length());
                            Barre.drawGhost(gc, controleur.getFirstSegmentPoint(), point1, origin);
                        } else if (terrain.containOutTriangle(point.getPx() - origin.getPx(), point.getPy() - origin.getPy())) {
                            Barre.drawGhost(gc, controleur.getFirstSegmentPoint(), point, origin);
                            point.drawGhost(gc, new Point(0, 0));
                        }
                    }

                }
            }

            //dessin du fantome des noeuds
            if (!(selectedButton == 0 || selectedButton == 30) && (selectedButton != 20 || controleur.getCurrentClick() == 0)) {
                assert terrain != null;

                SegmentTerrain segment = NoeudAppui.isCreable(terrain, mousePoint.getPx() - origin.getPx(), mousePoint.getPy() - origin.getPy());
                if (selectedButton == 10 || (segment == null && (selectedButton != 11 && selectedButton != 12))) {
                    if (terrain.containOutTriangle(mousePoint.getPx() - origin.getPx(), mousePoint.getPy() - origin.getPy())) {
                        mousePoint.drawGhost(gc, new Point(0, 0));
                    }
                } else if (segment != null) {
                   NoeudAppui.drawGhost(gc, origin, segment, Maths.distancePoint(segment.getDebut(), mousePoint.getPx() - origin.getPx(), mousePoint.getPy() - origin.getPy()) / segment.length());
                }

            }
        }else{
            HashMap<Integer, double[]> results = ac.getResultCalcul();
            HashMap<Forme, Integer> formeId = ac.getFormeId();

            if(results.size() > 0) {
                for (Forme value : formeId.keySet()) {
                    int id = formeId.get(value);
                    if (value instanceof NoeudAppuiDouble) {
                        value.drawResult(results.get(id)[0], gc, origin);
                        value.drawResult(results.get(id)[1], gc, new Point(origin.getPx() + 5, origin.getPy() + 10));

                    } else if (value instanceof Barre) {
                        value.drawResult(results.get(id)[0], gc, origin);
                    }else {
                        value.drawResult(results.get(id)[0], gc, origin);
                    }
                }
            }
        }

    }

    public void drawTerrainZone(double x1, double y1, double x2, double y2){

        double x = Math.min(x1, x2), y = Math.min(y1, y2);
        double w = Math.abs(x2 - x1), h = Math.abs(y2 - y1);

        gc.setGlobalAlpha(0.5);
        gc.setFill(Color.LIGHTGREEN);
        gc.fillRect(x, y, w, h);
        gc.setGlobalAlpha(1);

        gc.setStroke(Color.GREEN);
        gc.strokeRect(x, y, w, h);
    }

    public void remove(Forme f){
        formes.remove(f);
    }

    public ArrayList<Forme> getFormes() {
        return formes;
    }

    public void setMainScene(MainPanel mainpanel) {
        this.mainpanel = mainpanel;
        this.fenetreinfo = mainpanel.getInfos();
        this.gc = mainpanel.getcDessin().getGraphicsContext();
    }

    public void moveOrigin(double x, double y, double newX, double newY){
        origin.setPx(lastOrigin.getPx() + (newX - x));
        origin.setPy(lastOrigin.getPy() + (newY - y));
    }

    public void updateLastOrigin() {
        this.lastOrigin.setPx(origin.getPx());
        this.lastOrigin.setPy(origin.getPy());
    }

    public Point getOrigin() {
        return origin;
    }

    public void resetOrigin() {
        origin.setPx(0);
        origin.setPy(0);
        lastOrigin.setPx(0);
        lastOrigin.setPy(0);
    }
}
