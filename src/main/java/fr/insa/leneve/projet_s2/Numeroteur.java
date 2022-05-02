/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insa.leneve.projet_s2;

import java.util.HashMap;// clé qui associe à un truc, ici à partir d'un objet on peut retrouver son id
import java.util.Map;
import java.util.TreeMap; // relation d'association entre clé et objet, c'est organisé contrairement à la HashMap (ex : 1 2 3 4)

/**
 *
 * @author adrie
 */
public class Numeroteur<TO> { 


// on va indiquer le type de l'objet qu'on veut numéroter --> ça va créer 2 map
    /* créer des maps entre identificateur et objet
     préciser le type objet TO en faisant appel à la classe
     exemple : Numeroteur<Figure> num 
    */
    private TreeMap<Integer, TO> idVersObjet;
    private Map<TO, Integer> objetVersId;
    
    private int prochainID;
    
    public Numeroteur() {
        this(0);
    }
    
    private Numeroteur(int prochainID) {
        this.prochainID = prochainID;
        this.idVersObjet = new TreeMap<>();
        this.objetVersId = new HashMap<>();
    }
   
    public int creeID (TO objet){ // crée un identificateur à un objet
        if(this.objetVersId.containsKey(objet)){ // erreur si l'objet possède déjà un identificateur
            throw new Error("objet " + objet + " déjà dans le numéroteur");
        }
        this.associe(getProchainID(), objet);
        return genererIdLibre();
    }
    
    /*
    retourne l'identificateur libre
    cette méthode s'appele dans le constructeur de l'objet à associer
    attention bien appeler la méthode associe après la création de l'objet
    */
    public int genererIdLibre(){
        prochainID ++;
        return getProchainID() -1;
    }
    
    public boolean objExist(TO objet) { // permet de voir si l'objet existe
        return this.objetVersId.containsKey(objet);
    }
    
    public int getID(TO objet) { // permet de récuperer l'identificateur 
        if (this.objExist(objet)) {
            return this.objetVersId.get(objet);
        } else {
            throw new Error("Objet" + objet + " inconnu dans numéroteur");
        }
    }

    public int getOuCreeID(TO objet) { // récupère l'identificateur si il existe sinon le crée
        if (this.objExist(objet)) {
            return this.objetVersId.get(objet);
        } else {
            return this.creeID(objet);
        }
    }
    
     public TO getObjet(int id) { // récupere l'objet
        if (! this.idExist(id)) {
            throw new Error("identificateur non existant");
        }
        return this.idVersObjet.get(id);
    }
    
    public boolean idExist(int id) { // permet de voir si l'identificateur existe
        return this.idVersObjet.containsKey(id);
    }
    
    public void associe(int id,TO obj) { // associe un identificateur et un objet ?
        if (this.idExist(id)) {
            throw new Error("identificateur existant");
        }
        this.idVersObjet.put(id, obj);
        this.objetVersId.put(obj, id);
    }
    
    /**
     * @return the prochainID
     */
    public int getProchainID() {
        return prochainID;
    }
}
   