/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2.interfa;

import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * @author francois
 */
public class OutilsLeft extends VBox {
    
    private MainPanel main;

    private ToggleButton tbNoeudS;
    private ToggleButton tbNoeudAS;
    private ToggleButton tbNoeudAD;
    private ToggleButton tbBarre;
    private ToggleButton tbTriTerrain;
    private Button bRes;

    final ToggleGroup groupG = new ToggleGroup(); // permet de ne sélectionner qu'un seul bouton à la fois faisant parti du groupe de gauche
    public List<ToggleButton> lB = new ArrayList<>(); // permet de parcourir tous les ToggleButton présent dans la liste
    //pour ensuite les mettre à la même taille
    
    public OutilsLeft(MainPanel main) {
        this.main = main;
        
        this.tbNoeudS = new ToggleButton("Noeud simple"); //crée le toggle bouton 
        tbNoeudS.setToggleGroup(groupG); //le met dans le ToggleGroup
        lB.add(tbNoeudS); // le met dans lB
        this.tbNoeudS.setOnAction((t) -> {
            System.out.println("bouton NS cliqué");
            this.main.getControl().changeEtat(20);
        });
        this.tbNoeudAS = new ToggleButton("Noeud appui simple"); //crée le toggle bouton
        tbNoeudAS.setToggleGroup(groupG); //le met dans le ToggleGroup
        lB.add(tbNoeudAS); // le met dans lB
        this.tbNoeudAS.setOnAction((t)->{
            System.out.println("bouton NAS cliqué");
            this.main.getControl().changeEtat(10);
        });
        tbNoeudAS.setTooltip(new Tooltip("Un noeud appui doit être sur un triangle terrain pour être créé."));//permet de donner des indications
       
        this.tbNoeudAD = new ToggleButton("Noeud appui double"); //crée le toggle bouton
        tbNoeudAD.setToggleGroup(groupG); //le met dans le ToggleGroup
        lB.add(tbNoeudAS); // le met dans lB
        this.tbNoeudAD.setOnAction((t)->{
            this.main.getControl().changeEtat(10);
        });
        
        tbNoeudAD.setTooltip(new Tooltip("Un noeud appui doit être sur un triangle terrain pour être créé."));//permet de donner des indications
        
        this.tbBarre = new ToggleButton("Barre"); //crée le toggle bouton 
        tbBarre.setToggleGroup(groupG); //le met dans le ToggleGroup de gauche
        lB.add(tbBarre); // le met dans lB
        this.tbBarre.setOnAction((t) -> {
            
        });
        tbBarre.setTooltip(new Tooltip ("Veuillez choisir le type de barre à droite avant de créer des barres."));
        
        this.tbTriTerrain = new ToggleButton("Triangle terrain");
        tbTriTerrain.setToggleGroup(groupG);
        lB.add(tbTriTerrain);
        this.tbTriTerrain.setOnAction((t)->{
            System.out.println("bouton TT cliqué");
            this.main.getControl().changeEtat(0);
        });
        
        this.bRes = new Button("Résolution"); //crée le toggle bouton        
        
       
        this.getChildren().addAll(this.tbBarre, this.tbNoeudS, this.tbNoeudAS,
                this.tbNoeudAD, this.tbTriTerrain, this.bRes);
        
        
        //redimensionne les tb pour qu'ils aient la même taille
        lB.forEach(b -> {
            b.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        });
        
        bRes.setMaxSize(tbBarre.getMaxWidth(), tbBarre.getMaxHeight());
        FxUtils.setSimpleBorder(this, Color.CYAN, 2);

    }
}
