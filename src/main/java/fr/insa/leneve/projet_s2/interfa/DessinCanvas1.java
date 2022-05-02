/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insa.leneve.projet_s2.interfa;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 *
 * @author cheraita
 */
public class DessinCanvas1 extends Pane {
    
    private MainPanel main;
    
    private Canvas realCanvas;
    
    public DessinCanvas1(MainPanel main){
        this.main=main;
        this.realCanvas = new Canvas(this.getWidth(),this.getHeight());
        this.getChildren().add(this.realCanvas);
        this.realCanvas.heightProperty().bind(this.heightProperty());
        this.realCanvas.heightProperty().addListener((o) -> {
            
             System.out.println("w = "+this.realCanvas.getWidth()+" ; h = "+this.realCanvas.getHeight());
        
        });
        this.realCanvas.widthProperty().bind(this.widthProperty());
        this.realCanvas.widthProperty().addListener((o) -> {
        
        });
        this.realCanvas.setOnMouseClicked((t) -> {
            System.out.println("clic");
            Controleur control = this.main.getControleur();
            control.clicDansZoneDessin(t);
        });
        
        
        
    }
    
    
    /**
     * @return the realCanvas
     */
    public Canvas getRealCanvas() {
        return realCanvas;
    }
    
    
    
}
