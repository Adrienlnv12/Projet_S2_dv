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

import fr.insa.leneve.projet_s2.structure.Barre;
import fr.insa.leneve.projet_s2.structure.Terrain.PointTerrain;
import fr.insa.leneve.projet_s2.structure.Terrain.SegmentTerrain;
import fr.insa.leneve.projet_s2.structure.Terrain.Terrain;
import fr.insa.leneve.projet_s2.structure.Terrain.Triangle;
import fr.insa.leneve.projet_s2.structure.Treillis;
import fr.insa.leneve.projet_s2.structure.TypedeBarre;
import fr.insa.leneve.projet_s2.structure.forme.Forme;
import fr.insa.leneve.projet_s2.graphic.Graphics;
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
 * @author francois
 */
public class Controleur {
    
    private Terrain terrain;
    private MainPanel mainpanel;
    private final Graphics graphics;
    private Stage stage;
    private String name;
    private String path;

    private Treillis treillis;
    private MainPanel vue;
    private int boutonSelect = 0;
    private TypedeBarre barreType = null;


    private int etat;
    private final ArrayList<Forme> multipleSelect = new ArrayList<>();
    
    private Point noeud1DansModel,noeud2DansModel;

    private List<Forme> selection;

    private Segment barreEnCoursDeCreation = null;
    
    private final HostServices hostServices;
    
    public Controleur(Treillis treillis, HostServices hostServices) {
        graphics = new Graphics();
        this.treillis = treillis;
        this.hostServices = hostServices;
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
        addKeyboardEvent();
    }
    
    private void realSave(File f) { //a refaire la partie sauvegarde
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
    }

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
        pclic.setCouleur(this.vue.getCpCouleur().getValue());
        return pclic;
    }
    
    //ajout des fonctions appelÃƒÂ©s durant diffÃƒÂ©rentes actions de la souris
    private void addMouseEvent() {
        MainCanvas canvas = mainScene.getCanvas();

        //actions quand la couris est dÃƒÂ©placÃƒÂ© dans le canvas
        canvas.setOnMouseMoved(mouseEvent -> {
            mouseX = mouseEvent.getX();
            mouseY = mouseEvent.getY();
            if(inDrawing && (selectedButton/10) % 2 == 0){
                selection(selectedButton/10);
            }
            graphics.draw(selectedButton, inDrawing);
        });

        //action quand on clic sur la souris
        canvas.setOnMousePressed(mouseEvent -> {
            mouseX = mouseEvent.getX();
            mouseY = mouseEvent.getY();

            if(mouseEvent.getButton() == MouseButton.PRIMARY && inDrawing) {
                inMultSelect = false;
                removeSelectedAll();

                switch (selectedButton/10) {
                    case 0 -> setSelected();
                    case 1 -> addNoeud();
                    case 2 -> addBarre();
                    case 3 -> addZoneConstructible();
                    case 4 -> addTriangleTrn();
                }
            }
        });
    
     //appele la bonne fonction d'ajout du noeud selon le type choisi
    private void addNoeud(){
        if(terrain.contain(mouseX - graphics.getOrigin().getPosX(), mouseY - graphics.getOrigin().getPosY())) {
            switch (selectedButton) {
                case 10 -> addNoeudSimple();
                case 11 -> addAppui(false);
                case 12 -> addAppui(true);
                default -> System.out.println(selectedButton);
            }

        }else{

            Alert alerteZoneConstructible = new Alert(Alert.AlertType.ERROR);
            alerteZoneConstructible.setTitle("Erreur");
            alerteZoneConstructible.setContentText("Noeud hors zone constructible!");
            alerteZoneConstructible.showAndWait();

        }

    }
    
    public void clicDansZoneDessin(MouseEvent t) {
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
    }
    
    //met en place la zone constructible
    private void addZoneConstructible() {
        currentClick ++;

        if(currentClick == 1){
            terrainX = mouseX - graphics.getOrigin().getPosX();
            terrainY = mouseY - graphics.getOrigin().getPosY();
        }else{
            currentClick = 0;
            treillis.updateTerrain(Math.min(terrainX, mouseX - graphics.getOrigin().getPosX()), Math.min(terrainY, mouseY- graphics.getOrigin().getPosY()),
                    Math.max(terrainX,mouseX - graphics.getOrigin().getPosX()), Math.max(terrainY, mouseY- graphics.getOrigin().getPosY()));

            treillis.updateNoeuds(graphics);
            graphics.updateFormes(treillis);
            redraw();
        }
    }

    //ecrit les infos liÃƒÂ© au calcul
    public void writeCalculInfo(HashMap<Forme, Integer> formeId, HashMap<Integer, double[]> idValues){
        mainScene.getInfos().drawCalculInfo(formeId, idValues);
    }

    //fonction de creation des points composant un triangle
    public PointTerrain addPointTrn() {
        double px = mouseX - graphics.getOrigin().getPosX(), py = mouseY - graphics.getOrigin().getPosY();
        PointTerrain pt = null;

        boolean creable = true;
        for (PointTerrain p : terrain.getPoints()) {
            if(Maths.dist(p, new Point(px, py)) < 15) creable = false;
        }
        if(creable) pt = terrain.addPoint(px, py);

        graphics.draw(selectedButton, inDrawing);
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


    public void calculs(){
        int id = 0;
        int ns = treillis.getNoeuds().size(), nb = treillis.getBarres().size(), nas = 0, nad = 0;
        //liste les formes et les associes ÃƒÂ  un identifiant
        formeId.clear();
        HashMap<Integer, Forme> idForme = new HashMap<>();
        for (Barres barre : treillis.getBarres()) {
            formeId.put(barre, id);
            idForme.put(id, barre);
            id ++;
        }
        for (Noeud noeud : treillis.getNoeuds()) {
            if(noeud instanceof AppuiSimple){
                formeId.put(noeud, id);
                idForme.put(id, noeud);
                id ++;
                nas ++;
            }else if(noeud instanceof AppuiDouble){
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
            if(idForme.get(value) instanceof AppuiDouble){
                formesRes.put(value, new double[]{res.get(value, 0), res.get(value + 1, 0)});
            }else{
                formesRes.put(value, new double[]{res.get(value, 0)});
            }
        }
        System.out.println(formesRes);

        writeCalculInfo(formeId, formesRes);
    }

    public Matrice fillMatrice(HashMap<Forme, Integer> formeToId, int lastId){

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
        int size = treillis.getNoeuds().size() * 2;
        Matrice systeme = new Matrice(size, lastId);
        double[] vecResult = new double[size];

        //rempli la matrice
        for (int i = 0; i < treillis.getNoeuds().size() * 2; i += 2) {
            Noeud noeud = treillis.getNoeuds().get(i/2);
            System.out.println(noeud);
            //ajout des valeurs liÃƒÂ©es aux barres
            for (Barres barre : noeud.getLinkedBarres()) {
                int col = formeToId.get(barre);
                double angle;
                if(noeud == barre.getpA()){
                    angle = Maths.angle(noeud, barre.getpB());
                }else if(noeud == barre.getpB()){
                    angle = Maths.angle(noeud, barre.getpA());
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


            if(noeud instanceof AppuiSimple){
                int col = formeToId.get(noeud);
                double angle = Maths.angle(((AppuiSimple) noeud).getSegmentTerrain().getpA(), ((AppuiSimple) noeud).getSegmentTerrain().getpB());
                systeme.set(i, col, Math.cos(Math.PI /2 + angle));
                systeme.set(i + 1, col, Math.sin(angle));

            }else if(noeud instanceof AppuiDouble){
                int col = formeToId.get(noeud);
                systeme.set(i, col, 1);
                systeme.set(i + 1, col + 1, 1);
            }

        }

        System.out.println(formeToId);
        return Matrice.concatCol(systeme, Matrice.creeVecteur(vecResult));
    }
    
    public void boutonSelect(int t) {
        this.boutonSelect = t ;
;
    }
    public int getboutonSelect() {
        return boutonSelect;
    }

    public void setBarreType(TypedeBarre barreType) {
        this.barreType = barreType;
    }
    
    public void boutonNoeud(ActionEvent t) {
        this.changeEtat(30);
    }

    public void boutonBarre(ActionEvent t) {
        this.changeEtat(40);
    }

    private void activeBoutonsSuivantSelection() {
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
    }

    /**
     * @return the selection
     */
    public List<Forme> getSelection() {
        return selection;
    }

    public void boutonGrouper(ActionEvent t) {
        if (this.etat == 20 && this.selection.size() > 1) {
            // normalement le bouton est disabled dans le cas contraire
            Groupe ssGroupe = this.vue.getModel().sousGroupe(selection);
            this.selection.clear();
            this.selection.add(ssGroupe);
            this.activeBoutonsSuivantSelection();
            this.vue.redrawAll();
        }
    }

    public void boutonSupprimer(ActionEvent t) {
        if (this.etat == 20 && this.selection.size() > 0) {
            // normalement le bouton est disabled dans le cas contraire
            this.vue.getModel().removeAll(this.selection);
            this.selection.clear();
            this.activeBoutonsSuivantSelection();
            this.vue.redrawAll();
        }
    }

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
            this.vue.getModel().add(p.get());
            this.vue.redrawAll();
        }
    }

    public Treillis getTreillis() {
        return treillis;
    }

}
