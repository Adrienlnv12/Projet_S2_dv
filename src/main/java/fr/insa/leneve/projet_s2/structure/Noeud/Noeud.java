/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2.structure.Noeud;

import fr.insa.leneve.projet_s2.structure.Barre;
import fr.insa.leneve.projet_s2.structure.Force;
import fr.insa.leneve.projet_s2.structure.forme.Point;
import java.util.ArrayList;

/**
 *
 * @author adrie
 */
public abstract class Noeud extends Point {
    
   protected Force forceApplique;

    private final ArrayList<Barre> linkedBarres = new ArrayList<>();

    public Noeud (){

    }

    public Noeud(double px, double py, int id){
        this.px = px;
        this.py = py;
        this.id = id;
    }
     
     public void addBarres(Barre barre){
        linkedBarres.add(barre);
    }

    public void removeBarres (Barre barre){
        linkedBarres.remove(barre);
    }

    public abstract String saveString();

    public void setForceApplique(Force forceApplique) {
        this.forceApplique = forceApplique;
    }

    public Force getForceApplique() {
        if(forceApplique == null){
            return new Force();
        }
        return forceApplique;
    }

    public ArrayList<Barre> getLinkedBarres() {
        return linkedBarres;
    } 
}
