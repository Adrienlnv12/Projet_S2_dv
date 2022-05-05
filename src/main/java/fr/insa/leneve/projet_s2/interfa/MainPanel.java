/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insa.leneve.projet_s2.interfa;


import java.io.File;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import fr.insa.leneve.projet_s2.Groupe;
/**
 *
 * @author adrie
 */
public class MainPanel extends BorderPane {
    
    private static double MULT_POUR_FIT_ALL = 1.1;
    private Groupe model;
    private Controleur controleur;

    private Stage inStage;
    private File curFile;

    private RadioButton rbSelect;
    private RadioButton rbPoints;
    private RadioButton rbSegments;

    private Button bGrouper;
    private Button bSupprimer;
    private ColorPicker cpCouleur;

    private BoutonIcone bZoomDouble;
    private BoutonIcone bZoomDemi;
    private BoutonIcone bZoomFitAll;
    
    private BoutonIcone bTranslateGauche;
    private BoutonIcone bTranslateDroite;
    private BoutonIcone bTranslateHaut;
    private BoutonIcone bTranslateBas;
    
    private Button bCreePointDialog;
 
    private DessinCanvas1 cDessin;
    private RectangleHV zoneModelVue;

    private MainMenu menu;
    
    
    private ToggleButton tbNoeudS;
    private ToggleButton tbNoeudAS;
    private ToggleButton tbNoeudAD;
    private ToggleButton tbBarre;
    private ToggleButton tbTriTerrain;
    private Button bRes;
    
    private OutilsTop outilsTop;
    private OutilsLeft outilsLeft; 
    
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
        
        this.outilsTop = new OutilsTop();
        this.outilsLeft = new OutilsLeft(this);
        this.controleur = new Controleur(this);
             
        this.setTop(this.outilsTop);
        this.setLeft(this.outilsLeft);
        
        
       
        
       
        
        
        this.information = new Label(" Informations :");
// label à mettre à côté d'un TextArea pour donner des informations à l'utilisateur
        this.setBottom(information);
        
        
        this.cpCouleur = new ColorPicker(); // bouton pour selectionner la couleur
        this.cpCouleur.setOnAction((t) -> {
            System.out.println("bouton couleur cliqué"); 
            couleur = cpCouleur.getValue();
        });
        this.setRight(this.cpCouleur);
        //en bas a droite
      
        this.cDessin = new DessinCanvas1(this);// zone de dessin
        this.setCenter(this.cDessin);
        
    }    

    /**
     * @return the controleur
     */
    public Controleur getControleur() {
        return controleur;
    }

    /**
     * @return the tbNoeudS
     */
    public ToggleButton getTbNoeudS() {
        return tbNoeudS;
    }

    /**
     * @return the tbNoeudAS
     */
    public ToggleButton getTbNoeudAS() {
        return tbNoeudAS;
    }

    /**
     * @return the tbNoeudAD
     */
    public ToggleButton getTbNoeudAD() {
        return tbNoeudAD;
    }

    /**
     * @return the tbBarre
     */
    public ToggleButton getTbBarre() {
        return tbBarre;
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
        return getbSupObj();
    }

    /**
     * @return the tbSelect
     */
    public Button getBSelect() {
        return getbSelect();
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


    /**
     * @return the tbTriTerrain
     */
    public ToggleButton getTbTriTerrain() {
        return tbTriTerrain;
    }
    
    public Color getCouleur() {
        return couleur;
    }
    
    
    /**
     * @return the bSupObj
     */
    public Button getbSupObj() {
        return bSupObj;
    }

    /**
     * @return the bSelect
     */
    public Button getbSelect() {
        return bSelect;
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

    /**
     * @return the rbSelect
     */
    public RadioButton getRbSelect() {
        return rbSelect;
    }

    /**
     * @return the rbPoints
     */
    public RadioButton getRbPoints() {
        return rbPoints;
    }

    /**
     * @return the rbSegments
     */
    public RadioButton getRbSegments() {
        return rbSegments;
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

    /**
     * @return the bSupprimer
     */
    public Button getbSupprimer() {
        return bSupprimer;
    }

    
}
