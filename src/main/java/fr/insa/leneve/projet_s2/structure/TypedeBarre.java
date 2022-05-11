/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insa.leneve.projet_s2.structure;


import fr.insa.leneve.projet_s2.calcul.Maths;
import fr.insa.leneve.projet_s2.interfa.Controleur;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author adrie
 */
public class TypedeBarre {
    private final int id;
    private final double cout;
    private final double lMin;
    private final double lMax;
    private final double rTension;
    private final double rComp;
    private final String name;
    private final Color color;
    
    public TypedeBarre(String name, double cout, double lMin, double lMax, double rTension, double rComp, int id,Color color){
        this.name = name;
        this.id = id;
        this.cout = cout;
        this.lMin = lMin;
        this.lMax = lMax;
        this.rTension = rTension;
        this.rComp = rComp;   
        this.color = color;
    }
    @Override
    public String toString() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String saveString() {
        return "TypeBarre;" + id + ";" + cout + ";" + lMin + ";" + lMax + ";" + rTension + ";" + rComp + ";" + name+ ";" + Maths.colorToHexa(color);
    }
    
    static public void BoutonTypedeBarre(Controleur ac, ComboBox<TypedeBarre> typeComboBox){
        Stage typeChoice = new Stage();

        typeChoice.initModality(Modality.APPLICATION_MODAL);
        typeChoice.setTitle("créer un type de barre");
        typeChoice.setResizable(false);


        //name
        Label nameLbl = new Label("Nom :");
        TextField nameTF = new TextField();
        

        HBox nameHB = new HBox(10);
        nameHB.getChildren().addAll(nameLbl, nameTF);
        nameHB.setAlignment(Pos.CENTER);


       //cout
        Label costLbl = new Label("Coût :");
        TextField costTF = new TextField();

        HBox costHB = new HBox(10);
        costHB.getChildren().addAll(costLbl, costTF);
        costHB.setAlignment(Pos.CENTER);


       //lmin
        Label lMinLbl = new Label("Longueur minimale :");
        TextField lMinTF = new TextField();

        HBox lMinHB = new HBox(10);
        lMinHB.getChildren().addAll(lMinLbl, lMinTF);
        lMinHB.setAlignment(Pos.CENTER);


        //lmax
        Label lmaxLbl = new Label("Longueur maximale :");
        TextField lmaxTF = new TextField();

        HBox lmaxHB = new HBox(10);
        lmaxHB.getChildren().addAll(lmaxLbl, lmaxTF);
        lmaxHB.setAlignment(Pos.CENTER);


        //rtension
        Label rTensionLbl = new Label("Tension exercée :");
        TextField rTensionTF = new TextField();

        HBox rTensionHB = new HBox(10);
        rTensionHB.getChildren().addAll(rTensionLbl, rTensionTF);
        rTensionHB.setAlignment(Pos.CENTER);


        //rcomp
        Label rCompLbl = new Label("Compression exercée :");
        TextField rCompTF = new TextField();

        HBox rCompHB = new HBox(10);
        rCompHB.getChildren().addAll(rCompLbl, rCompTF);
        rCompHB.setAlignment(Pos.CENTER);


        //couleurs
        Label colorLBL = new Label("couleur :");
        ColorPicker cpCouleur = new ColorPicker(Color.BLACK);
        cpCouleur.setId("colorPicker");

        HBox colorHB = new HBox(10);
        colorHB.getChildren().addAll(colorLBL, cpCouleur);
        colorHB.setAlignment(Pos.CENTER);

        //buttons
        //boutons d'ajout, ne fait rien si une case n'est pas remplie
        Button addTypeBtn = new Button("Ajouter ce type");
        addTypeBtn.setOnAction(e -> {
            try {
                Treillis treillis = ac.getTreillis();
                TypedeBarre type = new TypedeBarre(nameTF.getText(), Double.parseDouble(costTF.getText()), Double.parseDouble(lMinTF.getText()),
                        Double.parseDouble(lmaxTF.getText()), Double.parseDouble(rTensionTF.getText()),
                        Double.parseDouble(rCompTF.getText()), treillis.getNumerateur().getNewTypeId(), cpCouleur.getValue());
                treillis.addType(type);
                ArrayList<TypedeBarre> catalogue = treillis.getCatalogue();
                typeComboBox.setItems(FXCollections.observableArrayList(catalogue));
                typeComboBox.getSelectionModel().select(catalogue.get(catalogue.indexOf(type)));
                typeChoice.close();
            }catch (NumberFormatException exception){
                exception.printStackTrace();
            }
        });

        //bouton pour annuler, ferme juste la fenetre
        Button cancelBtn = new Button("Annuler");
        cancelBtn.setOnAction(e -> typeChoice.close());

        HBox buttonHB = new HBox(10);
        buttonHB.getChildren().addAll(addTypeBtn, cancelBtn);
        buttonHB.setAlignment(Pos.CENTER);


        VBox mainVB = new VBox(10);
        mainVB.getChildren().addAll(nameHB, costHB, lMinHB, lmaxHB, rTensionHB, rCompHB, colorHB, buttonHB);
        mainVB.setId("vBox");

        Scene scene1 = new Scene(mainVB, 560, 300);

        typeChoice.setScene(scene1);
        typeChoice.showAndWait();

    }
    
    public double getCout() {
        return cout;
    }


    public double getlMin() {
        return lMin;
    }

    public double getlMax() {
        return lMax;
    }

    public ArrayList<String> getInfos() {
        ArrayList<String> infos = new ArrayList<>();
        infos.add("Nom : " + name);
        infos.add("Cout : " + cout);
        infos.add("Longueur min : " + lMin);
        infos.add("Longueur max : " + lMax);
        infos.add("Resistance maximale Ã  la compression : " + rComp);
        infos.add("Resistance maximale Ã  la tension : " + rTension);
        infos.add("Couleur : " + Maths.colorToHexa(color));

        return infos;
    }
    
    
    public double getrComp() {
        return rComp;
    }

    public double getrTension() {
        return rTension;
    }
    
}
