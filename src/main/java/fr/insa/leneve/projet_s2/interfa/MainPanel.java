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
    
    private OutilsTop outilsTop;
    private OutilsLeft outilsLeft; 
    
    private Button bSupObj;
    private Button bSelect;

    
    private ChoiceBox cbTypeBarre;
    private Button bSave;
    
    private DessinCanvas1 cDessin;
    private Label information;
    
    private Color couleur;
    private ColorPicker cpCouleur;
    
    
    
    public MainPanel(){  
        
        this.outilsTop = new OutilsTop();
        this.outilsLeft = new OutilsLeft(this);
        this.controleur = new Controleur(this);
        

        //à gauche
        
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
    

    
}
