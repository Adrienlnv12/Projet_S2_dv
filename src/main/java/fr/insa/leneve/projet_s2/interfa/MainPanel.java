/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insa.leneve.projet_s2.interfa;


import fr.insa.leneve.projet_s2.Boutton.RadioBouton;
import fr.insa.leneve.projet_s2.structure.Treillis;
import fr.insa.leneve.projet_s2.structure.TypedeBarre;
import java.io.File;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    
    private final Controleur controleur;
 
    private final DessinCanvas cDessin;

    private final MainMenu menu;
    private final FenetreInfo Info;
    private Treillis treillis;
    
    private final ToggleGroup bgEtat= new ToggleGroup();
    
    private RadioButton rbSelect;
    private RadioButton RbNoeud;
    private final Button choixNoeud;
    private VBox typeBox; //determiner a quoi il sert
    private RadioButton RbBarre;
    private RadioButton RbTerrain;
    private RadioButton RbTriangleTerrain;
    //types : 0 -> simple, 1 -> appuiDouble, 2 -> appuiSimple, 3 -> appuiEncastre
    private String typeNoeud = "0";
    private String name = "Noeud Simple";
    private int NoeudChoisi = 10;
    
    
    private final BoutonDroite boutond;
    private RectangleHV zoneModelVue;
    private Stage inStage;
    private File curFile;
    
    public MainPanel(Stage inStage) {
        this(inStage, new Treillis());
    }

    public MainPanel(Stage inStage, Treillis treillis) {
        this(inStage, null, treillis);
    }

    public MainPanel(Stage inStage, File fromFile, Treillis treillis) {
        this.inStage = inStage;
        this.curFile = fromFile;
        this.treillis = treillis;
        //this.fitAll();
        this.controleur = new Controleur(this);
       
        
        boutond = new BoutonDroite(this);
        this.setRight(boutond);
        
        initSelect();

        Label treillisLbl = new Label("      treillis      ");

        choixNoeud = new Button("Noeud Simple");
        choixNoeud.setOnAction(a -> {
            selectNoeud();
        });

        initNoeud();
        initBarre();
        initTypeBarre();

        Label terrainLbl = new Label("      Sol      ");

        initTrn();
        initTriangleTrn();


        VBox vbGauche = new VBox(this.rbSelect, treillisLbl, this.RbNoeud, this.choixNoeud, this.RbBarre, this.typeBox, terrainLbl, this.RbTerrain, this.RbTriangleTerrain);
        this.setRight(vbGauche);       
        
        Info = new FenetreInfo(this);
        this.setRight(Info);
        
        this.cDessin = new DessinCanvas(this);// zone de dessin
        this.setCenter(this.cDessin);
        
        this.menu = new MainMenu(this);
        this.setTop(this.menu);
    }  
    
    private void initNoeud() {
        RbNoeud = new RadioButton("Noeud");
        RbNoeud.setToggleGroup(bgEtat);

        RbNoeud.setOnAction(actionEvent -> {
            System.out.println(NoeudChoisi);
            controleur.boutonSelect(NoeudChoisi);
            /*controleur.removeSelected();*/
        });
    
    }

    //pop up de selection du type de noeud
    private void selectNoeud() {
        Stage choixNoeud1 = new Stage();

        choixNoeud1.initModality(Modality.APPLICATION_MODAL);
        choixNoeud1.setTitle("Choix des Noeuds");
        choixNoeud1.setResizable(false);

        //text devant les boutons
        Label label = new Label("Noeud");

        //bouton de fermeture et confirmation du choix
        Button fin = new Button("Choisir");
        fin.setOnAction(e -> {
            NoeudChoisi = 10 + Integer.parseInt(typeNoeud);
            if(controleur.getboutonSelect() / 10 == 1) controleur.boutonSelect(NoeudChoisi);
            this.choixNoeud.setText(name);
            choixNoeud1.close();
        });

        //radiobutton pour le choix du type de noeud
        ToggleGroup tGroup = new ToggleGroup();

        tGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            if (tGroup.getSelectedToggle() != null) {
                RadioBouton button = (RadioBouton) tGroup.getSelectedToggle();
                typeNoeud = button.getInfo();
                name = button.getName();
            }
        });

        RadioBouton noeudSimple = new RadioBouton("Noeud Simple", "0");
        noeudSimple.setToggleGroup(tGroup);
        noeudSimple.setSelected(true);

        RadioBouton noeudappuiDouble = new RadioBouton("Noeud appui double", "1");
        noeudappuiDouble.setToggleGroup(tGroup);

        RadioBouton noeudappuiSimple = new RadioBouton("Noeud appui simple", "2");
        noeudappuiSimple.setToggleGroup(tGroup);


        //hbox contenant les boutons
        HBox radioLayout = new HBox(5);
        radioLayout.getChildren().addAll(noeudSimple, noeudappuiDouble, noeudappuiSimple);

        //Vbox contenant tout les items
        VBox layout= new VBox(5);
        layout.getChildren().addAll(label, radioLayout, fin);
        layout.setAlignment(Pos.CENTER);
        layout.setId("vBox");

        Scene scene = new Scene(layout, 420, 100);

        choixNoeud1.setScene(scene);
        choixNoeud1.showAndWait();

    }

    private void initSelect() {
        rbSelect = new RadioButton("Select");
        rbSelect.setToggleGroup(bgEtat);
        rbSelect.setSelected(true);

        this.rbSelect.setOnAction((t) -> {
            this.controleur.boutonSelection(t);
        });
    }

    private void initBarre() {
        RbBarre = new RadioButton("Barre");
        RbBarre.setToggleGroup(bgEtat);

        this.RbBarre.setOnAction((t) -> {
            this.controleur.boutonBarre(t);
        });
    }

    public void initTypeBarre() {
        //type list
        Label typeLabel = new Label("type");

        ComboBox<TypedeBarre> typeComboBox = new ComboBox<>(FXCollections.observableArrayList(controleur.getTreillis().getCatalogue()));
        typeComboBox.setId("typeCB");

        HBox typeHB = new HBox(10);
        typeHB.getChildren().addAll(typeLabel, typeComboBox);
        typeHB.setAlignment(Pos.CENTER);

        //boutons
        Button addTypeBtn = new Button("Ajouter un type");
        addTypeBtn.setOnAction(e -> TypedeBarre.BoutonTypedeBarre(controleur, typeComboBox));

        Button chooseBtn = new Button("Choisir");
        chooseBtn.setOnAction(e -> controleur.setBarreType(typeComboBox.getValue()));


        typeBox = new VBox(5);
        typeBox.getChildren().addAll(typeHB, addTypeBtn, chooseBtn);
        typeBox.setAlignment(Pos.CENTER);
        typeBox.setId("typeBox");

    }

    public void initTrn(){
        RbTerrain = new RadioButton("Aire de construction");
        RbTerrain.setToggleGroup(bgEtat);
        this.RbTerrain.setOnAction((t) -> {
            this.controleur.boutonTerrain(t);
        });
    }

    private void initTriangleTrn() {
        RbTriangleTerrain = new RadioButton("Triangle");
        RbTriangleTerrain.setToggleGroup(bgEtat);
        this.RbTriangleTerrain.setOnAction((t) -> {
            this.controleur.boutonTriangle(t);
        });
    }

    public void setSelected(int id){
        switch (id) {
            case 0 -> rbSelect.setSelected(true);
            case 10 -> {
                RbNoeud.setSelected(true);
                choixNoeud.setText("Noeud Simple");
            }
            case 11 -> {
                RbNoeud.setSelected(true);
                choixNoeud.setText("Noeud Appui Double");
            }
            case 12 -> {
                RbNoeud.setSelected(true);
                choixNoeud.setText("Noeud Appui Simple");
            }
            case 20 -> RbBarre.setSelected(true);
            case 30 -> RbTerrain.setSelected(true);
            case 40 -> RbTriangleTerrain.setSelected(true);
        }
        if(id != 0) {
          /*controleur.removeSelected();*/
        }
        controleur.boutonSelect(id);
    }
    
    /**
     * @return the rbSelect
     */
    public RadioButton getRbSelect() {
        return rbSelect;
    }

    /**
     * @return the RbNoeud
     */
    public RadioButton getRbNoeud() {
        return RbNoeud;
    }

    /**
     * @return the RbBarre
     */
    public RadioButton getRbBarre() {
        return RbBarre;
    }
    
    /**
     * @return the RbTerrain
     */
    public RadioButton getRbTerrain() {
        return RbTerrain;
    }
    
     /**
     * @return the RbTriangleTerrain
     */
    public RadioButton getRbTriangleTerrain() {
        return RbTriangleTerrain;
    }    

    /*public void redrawAll() {
        this.cDessin.redrawAll();
    }*/

    /**
     * @return the controleur
     */
    public Controleur getControleur() {
        return controleur;
    }
    
        /**
     * @return the model
     */
    public Treillis getTreillis() {
        return treillis;
    }
    
    public FenetreInfo getInfos() {
        return Info;
    }
    
    /**
     * @return the cDessin
     */
    public DessinCanvas getcDessin() {
        return cDessin;
    }

    public BoutonDroite getBoutonDroite() {
        return boutond;
    }
    
    /*public BoutonGauche getBoutonGauche() {
        return boutong;
    }*/

    /*public void fitAll() {
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
   
    
}
