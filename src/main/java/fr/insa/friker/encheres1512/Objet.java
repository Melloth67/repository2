/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.friker.encheres1512;

import static fr.insa.friker.encheres1512.Encheres1512.curSGBD;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

        
        

/**
 *
 * @author ldefossez01
 */
public class Objet {
   private final int idObjet;
    private String designation ;
    private String description;
    private String idVendeur ;
    private String idCategorie ; 
    private float PrixBase; 
    private String DateFin;

    public Objet(int idObjet, String designation, String description, String idVendeur, String idCategorie, float PrixBase, String DateFin) {
        this.idObjet = idObjet;
        this.designation = designation;
        this.description = description;
        this.idVendeur = idVendeur;
        this.idCategorie = idCategorie;
        this.PrixBase = PrixBase;
        this.DateFin = DateFin;
    }

    public int getIdObjet() {
        return idObjet;
    }
  
    public void setIdObjet(int idObjet) {
       this.idObjet = idObjet;
    }
    
    public String getdesignation() {
        return designation;
    }
     
    public void setdesignation( String designation ) {
       this.designation = designation;
    }
    
    public String getdescription() {
        return description ;
    }
     
    public void setdescription( String description ) {
       this.description = description;
    }
    public String getidVendeur() {
        return idVendeur;
    }
     
    public void setidVendeur( String idVendeur ) {
       this.idVendeur = idVendeur;
    }

    public String getidCategorie() {
        return idCategorie;
    }
     
    public void setidCategorie( String idCategorie ) {
       this.idCategorie = idCategorie;
    }
    
     public float getPrixBase() {
        return PrixBase;
    }
     
    public void setPrixBase( float PrixBase ) {
       this.PrixBase = PrixBase;
    }
    
    public String getDateFin() {
        return DateFin;
    }
     
    public void setDateFin( String DateFin ) {
       this.DateFin = DateFin;
    }
    
    @Override
        public String toString() {
            return "Objet{" + "IdObjet =" + idObjet + ", designation=" + designation + ", description =" + description + ", IdVendeur =" + idVendeur + ", IdCategorie =" + idCategorie +,", PrixBase =" + PrixBase + ", DateFin =" + DateFin +'}';
        }
    
        
        public static void creeObjet(Connection con)
            throws SQLException {
        con.setAutoCommit(false);
        try ( Statement st = con.createStatement()) {            
            st.executeUpdate(
                    """
                    create table objet (
                        idObjet integer not null primary key,
                        generated always as identity,
                        designation varchar(80) not null,
                        description varchar(500) not null,
                        idVendeur varchar(50) not null,
                        idCategorie varchar(50) not null,
                        PrixBase float not null,
                    )
                    """);
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException ex) {
            con.rollback();
            throw ex;
        } finally {
            con.setAutoCommit(true);
        }
    }
}

public static void afficheObjet(Connection con) throws SQLException {
        try ( Statement st = con.createStatement()) {
            try ( ResultSet tlu = st.executeQuery("select * from objet")) {
                System.out.println("liste des utilisateurs :");
                System.out.println("------------------");
                while (tlu.next()) {
                String codepostal = tlu.getString("codepostal");
                String nom = tlu.getString("nom");
                String pass = tlu.getString("pass");
                String email = tlu.getString("email");
                int id = tlu.getInt("id");
        
                String mess = id + " : " + nom + " (" + pass + ")" + "  email : " + email + "  code postal : " + codepostal;
                    int idRole = tlu.getInt("role");
                    if (idRole == 1) {
                        mess = mess + " --> admin";
                    }
                    System.out.println(mess);

                }
        }

    }
}


public static class DesignationExisteDejaException extends Exception {
    }

public static void deleteObjet(Connection con) throws SQLException {
        try ( Statement st = con.createStatement()) {
                try {
                st.executeUpdate(
                        """
                    drop table objet
                    """);
                System.out.println("table objet supprimée");
            } catch (SQLException ex) {
            }
        }
}

