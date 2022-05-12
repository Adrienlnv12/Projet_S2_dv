/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2.interfa;

import fr.insa.leneve.projet_s2.calcul.Maths;
import fr.insa.leneve.projet_s2.graphic.Graphics;
import fr.insa.leneve.projet_s2.interfa.BoutonGauche;
import fr.insa.leneve.projet_s2.structure.Barre;
import fr.insa.leneve.projet_s2.structure.Noeud.*;
import fr.insa.leneve.projet_s2.structure.Terrain.*;
import fr.insa.leneve.projet_s2.structure.Treillis;
import fr.insa.leneve.projet_s2.structure.TypedeBarre;
import fr.insa.leneve.projet_s2.structure.forme.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

/**
 *
 * @author adrie
 */
public class Controleur {
    private String lastOpen;
    private Terrain terrain;
    private MainPanel mainpanel;
    private final Graphics graphics;
    private Stage stage;
    private String name;
    private String path;
    
    private Forme nearest, currentSelect;
    private double X, Y;
    private double dragMouseX, dragMouseY;
    
    private Treillis treillis;
    private MainPanel vue;
    private int boutonSelect = 0;
    private TypedeBarre barreType = null;
    private Segment barreEnCoursDeCreation = null;

    private int etat;
    private final ArrayList<Forme> multipleSelect = new ArrayList<>();

    private List<Forme> selection;
    
    private final HashMap<Integer, double[]> formesRes = new HashMap<>();
    private final HashMap<Forme, Integer> formeId = new HashMap<>();

    private int currentClick = 0;
    private Point firstSegmentPoint = null, secondSegmentPoint = null;

    private double terrainX, terrainY;

    private boolean inMultSelect = false, drag = false;

    private boolean inDrawing = true;

    private HostServices hostServices;

    //1 m => 50 px
    private final double echelle = 50;
    
    
    
    public Controleur(MainPanel vue) {
        this.vue = vue;
        this.selection = new ArrayList<>();
        graphics = new Graphics();
    }

    /*//initialisation de la classe
    // impossible de le faire dans son constructeur car cette classe a besoin de "mainscene" qui a besoin de
    // cet "action center" pour etre initialisÃƒÂ©
    public void init(MainPanel mainpanel, Stage stage, String path){
        this.mainpanel = mainpanel;
        this.stage = stage;
        this.path = path;
        this.name = nameFromPath(path);
        this.terrain = treillis.getTerrain();

        GraphicsContext gc = mainpanel.getcDessin().getGraphicsContext();
        graphics.init(mainpanel, gc, this);
        graphics.updateFormes(treillis);
        graphics.draw(boutonSelect, inDrawing);

        addMouseEvent();
    }*/
    
    public void changeEtat(int nouvelEtat) {
        switch (nouvelEtat) {
            case 20 -> {
                this.vue.getRbSelect().setSelected(true);
                this.selection.clear();
            }
            case 30 -> {

                this.vue.getRbNoeud().setSelected(true);
                this.selection.clear();
            }
            case 40 -> {
                this.vue.getRbBarre().setSelected(true);
                this.selection.clear();
            }
            case 50 -> {
                this.vue.getRbTerrain().setSelected(true);
                this.selection.clear();  
                }
            case 60 -> {
                //création d'un terrain
                this.vue.getRbTriangleTerrain().setSelected(true);
                this.selection.clear();
                }
            default -> {
            }
        }
        // creation de segments étape 2
        this.etat = nouvelEtat;
        //this.activeBoutonsSuivantSelection();

    }
    
    /**
     * transforme les coordonnées (xVue,yVue) dans le repère de la vue, en un
     * point du modele en tenant compte de la transformation actuelle appliquée
     * à la vue.
     *
     * @param xVue pos x dans la vue
     * @param yVue pos y dans la vue
     * @return un Point apprès application de la transformation vue --> model
     */
    public Point posInModel(double xVue, double yVue) {
        Transform modelVersVue = this.vue.getcDessin().getTransform();
        Point2D ptrans;
        try {
            ptrans = modelVersVue.inverseTransform(xVue, yVue);
        } catch (NonInvertibleTransformException ex) {
            throw new Error(ex);
        }
        Point pclic = new Point(ptrans.getX(), ptrans.getY());
        //pclic.setCouleur(this.vue.getCpCouleur().getValue());
        return pclic;
    }
    
    //ajout des fonctions appelÃƒÂ©s durant diffÃƒÂ©rentes actions de la souris
    private void addMouseEvent() {
        DessinCanvas canvas = mainpanel.getcDessin();

        //actions quand la couris est dÃƒÂ©placÃƒÂ© dans le canvas
        canvas.setOnMouseMoved(mouseEvent -> {
            X = mouseEvent.getX();
            Y = mouseEvent.getY();
            if(inDrawing && (boutonSelect/10) % 2 == 0){
                selection(boutonSelect/10);
            }
            graphics.draw(boutonSelect, inDrawing);
        });

        //action quand on clic sur la souris
        canvas.setOnMousePressed(mouseEvent -> {
            X = mouseEvent.getX();
            Y = mouseEvent.getY();

            if(mouseEvent.getButton() == MouseButton.PRIMARY && inDrawing) {
                inMultSelect = false;
                removeSelectedAll();

                switch (this.etat) {
                    case 20 -> {
                        // selection
                            Point pclic = this.posInModel(t.getX(), t.getY());
                            // pas de limite de distance entre le clic et l'objet selectionné
                            Forme proche = this.vue.getTreillis().plusProche(pclic, Double.MAX_VALUE);
                            // il faut tout de même prévoir le cas ou le groupe est vide
                            // donc pas de plus proche
                            if (proche != null) {
                                if (t.isShiftDown()) {
                                    this.selection.add(proche);
                                } else if (t.isControlDown()) {
                                    if (this.selection.contains(proche)) {
                                        this.selection.remove(proche);
                                    } else {
                                        this.selection.add(proche);
                                    }
                                } else {
                                    this.selection.clear();
                                    this.selection.add(proche);
                                }
                            } 
                    }
                    case 30 -> {
                        //création d'un point
                        addNoeud();
                    }
                    case 40 -> {
                        //création d'une barre
                        addBarre();
                    }
                    case 50 -> {
                        //création d'un zone constructible
                        addZoneConstructible();   
                        }
                    case 60 -> {
                        //création d'un terrain
                        addTriangleTrn();   
                        }
                    default -> {
                    }
                }
            }
        });
    }
    //appele la bonne fonction d'ajout du noeud selon le type choisi
    private void addNoeud(){
        
        if(terrain.contain(X ,Y)) {
            switch (boutonSelect) {
                case 10 -> addNoeudSimple();
                case 11 -> addAppui(false);
                case 12 -> addAppui(true);
                default -> System.out.println(boutonSelect);
            }

        }else{

            Alert alerteZoneConstructible = new Alert(Alert.AlertType.ERROR);
            alerteZoneConstructible.setTitle("Erreur");
            alerteZoneConstructible.setContentText("Noeud hors zone constructible!");
            alerteZoneConstructible.showAndWait();

        }

    }
    
    //fonctions d'ajout de noeuds simple
    private void addNoeudSimple() {
        addNoeudSimple(X ,Y);
    }

    private NoeudSimple addNoeudSimple(double posX, double posY) {

        boolean distCreable = NoeudSimple.DistestCreable(treillis, posX, posY);
        boolean triangleCreable = NoeudSimple.TriangleestCreable(treillis, posX, posY);
        System.out.println(distCreable + " " + triangleCreable);
        if(distCreable && triangleCreable) {

            NoeudSimple ns = treillis.createNoeudSimple(posX, posY);
            graphics.updateFormes(treillis);
            graphics.draw(boutonSelect, inDrawing);
            return ns;
        }


        String textError = "";

        if(!distCreable){
            textError = "Noeuds trop proches!";
        }
        if(!triangleCreable){
            if(textError.length() > 0) textError += " et ";
            textError += "Noeud simple compris dans un triangle terrain!";
        }

        Alert alerteTriangleTerrain = new Alert(Alert.AlertType.WARNING);
        alerteTriangleTerrain.setTitle("Erreur crÃ©ation noeud");
        alerteTriangleTerrain.setContentText(textError);
        alerteTriangleTerrain.showAndWait();


        return null;
    }

    //fonctions d'ajout d'un appui
    public void addAppui(boolean simple) {
        testAppui(simple, X ,Y);
    }

    //test si il est possible de creer un appui, et si oui alors il le crÃƒÂ©e
    public NoeudAppui testAppui(boolean simple, double posX, double posY){
        SegmentTerrain segment = NoeudAppui.isCreable(terrain, posX, posY);
        if(segment != null) {
            return createAppui(simple, posX, posY, segment);
        }else {
            Alert alerteNoeudAppui = new Alert(Alert.AlertType.WARNING);
            alerteNoeudAppui.setTitle("Erreur crÃ©ation noeud");
            alerteNoeudAppui.setContentText("Noeud non positionnÃ© sur un segment de terrain!");
            alerteNoeudAppui.showAndWait();
        }
        return null;
    }

    public NoeudAppui createAppui(boolean simple, double posX, double posY, SegmentTerrain segment) {
            NoeudAppui appui = treillis.createAppui(simple, segment.getTriangles().get(0), segment, Maths.distancePoint(segment.getDebut(), posX, posY) / segment.length());
            graphics.updateFormes(treillis);
            graphics.draw(boutonSelect, inDrawing);
            return appui;
    }
    
    //fonction de crÃƒÂ©ation d'une barre
    private void addBarre(){
        currentClick++;
        Noeud p = null;
        //test si on clique a cotÃƒÂ© d'un point ou pas
        //Besoin d'ajouter la vÃƒÂ©rification que le point est crÃƒÂ©able, et quel type de point
        if(barreType == null){
            System.err.println("TYPE NULL");
            Alert alerteTypeNull = new Alert(Alert.AlertType.INFORMATION);
            alerteTypeNull.setTitle("");
            alerteTypeNull.setHeaderText("CREATION BARRE IMPOSSIBLE");
            alerteTypeNull.setContentText("Type nul!");
            alerteTypeNull.showAndWait();
            currentClick --;
            return;
        }

        double lMin = barreType.getlMin() * echelle;
        double lMax = barreType.getlMax() * echelle;

        if(nearest != null && nearest instanceof Noeud){
            boolean creable = true;
            if(currentClick > 1 && firstSegmentPoint != null) {
                double dist = Maths.distancePoint((Point) nearest, firstSegmentPoint);
                if(dist < lMin || dist > lMax){
                    creable = false;
                }
            }
            if(creable) p = (Noeud) nearest;
        }
        if(p == null){
            double dist = 0;
            if(currentClick > 1 && firstSegmentPoint != null) {
                dist = Maths.distancePoint(firstSegmentPoint, (new Point(X ,Y)).substract(graphics.getOrigin()));
            }
            if(dist >= lMin || currentClick == 1){
                p = createNoeudBarre(dist > lMax, firstSegmentPoint);
            }
            if(p == null){
                currentClick --;
                return;
            }
        }
          if(currentClick == 1){
            p.setSegmentSelected(true);
            firstSegmentPoint = p;
        }else{
            assert firstSegmentPoint != null;
            if(firstSegmentPoint.equals(p)){
                currentClick--;
                return;
            }
            currentClick = 0;
            treillis.createBarre((Noeud) firstSegmentPoint, p, barreType);
            firstSegmentPoint.setSegmentSelected(false);
            graphics.updateFormes(treillis);
        }
        graphics.draw(boutonSelect, inDrawing);
    }

    //fonction de creation de noeud pour les barres
    //construit par defaut un noeud simple ou sinon un appui simple s'il ne peut pas
    public Noeud createNoeudBarre(boolean distSup, Point firstSegmentPoint){
        Noeud noeudRes = null;
        double posX, posY;
        if(distSup){
            double angle = Maths.angle(firstSegmentPoint, (new Point(X ,Y)).substract(graphics.getOrigin()));
            posX = firstSegmentPoint.getPx() + barreType.getlMax() * echelle * Math.cos(angle);
            posY = firstSegmentPoint.getPy() + barreType.getlMax() * echelle * Math.sin(angle);
        }else {
            posX = X;
            posY = Y;
        }
        if (!terrain.contain(posX, posY)){
            Alert alerteZoneConstructible = new Alert(Alert.AlertType.WARNING);
            alerteZoneConstructible.setTitle("Erreur");
            alerteZoneConstructible.setHeaderText("CREATION BARRE IMPOSSIBLE");
            alerteZoneConstructible.setContentText("Point hors zone constructible!");
            alerteZoneConstructible.showAndWait(); 
        }
        if(terrain.contain(posX, posY)) {
            noeudRes = addNoeudSimple(posX, posY);
            if(noeudRes == null){
                noeudRes = testAppui(true, posX, posY);
            }

        }else{
            Alert alerteZoneConstructible = new Alert(Alert.AlertType.WARNING);
            alerteZoneConstructible.setTitle("Erreur");
            alerteZoneConstructible.setHeaderText("CREATION BARRE IMPOSSIBLE");
            alerteZoneConstructible.setContentText("Point hors zone constructible!");
            alerteZoneConstructible.showAndWait();
        }
        return noeudRes;

    }
    
    //met en place la zone constructible
    private void addZoneConstructible() {
        currentClick ++;

        if(currentClick == 1){
            terrainX = X;
            terrainY = Y;
        }else{
            currentClick = 0;
            treillis.updateTerrain(Math.min(terrainX, X), Math.min(terrainY, Y),
                    Math.max(terrainX,X),Math.max(terrainY, Y));

            treillis.updateNoeuds(graphics);
            graphics.updateFormes(treillis);
            redraw();
        }
    }

   /* //ecrit les infos liÃƒÂ© au calcul
    public void writeCalculInfo(HashMap<Forme, Integer> formeId, HashMap<Integer, double[]> idValues){
        mainpanel.getInfos().drawCalculInfo(formeId, idValues);
    }*/

    //fonction de creation des points composant un triangle
    public PointTerrain addPointTrn() {
        double px = X, py = Y;
        PointTerrain pt = null;

        boolean creable = true;
        for (PointTerrain p : terrain.getPoints()) {
            if(Maths.distancePoint(p, new Point(px, py)) < 15) creable = false;
        }
        if(creable) pt = terrain.addPoint(px, py);

        graphics.draw(boutonSelect, inDrawing);
        return pt;
    }
    
    //fonction de creation de triangles
    public void addTriangleTrn(){
        currentClick++;
        PointTerrain p;
        //test si on clique a cotÃƒÂ© d'un point ou pas
        //Besoin d'ajouter la vÃƒÂ©rification que le point est crÃƒÂ©able, et quel type de point
        if(nearest != null && nearest instanceof PointTerrain){
            p = (PointTerrain) nearest;
        }else{
            p = addPointTrn();
            if(p == null) {
                currentClick --;
                return;
            }
        }
        if(currentClick == 1){
            p.setSegmentSelected(true);
            firstSegmentPoint = p;
        }else if(currentClick == 2){
            if(firstSegmentPoint.equals(p)){
                currentClick--;
                return;
            }
            p.setSegmentSelected(true);
            secondSegmentPoint = p;
        }else{
            if(secondSegmentPoint.equals(p) || firstSegmentPoint.equals(p)){
                currentClick--;
                return;
            }
            currentClick = 0;
            Triangle triangle = new Triangle((PointTerrain) firstSegmentPoint, (PointTerrain) secondSegmentPoint, p, treillis.getNumerateur().getNewTriangleId(), terrain);
            terrain.addTriangle(triangle);

            treillis.updateNoeuds(graphics);
            firstSegmentPoint.setSegmentSelected(false);
            secondSegmentPoint.setSegmentSelected(false);
        }
        redraw();
    }
    
    public void changeColor(Color value) {
        if (this.etat == 20 && !this.selection.isEmpty()) {
            for (Forme f : this.selection) {
                f.changeCouleur(value);
            }
            redraw();
        }
    }
    /*
    public void zoomDouble() { //a refaire les partie zoom et déplacement
        this.vue.setZoneModelVue(this.vue.getZoneModelVue().scale(0.5));
        redraw();
    }

    public void zoomDemi() {
        this.vue.setZoneModelVue(this.vue.getZoneModelVue().scale(2));
        redraw();
    }

    /*public void zoomFitAll() {
        this.vue.fitAll();
        redraw();
    }

    public void translateGauche() {
         this.vue.setZoneModelVue(this.vue.getZoneModelVue().translateGauche(0.8));
        redraw();
   }

    public void translateDroite() {
         this.vue.setZoneModelVue(this.vue.getZoneModelVue().translateDroite(0.8));
        redraw();
   }

    public void translateHaut() {
         this.vue.setZoneModelVue(this.vue.getZoneModelVue().translateHaut(0.8));
        redraw();
   }

    public void translateBas() {
         this.vue.setZoneModelVue(this.vue.getZoneModelVue().translateBas(0.8));
        redraw();
   }*/
    
    //calcul le prix final du treillis
    public double getCost(){
        double price = 0;
        for (Barre barres : treillis.getBarres()) {
            if(barres.getType() == null) continue;
            price += barres.getType().getCout() * barres.length() / echelle;
        }
        return (double) ((int) price * 100) / 100;
    }

    //ecrit les infos liÃƒÂ© au calcul
    public void writeCalculInfo(HashMap<Forme, Integer> formeId, HashMap<Integer, double[]> idValues){
        mainpanel.getInfos().dessineCalculInfo(formeId, idValues);
    }
    
    //supprime un element
    public void deleteForme(Forme f){
        if(f != null) {
            if (f instanceof PointTerrain || f instanceof Triangle || f instanceof SegmentTerrain) {
                if(terrain != null) {
                    terrain.remove(f, false);
                    graphics.draw(boutonSelect, inDrawing);
                }
            } else {
                graphics.remove(f);
                treillis.removeElement(f);
                currentSelect = null;
            }
        }
    }
    
    public void deleteAllFormes() {
        multipleSelect.forEach(f -> {
            graphics.remove(f);
            treillis.removeElement(f);
        });

        multipleSelect.clear();
        graphics.removeInfos();
        inMultSelect = false;
        graphics.draw(boutonSelect, inDrawing);
    }
    
    public void deleteZoneConstru(Terrain t){
        if(t != null){
            t.setBorderNull();
        }
    }

    
    //retire le point selectionnÃƒÂ©
    public void removeSelected() {
        if (currentSelect != null) {
            currentSelect.setSelected(false);
        }
        if(terrain != null) {
            terrain.setSelected(false);
        }
        mainpanel.getInfos().removeInfos();
        currentSelect = null;
    }

    //retire tout les points selectionnÃƒÂ©s
    public void removeSelectedAll() {
        multipleSelect.forEach(p -> p.setSelected(false));
        multipleSelect.clear();
        graphics.removeInfos();
    }
    
    public void selection(int selectedButton){
        //ajoute les formes dans la selection

        ArrayList<Forme> formes = new ArrayList<>();

        formes.addAll(graphics.getFormes());
        Terrain terrain = treillis.getTerrain();
        if(terrain != null){
            formes.addAll(terrain.getTriangles());
            formes.addAll(terrain.getPoints());
            formes.addAll(terrain.getSegments());
        }

        double bestDist = 15;

        for (Forme f: formes) {
            Point p;
            if(f instanceof Noeud && (selectedButton == 2 || selectedButton == 0)) {
                p = (Point) f;
            }else if(f instanceof PointTerrain && (selectedButton == 4 || selectedButton == 0)){
                p = (Point) f;
            }else if(selectedButton == 0){
                if(f instanceof Segment segment){
                    p = segment.getCenter();
                }else if(f instanceof Triangle triangle){
                    p = triangle.getCenter();
                }else {
                    return;
                }
            }else{
                continue;
            }

            //trouve la forme le plus proche de la souris
            double dist = Maths.distancePoint(p, new Point(X, Y));
            if(dist < bestDist){
                nearest = f;
                bestDist = dist;
            }
            if(bestDist == 15) {
                nearest = null;
            }
        }

    }
    
    private void dragSelection() {
        ArrayList<Forme> formes = graphics.getFormes();

        for (Forme f: formes) {
            Point p;
            if(f instanceof Point) {
                p = (Point) f;
            }else{
                p = ((Segment) f).getCenter();
            }

            if (drag) {
                Point origin = graphics.getOrigin();
                if (p.getPx() + origin.getPx() < Math.max(X, dragMouseX) && p.getPx() + origin.getPx()> Math.min(X, dragMouseX) &&
                        p.getPy() + origin.getPy() < Math.max(Y, dragMouseY) && p.getPy() + origin.getPy() > Math.min(Y, dragMouseY)) {
                    if (!multipleSelect.contains(f)) {
                        multipleSelect.add(f);
                        f.setSelected(true);
                    }
                } else {
                    multipleSelect.remove(f);
                    f.setSelected(false);
                }
            }
        }
    }
    
    
    public void boutonSelection(ActionEvent t) {
        this.changeEtat(20);
    }

    public void boutonNoeud(ActionEvent t) {
        this.changeEtat(30);
    }

    public void boutonBarre(ActionEvent t) {
        this.changeEtat(40);
    }
    
    public void boutonTerrain(ActionEvent t) {
        this.changeEtat(50);
    }
    
    public void boutonTriangle(ActionEvent t) {
        this.changeEtat(60);
    }
    
    public void boutonSelect(int t) {
        this.boutonSelect=t ;
    }
   
    public int getboutonSelect() {
        return boutonSelect;
    }
   
    public List<Forme> getSelection() {
        return selection;
    }
    
    public void setBarreType(TypedeBarre barreType) {
        this.barreType = barreType;
    }
    
    void creePointParDialog() {
        Optional<Point> p = EnterPointDialog.demandePoint();
        if (p.isPresent()) {
            //this.vue.getModel().add(p.get());
            redraw();
        }
    }
    public String getLastOpen() {
        return lastOpen;
    }
    
    public Graphics getGraphics() {
        return graphics;
    }
    
    public Forme getNearest(){
        return nearest;
    }
    
    public void redraw() {
        graphics.draw(boutonSelect, inDrawing);
    }

    public boolean isDrag() {
        return drag;
    }

    public double getMouseX() {
        return X;
    }

    public double getMouseY() {
        return Y;
    }

    public double getDragMouseX() {
        return dragMouseX;
    }

    public double getDragMouseY() {
        return dragMouseY;
    }

    public String getName() {
        return name;
    }

    public double getTerrainX() {
        return terrainX;
    }

    public double getTerrainY() {
        return terrainY;
    }

    public int getCurrentClick() {
        return currentClick;
    }

    public Treillis getTreillis() {
        return treillis;
    }

    public Stage getStage() {
        return stage;
    }

    public String getPath() {
        return path;
    }

    public Point getFirstSegmentPoint() {
        return firstSegmentPoint;
    }

    public Point getSecondSegmentPoint() {
        return secondSegmentPoint;
    }

    public boolean isInDrawing() {
        return inDrawing;
    }

    public boolean isInMultSelect() {
        return inMultSelect;
    }

    public ArrayList<Forme> getMultipleSelect() {
        return multipleSelect;
    }

    public static String nameFromPath(String path){
        String[] nameS = path.split("\\\\");
        return nameS[nameS.length - 1].split("\\.")[0];
    }

    public double getEchelle() {
        return echelle;
    }

    public TypedeBarre getBarreType() {
        return barreType;
    }

    public HashMap<Integer, double[]> getResultCalcul() {
        return formesRes;
    }

    public HashMap<Forme, Integer> getFormeId() {
        return formeId;
    }

    public void setInDrawing(boolean inDrawing){
        this.inDrawing = inDrawing;
    }

    }