/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2.interfa;

import fr.insa.leneve.projet_s2.calcul.Maths;
import fr.insa.leneve.projet_s2.structure.Noeud.*;
import fr.insa.leneve.projet_s2.structure.Terrain.*;
import fr.insa.leneve.projet_s2.structure.forme.Treillis;
import fr.insa.leneve.projet_s2.structure.forme.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
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
        this.treillis = this.vue.getModel();
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
        Scene sc = new Scene(new MainPanel(nouveau), 1000,700);
        nouveau.setScene(sc);
        nouveau.show();
    }
    
    public void menuApropos(ActionEvent t) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("A propos");
        alert.setHeaderText(null);
        alert.setContentText("""
                             NUL
                             Vraiment
                             NUL
                             \u00e0 l'INSA de Strasbourg""");

        alert.showAndWait();
    }
    
    
    public void clicDansZoneDessin(MouseEvent t) {
        switch (boutonSelect/10) {
            case 0:
                {
                             /*if (currentSelect != null) {
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
                        }*/
                        this.vue.redrawAll();
                        break;
                
                }
            case 1:
                {
                //appele la bonne fonction d'ajout du noeud selon le type choisi    
                    switch (boutonSelect) {
                        case 10:
                        {
                            addNoeudSimple(t);
                            this.vue.redrawAll();
                        break;
                        }
                        case 11:
                        {   addAppui(false,t);
                            this.vue.redrawAll();
                        break;
                        }
                        case 12:
                        {   addAppui(true,t);
                            this.vue.redrawAll();
                        break;
                        }
                        default: System.out.println(boutonSelect);
                        break;
                    }
                    break;
                }
            case 2:
                addBarre(t);
                this.vue.redrawAll();
                break;
            case 3:
                {
                    addTriangleTrn(t);
                    this.vue.redrawAll();
                    break;
                }
            default:
                break;
        }
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
    
    //fonction permettant la selection d'un point
    private void setSelected(MouseEvent t){
        
        // selection
        Point pclic = this.posInModel(t.getX(), t.getY());
                    // pas de limite de distance entre le clic et l'objet selectionné
                    Forme proche = this.vue.getModel().plusProche(pclic, Double.MAX_VALUE);
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
                        this.vue.redrawAll();            
        
        
        }
    }
    
   
    
    private NoeudSimple addNoeudSimple(MouseEvent t) {
        Transform modelVersVue = this.vue.getcDessin().getTransform();
        Point2D ptrans;
        try {
            ptrans = modelVersVue.inverseTransform(t.getX() ,t.getY());
        } catch (NonInvertibleTransformException ex) {
            throw new Error(ex);
        }
        boolean distCreable = NoeudSimple.DistestCreable(treillis, ptrans.getX() ,ptrans.getY());
        boolean triangleCreable = NoeudSimple.TriangleestCreable(treillis, ptrans.getX() ,ptrans.getY());
        //System.out.println(distCreable + " " + triangleCreable);
        if(distCreable && triangleCreable) {

            NoeudSimple ns = treillis.createNoeudSimple(ptrans.getX() ,ptrans.getY());
            System.out.println(ptrans.getX() + " " +ptrans.getY());
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
    public void addAppui(boolean simple, MouseEvent t) {
        testAppui(simple, t);
    }

    //test si il est possible de creer un appui, et si oui alors il le crÃƒÂ©e
    public NoeudAppui testAppui(boolean simple, MouseEvent t){
        SegmentTerrain segment = NoeudAppui.isCreable(terrain,t.getX() ,t.getY());
        if(segment != null) {
            return createAppui(simple, t.getX() ,t.getY(), segment);
        }else {
            Alert alerteNoeudAppui = new Alert(Alert.AlertType.WARNING);
            alerteNoeudAppui.setTitle("Erreur création noeud");
            alerteNoeudAppui.setContentText("Noeud non positionné sur un segment de terrain!");
            alerteNoeudAppui.showAndWait();
        }
        return null;
    }

    public NoeudAppui createAppui(boolean simple, double posX, double posY, SegmentTerrain segment) {
            NoeudAppui appui = treillis.createAppui(simple, segment.getTriangles().get(0), segment, Maths.distancePoint(segment.getDebut(), posX, posY) / segment.length());
            this.vue.redrawAll();
            return appui;
    }
    
    //fonction de création d'une barre
    private void addBarre(MouseEvent t){
        currentClick++;
        Noeud p = null;
        Point pclic = this.posInModel(t.getX(), t.getY());
        Forme proche = this.vue.getModel().plusProche(pclic, 30);
        //test si on clique a coté d'un point ou pas
        //Besoin d'ajouter la vérification que le point est créable, et quel type de point



        if(proche != null && proche instanceof Noeud){
            boolean creable = true;
            if(currentClick > 1 && firstSegmentPoint != null) {
                double dist = Maths.distancePoint((Point) proche, firstSegmentPoint);
                if(dist < 0){
                    creable = false;
                }
            }
            if(creable){
                p = (Noeud) proche;
            }
        }
        if(p == null){
            double dist = 0;
            if(currentClick > 1 && firstSegmentPoint != null) {
                Point pdclic = this.posInModel(t.getX(), t.getY());
                dist = Maths.distancePoint(firstSegmentPoint, (new Point(pdclic.getPx() ,pdclic.getPy())));
            }
            if(dist >= 0 || currentClick == 1){
                p = createNoeudBarre(firstSegmentPoint,t);
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
            this.vue.redrawAll();
        }
    }

    //fonction de creation de noeud pour les barres
    //construit par defaut un noeud simple ou sinon un appui simple s'il ne peut pas
    public Noeud createNoeudBarre(Point firstSegmentPoint,MouseEvent t){
        Noeud noeudRes;
            noeudRes = addNoeudSimple(t);
            if(noeudRes == null){
                noeudRes = testAppui(true, t);
            }
        return noeudRes;

    }
    

    //ecrit les infos lié au calcul
    /*public void writeCalculInfo(HashMap<Forme, Integer> formeId, HashMap<Integer, double[]> idValues){
        mainpanel.getInfo().dessineCalculInfo(formeId, idValues);
    }*/

    //fonction de creation des points composant un triangle
    public PointTerrain addPointTrn(MouseEvent t) {
        double px = t.getX(), py = t.getY();
        PointTerrain pt = null;

        boolean creable = true;
        for (PointTerrain p : terrain.getPoints()) {
            if(Maths.distancePoint(p, new Point(px, py)) < 15) creable = false;
        }
        if(creable) pt = terrain.addPoint(px, py);

        this.vue.redrawAll();
        return pt;
    }
    
    //fonction de creation de triangles
    public void addTriangleTrn(MouseEvent t){
        currentClick++;
        PointTerrain p;
        //test si on clique a coté d'un point ou pas
        //Besoin d'ajouter la vérification que le point est crÃƒÂ©able, et quel type de point
        if(nearest != null && nearest instanceof PointTerrain){
            p = (PointTerrain) nearest;
        }else{
            p = addPointTrn(t);
            if(p == null) {
                currentClick --;
                return;
            }
        }
      
            treillis.createTriangle((PointTerrain)p, t.getX(), terrain);
            
            //terrain.addTriangle(triangle);

            treillis.updateNoeuds();
            firstSegmentPoint.setSegmentSelected(false);
            secondSegmentPoint.setSegmentSelected(false);
            this.vue.redrawAll();
        
    }
    
    private void dessinProche(){
        Forme nearestforme = getNearest();
        if(nearestforme != null){
            nearestforme.dessinProche(context);
        }
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
        this.vue.redrawAll();
    }

    
   /* //retire le point selectionné
    public void removeSelected() {
        if (currentSelect != null) {
            currentSelect.setSelected(false);
        }
        mainpanel.getInfo().removeInfos();
        currentSelect = null;
    }*/

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
    
    void creeNoeudSimpleParDialog() {
        Optional<NoeudSimple> p = EnterNoeudSimpleDialog.demandeNoeudSimple();
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