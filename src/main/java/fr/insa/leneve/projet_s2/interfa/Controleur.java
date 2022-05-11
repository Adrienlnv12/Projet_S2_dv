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

import fr.insa.leneve.projet_s2.structure.Treillis;
import fr.insa.leneve.projet_s2.structure.TypedeBarre;
import fr.insa.leneve.projet_s2.structure.forme.Forme;
import fr.insa.leneve.projet_s2.structure.forme.Point;
import fr.insa.leneve.projet_s2.structure.forme.Segment;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    private MainPanel vue;
    private int boutonSelect = 0;
    private TypedeBarre barreType = null;
    private Treillis treillis;

    private int etat;

    private Point noeud1DansModel,noeud2DansModel;

    private List<Forme> selection;

    private Segment barreEnCoursDeCreation = null;

    public Controleur(MainPanel vue) {
        this.vue = vue;
        this.selection = new ArrayList<>();
    }

    public void changeEtat(int nouvelEtat) {
        switch (nouvelEtat) {
            case 20 -> {
                this.vue.getRbSelect().setSelected(true);
                this.selection.clear();
                this.barreEnCoursDeCreation = null;
                this.vue.redrawAll();
            }
            case 30 -> {
                // creation de points
                this.vue.getRbNoeud().setSelected(true);
                this.selection.clear();
                this.barreEnCoursDeCreation = null;
                this.vue.getbGrouper().setDisable(true);
                this.vue.redrawAll();
            }
            case 40 -> {
                // creation de segments étape 1
                this.vue.getRbBarre().setSelected(true);
                this.selection.clear();
                this.barreEnCoursDeCreation = null;
                this.vue.getbGrouper().setDisable(true);
                this.vue.redrawAll();
            }
            case 41 -> {
            }
            default -> {
            }
        }
        // creation de segments étape 2
        this.etat = nouvelEtat;
        this.activeBoutonsSuivantSelection();

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

    private void realSave(File f) {
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
