/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.douwe.banque.connexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Maurice PENKA
 * utilisation du patern singleton pour creer une classe
 * permettant d'accédé à la base de donnée
 * tout en s'assurant qu'une seule instance de cette classe
 * sera crée
 */
public class Connexion {
    private static Connection con;
    private static Connexion iniqueInstance = null;
    
    /**
     * constructeur de la classe de connexion
     */
    private Connexion() {
    }
    
    /**
     * methode permetant de retourner une instance de connection
     * a la base de donnée
     * @return une instance de la connection a la base de donnée
     */
    public static Connection getCon() {
        return con;
    }

    public static Connexion getIniqueInstance() {
        return iniqueInstance;
    }

    public static void setIniqueInstance(Connexion iniqueInstance) {
        Connexion.iniqueInstance = iniqueInstance;
    }
    
    
    
    
    
    /**
     * methode permettant de créer une instance de connexion et
     * s'assure de l'unicité d'une seule instance de connexion
     * @return une instance de connexion
     */
    public static Connexion instance(){
        if(iniqueInstance == null){
            iniqueInstance = new Connexion();
            try{
                Class.forName("org.sqlite.JDBC");
                con = DriverManager.getConnection("jdbc:sqlite:banque.db");
                if(con != null){
                    System.out.println("success connection");
                }
            }catch(Exception e){
                Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, e);
            }
        }   
        return iniqueInstance;
    }
}
