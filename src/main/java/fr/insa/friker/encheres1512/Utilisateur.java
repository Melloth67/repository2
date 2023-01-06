/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.friker.encheres1512;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Elève
 */
public class Utilisateur {
    
        private final int id;
    private String email;
    private String pass;
    private String nomRole;

    public Utilisateur(int id, String email, String pass, String nomRole) {
        this.id = id;
        this.email = email;
        this.pass = pass;
        this.nomRole = nomRole;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the email
     */
    public String getemail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setemail(String email) {
        this.email = email;
    }

    /**
     * @return the pass
     */
    public String getPass() {
        return pass;
    }

    /**
     * @param pass the pass to set
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getNomRole() {
        return nomRole;
    }
    
    
   public static boolean testemail(Connection con,String mail) throws SQLException{
       
        boolean test = false;
        try ( Statement st = con.createStatement()) {
            try ( ResultSet tlu = st.executeQuery("select * from utilisateurs")) {
                 while (tlu.next()) {
                     if(mail.equals(tlu.getString("email"))){
                         test =true;
                         System.out.println("L'email existe");
                     }
                 }
                 if(test == false ){
                     System.out.println("L'email saisi ne correspond à aucun utilisateur");
                 }
            }
        }
        return test;
     }
    
}
