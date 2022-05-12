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
package fr.insa.leneve.projet_s2.interfa;

import fr.insa.leneve.projet_s2.calcul.Maths;
import fr.insa.leneve.projet_s2.structure.Barre;
import fr.insa.leneve.projet_s2.structure.Terrain.PointTerrain;
import fr.insa.leneve.projet_s2.structure.Terrain.SegmentTerrain;
import fr.insa.leneve.projet_s2.structure.Terrain.Terrain;
import fr.insa.leneve.projet_s2.structure.Terrain.Triangle;
import fr.insa.leneve.projet_s2.structure.Treillis;
import fr.insa.leneve.projet_s2.structure.TypedeBarre;
import fr.insa.leneve.projet_s2.structure.forme.Forme;
import fr.insa.leneve.projet_s2.graphic.Graphics;
import fr.insa.leneve.projet_s2.structure.Noeud.*;
import fr.insa.leneve.projet_s2.structure.forme.Point;
import fr.insa.leneve.projet_s2.structure.forme.Segment;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.input.MouseButton;

/**
 *
 * @author francois
 */
public class Controleur {
    
    private Terrain terrain;
    private MainPanel mainpanel;
    private final Graphics graphics;
    private Stage stage;
    private String name;
    private String path;
    
    private Forme nearest, currentSelect;
    private double mouseX, mouseY;
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
    
    
    
    public Controleur(/*Treillis treillis /*HostServices hostServices*/MainPanel vue) {
        this.vue = vue;
        this.selection = new ArrayList<>();
        graphics = new Graphics();
        //this.treillis = treillis;
        //this.hostServices = hostServices;
    }

    //initialisation de la classe
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
        //addKeyboardEvent();
    }
    
   /* private void realSave(File f) { //a refaire la partie sauvegarde
        try {
            this.vue.getModel().sauvegarde(f);
            this.vue.setCurFile(f);
            this.vue.getInStage().setTitle(f.getName());
        } catch (IOException ex){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Problème durant la sauvegarde");
            alert.setContentText(ex.getLocalizedMessage());

            alert.showAndWait();
        } finally {
            this.changeEtat(20);
        }
    }

    public void menuSave(ActionEvent t) {
        if (this.vue.getCurFile() == null) {
            this.menuSaveAs(t);
        } else {
            this.realSave(this.vue.getCurFile());
        }
    }

    public void menuSaveAs(ActionEvent t) {
        FileChooser chooser = new FileChooser();
        File f = chooser.showSaveDialog(this.vue.getInStage());
        if (f != null) {
            this.realSave(f);
        }
    }

    public void menuOpen(ActionEvent t) {
        FileChooser chooser = new FileChooser();
        File f = chooser.showOpenDialog(this.vue.getInStage());
        if (f != null) {
            try {
                Figure lue = Figure.lecture(f);
                Groupe glu = (Groupe) lue;
                Stage nouveau = new Stage();
                nouveau.setTitle(f.getName());
                Scene sc = new Scene(new MainPanel(nouveau, f), 800, 600);
                nouveau.setScene(sc);
                nouveau.show();
            } catch (IOException ex) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Problème durant la sauvegarde");
                alert.setContentText(ex.getLocalizedMessage());

                alert.showAndWait();
            } finally {
                this.changeEtat(20);
            }
        }
    }
//    }

    public void menuNouveau(ActionEvent t) {
        Stage nouveau = new Stage();
        nouveau.setTitle("Nouveau");
        Scene sc = new Scene(new MainPanel(nouveau), 800, 600);
        nouveau.setScene(sc);
        nouveau.show();
    }*/

    public void menuApropos(ActionEvent t) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("A propos");
        alert.setHeaderText(null);
        alert.setContentText("On galère ces grands mort ici\n"
                + "bouhhh\n"
                + "Angèle aide moi\n"
                + "STH on souffre");

        alert.showAndWait();
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

    //calcul le prix final du treillis
    public double getCost(){
        double price = 0;
        for (Barre barres : treillis.getBarres()) {
            if(barres.getType() == null) continue;
            price += barres.getType().getCout() * barres.length() / echelle;
        }
        return (double) ((int) price * 100) / 100;
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
    private void addMouseEvent() {//a refaire
        DessinCanvas canvas = mainpanel.getcDessin();

        //actions quand la souris est déplacé dans le canvas
        canvas.setOnMouseMoved(mouseEvent -> {
            mouseX = mouseEvent.getX();
            mouseY = mouseEvent.getY();
            if(inDrawing && (boutonSelect/10) % 2 == 0){
                selection(boutonSelect/10);
            }
            graphics.draw(boutonSelect, inDrawing);
        });

        //action quand on clic sur la souris
        canvas.setOnMousePressed(mouseEvent -> {
            mouseX = mouseEvent.getX();
            mouseY = mouseEvent.getY();

            if(mouseEvent.getButton() == MouseButton.PRIMARY && inDrawing) {
                inMultSelect = false;
                removeSelectedAll();

                switch (boutonSelect/10) {
                    case 0 -> setSelected();
                    case 1 -> addNoeud();
                    case 2 -> addBarre();
                    case 3 -> addZoneConstructible();
                    case 4 -> addTriangleTrn();
                }
            }
        });
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
    
     //appele la bonne fonction d'ajout du noeud selon le type choisi
    private void addNoeud(){
        if(terrain.contain(mouseX - graphics.getOrigin().getPx(), mouseY - graphics.getOrigin().getPy())) {
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
        addNoeudSimple(mouseX - graphics.getOrigin().getPx(), mouseY - graphics.getOrigin().getPy());
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
        testAppui(simple, mouseX - graphics.getOrigin().getPx(), mouseY - graphics.getOrigin().getPy());
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
                dist = Maths.distancePoint(firstSegmentPoint, (new Point(mouseX, mouseY)).substract(graphics.getOrigin()));
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
            double angle = Maths.angle(firstSegmentPoint, (new Point(mouseX, mouseY)).substract(graphics.getOrigin()));
            posX = firstSegmentPoint.getPx() + barreType.getlMax() * echelle * Math.cos(angle);
            posY = firstSegmentPoint.getPy() + barreType.getlMax() * echelle * Math.sin(angle);
        }else {
            posX = mouseX - graphics.getOrigin().getPx();
            posY = mouseY - graphics.getOrigin().getPy();
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
    
    /*public void clicDansZoneDessin(MouseEvent t) {
        if (this.etat == 20) {
            // selection
            Point pclic = this.posInModel(t.getX(), t.getY());
            // pas de limite de distance entre le clic et l'objet selectionné
            Figure proche = this.vue.getModel().plusProche(pclic, Double.MAX_VALUE);
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
                this.activeBoutonsSuivantSelection();
                this.vue.redrawAll();
            }
        } else if (this.etat == 30) {
            // creation points
            Point pclic = this.posInModel(t.getX(), t.getY());
            Groupe model = this.vue.getModel();
            model.add(pclic);
            this.vue.redrawAll();
        } else if (this.etat == 40) {
            // creation segment premier point
            this.noeud1DansModel = this.posInModel(t.getX(), t.getY());
            this.noeud2DansModel = this.posInModel(t.getX(), t.getY());
            this.barreEnCoursDeCreation = new Segment(this.noeud1DansModel,this.noeud2DansModel);
            this.changeEtat(41);
        } else if (this.etat == 41) {
            // creation de segment deuxieme point
            Point pclic = this.posInModel(t.getX(), t.getY());
            Segment ns = new Segment(this.noeud1DansModel, pclic);
            this.vue.getModel().add(ns);
            this.barreEnCoursDeCreation = null;
            this.vue.redrawAll();
            this.changeEtat(40);
        }
    }*/
    
    //met en place la zone constructible
    private void addZoneConstructible() {
        currentClick ++;

        if(currentClick == 1){
            terrainX = mouseX - graphics.getOrigin().getPx();
            terrainY = mouseY - graphics.getOrigin().getPy();
        }else{
            currentClick = 0;
            treillis.updateTerrain(Math.min(terrainX, mouseX - graphics.getOrigin().getPx()), Math.min(terrainY, mouseY- graphics.getOrigin().getPy()),
                    Math.max(terrainX,mouseX - graphics.getOrigin().getPx()), Math.max(terrainY, mouseY- graphics.getOrigin().getPy()));

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
        double px = mouseX - graphics.getOrigin().getPx(), py = mouseY - graphics.getOrigin().getPy();
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


   /* public void calculs(){
        int id = 0;
        int ns = treillis.getNoeuds().size(), nb = treillis.getBarres().size(), nas = 0, nad = 0;
        //liste les formes et les associes ÃƒÂ  un identifiant
        formeId.clear();
        HashMap<Integer, Forme> idForme = new HashMap<>();
        for (Barre barre : treillis.getBarres()) {
            formeId.put(barre, id);
            idForme.put(id, barre);
            id ++;
        }
        for (Noeud noeud : treillis.getNoeuds()) {
            if(noeud instanceof NoeudAppuiSimple){
                formeId.put(noeud, id);
                idForme.put(id, noeud);
                id ++;
                nas ++;
            }else if(noeud instanceof NoeudAppuiDouble){
                formeId.put(noeud, id);
                idForme.put(id, noeud);
                id += 2;
                nad ++;
            }
            
        }

        System.out.println(2 * ns + " " + nb + nas + 2 * nad);
        if(2 * ns != nb + nas + 2 * nad) {
            Alert alerteHyperstatique = new Alert(Alert.AlertType.ERROR);
            alerteHyperstatique.setTitle("Erreur calcul");
            alerteHyperstatique.setContentText("Le treillis n'est pas isostatique!");
            alerteHyperstatique.showAndWait();
            inDrawing = true;
            throw new Error("treillis hyperstatique");
        }
        Matrice systeme = fillMatrice(formeId, id);
        System.out.println(systeme);

        Matrice res = systeme.resolution();
        System.out.println(systeme.resolution());
        if(res == null) {
            inDrawing = true;
            throw new Error("Matrice non inversible");
        }
        res = res.subCol(systeme.getNbrCol() - 1, systeme.getNbrCol() - 1);
        System.out.println(res);

        formesRes.clear();
        for (int value : formeId.values()) {
            if(idForme.get(value) instanceof NoeudAppuiDouble){
                formesRes.put(value, new double[]{res.get(value, 0), res.get(value + 1, 0)});
            }else{
                formesRes.put(value, new double[]{res.get(value, 0)});
            }
        }
        System.out.println(formesRes);

        writeCalculInfo(formeId, formesRes);
    }*/

    /*public Matrice fillMatrice(HashMap<Forme, Integer> formeToId, int lastId){

        /*
        double[][] coeffs = {
                {0.707, 0, 0, 1, 0, 0},
                {-0.707, 0, -1, 0, 1, 0},
                {0, 0.707, 0, 0, 0, 1},
                {0, 0.707, 1, 0, 0, 0},
                {-0.707, -0.707, 0, 0, 0, 0},
                {0.707, -0.707, 0, 0, 0, 0}
        };

        Matrice systeme = new Matrice(6, 6, coeffs);
        double[] vecResult = {0, 0, 0, 0, 0, 1000};
        */
        /*int size = treillis.getNoeuds().size() * 2;
        Matrice systeme = new Matrice(size, lastId);
        double[] vecResult = new double[size];

        //rempli la matrice
        for (int i = 0; i < treillis.getNoeuds().size() * 2; i += 2) {
            Noeud noeud = treillis.getNoeuds().get(i/2);
            System.out.println(noeud);
            //ajout des valeurs liÃƒÂ©es aux barres
            for (Barre barre : noeud.getLinkedBarres()) {
                int col = formeToId.get(barre);
                double angle;
                if(noeud == barre.getDebut()){
                    angle = Maths.angle(noeud, barre.getFin());
                }else if(noeud == barre.getFin()){
                    angle = Maths.angle(noeud, barre.getDebut());
                }else{
                    angle = 0;
                    System.err.println("euuuuh");
                }
                systeme.set(i, col, Math.cos(angle));
                systeme.set(i + 1, col, Math.sin(angle));
            }
            //ajout des forces
            vecResult[i] = - noeud.getForceApplique().getfX();
            vecResult[i + 1] = - noeud.getForceApplique().getfY();


            if(noeud instanceof NoeudAppuiSimple){
                int col = formeToId.get(noeud);
                double angle = Maths.angle(((NoeudAppuiSimple) noeud).getSegmentTerrain().getDebut(), ((NoeudAppuiSimple) noeud).getSegmentTerrain().getFin());
                systeme.set(i, col, Maths.cos(Math.PI /2 + angle));
                systeme.set(i + 1, col, Math.sin(angle));

            }else if(noeud instanceof NoeudAppuiDouble){
                int col = formeToId.get(noeud);
                systeme.set(i, col, 1);
                systeme.set(i + 1, col + 1, 1);
            }

        }

        System.out.println(formeToId);
        return Matrice.concatCol(systeme, Matrice.creeVecteur(vecResult));
    }*/
    
   
    
    public void boutonSelect(ActionEvent t) {
        this.boutonSelect(t) ;
    }
    public int getboutonSelect() {
        return boutonSelect;
    }

    public void setBarreType(TypedeBarre barreType) {
        this.barreType = barreType;
    }

    /*private void activeBoutonsSuivantSelection() {
        this.vue.getbGrouper().setDisable(true);
        this.vue.getBSupObj().setDisable(true);
        if (this.etat == 20) {
            if (this.selection.size() > 0) {
                this.vue.getBSupObj().setDisable(false);
                if (this.selection.size() > 1) {
                    this.vue.getbGrouper().setDisable(false);
                }
            }
        }
    }*/

    /**
     * @return the selection
     */
    public List<Forme> getSelection() {
        return selection;
    }

    /*public void boutonGrouper(ActionEvent t) {
        if (this.etat == 20 && this.selection.size() > 1) {
            // normalement le bouton est disabled dans le cas contraire
            Groupe ssGroupe = this.vue.getModel().sousGroupe(selection);
            this.selection.clear();
            this.selection.add(ssGroupe);
            this.activeBoutonsSuivantSelection();
            this.vue.redrawAll();
        }
    }*/

    /*public void boutonSupprimer(ActionEvent t) {
        if (this.etat == 20 && this.selection.size() > 0) {
            // normalement le bouton est disabled dans le cas contraire
            this.vue.getModel().removeAll(this.selection);
            this.selection.clear();
            this.activeBoutonsSuivantSelection();
            this.vue.redrawAll();
        }
    }*/

    public void boutonResolution(ActionEvent t) {
                
    }
    
    public void changeColor(Color value) {
        if (this.etat == 20 && this.selection.size() > 0) {
            for (Forme f : this.selection) {
                f.changeCouleur(value);
            }
            this.vue.redrawAll();
        } else if (this.etat == 41 && this.barreEnCoursDeCreation != null) {
            this.barreEnCoursDeCreation.changeCouleur(value);
        }
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
            double dist = Maths.distancePoint(p, new Point(mouseX - graphics.getOrigin().getPx(), mouseY - graphics.getOrigin().getPy()));
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
                if (p.getPx() + origin.getPx() < Math.max(mouseX, dragMouseX) && p.getPx() + origin.getPx()> Math.min(mouseX, dragMouseX) &&
                        p.getPy() + origin.getPy() < Math.max(mouseY, dragMouseY) && p.getPy() + origin.getPy() > Math.min(mouseY, dragMouseY)) {
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
    

    public void zoomDouble() {
        this.vue.setZoneModelVue(this.vue.getZoneModelVue().scale(0.5));
        this.vue.redrawAll();
    }

    public void zoomDemi() {
        this.vue.setZoneModelVue(this.vue.getZoneModelVue().scale(2));
        this.vue.redrawAll();
    }

    public void zoomFitAll() {
        this.vue.fitAll();
        this.vue.redrawAll();
    }

    public void translateGauche() {
         this.vue.setZoneModelVue(this.vue.getZoneModelVue().translateGauche(0.8));
        this.vue.redrawAll();
   }

    public void translateDroite() {
         this.vue.setZoneModelVue(this.vue.getZoneModelVue().translateDroite(0.8));
        this.vue.redrawAll();
   }

    public void translateHaut() {
         this.vue.setZoneModelVue(this.vue.getZoneModelVue().translateHaut(0.8));
        this.vue.redrawAll();
   }

    public void translateBas() {
         this.vue.setZoneModelVue(this.vue.getZoneModelVue().translateBas(0.8));
        this.vue.redrawAll();
   }

    void mouseMovedDansZoneDessin(MouseEvent t) {
        if (this.etat == 41) {
            // attente deuxieme point segment
            this.barreEnCoursDeCreation.setFin(this.posInModel(t.getX(), t.getY()));
            this.vue.redrawAll();
        }
    }

    /**
     * @return the barreEnCoursDeCreation
     */
    public Segment getBarreEnCoursDeCreation() {
        return barreEnCoursDeCreation;
    }

    void creePointParDialog() {
        Optional<Point> p = EnterPointDialog.demandePoint();
        if (p.isPresent()) {
            //this.vue.getModel().add(p.get());
            this.vue.redrawAll();
        }
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
        return mouseX;
    }

    public double getMouseY() {
        return mouseY;
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

    public HostServices getHostServices() {
        return hostServices;
    }

}
