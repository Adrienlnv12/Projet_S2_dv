/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2.interfa;

import fr.insa.leneve.projet_s2.calcul.Maths;
import fr.insa.leneve.projet_s2.calcul.Matrice;
import fr.insa.leneve.projet_s2.structure.Barre;
import fr.insa.leneve.projet_s2.structure.Force;
import fr.insa.leneve.projet_s2.structure.Noeud.*;
import fr.insa.leneve.projet_s2.structure.Terrain.*;
import fr.insa.leneve.projet_s2.structure.forme.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author adrie
 */
public class Controleur {
    private Stage stage;
    private String name;
    private String path;
    private boolean NoeudBoutton;
    private PointTerrain pointTT;
    private Forme nearest;
    private double x;
    private final MainPanel vue;
    private int boutonSelect = 0;
    private final ArrayList<Forme> selection;
    
    private final HashMap<Integer, double[]> formesRes = new HashMap<>();
    private final HashMap<Forme, Integer> formeId = new HashMap<>();

    private int currentClick = 0;
    private Point firstSegmentPoint = null, secondSegmentPoint = null;

    public boolean inMultSelect = false, isostatique=false;

    private FenetreInfo fenetreinfo;


    //1 m => 50 px
    private final double echelle = 50;
    
    
    
    public Controleur(MainPanel vue) {
        this.vue = vue;
        this.selection = new ArrayList<>();
        this.fenetreinfo = new FenetreInfo(this.vue);
    }
    
    private void realSave(File f) {
        try {
            Treillis.saveTreillis(this.vue.getModel(),f);
            this.vue.setCurFile(f);
            this.vue.getInStage().setTitle(f.getName());
        }catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Problème durant la sauvegarde");
            alert.setContentText(ex.getLocalizedMessage());

            alert.showAndWait();
        } finally {
            this.boutonSelect=0;
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
                Forme lue = null;//Forme.lecture(f);
                Treillis glu = (Treillis) lue;
                Stage nouveau = new Stage();
                nouveau.setTitle(f.getName());
                Scene sc = new Scene(new MainPanel(nouveau, f, glu), 800, 600);
                nouveau.setScene(sc);
                nouveau.show();
            /*} catch (IOException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Problème durant la sauvegarde");
                alert.setContentText(ex.getLocalizedMessage());

                alert.showAndWait();*/
            } finally {
                this.boutonSelect=0;
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
                             Merci d'utiliser notre programme
                             Nous esperons que vous passez
                             un bon moment avec le groupe B3
                             à l'INSA de Strasbourg""");

        alert.showAndWait();
    }
    
    public void calculs(){
        int id = 0;
        int ns = this.vue.getModel().getNoeuds().size(), nb = this.vue.getModel().getBarres().size(), nas = 0, nad = 0;
        //liste les formes et les associes Ã  un identifiant
        formeId.clear();
        HashMap<Integer, Forme> idForme = new HashMap<>();
        for (Barre barre : this.vue.getModel().getBarres()) {
            formeId.put(barre, id);
            idForme.put(id, barre);
            id ++;
        }
        for (Noeud noeud : this.vue.getModel().getNoeuds()) {
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
        String textError;
        if (2 * ns != nb + nas + 2 * nad) {
                textError = "Le Treillis n'est pas isostatique !";
            if (2 * ns < nb + nas + 2 * nad) {
                if(textError.length() > 0) textError += " et ";
                textError += "il est hyperstatique!";
            }
        Alert alerteHyperstatique = new Alert(Alert.AlertType.ERROR);
        alerteHyperstatique.setTitle("Isostatisme du treillis");
        alerteHyperstatique.setContentText(textError);
        alerteHyperstatique.showAndWait();
        }else{
            textError ="Le Treillis est isostatique !";
            Alert alerteHyperstatique = new Alert(AlertType.INFORMATION);
            alerteHyperstatique.setTitle("Isostatisme du treillis");
            alerteHyperstatique.setContentText(textError);
            alerteHyperstatique.showAndWait();
        }
       
        Matrice systeme = fillMatrice(formeId, id);

        Matrice res = systeme.resolution();
        if(res == null) {
            textError ="La Matrice n'est pas inversible !";
            Alert alerteHyperstatique = new Alert(Alert.AlertType.ERROR);
            alerteHyperstatique.setTitle("Calcul de la matrice");
            alerteHyperstatique.setContentText(textError);
            alerteHyperstatique.showAndWait();
            throw new Error("Matrice non inversible");
        }
        res = res.subCol(systeme.getNbrCol() - 1, systeme.getNbrCol() - 1);

        formesRes.clear();
        for (int value : formeId.values()) {
            if(idForme.get(value) instanceof NoeudAppuiDouble){
                formesRes.put(value, new double[]{res.get(value, 0), res.get(value + 1, 0)});
            }else{
                formesRes.put(value, new double[]{res.get(value, 0)});
            }
        }
        writeCalculInfo(formeId, formesRes);
    }

    public Matrice fillMatrice(HashMap<Forme, Integer> formeToId, int lastId){
        
        int size = this.vue.getModel().getNoeuds().size()* 2; //*2 car on a x et y
        Matrice systeme = new Matrice(size, lastId);
        double[] vecResult = new double[size];

        //rempli la matrice
        for (int i = 0; i < this.vue.getModel().getNoeuds().size() * 2; i += 2) {
            Noeud noeud = this.vue.getModel().getNoeuds().get(i/2);
            
            //ajout des valeurs liées aux barres
            for (Barre barre : noeud.getLinkedBarres()) {
                int col = formeToId.get(barre);
                double angle;
                if(noeud == barre.getDebut()){
                    angle = Maths.angle(noeud, barre.getFin());
                }else if(noeud == barre.getFin()){
                    angle = Maths.angle(noeud, barre.getDebut());
                }else{
                    angle = 0;//cos(90) xnoeud=xbarre
                    System.err.println("euuuuh");
                }
                systeme.set(i, col, Math.cos(angle));
                systeme.set(i + 1, col, Math.sin(angle));
            }
            //ajout des forces
            vecResult[i] = - noeud.getForceApplique().getfX();
            vecResult[i + 1] = - noeud.getForceApplique().getfY();


            if(noeud instanceof NoeudAppuiSimple noeudAppuiSimple){
                int col = formeToId.get(noeudAppuiSimple);
                double angle=0;
                if(noeudAppuiSimple.getSegmentTerrain().getDebut().getPy()!= noeudAppuiSimple.getSegmentTerrain().getFin().getPy()){
                    angle = Maths.angle(noeudAppuiSimple.getSegmentTerrain().getDebut(), noeudAppuiSimple.getSegmentTerrain().getFin());
                }
                systeme.set(i, col, Math.cos((Math.PI /2) + angle));
                systeme.set(i + 1, col, Math.sin(angle));

            }else if(noeud instanceof NoeudAppuiDouble){
                int col = formeToId.get(noeud);
                systeme.set(i, col, 1);
                systeme.set(i + 1, col + 1, 1);

            }

        }
        return Matrice.concatCol(systeme, Matrice.creeVecteur(vecResult));
        
    }
    
    public void MoveDansZoneDessin(MouseEvent t) {
            Point pclic = this.posInModel(t.getX(), t.getY());
            if((boutonSelect/10) % 2 == 0){
                selection(boutonSelect/10,pclic);
            }     
    }
    
    public void selection(int selectedButton,Point pclic){
        //ajoute les formes dans la selection

        ArrayList<Forme> formes = new ArrayList<>();

        formes.addAll(this.vue.getModel().getContient());

        double bestDist = 4;

        for (Forme f: formes) {
            Point p;
            if(f instanceof Noeud && (selectedButton == 2 || selectedButton == 0)) {
                p = (Point) f;
            }else if(selectedButton == 0){
                if(f instanceof Segment){
                    p = ((Segment) f).getCenter();
                }else if(f instanceof Triangle){
                    p = ((Triangle) f).getCenter();
                }else {
                    return;
                }
            }else{
                continue;
            }

            //trouve la forme le plus proche de la souris
            
            double dist = Maths.distancePoint(p, pclic);
            if(dist < bestDist){
                nearest = f;
                bestDist = dist;
            }
            if(bestDist == 4) {
                nearest = null;
            }
        }

    }
    
    public void clicDansZoneDessin(MouseEvent t) {
        switch (boutonSelect/10) {
            case 0:
                {
                    setSelected(t); 
                    this.vue.redrawAll();                
                    break;
                }
            case 1:
                {         
                //appele la bonne fonction d'ajout du noeud selon le type choisi    
                    switch (boutonSelect) {
                        case 10 ->                         {
                            addNoeudSimple(t);
                            this.vue.redrawAll();
                        }
                        case 11 ->                         {   
                            addAppui(false,t);
                            this.vue.redrawAll();
                        }
                        case 12 ->                         { 
                            addAppui(true,t);
                            this.vue.redrawAll();
                        }
                        default -> System.out.println(boutonSelect);
                    }
                    break;
                }
            case 2:
                
                addBarre(t);
                this.vue.redrawAll();
                break;
            case 3:
                {
                    
                    switch (boutonSelect) {
                        case 30 ->                         {
                            this.pointTT = addPointTrn(t);
                            this.boutonSelect=31;
                        }
                        case 31 ->                         {   
                            Point pclic = this.posInModel(t.getX(), t.getY());
                            this.x=pclic.getPx();
                            this.vue.getModel().createTriangle(pointTT, x);
                            this.vue.redrawAll();
                            this.boutonSelect=30;
                        }
                        default -> System.out.println(boutonSelect);
                    }
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
        return pclic;
    }
   
    public void boutonadForce(ActionEvent t) {
        for (Forme f: this.selection) {
        if(f instanceof Noeud noeud) {
            Force.createTypePopUp(noeud);
            }
        }
    }
    
    public void boutonresolution(ActionEvent t) {
        calculs();
    }
    
    public void boutonSupprimer(ActionEvent t) {
        if (boutonSelect == 0 && !this.selection.isEmpty()) {
            this.vue.getModel().remove(this.selection);
            this.vue.getInfo().removeInfos();
            inMultSelect=false;
            this.selection.clear();
            this.vue.redrawAll();
        }
    }
    
    public void boutonToutSupprimer(ActionEvent t) {
            this.vue.getModel().removeAll();
            inMultSelect=false;
            this.selection.clear();
            this.vue.getInfo().removeInfos();
            this.vue.redrawAll();
        
    }
    
    private void setSelected(MouseEvent t){
                    // selection
                    Point pclic = this.posInModel(t.getX(), t.getY());
                    // pas de limite de distance entre le clic et l'objet selectionné
                    Forme proche = this.vue.getModel().plusProche(pclic, 4);
                    for (Noeud p : this.vue.getModel().getNoeuds()) {
                        if (Maths.distancePoint(p, pclic) < 4){
                            if(p instanceof NoeudSimple n){
                                proche=n;
                            }
                            if(p instanceof NoeudAppui np){
                                proche=np;
                            }
                        }   
                    }    
                    // il faut tout de même prévoir le cas ou le groupe est vide
                    // donc pas de plus proche
                    if (proche != null) {
                    this.vue.getInfo().dessineInfos(proche);
                        if (t.isShiftDown()) {
                            this.selection.add(proche);
                        }else if (t.isControlDown()) {
                            if (this.selection.contains(proche)) {
                                this.selection.remove(proche);
                                this.vue.redrawAll();
                            }else {
                                this.selection.add(proche);
                            }
                        }else {
                            this.selection.clear();
                            this.selection.add(proche);
                        }
                    }else{
                        this.selection.clear();
                        this.vue.getInfo().removeInfos();
                        
                    }
                        
                    if(this.selection.size()>1){
                        inMultSelect=true;
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
        boolean distCreable = NoeudSimple.DistestCreable(this.vue.getModel(), ptrans.getX() ,ptrans.getY());
        boolean triangleCreable = NoeudSimple.TriangleestCreable(this.vue.getModel(),ptrans.getX() ,ptrans.getY());
        if(distCreable && triangleCreable) {

            NoeudSimple ns = this.vue.getModel().createNoeudSimple(ptrans.getX() ,ptrans.getY());
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
        alerteTriangleTerrain.setTitle("Erreur création noeud");
        alerteTriangleTerrain.setContentText(textError);
        alerteTriangleTerrain.showAndWait();


        return null;
    }

    //fonctions d'ajout d'un appui
    public void addAppui(boolean simple, MouseEvent t) {
        testAppui(simple, t);
        
    }

    //test si il est possible de creer un appui, et si oui alors il le crée
    public NoeudAppui testAppui(boolean simple, MouseEvent t){
        SegmentTerrain segment =null;
        Point pclic = this.posInModel(t.getX(), t.getY());
        Forme proche = this.vue.getModel().plusProche(pclic, 4);
        if(proche instanceof Triangle tr){
        segment = NoeudAppui.isCreable(tr,pclic.getPx() ,pclic.getPy());
        }
        boolean distCreable = NoeudAppui.DistestCreable(this.vue.getModel(), pclic.getPx() ,pclic.getPy());
        for (Noeud p : this.vue.getModel().getNoeuds()) {
            if (Maths.distancePoint(p, pclic) < 4){
                if(p instanceof NoeudAppui np){
                    return np;
                }
            }   
        } 
        if(segment != null && distCreable) {
            return createAppui(simple, pclic.getPx() ,pclic.getPy(), segment);
        }
        
        String textError1 = "";
        if(!distCreable){
            textError1 = "Noeuds trop proches!";
        }
        if((segment == null)&&(boutonSelect==11 || boutonSelect==12)){
            if(textError1.length() > 0) textError1 += " et ";
            textError1 += "Noeud non positionné sur un segment de terrain!";
        }
      
        Alert alerteNoeudAppui = new Alert(Alert.AlertType.WARNING);
        alerteNoeudAppui.setTitle("Erreur création noeud appui");
        alerteNoeudAppui.setContentText(textError1);
        alerteNoeudAppui.showAndWait();
        return null;
    }

    public NoeudAppui createAppui(boolean simple, double posX, double posY, SegmentTerrain segment) {
        
            NoeudAppui appui = this.vue.getModel().createAppui(simple, segment.getTriangles().get(0), segment, Maths.distancePoint(segment.getDebut(), posX, posY) / segment.length());
            return appui;
    }
    
    //fonction de création d'une barre
    private void addBarre(MouseEvent t){
        currentClick++;
        Noeud p = null;
        Point pclic = this.posInModel(t.getX(), t.getY());
        Forme proche = this.vue.getModel().plusProche(pclic, 4);
        //test si on clique a coté d'un point ou pas
        //Besoin d'ajouter la vérification que le point est créable, et quel type de point
        for (Noeud pr : this.vue.getModel().getNoeuds()) {
            if (Maths.distancePoint(pr, pclic) < 4){
                if(pr instanceof NoeudSimple n){
                    System.out.println("ler");
                    proche=n;
                }
                if(pr instanceof NoeudAppui np){
                    proche=np;
                }
            }   
        }    
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
            this.vue.getModel().createBarre((Noeud) firstSegmentPoint, p);
            firstSegmentPoint.setSegmentSelected(false);
            this.vue.redrawAll();
        }
    }

    //fonction de creation de noeud pour les barres
    //construit par defaut un noeud simple ou sinon un appui simple s'il ne peut pas
    public Noeud createNoeudBarre(Point firstSegmentPoint,MouseEvent t){
        
        Noeud noeudRes;
        SegmentTerrain segment=null;
        Point pclic = this.posInModel(t.getX(), t.getY());
        Forme proche = this.vue.getModel().plusProche(pclic, 4);
        if(proche instanceof Triangle tr){
        segment = NoeudAppui.isCreable(tr,pclic.getPx() ,pclic.getPy());
        }
        if(segment==null){
            noeudRes = addNoeudSimple(t);     
        }else if(NoeudBoutton==false){
            noeudRes = testAppui(true, t);          
        }
        else{
            noeudRes = testAppui(false, t);
        }
        return noeudRes;

    }
    

    //ecrit les infos lié au calcul
    public void writeCalculInfo(HashMap<Forme, Integer> formeId, HashMap<Integer, double[]> idValues){
        this.vue.getInfo().dessineCalculInfo(formeId, idValues);
    }

    //fonction de creation des points composant un triangle
    public PointTerrain addPointTrn(MouseEvent t) {
        Point pclic = this.posInModel(t.getX(), t.getY());
        double px = pclic.getPx(), py = pclic.getPy();
        PointTerrain pt = new PointTerrain(px, py);
        return pt;
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

    public void removeInfos()
    {
        fenetreinfo.removeInfos();
    }
    

    public void boutonSelect(int t) {
        this.boutonSelect=t ;
        if(this.boutonSelect!=0){
                this.selection.clear();
                this.vue.getInfo().removeInfos();
                this.vue.redrawAll(); 
                }
    }
   
    public int getboutonSelect() {
        return boutonSelect;
    }
    
    public boolean getNoeudBoutton() {
        return NoeudBoutton;
    }
    
    public void NoeudBoutton(boolean t) {
        this.NoeudBoutton=t ;
    }
   
    public ArrayList<Forme> getSelection() {
        return selection;
    }
    
    void creePointParDialog() {
        Optional<NoeudSimple> p = EnterNoeudSimpleDialog.demandeNoeudSimple();
        if (p.isPresent()) {
            this.vue.getModel().add(p.get());
            this.vue.redrawAll();
        }
    }
    
    public boolean isInMultSelect() {
        return inMultSelect;
    }
    
    public Forme getNearest(){
        return nearest;
    }

    public String getName() {
        return name;
    }

    public int getCurrentClick() {
        return currentClick;
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

    public double getEchelle() {
        return echelle;
    }

    public HashMap<Integer, double[]> getResultCalcul() {
        return formesRes;
    }

    public HashMap<Forme, Integer> getFormeId() {
        return formeId;
    }

    }