/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insa.leneve.projet_s2.interfa;


import fr.insa.leneve.projet_s2.Boutton.RadioBouton;
import fr.insa.leneve.projet_s2.structure.forme.Treillis;
import java.io.File;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
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
import javafx.stage.Modality;
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
    
    private Treillis model;
    
    private final Controleur controleur;
    
    private Stage inStage;
    private File curFile;
    
    private  DessinCanvas cDessin;

    private final MainMenu menu;
    private final FenetreInfo Info;
    private final BoutonGauche bgauche;
    private RectangleHV zoneModelVue;
    
    public MainPanel(Stage inStage) {
        this(inStage, new Treillis());
    }

    public MainPanel(Stage inStage, Treillis model) {
        this(inStage, null, model);
    }

    public MainPanel(Stage inStage, File fromFile, Treillis model) {
        this.inStage = inStage;
        this.curFile = fromFile;
        this.model = model;
        this.fitAll();
        this.controleur = new Controleur(this);
                       
        Info = new FenetreInfo(this);
        this.setRight(Info);
        
        bgauche = new BoutonGauche(this);
        this.setLeft(bgauche);
        
        this.cDessin = new DessinCanvas(this);// zone de dessin
        this.setCenter(this.cDessin);
        
        this.menu = new MainMenu(this);
        this.setTop(this.menu);
    }  

    public void redrawAll() {
        this.cDessin.redrawAll();
    }

    /**
     * @return the controleur
     */
    public Controleur getControleur() {
        return controleur;
    }
    
    public FenetreInfo getInfos() {
        return Info;
    }
    
    public void fitAll() {
        this.zoneModelVue = new RectangleHV(this.model.minX(),
                this.model.maxX(), this.model.minY(), this.model.maxY());
        this.zoneModelVue = this.zoneModelVue.scale(1.1);
    }
    
    public void setZoneModelVue(RectangleHV zoneModelVue) {
        this.zoneModelVue = zoneModelVue;
    }
    
    /**
     * @return the zoneModelVue
     */
    public RectangleHV getZoneModelVue() {
        return zoneModelVue;
    }
    
    /**
     * @return the cDessin
     */
    public DessinCanvas getcDessin() {
        return cDessin;
    }
    
    /**
     * @return the treillis
     */
    public Treillis getModel() {
        return model;
    }
    
     /**
     * @return the inStage
     */
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
    
    public BoutonGauche getBoutonGauche() {
        return bgauche;
    }
}
