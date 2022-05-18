/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2.interfa;

import fr.insa.leneve.projet_s2.Boutton.RadioBouton;
import fr.insa.leneve.projet_s2.structure.forme.Treillis;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Border;
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
public final class BoutonGauche extends VBox {
    
    private final ToggleGroup bgEtat= new ToggleGroup();
    
    private RadioButton rbSelect;
    private RadioButton RbNoeud;
    private final Button choixNoeud;
    private RadioButton RbBarre;
    private RadioButton RbTriangleTerrain;
    private Treillis Treillis;
    private ColorPicker cpCouleur;
    

    private BoutonIcone bZoomDouble;
    private BoutonIcone bZoomDemi;
    private BoutonIcone bZoomFitAll;
    
    private BoutonIcone bTranslateGauche;
    private BoutonIcone bTranslateDroite;
    private BoutonIcone bTranslateHaut;
    private BoutonIcone bTranslateBas;
    
    private Button bCreeNoeudDialog;
    
    private DessinCanvas cDessin;
    private RectangleHV zoneModelVue;
    
    private final Controleur controleur;
    
    //types : 0 -> simple, 1 -> appuiDouble, 2 -> appuiSimple, 3 -> appuiEncastre
    private String typeNoeud = "0";
    private String name = "Noeud Simple";
    private int NoeudChoisi = 10;
    
    public BoutonGauche(MainPanel mainpanel) {
        super(10);
        this.setAlignment(Pos.CENTER);
        this.setId("BoutonGauche");

        this.controleur = mainpanel.getControleur();

        initSelect();
        choixNoeud = new Button("Choix du Noeud");
        choixNoeud.setOnAction(a -> {selectNoeud();});
        initNoeud();
        initBarre();
        initTriangleTrn();
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

        this.getChildren().addAll(rbSelect,RbNoeud, RbBarre, RbTriangleTerrain, choixNoeud,this.bCreeNoeudDialog,this.cpCouleur, vbZoom);
        FxUtils.setSimpleBorder(this, Color.BLACK, 2);
    }

    private void initNoeud() {
        RbNoeud = new RadioButton("Noeud");
        RbNoeud.setToggleGroup(bgEtat);
        RbNoeud.setOnAction(actionEvent -> {
            System.out.println(NoeudChoisi);
            controleur.boutonSelect(NoeudChoisi);
            //controleur.removeSelected();
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
        rbSelect.setOnAction(actionEvent -> {
            controleur.boutonSelect(0);
                });
        
    }

    private void initBarre() {
        RbBarre = new RadioButton("Barre");
        RbBarre.setToggleGroup(bgEtat);

        RbBarre.setOnAction(actionEvent -> {
            //controleur.removeSelected();
            controleur.boutonSelect(20);
            });
    }

    private void initTriangleTrn() {
        RbTriangleTerrain = new RadioButton("Triangle");
        RbTriangleTerrain.setToggleGroup(bgEtat);
        RbTriangleTerrain.setOnAction(actionEvent -> {
            //controleur.removeSelected();
            controleur.boutonSelect(30);
        });
    }

    public void setSelected(int id){
        switch (id) {
            case 0 -> {
                rbSelect.setSelected(true);
        }
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
            case 30 -> RbTriangleTerrain.setSelected(true);
        }
        if(id != 0) {
          //controleur.removeSelected();
        }
        controleur.boutonSelect(id);
    }
    
       private void initColorPicker() {
        this.cpCouleur = new ColorPicker(Color.BLACK);
        this.cpCouleur.setOnAction((t) -> {
            this.controleur.changeColor(this.cpCouleur.getValue());
        });
    }

        
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

  
    public void fitAll() {
        this.zoneModelVue = new RectangleHV(this.Treillis.minX(),
                this.Treillis.maxX(), this.Treillis.minY(), this.Treillis.maxY());
        this.zoneModelVue = this.zoneModelVue.scale(1.1);
    }
    
        /**
     * @return the cpCouleur
     */
    public ColorPicker getCpCouleur() {
        return cpCouleur;
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
}
