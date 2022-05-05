/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2.interfa;

import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;


/**
 *
 * @author francois
 */
public class OutilsTop extends HBox {
    
     // à faire ouvrir une fenetre pour modifier les couleurs de tous les obj de même type
    private Button zoomIn;
    private Button zoomOut;
    private Color couleur;
    
    private Button bSupObj;
    private Button bSelect;
    private ChoiceBox cbTypeBarre;
    
    public OutilsTop() {
       
        
        this.bSupObj = new Button("Supprimer");
        bSupObj.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);

        this.cbTypeBarre = new ChoiceBox(FXCollections.observableArrayList(
               "A", "B", "C"));// mettre les différents type de barres
        cbTypeBarre.setTooltip(new Tooltip("Séléctionner le type de barre"));//permet d'indiquer la fonction du bouton
        cbTypeBarre.setMaxSize(bSupObj.getMaxWidth(), bSupObj.getMaxHeight());
        
        this.bSelect = new Button("Select"); //crée le bouton select
        bSelect.setMaxSize(bSupObj.getMaxWidth(), bSupObj.getMaxHeight());
        this.bSelect.setOnAction((t)->{
        
        });
        this.zoomIn = new Button("Zoom *2");
        this.zoomOut = new Button("Zoom /2");
        
        this.getChildren().add(this.cbTypeBarre);
        this.getChildren().add(this.bSelect);
        this.getChildren().add(this.bSupObj);
        this.getChildren().add(this.zoomIn);
        this.getChildren().add(this.zoomOut);
        FxUtils.setSimpleBorder(this, Color.GREEN, 2);
        
    }
    public Color getCouleur() {
        return couleur;
    }
   
}
