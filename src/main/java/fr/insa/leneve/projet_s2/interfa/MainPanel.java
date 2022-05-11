/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insa.leneve.projet_s2.interfa;


import javafx.scene.control.*;
import fr.insa.leneve.projet_s2.Groupe;
import javafx.collections.FXCollections;
import java.io.File;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
/**
 *
 * @author adrie
 */
public final class MainPanel extends BorderPane {
    /**
     * multiplicateur pour l'espace de départ : pour ne pas que les bords de la
     * figure soit confondus avec les bords de la fenêtre, on considère que l'on
     * veut que la fenêtre puisse contenir une figure MULT_POUR_FIT_ALL fois
     * plus grande que la figure réelle. Par exemple, si MULT_POUR_FIT_ALL = 2,
     * la figure complète n'occupera en fait qu'environ la moitié de la fenetre
     * graphique.
     */
    private static double MULT_POUR_FIT_ALL = 1.1;
    
    private Controleur controleur;

    private Stage inStage;
    private File curFile;
    
    private RadioButton rbSelect;

    private Button bGrouper;
    private ColorPicker cpCouleur;
 
    private DessinCanvas1 cDessin;
    private RectangleHV zoneModelVue;

    private MainMenu menu;
    private final FenetreInfo Info;
    
    private RadioButton RbNoeud;
    private RadioButton RbNoeudAS;
    private RadioButton RbNoeudAD;
    private RadioButton RbBarre;

    private Button bRes;
    
    private final BoutonGauche boutong;
    private final BoutonDroite boutond;
    
    private Button bSupObj;

    
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
        this.fitAll(); 
        this.controleur = new Controleur(this);
       
        this.information = new Label(" Informations :");
// label à mettre à côté d'un TextArea pour donner des informations à l'utilisateur
        this.setBottom(information);
        
        boutond = new BoutonDroite(this);
        this.setRight(boutond);
        
        boutong = new BoutonGauche(this);
        this.setLeft(boutong);
                
        Info = new FenetreInfo(this);
        this.setRight(Info);
        
        this.cDessin = new DessinCanvas1(this);// zone de dessin
        this.setCenter(this.cDessin);
        
        this.menu = new MainMenu(this);
        this.setTop(this.menu);

        this.controleur.changeEtat(20);
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
    
   
    public RadioButton getRbNoeud() {
        return RbNoeud;
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
    public DessinCanvas getcDessin() {
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
