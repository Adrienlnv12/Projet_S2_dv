/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insa.leneve.projet_s2.interfa;


import javafx.scene.layout.BorderPane;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.BLACK;

/**
 *
 * @author adrie
 */
public class MainPanel extends BorderPane {
    
    private Controleur controleur;
    
    private ToggleButton tbNoeudS;
    private ToggleButton tbNoeudAS;
    private ToggleButton tbNoeudAD;
    private ToggleButton tbBarre;
    private ToggleButton tbTriTerrain;
    private Button bRes;
    
    
    private Button bSupObj;
    private Button bSelect;

    
    private ChoiceBox cbTypeBarre;
    private Button bSave;
    private ColorPicker cpCouleur; // à faire ouvrir une fenetre pour modifier les couleurs de tous les obj de même type
    
    private DessinCanvas1 cDessin;
    private Label information;
    
    
    
    private Color couleur;
    
    public MainPanel(){  
        
        this.controleur = new Controleur(this);
        this.couleur = BLACK;

        //à gauche
        
        this.tbBarre = new ToggleButton("Barre"); //crée le toggle bouton 
        tbBarre.setToggleGroup(groupG); //le met dans le ToggleGroup de gauche
        lB.add(tbBarre); // le met dans lB
        this.tbBarre.setOnAction((t) -> {
            
        });
        tbBarre.setTooltip(new Tooltip ("Veuillez choisir le type de barre à droite avant de créer des barres."));
        
       
        
        this.tbNoeudAS = new ToggleButton("Noeud appui simple"); //crée le toggle bouton
        tbNoeudAS.setToggleGroup(groupG); //le met dans le ToggleGroup
        this.tbNoeudAS.setOnAction((t)->{
            controleur.changeEtat(10);
        });
        lB.add(tbNoeudAS); // le met dans lB
        tbNoeudAS.setTooltip(new Tooltip("Un noeud appui doit être sur un triangle terrain pour être créé."));//permet de donner des indications
        
        this.tbNoeudAD = new ToggleButton("Noeud appui double"); //crée le toggle bouton
        tbNoeudAD.setToggleGroup(groupG); //le met dans le ToggleGroup
        this.tbNoeudAD.setOnAction((t)->{
            controleur.changeEtat(10);
        });
        lB.add(tbNoeudAS); // le met dans lB
        tbNoeudAD.setTooltip(new Tooltip("Un noeud appui doit être sur un triangle terrain pour être créé."));//permet de donner des indications
        
        this.tbTriTerrain = new ToggleButton("Triangle terrain");
        tbTriTerrain.setToggleGroup(groupG);
        this.tbTriTerrain.setOnAction((t)->{
            System.out.println("bouton TT cliqué");
            controleur.changeEtat(0);
        });
        lB.add(tbTriTerrain);  
        
        this.bRes = new Button("Résolution"); //crée le toggle bouton        
        
       
        VBox vbGauche = new VBox(this.getTbBarre(), this.getTbNoeudS(), this.getTbNoeudAS(),
                this.getTbNoeudAD(), this.getTbTriTerrain(), this.getbRes());
        this.setLeft(vbGauche);
        
        //redimensionne les tb pour qu'ils aient la même taille
        lB.forEach(b -> {
            b.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        });
        
        bRes.setMaxSize(tbBarre.getMaxWidth(), tbBarre.getMaxHeight());
        
        this.information = new Label(" Informations :");
// label à mettre à côté d'un TextArea pour donner des informations à l'utilisateur
        this.setBottom(information);
        
        //à droite
        
        this.bSupObj = new Button("Supprimer");
        bSupObj.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);

        this.cbTypeBarre = new ChoiceBox(FXCollections.observableArrayList(
               "A", "B", "C"));// mettre les différents type de barres
        cbTypeBarre.setTooltip(new Tooltip("Séléctionner le type de barre"));//permet d'indiquer la fonction du bouton
        cbTypeBarre.setMaxSize(bSupObj.getMaxWidth(), bSupObj.getMaxHeight());
        
        this.bSelect = new Button("Select"); //crée le bouton select
        bSelect.setMaxSize(bSupObj.getMaxWidth(), bSupObj.getMaxHeight());
        this.bSelect.setOnAction((t)->{
        
        });
       
        
        this.cpCouleur = new ColorPicker(); // bouton pour selectionner la couleur
        cpCouleur.setMaxSize(bSupObj.getMaxWidth(), bSupObj.getMaxHeight());
        this.cpCouleur.setOnAction((t) -> {
            System.out.println("bouton couleur cliqué"); 
            couleur = cpCouleur.getValue();
        });
        
        
        VBox vbDroit = new VBox(this.getCpCouleur(), this.getCbTypeBarre(), this.getBSelect(), this.getBSupObj());
        this.setRight(vbDroit);
        
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
     * @return the bCouleur
     */
    public ColorPicker getCpCouleur() {
        return cpCouleur;
    }

    /**
     * @return the cDessin
     */
    public DessinCanvas1 getcDessin() {
        return cDessin;
    }

    /**
     * @return the couleur
     */
    public Color getCouleur() {
        return couleur;
    }
    


    /**
     * @return the tbTriTerrain
     */
    public ToggleButton getTbTriTerrain() {
        return tbTriTerrain;
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
    

    
}
