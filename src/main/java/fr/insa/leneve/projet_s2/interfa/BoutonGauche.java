/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2.interfa;

import fr.insa.leneve.projet_s2.Boutton.RadioBouton;
import fr.insa.leneve.projet_s2.structure.TypedeBarre;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private VBox typeBox; //determiner a quoi il sert
    private RadioButton RbBarre;
    private RadioButton RbTerrain;
    private RadioButton RbTriangleTerrain;
    
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


        this.getChildren().addAll(rbSelect, treillisLbl, RbNoeud, choixNoeud, RbBarre, typeBox, terrainLbl, RbTerrain, RbTriangleTerrain);

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
            this.controleur.boutonSelect(t)
    }

    private void initBarre() {
        RbBarre = new RadioButton("Barre");
        RbBarre.setToggleGroup(bgEtat);

        RbBarre.setOnAction(actionEvent -> {
            /*controleur.removeSelected();*/
            controleur.boutonSelect(20);
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

        RbTerrain.setOnAction( actionEvent -> {
            /*controleur.removeSelected();*/
            controleur.boutonSelect(30);
        });
    }

    private void initTriangleTrn() {
        RbTriangleTerrain = new RadioButton("Triangle");
        RbTriangleTerrain.setToggleGroup(bgEtat);

        RbTriangleTerrain.setOnAction(actionEvent -> {
            /*controleur.removeSelected();*/
            controleur.boutonSelect(40);
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
}
