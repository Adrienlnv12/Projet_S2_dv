/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2.structure.forme;

import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author adrie
 */
public abstract class Forme{
    
    private Color couleur;
    public static Color COULEUR_SELECTION = Color.RED;
    protected boolean selected = false;
    protected int id;
    
    public abstract ArrayList<String> getInfos();
    
    public abstract void dessine(GraphicsContext context);
    
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void changeCouleur(Color value) {
        this.setCouleur(value);
    }
    
    public Color getCouleur() {
        return couleur;
    }

    public void setCouleur(Color couleur) {
        this.couleur = couleur;
    }
}
