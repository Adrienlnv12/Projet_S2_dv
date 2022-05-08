/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insa.leneve.projet_s2;

import static java.lang.Math.atan;

/**
 *
 * @author cheraita
 */
public class Segment_terrain {
        TriangleTerrain TT;
        double S1x;
        double S1y;
        double S2x;
        double S2y;
        double id;
        
        
    public Segment_terrain(TriangleTerrain TT) {
        this.S1x = TT.getPT1x();
        this.S1y = TT.getPT1y();
        this.S2x = TT.getPT2x();
        this.S2y = TT.getPT2y();
        this.TT = TT;
    }

    public TriangleTerrain getTT() {
        return TT;
    }

    public double getS1x() {
        return S1x;
    }

    public double getS1y() {
        return S1y;
    }

    public double getS2x() {
        return S2x;
    }

    public double getS2y() {
        return S2y;
    }
    
    
    // méthode permettant de savoir si un noeud appui simple appartient a un segment de droite 
    public boolean test_noeud_segment(NoeudAppuiSimple Ap){
        boolean a;
        double coeff_directeur = (this.S2y-this.S1y)/(this.S2x-this.S1x);
        double ordonnee_origine = this.S1y-coeff_directeur*this.S1x;
        if(coeff_directeur*Ap.getNx()+ordonnee_origine==Ap.getNy()){
             a= true;
        }
        else{
            a = false;
        }
               
        
        return(a);
    }
        //Méthode permettant de calculer l'angle entre la normale du segment de terrain et l'horizontale 

    public double angle_beta(Numeroteur<TriangleTerrain> num, Numeroteur<Noeud> n){
        double angle = 0;
        TriangleTerrain TT = new TriangleTerrain(num);
        NoeudAppui N1a = new NoeudAppui(TT/*,n*/);
        NoeudAppui N2a = new NoeudAppui(TT/*,n*/);
        angle = atan(Math.abs(this.S2y-this.S1y)/Math.abs(this.S2x-this.S1x));
         
        
        return(angle);
    }
    
    
    
}
