/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2.interfa;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * @author adrie
 */
public class BoutonDroite extends VBox{
    
    private final Controleur controleur;
    
    private Button bGrouper;
    private Button bSupObj;
    
    private ColorPicker cpCouleur;
    

    private BoutonIcone bZoomDouble;
    private BoutonIcone bZoomDemi;
    private BoutonIcone bZoomFitAll;
    
    private BoutonIcone bTranslateGauche;
    private BoutonIcone bTranslateDroite;
    private BoutonIcone bTranslateHaut;
    private BoutonIcone bTranslateBas;
    
    private Button bCreeNoeudDialog;
    
    private DessinCanvas1 cDessin;
    private RectangleHV zoneModelVue;
    
    public BoutonDroite(MainPanel mainpanel) {
        super(10);
        this.setAlignment(Pos.CENTER);
        this.setId("BoutonDroite");

        this.controleur = mainpanel.getControleur();
        
        initGrouper();
        initSup();


        initColorPicker();
        initZoomDouble();
        initZoomDemi();
        initZoomFitAll();
        bTranslateGauche();
        bTranslateDroite();
        bTranslateHaut();
        bTranslateBas();
        
        initCreeNoeudDialog(); 
        
        HBox hbZoom = new HBox(this.bZoomDouble, this.bZoomDemi, this.bZoomFitAll);
        
        GridPane gpTrans = new GridPane();
        // add(compo, column , row , columnSpan , rowSpan
        gpTrans.add(this.bTranslateGauche, 0, 1,1,1);
        gpTrans.add(this.bTranslateDroite, 2, 1,1,1);
        gpTrans.add(this.bTranslateHaut, 1, 0,1,1);
        gpTrans.add(this.bTranslateBas, 1, 2,1,1);
        
        VBox vbZoom = new VBox(hbZoom,gpTrans);
        vbZoom.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        this.getChildren().addAll(bGrouper, bSupObj, cpCouleur, vbZoom,bCreeNoeudDialog);

    }
    
    private void initGrouper() {
        bGrouper = new Button("Grouper");
        bGrouper.setOnAction((t) -> {
            this.controleur.boutonGrouper(t);
        });
        
        this.bGrouper.setOnMouseEntered((t) -> {
            System.out.println("entre dans groupe");
        });
    }
    
    private void initSup() {
        this.bSupObj = new Button("Supprimer");
        this.bSupObj.setOnAction((t) -> {
            this.controleur.boutonSupprimer(t);
        });
    }
    
    private void initColorPicker() {
        this.cpCouleur = new ColorPicker(Color.BLACK);
        this.cpCouleur.setOnAction((t) -> {
            this.controleur.changeColor(this.cpCouleur.getValue());
        });
    }
        
        /*this.bRes = new Button("Résolution"); //crée le toggle bouton 
        this.bRes.setOnAction((t) -> {
            this.controleur.boutonResolution(t);
        });  */ 
        
    private void initZoomDouble() {    
        this.bZoomDouble = new BoutonIcone("icones/zoomPlus.png",32,32);
        this.bZoomDouble.setOnAction((t) -> {
            this.controleur.zoomDouble();
        });
    }
    private void initZoomDemi() {  
        this.bZoomDemi = new BoutonIcone("icones/zoomMoins.png",32,32);
        this.bZoomDemi.setOnAction((t) -> {
            this.controleur.zoomDemi();
        });
    }
    
    private void initZoomFitAll() {  
        this.bZoomFitAll = new BoutonIcone("icones/zoomTout.png",32,32);
        this.bZoomFitAll.setOnAction((t) -> {
            this.controleur.zoomFitAll();
        });
    }
    private void bTranslateGauche() {
        this.bTranslateGauche = new BoutonIcone("icones/gauche.png",32,32);
        this.bTranslateGauche.setOnAction((t) -> {
            this.controleur.translateGauche();
        });
    }
    private void bTranslateDroite() {
        this.bTranslateDroite = new BoutonIcone("icones/droite.png",32,32);
       this.bTranslateDroite.setOnAction((t) -> {
            this.controleur.translateDroite();
        });
    }
    private void bTranslateHaut() {
         this.bTranslateHaut = new BoutonIcone("icones/haut.png",32,32);
        this.bTranslateHaut.setOnAction((t) -> {
            this.controleur.translateHaut();
        });
    }
    private void bTranslateBas() {
        this.bTranslateBas = new BoutonIcone("icones/bas.png",32,32);
       this.bTranslateBas.setOnAction((t) -> {
            this.controleur.translateBas();
        });
    }
      
    private void initCreeNoeudDialog() {  
        this.bCreeNoeudDialog = new Button("Point par coord");
        this.bCreeNoeudDialog.setOnAction((t) -> {
            this.controleur.creePointParDialog();
        });
    }

  
    /*public void fitAll() {
        this.zoneModelVue = new RectangleHV(this.Treillis.minX(),
                this.Treillis.maxX(), this.Treillis.minY(), this.Treillis.maxY());
        this.zoneModelVue = this.zoneModelVue.scale(1.1);
    }*/
}
