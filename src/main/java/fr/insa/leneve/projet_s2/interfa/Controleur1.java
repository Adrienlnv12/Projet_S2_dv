/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2.interfa;

import fr.insa.leneve.projet_s2.graphic.Graphics;
import fr.insa.leneve.projet_s2.structure.forme.Forme;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseButton;

/**
 *
 * @author adrie
 */
public class Controleur1 {
    
    private MainPanel vue;
    
    private final Graphics graphics;
    
    private int etat;

    private List<Forme> selection;


    
    public Controleur1(/*Treillis treillis /*HostServices hostServices*/MainPanel vue) {
        this.vue = vue;
        this.selection = new ArrayList<>();
        graphics = new Graphics();
        //this.treillis = treillis;
        //this.hostServices = hostServices;
    }
    

            case 3 -> addZoneConstructible();
            case 4 -> addTriangleTrn();
    
    public void changeEtat(int nouvelEtat) {
        switch (nouvelEtat) {
            case 20 -> {
                this.vue.getRbSelect().setSelected(true);
                this.selection.clear();
                this.segmentEnCoursDeCreation = null;
                this.vue.redrawAll();
            }
            case 30 -> {
                // creation de points
                addNoeud();
                this.vue.getRbPoints().setSelected(true);
                this.selection.clear();
                this.segmentEnCoursDeCreation = null;
                this.vue.getbGrouper().setDisable(true);
                this.vue.redrawAll();
            }
            case 40 -> {
                addBarre();
                // creation de segments étape 1
                this.vue.getRbSegments().setSelected(true);
                this.selection.clear();
                this.segmentEnCoursDeCreation = null;
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
        
    public void boutonSelect(ActionEvent t) {
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
    
    public List<Forme> getSelection() {
        return selection;
    }
    
    /**
     * @return the rbSelect
     */
    public RadioButton getRbSelect() {
        return rbSelect;
    }

    /**
     * @return the RbNoeud
     */
    public RadioButton getRbNoeud() {
        return RbNoeud;
    }

    /**
     * @return the RbBarre
     */
    public RadioButton getRbBarre() {
        return RbBarre;
    }
    
    /**
     * @return the RbTerrain
     */
    public RadioButton getRbTerrain() {
        return RbTerrain;
    }
    
     /**
     * @return the RbTriangleTerrain
     */
    public RadioButton getRbTriangleTerrain() {
        return RbTriangleTerrain;
    }
    
    

                
}
