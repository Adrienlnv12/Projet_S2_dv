/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2.interfa;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * @author francois
 */
public class OutilsLeft extends VBox {
    
    private MainPanel main;

    private ToggleButton bSelect;
    private ToggleButton bPoint;
    private ToggleButton bSegment;

    public OutilsLeft(MainPanel main) {
        this.main = main;
        this.bSelect = new ToggleButton("select");
        this.bSelect.setOnAction((ActionEvent t) -> {
            System.out.println("select clicked  ");
           this.main.getControl().clicDansDessin(t);
        });
        this.bPoint = new ToggleButton("point");
        this.bPoint.setOnAction((ActionEvent t) -> {
            System.out.println("point clicked");
        });
        this.bSegment = new ToggleButton("segment");

        ToggleGroup gBoutons = new ToggleGroup();
        this.bSelect.setToggleGroup(gBoutons);
        this.bPoint.setToggleGroup(gBoutons);
        this.bSegment.setToggleGroup(gBoutons);
        this.bPoint.setSelected(true);

        this.getChildren().addAll(this.bSelect, this.bPoint, this.bSegment);
        FxUtils.setSimpleBorder(this, Color.CYAN, 2);

    }

}
