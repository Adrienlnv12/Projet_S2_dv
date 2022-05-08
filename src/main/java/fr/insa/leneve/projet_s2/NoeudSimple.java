/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2;




/**
 *
 * @author adrie
 */
public class NoeudSimple extends Noeud{
    
    // Voici les constructeurs --> comment initialiser un point 
    public NoeudSimple(double px,double py /*Numeroteur<Noeud> N*/){ // constructeur général qui contient tous les attributs
        super(px,py/*,N*/);   
    }
    public NoeudSimple(/*Numeroteur<Noeud> N*/){ // si on donne pas de coord au point, ça donne le point (0,0)
        //super(N);   
        this.px=0;
        this.py=0;
    }
    
    @Override
    public String toString() { // on peut prendre autre méthode toString déf comme les autres sinon
        return "("+ "Px : "+ px +",Py : " + py +","+"Id : "+id+ ')';
    }
    
    
    
}
