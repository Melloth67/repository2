/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package fr.insa.friker.encheres1512;

import fr.insa.friker.utils.Console;
import fr.insa.friker.encheres1512.Utilisateur;
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
 * @author Elève
 */

public class Encheres1512 {
    
    
    
    
    
        public static class SGBD {

        private String name;
        private String autoGenerateKeys;
        private String defineDefautCharsetToUTF8;

        public SGBD(String name,String autoGenerateKeys, String defineDefautCharsetToUTF8) {
            this.name = name;
            this.autoGenerateKeys = autoGenerateKeys;
            this.defineDefautCharsetToUTF8 = defineDefautCharsetToUTF8;
        }
        
        public SGBD(String name,String autoGenerateKeys){
            this(name,autoGenerateKeys,null);
        }

        @Override
        public String toString() {
            return "SGBD{" + name + '}';
        }

        
        private String getSyntaxForAutogeneratedKeys() {
            return this.autoGenerateKeys;
        }

        private Optional<String> sqlOrderChangeCharsetToUTF8InCurrentDatabase() {
            if (this.defineDefautCharsetToUTF8 != null) {
                return Optional.of(this.defineDefautCharsetToUTF8);
            } else {
                return Optional.empty();
            }
        }
    }

    public static final SGBD MySQLSGBD = new SGBD("MySQL","AUTO_INCREMENT",
            "ALTER DATABASE CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci");
    public static final SGBD PostgresqlSGBD = new SGBD("PostgresQL","generated always as identity");

    public static SGBD curSGBD = PostgresqlSGBD;
    
    
    public static Connection connectGeneralPostGres(String host,
            int port, String database,
            String user, String pass)
            throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection(
                "jdbc:postgresql://" + host + ":" + port
                + "/" + database,
                user, pass);
        con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        return con;
    }

    public static Connection defautConnect()
            throws ClassNotFoundException, SQLException {
        return connectGeneralPostGres("localhost", 5432, "postgres", "postgres", "pass");
    }
    
    
   

    public static void creeSchema(Connection con)
            throws SQLException {
        // je veux que le schema soit entierement crÃ©Ã© ou pas du tout
        // je vais donc gÃ©rer explicitement une transaction
        con.setAutoCommit(false);
        try ( Statement st = con.createStatement()) {
            // creation des tables
            
            /*st.executeUpdate(
                    """
                    create table roles (
                        id integer not null primary key,
                        nrole varchar(30) not null unique
                    )
                    """);*/
            st.executeUpdate(
                    """
                    
                    create table encheres (
                        idenchere integer not null primary key
                        --generated always as identity
                    """
                    + curSGBD.getSyntaxForAutogeneratedKeys() + ","
                    + """
                        idobjet integer not null,
                        idvendeur integer not null,
                        idclient integer not null,
                        prixpropose integer not null
                        
                    )
                    """)
                    ;
            // si j'arrive jusqu'ici, c'est que tout s'est bien passÃ©
            // je confirme (commit) la transaction
            con.commit();
            // je retourne dans le mode par dÃ©faut de gestion des transaction :
            // chaque ordre au SGBD sera considÃ©rÃ© comme une transaction indÃ©pendante
            con.setAutoCommit(true);
        } catch (SQLException ex) {
            // quelque chose s'est mal passÃ©
            // j'annule la transaction
            con.rollback();
            // puis je renvoie l'exeption pour qu'elle puisse Ã©ventuellement
            // Ãªtre gÃ©rÃ©e (message Ã  l'utilisateur...)
            throw ex;
        } finally {
            // je reviens Ã  la gestion par dÃ©faut : une transaction pour
            // chaque ordre SQL
            con.setAutoCommit(true);
        }
    }
    
    public static List<Utilisateur> tousLesUtilisateurs(Connection con) throws SQLException {
        List<Utilisateur> res = new ArrayList<>();
        try ( PreparedStatement pst = con.prepareStatement(
                "select *"
                + " from utilisateurs "
                + " order by nom asc")) {

            try ( ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    res.add(new Utilisateur(rs.getInt("id"),
                            rs.getString("nom"), rs.getString("pass"),
                            rs.getString("role")));
                }
                return res;
            }
        }
    }

public static void afficheUtilisateurs(Connection con) throws SQLException {
        try ( Statement st = con.createStatement()) {
            try ( ResultSet tlu = st.executeQuery("select * from utilisateurs")) {
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

public static class NomExisteDejaException extends Exception {
    }
public static void deleteSchema(Connection con) throws SQLException {
        try ( Statement st = con.createStatement()) {
                try {
                st.executeUpdate(
                        """
                    drop table encheres
                    """);
                System.out.println("table utilisateurs dropped");
            } catch (SQLException ex) {
                // nothing to do : maybe the table was not created
            }
        }
}


    public static int createUtilisateur(Connection con, String nom, String pass, int roleID, String email, String codepostal)
            throws SQLException, NomExisteDejaException {
        // je me place dans une transaction pour m'assurer que la séquence
        // test du nom - création est bien atomique et isolée
        con.setAutoCommit(false);
        try ( PreparedStatement chercheNom = con.prepareStatement(
                "select id from utilisateurs where nom = ?")) {
            chercheNom.setString(1, nom);
            ResultSet testNom = chercheNom.executeQuery();
            if (testNom.next()) {
                throw new NomExisteDejaException();
            }
            // lors de la creation du PreparedStatement, il faut que je précise
            // que je veux qu'il conserve les clés générées
            try ( PreparedStatement pst = con.prepareStatement(
                    """
                insert into utilisateurs (nom,pass,role,email,codepostal) values (?,?,?,?,?)
                """, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pst.setString(1, nom);
                pst.setString(2, pass);
                pst.setInt(3, roleID);
                pst.setString(4, email);
                pst.setString(5, codepostal);
                pst.executeUpdate();
                con.commit();

                // je peux alors récupérer les clés créées comme un result set :
                try ( ResultSet rid = pst.getGeneratedKeys()) {
                    // et comme ici je suis sur qu'il y a une et une seule clé, je
                    // fait un simple next 
                    rid.next();
                    // puis je récupère la valeur de la clé créé qui est dans la
                    // première colonne du ResultSet
                    int id = rid.getInt(1);
                    return id;
                }
            }
        } catch (Exception ex) {
            con.rollback();
            throw ex;
        } finally {
            con.setAutoCommit(true);
        }
    }
    
    
    public static int createEnchere(Connection con, int idobjet, int idclient, int prixpropose) throws SQLException{
        con.setAutoCommit(false);
            try ( Statement st = con.createStatement()) {
                try ( ResultSet prixb = st.executeQuery("select prixbase from objets where idobjet = "+ idobjet)) {
                    prixb.next();
                    int prixbase = prixb.getInt("prixbase");
                int prixleplushaut = prixbase;                
            try ( ResultSet prix = st.executeQuery("select prixpropose from encheres where idobjet = "+ idobjet)) {
                
                while (prix.next()) {
                    int leprix = prix.getInt("prixpropose");
                    if(prixleplushaut < leprix){
                        prixleplushaut = leprix;
                    }
                }
            
            
            if (prixleplushaut >= prixpropose){
                System.out.println("Prix trop bas. Vous devez proposer un prix supérieur à : "+prixleplushaut+" €");
            return 0;
                }
            else {
            
            // lors de la creation du PreparedStatement, il faut que je précise
            // que je veux qu'il conserve les clés générées
            try ( PreparedStatement pst = con.prepareStatement(
                    """
                insert into encheres (idobjet,idclient,prixpropose) values (?,?,?)
                """, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pst.setInt(1, idobjet);
                pst.setInt(2, idclient);
                pst.setInt(3, prixpropose);
                pst.executeUpdate();
                con.commit();

                // je peux alors récupérer les clés créées comme un result set :
                try ( ResultSet rid = pst.getGeneratedKeys()) {
                    // et comme ici je suis sur qu'il y a une et une seule clé, je
                    // fait un simple next 
                    rid.next();
                    // puis je récupère la valeur de la clé créé qui est dans la
                    // première colonne du ResultSet
                    int id = rid.getInt(1);
                    System.out.println("Enchère créée.");
                    return id;
                }
            }
            }
            }
            }
        } catch (Exception ex) {
            con.rollback();
            throw ex;
        } finally {
            con.setAutoCommit(true);
        }
    }
    
    public static int qui_a_la_meilleure_offre (Connection con, int idobjet)throws SQLException{
        con.setAutoCommit(false);
            try ( Statement st = con.createStatement()) {
                try ( ResultSet prixb = st.executeQuery("select prixbase,idvendeur from objets where idobjet = "+ idobjet)) {
                    prixb.next();
                    int prixbase = prixb.getInt("prixbase");
                    int idvendeur = prixb.getInt("idvendeur");
                int prixleplushaut = prixbase;                
            try ( ResultSet prix = st.executeQuery("select prixpropose,idclient from encheres where idobjet = "+ idobjet)) {
                
                int idbestclient = idvendeur;
                while (prix.next()) {
                    int leprix = prix.getInt("prixpropose");
                    int client = prix.getInt("idclient");
                    if(prixleplushaut < leprix){
                        prixleplushaut = leprix;
                        idbestclient = client;
                    }
                }
                
                System.out.println(idbestclient);
                return idbestclient;
                }
            }
        }
    }
    
    
    public static void liste_encheres_utilisateur(Connection con, int idu)throws SQLException{
        con.setAutoCommit(false);
            try ( Statement st = con.createStatement()) {
                try ( ResultSet tle = st.executeQuery("select * from encheres Join objets on objets.idobjet = encheres.idobjet where idclient = "+ idu)){
                    System.out.println("liste des enchères :");
                System.out.println("------------------");
                tle.next();
                while (tle.next()) {
                    int prixpropose = tle.getInt("prixpropose");
                    int ide = tle.getInt("idenchere");
                    int ido = tle.getInt("idobjet");
                    int bgenchere = 0;
                    String designationobjet = "missing";
                    String descriptionobjet ="missing";
                    String mess = "missing";
                     designationobjet = tle.getString("designation");
                     descriptionobjet = tle.getString("description");
                     mess = ide + " : " + designationobjet + " (" + descriptionobjet + ")" + "  prix proposé : " + prixpropose + " €";
                    bgenchere = qui_a_la_meilleure_offre(con,ido);
                    if (bgenchere == idu) {
                        mess = mess + " --> possédée";  
                    } else{
                        mess = mess + " --> non possédée";}
                    System.out.println(mess);
                
                }
                } 
                }
    }
            

    
    public static Optional<Utilisateur> login(Connection con, String email, String pass) throws SQLException {
        try ( PreparedStatement pst = con.prepareStatement(
                "select utilcreeschisateurs.id as uid,nrole"
                + " from utilisateurs "
                + "   join role on utilisateurs.role = roles.id"
                + " where utilisateurs.email = ? and pass = ?")) {

            pst.setString(1, email);
            pst.setString(2, pass);
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                return Optional.of(new Utilisateur(res.getInt("uid"), email, pass, res.getString("role")));
            } else {
                return Optional.empty();
            }
        }
    }
    
    
    

    
    
    
    

    public static void menu(Connection con) throws NomExisteDejaException {
        int rep = -1;
        while (rep != 0) {
            System.out.println("Menu");
            System.out.println("=============");
            System.out.println("1) créer une table");
            System.out.println("2) supprimer une table");
            System.out.println("3) liste des utilisateurs");
            System.out.println("4) ajouter un utilisateur");
            System.out.println("0) quitter");
            rep = Console.entreeEntier("Votre choix : ");
               try {
                if (rep == 1) {
                    creeSchema(con);
                    System.out.println("Done");
                } else if (rep == 2) {
                        int conf = 0;
                    conf = Console.entreeEntier("Tapez 1 pour confirmer");
                    if (conf == 1){
                    deleteSchema(con);
                    System.out.println("Done");}
                } else if (rep == 3) {
                    afficheUtilisateurs(con);
                    System.out.println("voir plus haut");
                } else if (rep == 4) {
                    String nom = Console.entreeString("Le nom : ");
                    String pass = Console.entreeString("Le mot de passe : ");
                    int role = Console.entreeEntier("role : 1 = admin; 0 = non admin");
                    String mail = Console.entreeString("L'email : ");
                    String codepostal = Console.entreeString("Le code postal : ");
                    
                    createUtilisateur(con,nom,pass,role,mail,codepostal);

                    }
                } catch (SQLException ex) {
            throw new Error(ex);
        }
                }
            
        
        
    }
    
    
    
    
    public static void main(String[] args) throws NomExisteDejaException {

        try {
            Connection con = defautConnect();
            System.out.println("Connection OK");
            //creeSchema(con);
            //afficheUtilisateurs(con);
            //deleteSchema(con);
            /*
            try{
            createUtilisateur(con, "AAAAAAAAAA", "non", 0, "j'aimail", "67000000000");
            } catch (NomExisteDejaException ex) {}
             */
            //login(con, "maildeJ", "mdpp");
            //System.out.println("creation OK");
            //createEnchere(con,2,3,39000);
            //qui_a_la_meilleure_offre(con, 2);
            //liste_encheres_utilisateur(con,3);
            
            //menu(con);
        } catch (ClassNotFoundException ex) {
            throw new Error(ex);
        } catch (SQLException ex) {
            throw new Error(ex);
        }
    }
}
