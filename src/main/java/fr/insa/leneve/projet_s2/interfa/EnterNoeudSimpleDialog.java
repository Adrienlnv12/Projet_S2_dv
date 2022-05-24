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
package fr.insa.leneve.projet_s2.interfa;


import fr.insa.leneve.projet_s2.structure.Noeud.NoeudSimple;
import java.util.Optional;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

/**
 * Creation d'un Dialog pour permettre à l'utilisateur d'entrer les coordonnées
 * et la couleur d'un Point. Classe structurée en s'inspirant de l'exemple 5 de
 * :
 * https://examples.javacodegeeks.com/desktop-java/javafx/dialog-javafx/javafx-dialog-example/
 *
 * @author francois
 * Modifié par adrie
 */
// la classe Dialog est une classe générique : le paramètre de type (ici Point)
// indique quel est le type de l'objet éventuellement créé/retourné par le Dialog
public class EnterNoeudSimpleDialog extends Dialog<NoeudSimple> {

    private TextField tfPx;
    private TextField tfPy;

    /**
     *
     */
    public EnterNoeudSimpleDialog() {
        this.setTitle("Entrez les coordonnées du Noeud:");
        this.setResizable(true);
        Label lPx = new Label("px :");
        Label lPy = new Label("py :");
        this.tfPx = new TextField("0.0");
        this.tfPy = new TextField("0.0");
        
        GridPane grid = new GridPane();
        grid.add(lPx, 0, 0);
        grid.add(this.tfPx, 1, 0);
        grid.add(lPy, 0, 1);
        grid.add(this.tfPy, 1, 1);
        this.getDialogPane().setContent(grid);

        ButtonType bOK = new ButtonType("OK", ButtonData.OK_DONE);
        ButtonType bCancel = new ButtonType("Annuler", ButtonData.CANCEL_CLOSE);
        this.getDialogPane().getButtonTypes().add(bOK);
        this.getDialogPane().getButtonTypes().add(bCancel);

        this.setResultConverter(new Callback<ButtonType, NoeudSimple>() {
            @Override
            public NoeudSimple call(ButtonType p) {
                if (p == bOK) {
                    double px;
                    double py;
                    try {
                        px = Double.parseDouble(EnterNoeudSimpleDialog.this.tfPx.getText());
                        py = Double.parseDouble(EnterNoeudSimpleDialog.this.tfPy.getText());
                    }catch (NumberFormatException ex) {
                        return null;
                    }   return new NoeudSimple(px, py);
                } else {
                    return null;
                }
            }
        });

    }

    public static Optional<NoeudSimple> demandeNoeudSimple() {
        EnterNoeudSimpleDialog dialog = new EnterNoeudSimpleDialog();
        return dialog.showAndWait();
    }

}
