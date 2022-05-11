/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2.structure;

import java.util.ArrayList;
import java.util.Optional;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.control.ButtonType;
import javafx.util.Callback;

/**
 *
 * @author adrie
 */
public class BoutonType extends Dialog<TypedeBarre> {
    
    public BoutonType() {
       this.setTitle("créer un type de barre");
       this.setResizable(true);


        //name
        Label nameLbl = new Label("Nom :");
        TextField nameTF = new TextField();
        //cout
        Label costLbl = new Label("Coût :");
        TextField costTF = new TextField();
        //lmin
        Label lMinLbl = new Label("Longueur minimale :");
        TextField lMinTF = new TextField();
        //lmax
        Label lmaxLbl = new Label("Longueur maximale :");
        TextField lmaxTF = new TextField();
        //rtension
        Label rTensionLbl = new Label("Tension exercée :");
        TextField rTensionTF = new TextField();
        //rcomp
        Label rCompLbl = new Label("Compression exercée :");
        TextField rCompTF = new TextField();
        //couleurs
        Label lC = new Label("couleur :");
        ColorPicker cpCouleur = new ColorPicker(Color.BLACK);
        
        GridPane grid = new GridPane();
        grid.add(nameLbl, 0, 0);
        grid.add(nameTF, 1, 0);
        grid.add(costLbl, 0, 1);
        grid.add(costTF, 1, 1);
        grid.add(lMinLbl, 0, 2);
        grid.add(lMinTF, 1, 2);
        grid.add(lmaxLbl, 0, 3);
        grid.add(lmaxTF, 1, 3);
        grid.add(rTensionLbl, 0, 4);
        grid.add(rTensionTF, 1, 4);
        grid.add(rCompLbl, 0, 5);
        grid.add(rCompTF, 1, 5);
        grid.add(lC, 0, 6);
        grid.add(cpCouleur, 1, 6);
        this.getDialogPane().setContent(grid);
      

        
        //buttons
        //boutons d'ajout, ne fait rien si une case n'est pas remplie
        ButtonType addTypeBtn = new ButtonType("Ajouter ce type", ButtonData.OK_DONE);
        ButtonType bCancel = new ButtonType("Annuler", ButtonData.CANCEL_CLOSE);
        this.getDialogPane().getButtonTypes().add(addTypeBtn);
        this.getDialogPane().getButtonTypes().add(bCancel);
        this.setResultConverter(new Callback<ButtonType, TypedeBarre>() {
            @Override
            public TypedeBarre call(ButtonType p) {
                if (p == addTypeBtn) {
                    try {
                        Treillis treillis = ac.getTreillis();
                        TypedeBarre type = new TypedeBarre(nameTF.getText(), Double.parseDouble(costTF.getText()), Double.parseDouble(lMinTF.getText()),
                        Double.parseDouble(lmaxTF.getText()), Double.parseDouble(rTensionTF.getText()),
                        Double.parseDouble(rCompTF.getText()), treillis.getNumerateur().getNewTypeId());
                        treillis.addType(type);
                        ArrayList<TypedeBarre> catalogue = treillis.getCatalogue();
                    }catch (NumberFormatException ex) {
                        return null;
                    }
                } else {
                    return null;
                }
                return null;
            }
        });
    }

    public static Optional<TypedeBarre> demandeTypedeBarre() {
        BoutonType dialog = new BoutonType();
        return dialog.showAndWait();
    }
    
}
