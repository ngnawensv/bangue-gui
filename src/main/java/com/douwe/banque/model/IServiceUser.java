/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.douwe.banque.model;

import com.douwe.banque.data.User;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Maurice PENKA
 */
public interface IServiceUser {
    
    /**
     * methode permettant supprimer les utilisateurs et retoune le nombre de
     * lignes supprimés
     *
     * @param id : identifiant utilisateur
     * @return le nombre de lignes detectés
     */
    public int deleteUserById(int id);
    
    
    /**
     * methode permettant de renvoyer la liste de tous les utilisateurs prennant
     * en parametre une requete sql
     *
     * @param requete : requete sql
     * @return la liste de tous les utilisateurs
     * @throws SQLException
     */
    public List<User> findAllUserSimple(String requete) throws SQLException;
    
    
    /**
     * methode permettant de reinitialiser le mot de passe
     *
     * @param id : identifiant utilisateur
     */
    public void reinitialiserPassWord(int id);
    
    
    
    /**
     * methode permettant d'enregistrer un utilisateur pris en parametre
     *
     * @param u parametre representant un utilisateur
     */
    public void saveUtilisateur(User u);
    
    
    /**
     * methode permettant d'authentifier un utilisateur et prennant en parametre
     * son pseudo et son mot de passe retourne 1 si tous se passe bien 2 si
     * l'utilisateur n'a pas bien entré les parametres de connexion ou
     * l'utilisateur en question n'existe pas dans le systeme et enfin 3 si il
     * y'a impossibilité de verification de coordonnées
     *
     * @param login : pseudo de l'utilisateur
     * @param password : mot de passe de l'utilisateur
     * @return 1 si tous se passe bien,2 pour mauvaise parametre et 3 pour
     * impossibilité de verification des coordonnées
     */
    public int authentification(String login, String password);
}
