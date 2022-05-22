package fr.insa.leneve.projet_s2.interfa;

import fr.insa.leneve.projet_s2.structure.*;
import fr.insa.leneve.projet_s2.structure.Noeud.*;
import fr.insa.leneve.projet_s2.structure.forme.Forme;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;


//VBox avec les informations sur la selection en cours
public class FenetreInfo extends VBox {

    private MainPanel main;
    private final Controleur controleur;


    public FenetreInfo(MainPanel main) {
        super();
        this.main = main;
        
        this.controleur = main.getControleur();
        this.setAlignment(Pos.CENTER);
        this.setId("infoBox");
        FxUtils.setSimpleBorder(this, Color.BLACK, 2);
        
    }

    //dessine les informations de la forme sélectionné
    public void dessineInfos(Forme f) {
        removeInfos();
        ArrayList<String> infos = f.getInfos();
        for (String line : infos) {
            Label mL = new Label(line);
            this.getChildren().add(mL);
        }
    }

    public void removeInfos() {
        this.getChildren().clear();
    }

    //dessine des informations général des élements selectionné (nombre et possibilité de tout supprimer)
    public void dessineInfosMultiplePoint(int nbNoeud,int nbAppuiDouble, int nbAppuiSimple,int nbBarre) {
        removeInfos();

        Label mLN = new Label("nombre de noeuds simple : " + nbNoeud);
        Label mLAD = new Label("nombre d'appuis double : " + nbAppuiDouble);
        Label mLAS = new Label("nombre d'appuis simple : " + nbAppuiSimple);
        Label mLB = new Label("nombre de barres : " + nbBarre);

        this.getChildren().addAll(mLN, mLAD, mLAS, mLB);

    }


    public void dessineCalculInfo(HashMap<Forme, Integer> formeId, HashMap<Integer, double[]> idValues){
        removeInfos();


        for(Forme f : formeId.keySet()){
            int id = formeId.get(f);
            if(f instanceof Barre b){
                Label t = new Label("Traction de la barre n°"+b.getId()+" : " + idValues.get(id)[0]);
                this.getChildren().add(t);
            }else if(f instanceof NoeudAppuiSimple ns){
                Label r = new Label("Reaction de l'appui simple n°"+ns.getId()+" : " + idValues.get(id)[0]);
                this.getChildren().add(r);
            }else{
                if(f instanceof NoeudAppuiDouble nd){
                    Label rx = new Label("Reaction de l'appui double n°"+nd.getId()+" en x : " + idValues.get(id)[0]);
                    this.getChildren().add(rx);
                    Label ry = new Label("Reaction de l'appui double n°"+nd.getId()+" en y : " + idValues.get(id)[1]);
                    this.getChildren().add(ry);
                }
            }
        }
    }


}
