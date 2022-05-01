/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2.interfa;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * 
 * @author francois
 */
public class DessinPane extends Pane{
    
    private MainPanel main;
    
    public DessinPane(MainPanel main) {
        super();
        Rectangle clip = new Rectangle();
        clip.heightProperty().bind(this.heightProperty());
        clip.widthProperty().bind(this.widthProperty());
        this.setClip(clip);
        this.main = main;
        this.redrawAll();
    }
    
    public  void redrawAll() {
        this.getChildren().add(this.main.getModel().dessine());
    }
    
}
