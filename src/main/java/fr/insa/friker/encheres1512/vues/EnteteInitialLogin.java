/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.friker.encheres1512.vues;

import fr.insa.friker.encheres1512.Encheres1512;
import fr.insa.friker.encheres1512.InterfaceJavaFX.JavaFXUtils;
import fr.insa.friker.encheres1512.InterfaceJavaFX.VuePrincipale;
import fr.insa.friker.encheres1512.Utilisateur;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 *
 * @author francois
 */
public class EnteteInitialLogin extends HBox {

    private VuePrincipale main;
    
    private Button vbLogin;
    private Button vbNouvelUtilisateur;

    public EnteteInitialLogin(VuePrincipale main) {

                this.main = main;
        
        this.vbLogin = new Button("Login");
        this.vbLogin.setOnAction((event) -> {
            this.main.setMainContent(new LoginForm(this.main));
        });
        this.vbNouvelUtilisateur = new Button("Nouvel utilisateur");
        this.vbNouvelUtilisateur.setOnAction((t) -> {
            this.main.setMainContent(new NouvelUtilisateur(this.main));
        });
        this.getChildren().addAll(this.vbLogin,this.vbNouvelUtilisateur);
    }
}

//
//        this.main = main;
//
//        this.getChildren().add(new Label("nom : "));
//        this.tfNom = new TextField();
//        this.tfNom.setPromptText("nom");
//        this.getChildren().add(this.tfNom);
//        this.getChildren().add(new Label("pass : "));
//        this.pfPass = new PasswordField();
//        this.pfPass.setPromptText("pass");
//        this.getChildren().add(this.pfPass);
//        Button bLogin = new Button("Login");
//        bLogin.setOnAction((t) -> {
//            doLogin();
//        });
//        this.getChildren().add(bLogin);
//        //TODO debug
//        this.getChildren().add(new RoleComboBox(main));
//        Button bUsers = new Button("Utilisateurs");
//        bUsers.setOnAction((t) -> {
//            List<Utilisateur> allUsers = new ArrayList<>();
//            Connection con = this.main.getSessionInfo().getConBdD();
//            if (con != null) {
//                try {
//                    allUsers = GestionBdD.tousLesUtilisateurs(con);
//                } catch (SQLException ex) {
//                    JavaFXUtils.showErrorInAlert("Problem BDD",
//                            "impossible d'avoir la liste des utilisateurs",
//                            ex.getLocalizedMessage());
//                    // je ne fais rien, la table reste vide
//                }
//            } else {
//                JavaFXUtils.showErrorInAlert("Problem BDD",
//                        "pas de connection",
//                        "");
//
//            }
//            this.main.setMainContent(new UtilisateurTable(main, allUsers));
//        });
//        this.getChildren().add(bUsers);
//    }
//
//    public void doLogin() {
//        String nom = this.tfNom.getText();
//        String pass = this.pfPass.getText();
//        try {
//            Connection con = this.main.getSessionInfo().getConBdD();
//            Optional<Utilisateur> user = GestionBdD.login(con, nom, pass);
//            if (user.isEmpty()) {
//                JavaFXUtils.showErrorInAlert("Error", "Utilisateur ou pass invalide", "");
//            } else {
//                this.main.getSessionInfo().setCurUser(user);
//                this.main.setMainContent(new Label("Login OK"));
////                this.main.setEntete(new EnteteAfterLogin(this.main));
////                this.main.setMainContent(new MainAfterLogin(this.main));
//            }
//        } catch (SQLException ex) {
//            JavaFXUtils.showErrorInAlert("Error BdD", "Problème interne : ", ex.getLocalizedMessage());
//        }
//    }
//
//}
