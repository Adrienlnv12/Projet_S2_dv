/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insa.leneve.projet_s2.interfa;


import java.io.File;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import fr.insa.leneve.projet_s2.Groupe;
import javafx.collections.FXCollections;
/**
 *
 * @author adrie
 */
public class MainPanel extends BorderPane {
    /**
     * multiplicateur pour l'espace de départ : pour ne pas que les bords de la
     * figure soit confondus avec les bords de la fenêtre, on considère que l'on
     * veut que la fenêtre puisse contenir une figure MULT_POUR_FIT_ALL fois
     * plus grande que la figure réelle. Par exemple, si MULT_POUR_FIT_ALL = 2,
     * la figure complète n'occupera en fait qu'environ la moitié de la fenetre
     * graphique.
     */
    private static double MULT_POUR_FIT_ALL = 1.1;
    
    private Groupe model;
    private Controleur controleur;

    private Stage inStage;
    private File curFile;
    
    private RadioButton rbSelect;
    private RadioButton rbPoints;
    private RadioButton rbSegments;

    private Button bGrouper;
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

    private MainMenu menu;
    
    
    private RadioButton RbNoeudS;
    private RadioButton RbNoeudAS;
    private RadioButton RbNoeudAD;
    private RadioButton RbBarre;
    private RadioButton RbTriTerrain;
    private Button bRes;
    
   
    
    private Button bSupObj;
    private Button bSelect;

    
    private ChoiceBox cbTypeBarre;
    private Button bSave;
    
    private Label information;
    
    private Color couleur;
   
    public MainPanel(Stage inStage) {
        this(inStage, new Groupe());
    }

    public MainPanel(Stage inStage, Groupe model) {
        this(inStage, null, model);
    }
    
    
    public MainPanel(Stage inStage, File fromFile, Groupe model){  
        this.inStage = inStage;
        this.curFile = fromFile;
        this.model = model;
        this.fitAll(); 
        this.controleur = new Controleur(this);
       
        this.information = new Label(" Informations :");
// label à mettre à côté d'un TextArea pour donner des informations à l'utilisateur
        this.setBottom(information);
        
        this.rbSelect = new RadioButton("Select");
        this.rbSelect.setOnAction((t) -> {
            this.controleur.boutonSelect(t);
        });
        
        this.RbNoeudS = new RadioButton("Noeud simple");
        this.RbNoeudS.setOnAction((t) -> {
            this.controleur.boutonNoeudS(t);
        });
        
        this.RbNoeudAS = new RadioButton("Noeud appui simple");
        this.RbNoeudAS.setOnAction((t) -> {
            this.controleur.boutonNoeudAS(t);
        });
        
        this.RbNoeudAD = new RadioButton("Noeud appui double");
        this.RbNoeudAD.setOnAction((t) -> {
            this.controleur.boutonNoeudAD(t);
        });
        
        this.RbBarre = new RadioButton("Barre");
        this.RbBarre.setOnAction((t) -> {
            this.controleur.boutonBarre(t);
        });
        this.RbTriTerrain = new RadioButton("Triangle terrain");
        this.RbTriTerrain.setOnAction((t) -> {
            this.controleur.boutonTriTerrain(t);
        });
        
        ToggleGroup bgEtat = new ToggleGroup();
        this.rbSelect.setToggleGroup(bgEtat);
        this.RbNoeudS.setToggleGroup(bgEtat);
        this.RbNoeudAS.setToggleGroup(bgEtat);
        this.RbNoeudAD.setToggleGroup(bgEtat);
        this.RbBarre.setToggleGroup(bgEtat);
        this.RbTriTerrain.setToggleGroup(bgEtat);
        this.RbNoeudS.setSelected(true);
        
        VBox vbGauche = new VBox(this.rbSelect, this.RbNoeudS, this.RbNoeudAS, this.RbNoeudAD, this.RbTriTerrain);
        this.setLeft(vbGauche);
        
        this.bGrouper = new Button("Grouper");
        this.bGrouper.setOnAction((t) -> {
            this.controleur.boutonGrouper(t);
        });
        
        this.bGrouper.setOnMouseEntered((t) -> {
            System.out.println("entre dans bgroupe");
        });
        
        this.bSupObj = new Button("Supprimer");
        this.bSupObj.setOnAction((t) -> {
            this.controleur.boutonSupprimer(t);
        });
        this.cpCouleur = new ColorPicker(Color.BLACK);
        this.cpCouleur.setOnAction((t) -> {
            this.controleur.changeColor(this.cpCouleur.getValue());
        });
        this.cbTypeBarre = new ChoiceBox(FXCollections.observableArrayList(
               "A", "B", "C"));// mettre les différents type de barres
        cbTypeBarre.setTooltip(new Tooltip("Séléctionner le type de barre"));//permet d'indiquer la fonction du bouton
        cbTypeBarre.setMaxSize(cbTypeBarre.getMaxWidth(), cbTypeBarre.getMaxHeight());
        
        this.bRes = new Button("Résolution"); //crée le toggle bouton 
        this.bRes.setOnAction((t) -> {
            this.controleur.boutonResolution(t);
        });   
        
        
        this.bZoomDouble = new BoutonIcone("icones/zoomPlus.png",32,32);
        this.bZoomDouble.setOnAction((t) -> {
            this.controleur.zoomDouble();
        });
        this.bZoomDemi = new BoutonIcone("icones/zoomMoins.png",32,32);
        this.bZoomDemi.setOnAction((t) -> {
            this.controleur.zoomDemi();
        });
        this.bZoomFitAll = new BoutonIcone("icones/zoomTout.png",32,32);
        this.bZoomFitAll.setOnAction((t) -> {
            this.controleur.zoomFitAll();
        });
        
        this.bTranslateGauche = new BoutonIcone("icones/gauche.png",32,32);
        this.bTranslateGauche.setOnAction((t) -> {
            this.controleur.translateGauche();
        });
        this.bTranslateDroite = new BoutonIcone("icones/droite.png",32,32);
       this.bTranslateDroite.setOnAction((t) -> {
            this.controleur.translateDroite();
        });
         this.bTranslateHaut = new BoutonIcone("icones/haut.png",32,32);
        this.bTranslateHaut.setOnAction((t) -> {
            this.controleur.translateHaut();
        });
        this.bTranslateBas = new BoutonIcone("icones/bas.png",32,32);
       this.bTranslateBas.setOnAction((t) -> {
            this.controleur.translateBas();
        });
      
        HBox hbZoom = new HBox(this.bZoomDouble, this.bZoomDemi, this.bZoomFitAll);
        
        GridPane gpTrans = new GridPane();
        // add(compo, column , row , columnSpan , rowSpan
        gpTrans.add(this.bTranslateGauche, 0, 1,1,1);
        gpTrans.add(this.bTranslateDroite, 2, 1,1,1);
        gpTrans.add(this.bTranslateHaut, 1, 0,1,1);
        gpTrans.add(this.bTranslateBas, 1, 2,1,1);
        
        VBox vbZoom = new VBox(hbZoom,gpTrans);
        vbZoom.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        this.bCreeNoeudDialog = new Button("Point par coord");
        this.bCreeNoeudDialog.setOnAction((t) -> {
            this.controleur.creeNoeudParDialog();
        });
        
        VBox vbDroit = new VBox(this.bGrouper,this.bSupObj, this.cbTypeBarre, this.cpCouleur, vbZoom,this.bCreeNoeudDialog, this.bRes);
        this.setRight(vbDroit);
        
        this.cDessin = new DessinCanvas1(this);// zone de dessin
        this.setCenter(this.cDessin);
        
        this.menu = new MainMenu(this);
        this.setTop(this.menu);

        this.controleur.changeEtat(20);
    }    

    public void fitAll() {
        this.zoneModelVue = new RectangleHV(this.model.minX(),
                this.model.maxX(), this.model.minY(), this.model.maxY());
        this.zoneModelVue = this.zoneModelVue.scale(MULT_POUR_FIT_ALL);
    }

    public void redrawAll() {
        this.cDessin.redrawAll();
    }

    /**
     * @return the model
     */
    public Groupe getModel() {
        return model;
    }

    /**
     * @return the controleur
     */
    public Controleur getControleur() {
        return controleur;
    }
    
    /**
     * @return the rbSelect
     */
    public RadioButton getRbSelect() {
        return rbSelect;
    }
    
   
    public RadioButton getRbNoeudS() {
        return RbNoeudS;
    }
    
    public RadioButton getRbNoeudAS() {
        return RbNoeudAS;
    }

    public RadioButton getRbNoeudAD() {
        return RbNoeudAD;
    }

    public RadioButton getRbBarre() {
        return RbBarre;
    }
    
    public RadioButton getRbTriTerrain() {
        return RbTriTerrain;
    }
    
    /**
     * @return the bRes
     */
    public Button getbRes() {
        return bRes;
    }

    /**
     * @return the tbSupObj
     */
    public Button getBSupObj() {
        return bSupObj;
    }
    
    /**
     * @return the cbTypeBarre
     */
    public ChoiceBox getCbTypeBarre() {
        return cbTypeBarre;
    }

    /**
     * @return the bSave
     */
    public Button getbSave() {
        return bSave;
    }


    /**
     * @return the cDessin
     */
    public DessinCanvas1 getcDessin() {
        return cDessin;
    }


    public Color getCouleur() {
        return couleur;
    }
    
    
    /**
     * @return the information
     */
    public Label getInformation() {
        return information;
    }
    public Controleur getControl() {
        return controleur;
    }
   
    /**
     * @return the bGrouper
     */
    public Button getbGrouper() {
        return bGrouper;
    }

    /**
     * @return the cpCouleur
     */
    public ColorPicker getCpCouleur() {
        return cpCouleur;
    }

 
    public Stage getInStage() {
        return inStage;
    }

    /**
     * @return the curFile
     */
    public File getCurFile() {
        return curFile;
    }

    /**
     * @param curFile the curFile to set
     */
    public void setCurFile(File curFile) {
        this.curFile = curFile;
    }

    /**
     * @return the MULT_POUR_FIT_ALL
     */
    public static double getMULT_POUR_FIT_ALL() {
        return MULT_POUR_FIT_ALL;
    }

    /**
     * @return the bZoomDouble
     */
    public Button getbZoomDouble() {
        return bZoomDouble;
    }

    /**
     * @return the bZoomDemi
     */
    public Button getbZoomDemi() {
        return bZoomDemi;
    }

    /**
     * @return the bZoomFitAll
     */
    public Button getbZoomFitAll() {
        return bZoomFitAll;
    }

    /**
     * @return the zoneModelVue
     */
    public RectangleHV getZoneModelVue() {
        return zoneModelVue;
    }

    /**
     * @return the menu
     */
    public MainMenu getMenu() {
        return menu;
    }

    /**
     * @param zoneModelVue the zoneModelVue to set
     */
    public void setZoneModelVue(RectangleHV zoneModelVue) {
        this.zoneModelVue = zoneModelVue;
    }


    
}
