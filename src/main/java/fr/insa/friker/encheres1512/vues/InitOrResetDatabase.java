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
package fr.insa.friker.encheres1512.vues;

import fr.insa.friker.encheres1512.Encheres1512;
import fr.insa.friker.encheres1512.InterfaceJavaFX.JavaFXUtils;
import fr.insa.friker.encheres1512.InterfaceJavaFX.VuePrincipale;
import java.sql.Connection;
import java.sql.SQLException;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * @author francois
 */
public class InitOrResetDatabase extends VBox {

    private VuePrincipale main;

    private Button vbInitDatabase;

    public InitOrResetDatabase(VuePrincipale main) {
        this.main = main;

        TextArea note = new TextArea("Note : normalement le bouton ci-dessous ne devrait pas faire partie \n"
                + "de l'interface web : la création de la base de donnée se fait une fois pour \n"
                + "toute par l'administrateur système, en général hors de l'interface web\n");
        note.setEditable(false);
        note.setStyle("-fx-text-fill: red;");

        this.getChildren().add(note);

        this.vbInitDatabase = new Button("Initialisation ou ré-initialisation de la base de donnée");
        this.vbInitDatabase.setOnAction((event) -> {
            Connection con = this.main.getSessionInfo().getConBdD();
            try {
                Encheres1512.creeSchema(con);
                JavaFXUtils.showInfoInAlert("OK : BdD (ré)-initialisée");
            } catch (SQLException ex) {
                JavaFXUtils.showInfoInAlert("Problème : " + ex.getLocalizedMessage());
            }
        });
        this.getChildren().add(this.vbInitDatabase);
        JavaFXUtils.addSimpleBorder(this, Color.RED, 2);

    }
}
