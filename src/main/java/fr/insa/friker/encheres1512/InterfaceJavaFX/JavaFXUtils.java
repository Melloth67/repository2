/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.friker.encheres1512.InterfaceJavaFX;

import fr.insa.friker.utils.SQLUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.control.Alert;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;

/**
 *
 * @author francois
 */
public class JavaFXUtils {

    public static void addSimpleBorder(Region c) {
        addSimpleBorder(c, Color.BLACK, BorderWidths.DEFAULT.getTop());
    }
    
    public static void addSimpleBorder(Region c,Color couleur,double epaisseur) {
        c.setBorder(new Border(new BorderStroke(couleur,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY,new BorderWidths(epaisseur))));
    }

    public static WebView preparedStatementInWebView(PreparedStatement pst) {
        WebView view = new WebView();

        try ( ResultSet rs = pst.executeQuery()) {
            view.getEngine().loadContent(SQLUtils.formatResultSetAsHTMLTable(rs));
        } catch (SQLException ex) {
            view.getEngine().loadContent("<b> problem bdd : " + ex.getLocalizedMessage() + " </b>");
        }
        return view;
    }

    public static void showErrorInAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(message);
        alert.showAndWait();
    }
    public static void showErrorInAlert(String titre, String message, String detail) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(message);
        alert.setContentText(detail);
        alert.showAndWait();

    }
    public static void showInfoInAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(message);
        alert.showAndWait();

    }
}
