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
public class TriangleTerrain { // défini par 3 points (2 coord)
    private int id;
    private double PT1x,PT1y;
    private double PT2x,PT2y;
    private double PT3x,PT3y;
    
        
   
    public TriangleTerrain(double point1x,double point1y,double point2x,double point2y,
            double point3x,double point3y, Numeroteur<TriangleTerrain> TT){
       
        id = TT.genererIdLibre();
        this.PT1x = point1x;
        this.PT1y = point1y;
        this.PT2x = point2x;
        this.PT2y = point2y;
        this.PT3x = point3x;
        this.PT3y = point3y;   
    } 
    
     public TriangleTerrain(Numeroteur<TriangleTerrain> TT){
        id =TT.genererIdLibre();
        this.PT1x = 0;
        this.PT1y = 0;
        this.PT2x = 0;
        this.PT2y = 0;
        this.PT3x = 0;
        this.PT3y = 0;
     }
        
     
    public int getIdTT(){
        return id;
    } 

    public void setIdTT(int id){
        this.id = id;
    }

    public double getPT1x() {
        return PT1x;
    }

    public double getPT1y() {
        return PT1y;
    }

    public double getPT2x() {
        return PT2x;
    }

    public double getPT2y() {
        return PT2y;
    }

  

    public double getPT3x() {
        return PT3x;
    }

    public double getPT3y() {
        return PT3y;
    }
    public void setPT1x(double PT1x) {
        this.PT1x = PT1x;
    }

    public void setPT1y(double PT1y) {
        this.PT1y = PT1y;
    }

    public void setPT2x(double PT2x) {
        this.PT2x = PT2x;
    }

    public void setPT2y(double PT2y) {
        this.PT2y = PT2y;
    }

    public void setPT3x(double PT3x) {
        this.PT3x = PT3x;
    }

    public void setPT3y(double PT3y) {
        this.PT3y = PT3y;
    }

    @Override
    public String toString() {
        return "TriangleTerrain{" + "id=" + id + ", PT1x=" + PT1x + ", PT1y=" + PT1y + ", PT2x=" + PT2x + ", PT2y=" + PT2y + ", PT3x=" + PT3x + ", PT3y=" + PT3y + '}';
    }
    public Segment_terrain recup_segment(int a,int b){
        Segment_terrain st= new Segment_terrain(this);
        if (a==1&&b==2){
            st.S1x = this.PT1x;
            st.S1y = this.PT1y;
            st.S2x = this.PT2x;
            st.S2y = this.PT2y;
            
        }
        if (a==1&&b==3){
            st.S1x = this.PT1x;
            st.S1y = this.PT1y;
            st.S2x = this.PT3x;
            st.S2y = this.PT3y;
        }
        if(a==2&&b==3){
            st.S1x = this.PT2x;
            st.S1y = this.PT2y;
            st.S2x = this.PT3x;
            st.S2y = this.PT3y;
        }
        else{
            throw new Error("les coeffcients saisis doivent être compris entre 1 et 3 inclus");        
        }
        return (st);
    }
    
   
    
    public static void main(String[]args){
        Numeroteur<TriangleTerrain> TT = new Numeroteur<>();// créer un numéroteur pour ce main
        TriangleTerrain m = new TriangleTerrain(TT);// nouveau triangle terrain avec le numeroteur pour son id qui doit etre 0
        TT.associe(m.getIdTT(), m);// associe l'id de m et m dans les deux maps du numeroteur
        System.out.println(m.getIdTT());
        TriangleTerrain l = new TriangleTerrain(TT);// nouveau triangle terrain avec le numeroteur pour son id qui doit etre 1
        TT.associe(l.getIdTT(), l);// associe l'id de l et l dans les deux maps du numeroteur
        System.out.println(l.getIdTT());
        TriangleTerrain K = new TriangleTerrain(TT);
        TT.associe(K.getIdTT(), K);
        System.out.println(K.getIdTT());
    }
    
     
    
    
}




    


    
   
    



    

