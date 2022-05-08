/*
Copyright 2000- Francois de Bertrand de Beuvron

This file is part of CoursBeuvron.

CoursBeuvron is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

CoursBeuvron is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with CoursBeuvron.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.insa.leneve.projet_s2;

import java.util.HashMap;  // clé qui associe à un truc, ici à partir d'un objet on peut retrouver son id
import java.util.Map;
import java.util.TreeMap; // relation d'association entre clé et objet, c'est organisé contrairement à la HashMap (ex : 1 2 3 4)

/**
 *
 * @author francois
 */
public class Numeroteur<TO> {
   
    // on va indiquer le type de l'objet qu'on veut numéroter --> ça va créer 2 map
    /* créer des maps entre identificateur et objet
     préciser le type objet TO en faisant appel à la classe
     exemple : Numeroteur<Figure> num 
    */
    private TreeMap<Integer,TO> idVersObjet;
    private Map<TO,Integer> objetVersId;
    
    private int prochainID;
    
    public Numeroteur() {
        this(0);
    }
    
    private Numeroteur(int prochainID) {
        this.prochainID = prochainID;
        this.idVersObjet = new TreeMap<>();
        this.objetVersId = new HashMap<>();
    }
    
    public int creeID(TO objet) { // crée un identificateur à un objet
        if(this.objetVersId.containsKey(objet)) { // erreur si l'objet possède déjà un identificateur
            throw new Error("objet " + objet + " déjà dans le numéroteur");
        }
        this.idVersObjet.put(this.prochainID, objet);
        this.objetVersId.put(objet, this.prochainID);
        this.prochainID ++;
        return this.prochainID - 1;
    }
    
    /*
    retourne l'identificateur libre
    cette méthode s'appele dans le constructeur de l'objet à associer
    attention bien appeler la méthode associe après la création de l'objet
    */
    public int genererIdLibre(){
        prochainID ++;
        return prochainID -1;
    }
    
    public boolean objExist(TO obj) {
        return this.objetVersId.containsKey(obj);
    }
    
    public int getID(TO obj) {
        if (this.objExist(obj)) {
            return this.objetVersId.get(obj);
        } else {
            throw new Error("Objet" + obj + " inconnu dans numéroteur");
        }
    }

    public int getOuCreeID(TO obj) {
        if (this.objExist(obj)) {
            return this.objetVersId.get(obj);
        } else {
            return this.creeID(obj);
        }
    }
    
    public TO getObjet(int id) {
        if (! this.idExist(id)) {
            throw new Error("identificateur non existant");
        }
        return this.idVersObjet.get(id);
    }
    
    public boolean idExist(int id) {
        return this.idVersObjet.containsKey(id);
    }
    
    public void associe(int id,TO obj) {
        if (this.idExist(id)) {
            throw new Error("identificateur existant");
        }
        this.idVersObjet.put(id, obj);
        this.objetVersId.put(obj, id);
    }

    
    
}
