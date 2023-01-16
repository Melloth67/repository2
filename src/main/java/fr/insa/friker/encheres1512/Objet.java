/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.friker.encheres1512;

import static fr.insa.friker.encheres1512.Encheres1512.curSGBD;
import fr.insa.friker.utils.Console;
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

        
            public static void creeSchema2(Connection con) throws SQLException {
            con.setAutoCommit(false);
            try ( Statement st = con.createStatement()) {            
            st.executeUpdate(
                    """
                    create table objet (
                        idObjet integer not null primary key,
                        generated always as identity,
                        designation varchar(80) not null,
                        description varchar(500) not null,
                        idvendeur varchar(50) not null,
                        idcategorie varchar(50) not null,
                        PrixBase float not null,
                    )
                    """);
            st.executeUpdate(
                    """
                    create table categorie (
                        idCategorie integer not null primary key,
                        generated always as identity,
                        nomCategorie varchar(30) not null,
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
                         
            public static void afficheToutesLesCategories(Connection con) throws SQLException {
            try ( Statement st = con.createStatement()) {
                try ( ResultSet tlc = st.executeQuery("select * from categorie")) {
                System.out.println("liste des categories :");
                System.out.println("------------------");
                while (tlc.next()) {
                String nomCategorie = tlc.getString("nomCategorie");
                int idCategorie = tlc.getInt("idCategorie");
                System.out.println(idCategorie + " : " + nomCategorie);
            }
        }catch ( SQLException ex){
            con.rollback();
            throw ex;}
        }
    }
            
            public static void afficheTousLesObjets(Connection con) throws SQLException {
            try ( Statement st = con.createStatement()) {
                try ( ResultSet tlo = st.executeQuery("select * from objet")) {
                System.out.println("liste des objets :");
                System.out.println("------------------");
                while (tlo.next()) {
                String designation = tlo.getString("designation");
                String description = tlo.getString("description");
                String idvendeur = tlo.getString("Vendeur");
                String idcategorie = tlo.getString("Categorie");
                Float PrixBase = tlo.getFloat("PrixBase");
                int idObjet = tlo.getInt("idObjet");
                System.out.println(designation + " : " + idObjet + "; Description : " + description + "; Categorie : " + Categorie + "; Vendeur : " + Vendeur + "; Prix : " + PrixBase);
            }
        }catch ( SQLException ex){
            con.rollback();
            throw ex;}
        }
    }
             
            public static void supprimeObjetEntier(Connection con) throws SQLException {
            try ( Statement st = con.createStatement()) {
                try {
                    st.executeUpdate(
                        """
                        drop table objet
                        """);
                    System.out.println("La table objet est supprimée");
                } catch (SQLException ex) {
            }
        }
    }
            
            public static void supprimeCategorieEntiere(Connection con) throws SQLException {
            try ( Statement st = con.createStatement()) {
                try {
                    st.executeUpdate(
                        """
                        drop table categorie
                        """);
                    System.out.println("La table categorie est supprimée");
                } catch (SQLException ex) {
            }
        }
    }
           
            public static class DesignationExisteDejaException extends Exception {
        }
            
            public static class NomCategorieExisteDejaException extends Exception {
        } 
            
            public static int createObjet(Connection con, int idObjet, String designation, String description, String idCategorie, String idVendeur, Float PrixBase)
                throws SQLException, DesignationExisteDejaException {
            con.setAutoCommit(false);
            try ( PreparedStatement chercheDesignation = con.prepareStatement(
                "select idObjet from objet where designation = ?")) {
                chercheDesignation.setString(1, designation);
                ResultSet testDesignation = chercheDesignation.executeQuery();
                if (testDesignation.next()) {
                throw new DesignationExisteDejaException();
            }
            try ( PreparedStatement pst = con.prepareStatement(
                    """
                insert into objet (designation,description,idVendeur,idCategorie,PrixBase) values (?,?,?,?,?)
                """, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pst.setString(1, designation);
                pst.setString(2, description);
                pst.setString(3, idVendeur);
                pst.setString(4, idCategorie);
                pst.setFloat(5, PrixBase);
                pst.executeUpdate();
                con.commit();
                try ( ResultSet rid = pst.getGeneratedKeys()) {
                    rid.next();
                    int idObjets = rid.getInt(1);
                    return idObjets;
                }
            }
        } catch (Exception ex) {
            con.rollback();
            throw ex;
        } finally {
            con.setAutoCommit(true);
        }
    }
            
            public static int createCategorie(Connection con, int idCategorie, String nomCategorie)
                throws SQLException, NomCategorieExisteDejaException {
            con.setAutoCommit(false);
            try ( PreparedStatement cherchenomCategorie = con.prepareStatement(
                "select idCategorie from categorie where designation = ?")) {
                cherchenomCategorie.setString(1, nomCategorie);
                ResultSet testnomCategorie = cherchenomCategorie.executeQuery();
                if (testnomCategorie.next()) {
                throw new NomCategorieExisteDejaException();
            }
            try ( PreparedStatement pst = con.prepareStatement(
                    """
                insert into categorie (nomCategorie) values (?)
                """, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pst.setString(1, nomCategorie);
                pst.executeUpdate();
                con.commit();
                try ( ResultSet rid = pst.getGeneratedKeys()) {
                    rid.next();
                    int idCategories = rid.getInt(1);
                    return idCategories;
                }
            }
        } catch (Exception ex) {
            con.rollback();
            throw ex;
        } finally {
            con.setAutoCommit(true);
        }
    }

            public static boolean idObjetExistant(Connection con, int idObjet) 
                    throws SQLException {
                        try( PreparedStatement pst = con.prepareStatement(
                        "select idObjet from objet where idObjet = ?")) {
                         pst.setInt(1, idObjet);
                        ResultSet res = pst.executeQuery();
                        return res.next();
                        }
                     }
                        
            public static boolean idCategorieExistant(Connection con, int idObjet) 
                    throws SQLException {
                        try( PreparedStatement pst = con.prepareStatement(
                        "select idCategorie from categorie where idCategorie = ?")) {
                        pst.setInt(1, idObjet);
                        ResultSet res = pst.executeQuery();
                        return res.next();
                        }
                     }

            public static boolean DesignationExistante(Connection con, String designation) 
                    throws SQLException {
                        try( PreparedStatement pst = con.prepareStatement(
                        "select idObjet from objet where designation = ?")) {
                        pst.setString(1, designation);
                        ResultSet res = pst.executeQuery();
                        return res.next();
                    }
                }

            public static boolean nomCategorieExistante(Connection con, String nomCategorie) 
                    throws SQLException {
                        try( PreparedStatement pst = con.prepareStatement(
                        "select idCategorie from categorie where nomCategorie = ?")) {
                        pst.setString(1, nomCategorie);
                        ResultSet res = pst.executeQuery();
                        return res.next();
                    }
                }
            
            public static void afficheUnObjet(Connection con, int idObjet1) throws SQLException {
                try ( Statement st = con.createStatement()) {
                    try ( ResultSet tlu = st.executeQuery("select * from objet where idObjet = idObjet1")) {
                    System.out.println("L'objet recherché correspond à :");
                    System.out.println("------------------");
                    while (tlu.next()) {
                    int idObjet = tlu.getInt("idObjet");
                    String designation = tlu.getString("designation");
                    String description = tlu.getString("description");
                    String Vendeur = tlu.getString("Vendeur");
                    String Categorie = tlu.getString("Categorie");
                    Float PrixBase = tlu.getFloat("PrixBase");
                    System.out.println(idObjet + " : " + designation + "; Description : " + description + "; Categorie : " + Categorie + "; Vendeur : " + Vendeur + "; Prix : " + PrixBase);
                }
            }catch ( SQLException ex){
                con.rollback();
                throw ex;}
                }
            }
            
            public static void afficheUneCategorie(Connection con, int idCategorie1) throws SQLException {
                try ( Statement st = con.createStatement()) {
                    try ( ResultSet tlu = st.executeQuery("select * from categorie where idCategorie = idCategorie1")) {
                    System.out.println("La catégorie recherchée correspond à :");
                    System.out.println("------------------");
                    while (tlu.next()) {
                    int idCategorie = tlu.getInt("idCategorie");
                    String nomCategorie = tlu.getString("nomCategorie");
                    System.out.println(idCategorie + " : " + nomCategorie);
                }
            }catch ( SQLException ex){
                con.rollback();
                throw ex;}
                }
            }
            
            public static ArrayList<Objet> afficheObjetParCategorie(Connection con, String categorie) throws SQLException {
                ArrayList<Objet> res = new ArrayList<>();
                try (PreparedStatement pst = con.prepareStatement(
                        "select * from Objet join Categorie on Objet.Categorie = Categorie.nomCategorie")){
                        pst.setString(1,categorie);
                            try(ResultSet rs = pst.executeQuery()){
                                while(rs.next()){
                                    res.add(new Objet(rs.getInt("idObjet"), rs.getString("designation"),rs.getString("description"), rs.getString("idVendeur"), rs.getString("idCategorie"), rs.getFloat("PrixBase"), rs.getString("DateeFin"))); 
                            }
                            return res;
                        }
                    }
            }  
        
            public static int choisirUnObjet(Connection con) throws SQLException {
                 boolean ok = false;
                int idObjet = -1;
                while (!ok) {
                    System.out.println("------- Choix d'un objet");
                    afficheTousLesObjets(con);
                    idObjet = Console.entreeEntier("Précisez l'identificateur de l'objet recherché :");
                    ok = idObjetExistant(con, idObjet);
                    if (!ok) {
                        System.out.println("Aucun objet ne correspond à cet identificateur pour le moment.");
                    }
                }
            return idObjet;
        }
            
            public static int choisirUneCategorie(Connection con) throws SQLException {
                boolean ok = false;
                int idCategorie = -1;
                while (!ok) {
                    System.out.println("------- Choix d'une catégorie : ");
                    afficheToutesLesCategories(con);
                    idCategorie = Console.entreeEntier("Précisez l'identificateur de la catégorie recherchée :");
                    ok = idCategorieExistant(con, idCategorie);
                    if (!ok) {
                        System.out.println("Aucune catégorie ne correspond à cet identificateur pour le moment.");
                    }
                }
            return idObjet;
        } 

            public static boolean VerifObjet(Connection con, String designation) throws SQLException {
                boolean test1 = false;
                try ( Statement st = con.createStatement()) {
                try ( ResultSet tlo = st.executeQuery("select * from objet ")) {
                    while (tlo.next()) {
                        if(designation.equals(tlo.getString("designation"))){
                        test1 =true;
                        System.out.println("L'objet existe déjà.");
                     }
                 }
                    if(test1 == false ){
                     System.out.println("L'objet n'existe pas encore.");
                 }
            }
        }
        return test1;
    }
            
            public static boolean VerifCategorie(Connection con, String nomCategorie) throws SQLException {
                boolean test2 = false;
                try ( Statement st = con.createStatement()) {
                try ( ResultSet tlc = st.executeQuery("select * from objet ")) {
                    while (tlc.next()) {
                        if(nomCategorie.equals(tlc.getString("nomCategorie"))){
                        test2 = true;
                        System.out.println("La catégorie existe déjà.");
                     }
                 }
                 if(test2 == false ){
                     System.out.println("La catégorie n'existe pas encore.");
                 }
            }
        }
        return test2;
     }
            
            public static void creationNouvelleCategorie(Connection con) throws SQLException {
                boolean existe = true;
                while (existe) {
                    try {
                    System.out.println("--- Création d'une nouvelle catégorie");
                    String nomCategorie = Console.entreeString("Saisir le nom de la catégorie :");
                    createCategorie(con, Categorie, nomCategorie);
                    existe = false;
                    } catch (NomCategorieExisteDejaException ex) {
                    System.out.println("Cette catégorie existe déjà, il faut choisir un autre nom de catégorie.");
                }
            }
        }
            
             public static void creationNouvelObjet(Connection con) throws SQLException {
                boolean existe = true;
                while (existe) {
                    try {
                    System.out.println("--- Création d'un nouvel objet");
                    String designation = Console.entreeString("Saisir la désignation de l'objet :");
                    String categorie = Console.entreeString("Saisir la catégorie de l'objet :");
                    while(Objet.VerifCategorie(con, categorie)== false) {
                        System.out.println("La catégorie saisie n'existe pas encore. Il faut saisir une catégorie existante telles que : ");
                        Objet.afficheToutesLesCategories(con);
                        String categorie2 = Console.entreeString("Saisir la catégorie correspondante : ");    
                    }
                    String description = Console.entreeString("Saisir la description de l'objet : ");
                    String Vendeur = Console.entreeString("Saisir l'identifiant du vendeur : ");
                    String 
                            
                    createObjet(con, idCategorie, nomCategorie);
                    existe = false;
                    } catch (NomCategorieExisteDejaException ex) {
                    System.out.println("Cette catégorie existe déjà, il faut choisir un autre nom de catégorie.");
                }
            }
        }
            


