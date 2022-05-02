/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insa.leneve.projet_s2;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author adrie
 */
public class Terrain { // 
    private double xmin; // défini par une zone constructible défini par 4 réels 
    private double xmax;
    private double ymin;
    private double ymax;
    private List<TriangleTerrain> Triangles_Terrain; // terrain défini par un ensemble de triangles de terrain
    
  // Ceci est le constructeur de la classe
    public Terrain (double xmax,double xmin,double y,double ymin,Numeroteur<Terrain> numT){
       
        this.xmax=xmax;
        this.xmin=xmin;
        this.ymax=y;
        this.ymin=ymin;
        this.Triangles_Terrain = new ArrayList<>();
    }
    
    // Voici les méthodes get/set de la classe 
    public double getxmin(){
        return(this.xmin);
    }
    
    public double getxmax(){
        return(this.xmax);    
    }
    
    public double getymin(){
        return(this.ymin);
    }
    
    public double getymax(){
        return(this.ymax);  
    }
    
    public List<TriangleTerrain> getTriangles_Terrain() {
        return this.Triangles_Terrain ;
    } 
    
    public void setxmax(double a){
        this.xmax=a;
    }
    
    public void setxmin(double a){
        this.xmin=a;
    }
    
    public void setymax(double a){
        this.ymax=a;
    }
    
    public void setymin(double a){
        this.ymin=a;
    }
    
    public void setTriangles_Terrain(List<TriangleTerrain> Triangleterrain){
        this.Triangles_Terrain=Triangleterrain;
    }

    @Override
    public String toString() {
        return "Terrain{" + "xmin=" + xmin + ", xmax=" + xmax + ", ymin=" + ymin + ", ymax=" + ymax + /*", Triangles_Terrain=" + Triangles_Terrain*/ + '}';
    }
    public static void main(String []args){
        Matrice T = new Matrice(1,2);
        System.out.println(T);
    }
    
    
}
