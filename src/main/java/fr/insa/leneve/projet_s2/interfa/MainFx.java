/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2.interfa;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.stage.Stage;
import fr.insa.leneve.projet_s2.structure.forme.Treillis;

/**
 *
 * @author adrie
 */
public class MainFx extends Application {
    
    @Override
    public void start(Stage stage) {
        Scene sc = new Scene(new MainPanel(stage),1000,700);
        stage.setScene(sc);
        stage.setTitle("Modélisateur de treillis");
          stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}