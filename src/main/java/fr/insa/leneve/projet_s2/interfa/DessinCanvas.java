/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insa.leneve.projet_s2.interfa;
import fr.insa.leneve.projet_s2.structure.Barre;
import fr.insa.leneve.projet_s2.structure.Noeud.NoeudAppuiDouble;
import fr.insa.leneve.projet_s2.structure.Noeud.NoeudAppuiSimple;
import fr.insa.leneve.projet_s2.structure.Noeud.NoeudSimple;
import fr.insa.leneve.projet_s2.structure.forme.Treillis;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import fr.insa.leneve.projet_s2.structure.forme.Forme;
import fr.insa.leneve.projet_s2.structure.forme.Point;
import java.util.ArrayList;
import javafx.scene.paint.Color;
/**
 *
 * @author cheraita
 */
public class DessinCanvas extends Pane {
    
    private MainPanel main;
    private final Canvas realCanvas;
    private final RectangleHV asRect;
    
    public DessinCanvas(MainPanel main){
        this.main = main;
        this.realCanvas = new Canvas(this.getWidth(), this.getHeight());
        this.asRect = new RectangleHV(0, 0, this.getWidth(), this.getHeight());
        this.getChildren().add(this.realCanvas);
        this.realCanvas.heightProperty().bind(this.heightProperty());
        this.realCanvas.heightProperty().addListener((o) -> {
            this.redrawAll();
        });
        this.realCanvas.widthProperty().bind(this.widthProperty());
        this.realCanvas.widthProperty().addListener((o) -> {
            this.redrawAll();
        });
        this.realCanvas.setOnMouseClicked((t) -> {
            Controleur control = this.main.getControleur();
            control.clicDansZoneDessin(t);
            this.redrawAll();
        });
        
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
      
     public void redrawAll() {
        GraphicsContext context = this.realCanvas.getGraphicsContext2D();
        context.setTransform(new Affine());
        context.clearRect(0, 0, this.realCanvas.getWidth(), this.realCanvas.getHeight());
        //dessinne de l'echelle
        context.setStroke(Color.BLACK);
        context.setLineWidth(5);
        context.setFill(Color.BLACK);
        context.fillText("1 m", this.realCanvas.getWidth() - 30 - 50 / 2, this.realCanvas.getHeight() - 5);
        context.strokeLine(this.realCanvas.getWidth() - 20 - 50, this.realCanvas.getHeight() - 20, this.realCanvas.getWidth() - 20, this.realCanvas.getHeight() - 20);
        this.asRect.setxMax(this.realCanvas.getWidth());
        this.asRect.setyMax(this.realCanvas.getHeight());
        Transform curTrans = this.main.getZoneModelVue().fitTransform(this.asRect);
        this.setTransform(curTrans);
        Treillis model = this.main.getModel();
        model.dessine(context);
        ArrayList<Forme> select = this.main.getControleur().getSelection();
        //dessin des noeud et des barres
        if (!select.isEmpty()) {
            for (Forme f : select) {
                f.dessine(context);
            }
        }  
        //dessin des noeuds et barres selectionné + des infos associées
        if(this.main.getControleur().isInMultSelect()){
            ArrayList<Forme> multipleSelectec = this.main.getControleur().getMultipleSelect();
            int nbNoeud = 0;
            int nbAppuiSimple = 0;
            int nbAppuiDouble = 0;
            int nbBarre = 0;
            for (Forme f: multipleSelectec) {
                if(f instanceof NoeudSimple) nbNoeud ++;
                else if(f instanceof Barre) nbBarre ++;
                else if(f instanceof NoeudAppuiDouble) nbAppuiDouble ++;
                else if(f instanceof NoeudAppuiSimple) nbAppuiSimple ++;
            }
            this.main.getControleur().drawInfosMultiplePoint(nbNoeud, nbAppuiDouble, nbAppuiSimple, nbBarre);
        }
/*
        //dessin de la zone de selection avec clic droit aire que l'on veut séléctionner
        if (isDrag()) {
            double mouseX = getMouseX(), mouseY = getMouseY();
            double dragmouseX = getDragMouseX(), dragmouseY = getDragMouseY();
            context.setFill(Color.BLUE);
            context.setStroke(Color.DARKBLUE);
            context.setGlobalAlpha(0.5);
            context.fillRect(
                    Math.min(mouseX, dragmouseX),
                    Math.min(mouseY, dragmouseY),
                    Math.abs(dragmouseX - mouseX),
                    Math.abs(dragmouseY - mouseY));
            context.strokeRect(
                    Math.min(mouseX, dragmouseX),
                    Math.min(mouseY, dragmouseY),
                    Math.abs(dragmouseX - mouseX),
                    Math.abs(dragmouseY - mouseY));
        }else if(boutonSelect == 0 || boutonSelect == 20 || boutonSelect == 40){
         dessinProche();
         this.vue.redrawAll();
        }
        context.setGlobalAlpha(1);*/
    }
        
}
