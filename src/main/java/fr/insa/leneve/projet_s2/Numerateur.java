/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2;


/**
 *
 * @author adrie
 */
public class Numerateur {

    int currentNoeudId = 0;
    int currentBarreId = 0;
    int currentTriangleId = 0;

    public Numerateur(){

    }

    public Numerateur(int currentNoeudId, int currentBarreId, int currentTriangleId){
        this.currentNoeudId = currentNoeudId;
        this.currentBarreId = currentBarreId;
        this.currentTriangleId = currentTriangleId;
    }

    public int getNewNoeudId() {
        currentNoeudId ++;
        return currentNoeudId;
    }

    public int getCurrentNoeudId() {
        return currentNoeudId;
    }


    public int getNewBarreId() {
        currentBarreId ++;
        return currentBarreId;
    }

    public int getCurrentBarreId() {
        return currentBarreId;
    }

    public int getNewTriangleId() {
        currentTriangleId ++;
        return currentTriangleId;
    }

    public int getCurrentTriangleId() {
        return currentTriangleId;
    }
}
