/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2.interfa;

import javafx.event.ActionEvent;

/**
 *
 * @author francois
 */
public class Controleur {

    public void clicDansDessin(ActionEvent t) {
        if (this.etat == Etat.POINT) {
            
        }
    }
    
    public enum Etat { SELECT , POINT , SEGMENTP1, SEGMENTP2}
    
    private Etat etat;
    
    
}
