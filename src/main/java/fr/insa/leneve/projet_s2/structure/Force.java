/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2.structure;

/**
 *
 * @author adrie
 */
import fr.insa.leneve.projet_s2.interfa.Controleur;
import fr.insa.leneve.projet_s2.structure.Noeud.Noeud;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Force {

    private double fX;
    private double fY;

    public Force() {
        fX = 0;
        fY = 0;
    }

    public Force(double fx, double fy){
        this.fX = fx;
        this.fY = fy;
    }

    static public void createTypePopUp(Controleur ac, Noeud noeud){
        Stage addForce = new Stage();

        addForce.initModality(Modality.APPLICATION_MODAL);
        addForce.setTitle("ajouter des forces");
        addForce.setResizable(false);


        //Fx
        Label fxLbl = new Label("Fx :");
        TextField fxTF = new TextField();

        HBox fxHB = new HBox(10);
        fxHB.getChildren().addAll(fxLbl, fxTF);
        fxHB.setAlignment(Pos.CENTER);

        //Fy
        Label fyLbl = new Label("Fy :");
        TextField fyTF = new TextField();

        HBox fyHB = new HBox(10);
        fyHB.getChildren().addAll(fyLbl, fyTF);
        fyHB.setAlignment(Pos.CENTER);


        //buttons
        //boutons d'ajout, ne fait rien si une case n'est pas remplie
        Button addTypeBtn = new Button("Ajouter la Force");
        addTypeBtn.setOnAction(e -> {
            try {
                Force force = new Force(Double.parseDouble(fxTF.getText()), Double.parseDouble(fyTF.getText()));
                noeud.setForceApplique(force);
                addForce.close();
            }catch (NumberFormatException exception){
                exception.printStackTrace();
            }
        });

        //bouton pour annuler, ferme juste la fenetre
        Button cancelBtn = new Button("Annuler");
        cancelBtn.setOnAction(e -> addForce.close());

        HBox buttonHB = new HBox(10);
        buttonHB.getChildren().addAll(addTypeBtn, cancelBtn);
        buttonHB.setAlignment(Pos.CENTER);


        VBox mainVB = new VBox(10);
        mainVB.getChildren().addAll(fxHB, fyHB, buttonHB);
        mainVB.setId("vBox");

        Scene scene1 = new Scene(mainVB, 400, 130);

        addForce.setScene(scene1);
        addForce.showAndWait();

    }

    public ArrayList<String> getInfos(){
        String[] str = new String[]{"Fx : " + fX ,
                "Fy : " + fY
        };
        return new ArrayList<>(Arrays.asList(str));
    }

    public double getfX() {
        return fX;
    }

    public double getfY() {
        return fY;
    }
}
