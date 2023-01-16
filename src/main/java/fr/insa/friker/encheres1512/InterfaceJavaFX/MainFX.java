/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.friker.encheres1512.InterfaceJavaFX;


import java.sql.SQLException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 *
 * @author Elève
 */
public class MainFX extends Application {
    
   @Override
    public void start(Stage stage) throws ClassNotFoundException, SQLException {
        Scene sc = new Scene(new VuePrincipale());
//        Scene sc = new Scene(new TestFx());
        stage.setWidth(1000);
        stage.setHeight(600);
        stage.setScene(sc);
        stage.setTitle("AmourFx");
          stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
    
}
