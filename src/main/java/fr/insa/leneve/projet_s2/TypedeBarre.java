/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insa.leneve.projet_s2;


import Ressyslin.utils.Console;

/**
 *
 * @author adrie
 */
public class TypedeBarre {
    private int id;
    private double Cout;
    private double Lmax;
    private double Lmin;
    private double Rtmax;
    private double Rcmax; 
    public TypedeBarre(Numeroteur<TypedeBarre> TdB){
        id = TdB.genererIdLibre();
        this.Cout= 0;
        this.Lmax=0;
        this.Lmin=0;
        this.Rtmax=0;
        this.Rcmax=0;
                
    }
    public TypedeBarre(double cout,double lmax,double lmin,double rtmax,double rcmax, Numeroteur<TypedeBarre> TdB){
        id = TdB.genererIdLibre();
        this.Cout= cout;
        this.Lmax=lmax;
        this.Lmin=lmin;
        this.Rtmax=rtmax;
        this.Rcmax=rcmax;
    }
    public void barre_acier(){
        this.Cout = 200;
        this.Lmax = 160;
        this.Lmin = 30;
        this.Rcmax = 800;
        this.Rcmax = 1500;
        
    }
    public void barre_bois(){// bois de qualité standard 
        this.Cout = 100;
        this.Lmax = 180;
        this.Lmin = 25;
        this.Rcmax = 700;
        this.Rtmax = 400;
    }
    /* Voici quelques types de poutres IPN : 
    -Acier :
    Masse linéique = 18.30 Kg/m
    longueur max = 160 cm
    longueur min= 30 cm
    coût au mètre = 200 euros 
    résistance maximale tension = 800 N
    résistance maximale compression = 1500 N
    
    */

    @Override
    public String toString() {
        return "TypedeBarre{" + "Id=" + id + ", Cout=" + Cout + ", Lmax=" + Lmax + ", Lmin=" + Lmin + ", Rtmax=" + Rtmax + ", Rcmax=" + Rcmax + '}';
    }
    
    
    /*voici les méthodes get/set*/
    public void setIdTB(int id){
        this.id= id;
    }
    
    public int getIdTB(){
        return(this.id);
    }
    public double getcout(){
        return(this.Cout);
    }
    public double getLmax(){
        return(this.Lmax);
    }
    public double getLmin(){
        return(this.Lmin);
    }
    public double getRtmax(){
        return(this.Rtmax);
    }
    public double getRcmax(){
        return(this.Rcmax);
    }
    public void setCout(double a){
        this.Cout=a;
    }
    public void setLmax(double b){
        this.Lmax=b;
    }
    public void setLmin(double b){
        this.Lmin=b;
    }
    public void setRtmax(double b){
        this.Rtmax=b;
    }
    public void setRcmax(double b){
        this.Rcmax=b;
    }
     public static void main(String[]args){
            double a;
            int b;
           // a = Lire.d();
            b = Console.entreeEntier("Veuillez saisir un entier a");
            
            
            
            
        }  
    
    
}
