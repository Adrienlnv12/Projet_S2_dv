/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insa.leneve.projet_s2.interfa;
import fr.insa.leneve.projet_s2.graphic.Graphics;
import fr.insa.leneve.projet_s2.structure.Treillis;
import java.util.List;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import fr.insa.leneve.projet_s2.structure.forme.Forme;
import fr.insa.leneve.projet_s2.structure.forme.Segment;
/**
 *
 * @author cheraita
 */
public class DessinCanvas extends Pane {
    
    private MainPanel main;
    private final GraphicsContext gc;
    private final Controleur controleur;
    private Canvas realCanvas;
    private RectangleHV asRect;
    private final Graphics graphics;
    
    public DessinCanvas(MainPanel main){
        super();
        this.realCanvas = new Canvas(this.getWidth(), this.getHeight());
        this.asRect = new RectangleHV(0, 0, this.getWidth(), this.getHeight());

        Canvas canvas = new Canvas();
        this.controleur = main.getControleur();
        this.graphics = controleur.getGraphics();

        gc = canvas.getGraphicsContext2D();

        canvas.setManaged(false);
        canvas.widthProperty().bind(this.widthProperty());
        canvas.heightProperty().bind(this.heightProperty());
        this.getChildren().add(canvas);

        canvas.heightProperty().addListener((o) -> graphics.draw(controleur.getboutonSelect(), controleur.isInDrawing()));
        canvas.widthProperty().addListener((o) -> graphics.draw(controleur.getboutonSelect(), controleur.isInDrawing()));
        
    }
    
     public void concatenateTransform(Transform trans) {
        Transform oldTrans = this.realCanvas.getGraphicsContext2D().getTransform();
        Transform newTrans = oldTrans.createConcatenation(trans);
        this.setTransform(newTrans);
    }

    public void setTransform(Transform trans) {
        this.realCanvas.getGraphicsContext2D().setTransform(new Affine(trans));
    }

    public Transform getTransform() {
        return this.realCanvas.getGraphicsContext2D().getTransform();
    }
    
    /**
     * @return the realCanvas
     */
    public Canvas getRealCanvas() {
        return realCanvas;
    }
      public GraphicsContext getGraphicsContext() {
        return gc;
    }
        
}
