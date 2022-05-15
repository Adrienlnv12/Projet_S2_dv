/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2.interfa;

import fr.insa.leneve.projet_s2.calcul.Maths;
import fr.insa.leneve.projet_s2.structure.Barre;
import fr.insa.leneve.projet_s2.structure.Noeud.*;
import fr.insa.leneve.projet_s2.structure.Terrain.*;
import fr.insa.leneve.projet_s2.structure.forme.Treillis;
import fr.insa.leneve.projet_s2.structure.forme.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author adrie
 */
public class Controleur {
    private String lastOpen;
    private MainPanel mainpanel;
    private Stage stage;
    private String name;
    private String path;
    
    private Forme nearest, currentSelect;
    private double X, Y;
    private double dragMouseX, dragMouseY;
    private GraphicsContext context;
    private Terrain terrain;
    private Treillis treillis;
    private MainPanel vue;
    private int boutonSelect = 0;
    private Segment barreEnCoursDeCreation = null;

    private int etat;
    private final ArrayList<Forme> multipleSelect = new ArrayList<>();

    private ArrayList<Forme> selection;
    
    private final HashMap<Integer, double[]> formesRes = new HashMap<>();
    private final HashMap<Forme, Integer> formeId = new HashMap<>();

    private int currentClick = 0;
    private Point firstSegmentPoint = null, secondSegmentPoint = null;


    private boolean inMultSelect = false, drag = false;

    private boolean inDrawing = true;

    private FenetreInfo fenetreinfo;


    //1 m => 50 px
    private final double echelle = 50;
    
    
    
    public Controleur(MainPanel vue) {
        this.vue = vue;
        this.selection = new ArrayList<>();
    }
    
    private void realSave(File f) {
        try {
            //this.vue.getModel().sauvegarde(f);
            this.vue.setCurFile(f);
            this.vue.getInStage().setTitle(f.getName());
        }/* catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Problème durant la sauvegarde");
            alert.setContentText(ex.getLocalizedMessage());

            alert.showAndWait();
        }*/ finally {
            //this.changeEtat(20);
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
               /* Forme lue = Forme.lecture(f);
                Treillis glu = (Treillis) lue;
                Stage nouveau = new Stage();
                nouveau.setTitle(f.getName());
                Scene sc = new Scene(new MainPanel(nouveau, f, glu), 800, 600);
                nouveau.setScene(sc);
                nouveau.show();
            } catch (IOException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Problème durant la sauvegarde");
                alert.setContentText(ex.getLocalizedMessage());

                alert.showAndWait();*/
            } finally {
                //this.changeEtat(20);
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
    }
    
    public void menuApropos(ActionEvent t) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("A propos");
        alert.setHeaderText(null);
        alert.setContentText("NUL\n"
                + "Vraiment\n"
                + "NUL\n"
                + "à l'INSA de Strasbourg");

        alert.showAndWait();
    }
    
    
    //ajout des fonctions appelÃƒÂ©s durant diffÃƒÂ©rentes actions de la souris
    private void addMouseEvent() {
        DessinCanvas canvas = mainpanel.getcDessin();

        //actions quand la souris est déplacé dans le canvas
        canvas.setOnMouseMoved(mouseEvent -> {
            X = mouseEvent.getX();
            Y = mouseEvent.getY();
            if(inDrawing && (boutonSelect/10) % 2 == 0){
                selection(boutonSelect/10);
            }
            dessine(inDrawing);
        });

        //action quand on clic sur la souris
        canvas.setOnMousePressed(mouseEvent -> {
            X = mouseEvent.getX();
            Y = mouseEvent.getY();

            if(mouseEvent.getButton() == MouseButton.PRIMARY && inDrawing) {
                inMultSelect = false;
                removeSelectedAll();

                switch (boutonSelect/10) {
                    case 0 -> setSelected();
                    case 1 -> addNoeud();
                    case 2 -> addBarre();
                    case 3 -> addTriangleTrn();
                }
                
            }
        });
        
        //action quand on arrete de cliquer sur la souris
        canvas.setOnMouseReleased(mouseEvent -> {
            dragMouseX = -1;
            dragMouseY = -1;
            if(mouseEvent.getButton() == MouseButton.PRIMARY) {
                drag = false;
                dessine(inDrawing);
            }else if(mouseEvent.getButton() == MouseButton.SECONDARY) {
            //mettre qqch
            }
        });

        canvas.setOnMouseDragged(mouseEvent -> {
            if(mouseEvent.getButton() == MouseButton.PRIMARY && inDrawing) {
                if (boutonSelect == 0) {
                    drag = true;
                    inMultSelect = true;
                    dragMouseX = mouseEvent.getX();
                    dragMouseY = mouseEvent.getY();
                    dragSelection();
                   dessine(inDrawing);
                }
            } else if(mouseEvent.getButton() == MouseButton.SECONDARY) {
                dragMouseX = mouseEvent.getX();
                dragMouseY = mouseEvent.getY();
                dessine(inDrawing);
            }
        });
    }
    
    //fonction permettant la selection d'un point
    private void setSelected(){

        if (currentSelect != null) {
            currentSelect.setSelected(false);
        }
        if (nearest != null) {
            if(nearest.equals(currentSelect)){
                nearest.setSelected(false);
                currentSelect = null;
                removeInfos();
            }else {
                nearest.setSelected(true);
                currentSelect = nearest;
                drawInfos(nearest);
            }
        }
    }
    
    //appele la bonne fonction d'ajout du noeud selon le type choisi
    private void addNoeud(){
            switch (boutonSelect) {
                case 10 -> addNoeudSimple();
                case 11 -> addAppui(false);
                case 12 -> addAppui(true);
                default -> System.out.println(boutonSelect);
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
            dessine(inDrawing);
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
        SegmentTerrain segment = NoeudAppui.isCreable(terrain,posX, posY);
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
            dessine(inDrawing);
            return appui;
    }
    
    //fonction de crÃƒÂ©ation d'une barre
    private void addBarre(){
        currentClick++;
        Noeud p = null;
        //test si on clique a cotÃƒÂ© d'un point ou pas
        //Besoin d'ajouter la vÃƒÂ©rification que le point est crÃƒÂ©able, et quel type de point



        if(nearest != null && nearest instanceof Noeud){
            boolean creable = true;
            if(currentClick > 1 && firstSegmentPoint != null) {
                double dist = Maths.distancePoint((Point) nearest, firstSegmentPoint);
                if(dist < 0){
                    creable = false;
                }
            }
            if(creable) p = (Noeud) nearest;
        }
        if(p == null){
            double dist = 0;
            if(currentClick > 1 && firstSegmentPoint != null) {
                dist = Maths.distancePoint(firstSegmentPoint, (new Point(X ,Y)));
            }
            if(currentClick == 1){
                p = createNoeudBarre(firstSegmentPoint);
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
            treillis.createBarre((Noeud) firstSegmentPoint, p);
            firstSegmentPoint.setSegmentSelected(false);
        }
        dessine(inDrawing);
    }

    //fonction de creation de noeud pour les barres
    //construit par defaut un noeud simple ou sinon un appui simple s'il ne peut pas
    public Noeud createNoeudBarre(Point firstSegmentPoint){
        Noeud noeudRes = null;
        double posX, posY;
            posX = X;
            posY = Y;
            noeudRes = addNoeudSimple(posX, posY);
            if(noeudRes == null){
                noeudRes = testAppui(true, posX, posY);
            }
        return noeudRes;

    }
    

    //ecrit les infos lié au calcul
    public void writeCalculInfo(HashMap<Forme, Integer> formeId, HashMap<Integer, double[]> idValues){
        mainpanel.getInfos().dessineCalculInfo(formeId, idValues);
    }

    //fonction de creation des points composant un triangle
    public PointTerrain addPointTrn() {
        double px = X, py = Y;
        PointTerrain pt = null;

        boolean creable = true;
        for (PointTerrain p : terrain.getPoints()) {
            if(Maths.distancePoint(p, new Point(px, py)) < 15) creable = false;
        }
        if(creable) pt = terrain.addPoint(px, py);

        dessine(inDrawing);
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
            Triangle triangle = new Triangle((PointTerrain) firstSegmentPoint, (PointTerrain) secondSegmentPoint, p, treillis.getNumerateur().getNewTriangleId(),terrain);
             terrain.addTriangle(triangle);

            treillis.updateNoeuds();
            firstSegmentPoint.setSegmentSelected(false);
            secondSegmentPoint.setSegmentSelected(false);
        }
        this.vue.redrawAll();
    }
    
    private void dessinProche(){
        Forme nearestforme = getNearest();
        if(nearestforme != null){
            nearestforme.dessinProche(context);
        }
    }
    
    //fonction de dessin principale
    public void dessine(boolean inDrawing) {

        DessinCanvas canvas = mainpanel.getcDessin();

        //dessinne de l'echelle
        context.setStroke(Color.BLACK);
        context.setLineWidth(5);
        context.setFill(Color.BLACK);
        context.fillText("1 m", canvas.getWidth() - 30 - getEchelle() / 2, canvas.getHeight() - 5);
        context.strokeLine(canvas.getWidth() - 20 - getEchelle(), canvas.getHeight() - 20, canvas.getWidth() - 20, canvas.getHeight() - 20);

        //dessin des noeud et des barres
        for (Forme f: treillis.getContient()) {
            if(boutonSelect != 0) {
                f.setSelected(false);
            }
            if(boutonSelect != 20 && boutonSelect != 40 && f instanceof Point){
                ((Point) f).setSegmentSelected(false);
            }
            f.dessine(context);
            this.vue.redrawAll();
        }

        //dessin des noeuds et barres selectionné + des infos associées
        if(isInMultSelect()){
            ArrayList<Forme> multipleSelectec = getMultipleSelect();
            int nbNoeud = 0;
            int nbAppuiSimple = 0;
            int nbAppuiDouble = 0;
            int nbBarre = 0;
            for (Forme f: multipleSelectec) {
                if(f instanceof NoeudSimple) nbNoeud ++;
                else if(f instanceof Barre) nbBarre ++;
                else if(f instanceof NoeudAppuiDouble) nbAppuiDouble ++;
                else if(f instanceof NoeudAppuiSimple) nbAppuiSimple ++;
            }
            drawInfosMultiplePoint(nbNoeud, nbAppuiDouble, nbAppuiSimple, nbBarre);
        }

        //dessin de la zone de selection avec clic droit aire que l'on veut séléctionner
        if (isDrag()) {
            double mouseX = getMouseX(), mouseY = getMouseY();
            double dragmouseX = getDragMouseX(), dragmouseY = getDragMouseY();
            context.setFill(Color.BLUE);
            context.setStroke(Color.DARKBLUE);
            context.setGlobalAlpha(0.5);
            context.fillRect(
                    Math.min(mouseX, dragmouseX),
                    Math.min(mouseY, dragmouseY),
                    Math.abs(dragmouseX - mouseX),
                    Math.abs(dragmouseY - mouseY));
            context.strokeRect(
                    Math.min(mouseX, dragmouseX),
                    Math.min(mouseY, dragmouseY),
                    Math.abs(dragmouseX - mouseX),
                    Math.abs(dragmouseY - mouseY));
        }else if(boutonSelect == 0 || boutonSelect == 20 || boutonSelect == 40){
         dessinProche();
         this.vue.redrawAll();
        }
        context.setGlobalAlpha(1);
    }
    
    
    public void changeColor(Color value) {
        if (this.etat == 20 && !this.selection.isEmpty()) {
            for (Forme f : this.selection) {
                f.changeCouleur(value);
            }
            this.vue.redrawAll();
        }
    }
    
    public void zoomDouble() { //a refaire les partie zoom et déplacement
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
    
    //supprime un element
    public void deleteForme(Forme f){
        if(f != null) {
            if (f instanceof PointTerrain || f instanceof Triangle || f instanceof SegmentTerrain) {
                treillis.removeElement(f);
                currentSelect = null;
            }
        }
    }
    
    public void deleteAllFormes() {
        multipleSelect.forEach(f -> {
            treillis.removeElement(f);
        });

        multipleSelect.clear();
        removeInfos();
        inMultSelect = false;
        dessine(inDrawing);
    }

    
    //retire le point selectionné
    public void removeSelected() {
        if (currentSelect != null) {
            currentSelect.setSelected(false);
        }
        mainpanel.getInfos().removeInfos();
        currentSelect = null;
    }

    //retire tout les points selectionnÃƒÂ©s
    public void removeSelectedAll() {
        multipleSelect.forEach(p -> p.setSelected(false));
        multipleSelect.clear();
        removeInfos();
    }
    
    public void selection(int selectedButton){
        //ajoute les formes dans la selection

        ArrayList<Forme> formes = new ArrayList<>();

        formes.addAll(treillis.getContient());
        formes.addAll(terrain.getTriangles());
        formes.addAll(terrain.getPoints());
        formes.addAll(terrain.getSegments());

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
        ArrayList<Forme> formes = treillis.getContient();

        for (Forme f: formes) {
            Point p;
            if(f instanceof Point) {
                p = (Point) f;
            }else{
                p = ((Segment) f).getCenter();
            }

            if (drag) {
                if (p.getPx()< Math.max(X, dragMouseX) && p.getPx() > Math.min(X, dragMouseX) &&
                        p.getPy() < Math.max(Y, dragMouseY) && p.getPy() > Math.min(Y, dragMouseY)) {
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
    
    public void removeInfos()
    {
        fenetreinfo.removeInfos();
    }

    public void drawInfosMultiplePoint(int nbNoeud,int nbAppuiDouble, int nbAppuiSimple,int nbBarre) {
        fenetreinfo.dessineInfosMultiplePoint(nbNoeud, nbAppuiDouble, nbAppuiSimple, nbBarre);
    }

    public void drawInfos(Forme nearest){
        fenetreinfo.dessineInfos(nearest);
    }
    

    public void boutonSelect(int t) {
        this.boutonSelect=t ;
    }
   
    public int getboutonSelect() {
        return boutonSelect;
    }
   
    public ArrayList<Forme> getSelection() {
        return selection;
    }
    
    void creePointParDialog() {
        Optional<Point> p = EnterPointDialog.demandePoint();
        if (p.isPresent()) {
            this.vue.getModel().add(p.get());
            this.vue.redrawAll();
        }
    }
    
    public String getLastOpen() {
        return lastOpen;
    }
       
    public Forme getNearest(){
        return nearest;
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

    public double getEchelle() {
        return echelle;
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