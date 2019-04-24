/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.douwe.banque.model;

import com.douwe.banque.data.Account;
import com.douwe.banque.data.AccountType;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Maurice PENKA
 */
public interface IServiceAccount {
    
    /**
     * methode permetant renvoyer un compte en fonction de son identifiant
     *
     * @param id : identifiant du compte
     * @return un compte
     * @throws SQLException
     */
    public Account findAccountByIdCustumer(int id) throws SQLException;
    
    
    /**
     * methode permettant de crediter un compte donc prend en parametre le
     * numero de compte et le montant a credité
     *
     * @param numCompte : numero de compte
     * @param montant : montant à crediter
     */
    public void crediterCompte(String numCompte, double montant);
    
    
    
    /**
     * methode permettant de debiter un compte donc prend en parametre le numero
     * de compte et le montant a debité
     *
     * @param numcompte : numero de compte
     * @param montant : montant a debité
     */
    public void debiterCompte(String numcompte, double montant);
    
    
    /**
    *  methode renvoyant la liste des comptes avec le nom des titulaires
    * et prend en parametre une requete sql
    * 
    * @param requete : requete sql
    * @return liste des comptes
    * @throws SQLException 
    */
    public List<Account> findAllCompteByCustomer(String requete) throws SQLException;
    
    
    /**
     * methode permettant de renvoyer la liste des comptes d'un client en
     * prennant en parametre son identifiant
     *
     * @param id : identifiant du client
     * @return la liste des comptes
     * @throws SQLException
     */
    public List<Account> findAllCompteByIdCustomer(int id) throws SQLException;
    
    
    
    /**
     * methode permettant de retourner la liste des numero de compte de client
     * identifier par son identifiant pris en parametre
     *
     * @param id : identifiant d'un client
     * @return la liste de numero de compte d'un client
     */
    public List<String> findNumCompteByIdCustomer(int id) throws SQLException;
    
    
    /**
     * methode permettant d'apporter les modification dans un compte et prennant
     * en parametre identifiant du compte le numero de compte et le type de
     * compte
     *
     * @param id : identifiant du compte
     * @param numCompte : numero de compte
     * @param type : type de compte
     */
    public void modifierCompte(int id, String numCompte, AccountType type);
    
    
    /**
     * methode permertant d'enregistrer un compte prennant en parametre le nom
     * du client le numero de compte le type de compte et le solde initial du
     * compte
     *
     * @param customer : nom du client
     * @param number : numero de compte
     * @param type : type de compte
     * @param balance : solde initial du compte
     */
    public void saveCompte(String customer, String number, AccountType type, double balance);
    
}
