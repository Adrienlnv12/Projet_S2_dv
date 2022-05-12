/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insa.leneve.projet_s2.interfa;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.stage.Stage;
import fr.insa.leneve.projet_s2.structure.Treillis;

/**
 *
 * @author cheraita
 */
public class MainFx extends Application {
    
    private Controleur controleur;
    @Override
    public void start(Stage stage) throws Exception {
        
        String lastOpenPath = controleur.getLastOpen();
        String Name = Controleur.nameFromPath(lastOpenPath);
        Treillis treillis;
        if(lastOpenPath.equals("")){
            treillis = new Treillis();
        }else{
            treillis = Save.getTreillis(lastOpenPath);
            if(treillis == null){
                Name = "";
                treillis = new Treillis();
            }
        }
        
        Scene sc = new Scene(new MainPanel(stage,treillis),800,600);
        stage.setScene(sc);
        stage.setTitle("Nouveau");
        stage.show();
          
    }

    public static void main(String[] args) {
        launch();
    }

}