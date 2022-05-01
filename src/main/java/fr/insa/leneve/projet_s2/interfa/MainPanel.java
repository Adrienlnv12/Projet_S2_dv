/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2.interfa;


import fr.insa.leneve.projet_s2.Groupe;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author francois
 */
public class MainPanel extends BorderPane {

    /**
     * @return the model
     */
    public Groupe getModel() {
        return model;
    }
    
    private Groupe model;
    private Controleur control;
    
    
    
    private OutilsTop outilsTop;
    private OutilsLeft outilsLeft;
    private DessinPane dessin;
    
    public MainPanel(Groupe model) {
        this.model = model;
        this.outilsTop = new OutilsTop();
        this.outilsLeft = new OutilsLeft(this);
        this.dessin = new DessinPane(this);
        
        this.setTop(this.outilsTop);
        this.setLeft(this.outilsLeft);
        this.setCenter(this.dessin);
    }

    /**
     * @return the control
     */
    public Controleur getControl() {
        return control;
    }
    
}
