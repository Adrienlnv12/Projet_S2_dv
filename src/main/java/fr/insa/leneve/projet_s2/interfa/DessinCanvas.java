/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
import java.util.ArrayList;
import javafx.scene.paint.Color;

/**
 *
 * @author adrie
 */
public class DessinCanvas extends Pane {
    
    private MainPanel main;
    private final Canvas realCanvas;
    private final RectangleHV asRect;
    private FenetreInfo fenetreinfo;
    
    public DessinCanvas(MainPanel main){
        this.main = main;
        fenetreinfo=main.getInfo();
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
        //actions quand la couris est dÃ©placÃ© dans le canvas
        this.realCanvas.setOnMouseMoved((t) -> {
            Controleur control = this.main.getControleur();
            control.MoveDansZoneDessin(t);
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
        context.setLineWidth(2);
        context.setFill(Color.BLACK);
        context.fillText("y", 18, this.realCanvas.getHeight() - 55);
        context.fillText("x", 55, this.realCanvas.getHeight() - 17);
        context.strokeLine(20,this.realCanvas.getHeight()-50,20,this.realCanvas.getHeight()-20);
        context.strokeLine(20+30,this.realCanvas.getHeight()-20,20,this.realCanvas.getHeight()-20);
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
                f.dessineSelection(context);
            }
        } 
        Forme nearest = this.main.getControleur().getNearest();
        //dessin des noeud et des barres
        if (nearest!= null) {
                nearest.dessinProche(context);
            } 
        
        //dessin des noeuds et barres selectionné + des infos associées
        if(this.main.getControleur().getSelection().size()>1){
        if(this.main.getControleur().isInMultSelect()){
            ArrayList<Forme> multipleSelect = this.main.getControleur().getSelection();
            int nbNoeud = 0;
            int nbAppuiSimple = 0;
            int nbAppuiDouble = 0;
            int nbBarre = 0;
            for (Forme f: multipleSelect) {
                if(f instanceof NoeudSimple) nbNoeud ++;
                else if(f instanceof Barre) nbBarre ++;
                else if(f instanceof NoeudAppuiDouble) nbAppuiDouble ++;
                else if(f instanceof NoeudAppuiSimple) nbAppuiSimple ++;
            }
            dessineInfosMultiplePoint(nbNoeud, nbAppuiDouble, nbAppuiSimple, nbBarre);
        }else{
            removeInfos();
        }
        }
     }
    public void removeInfos()
    {
        fenetreinfo.removeInfos();
    }

    public void dessineInfosMultiplePoint(int nbNoeud,int nbAppuiDouble, int nbAppuiSimple,int nbBarre) {
        fenetreinfo.dessineInfosMultiplePoint(nbNoeud, nbAppuiDouble, nbAppuiSimple, nbBarre);
    }

    public void dessineInfos(Forme nearest){
        fenetreinfo.dessineInfos(nearest);
    }
      
}
