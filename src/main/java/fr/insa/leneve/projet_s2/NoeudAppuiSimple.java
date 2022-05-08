/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2;

import javafx.scene.paint.Color;

/**
 *
 * @author adrie
 */
public class NoeudAppuiSimple extends NoeudAppui {
    Segment_terrain segment_terrain;
    
    
    public NoeudAppuiSimple(TriangleTerrain triangleterrain, Numeroteur<Noeud> num){
        super(triangleterrain/*, num*/);
    }
    public NoeudAppuiSimple(TriangleTerrain TT,int j,double alpha, Numeroteur<Noeud> num, Numeroteur numTT){
        super(TT,j,alpha/*,numTT*/);       
    }
}