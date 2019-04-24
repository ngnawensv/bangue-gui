/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.douwe.banque.model;

import com.douwe.banque.data.Customer;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Maurice PENKA
 */
public interface IServiceCustomer {
    
    /**
     * methode retournant le nombre de lignes detectés
     *
     * @param id identifiant du client
     * @return le nombre de lignes
     */
    public int modifierCustomerById(int id);
    
    
    /**
     * methode renvoyant la liste de tous les clients
     *
     * @return la liste de tous les clients
     */
    public List<Customer> findAllCustomer() throws SQLException;
    
    
     /**
     * methode renvoyant un client a parti de son identifiant
     *
     * @param id : identifiant du client
     * @return un client
     * @throws SQLException
     */
    public Customer findCustomerById(int id) throws SQLException;
    
    
    /**
     * methode permettant de filter la liste des clients en fonction du nom pris
     * en parametre
     *
     * @param nom : nom des clients
     * @return la liste des clients
     */
    public List<Customer> findCustomerByName(String nom);
    
    
    /**
     * methode permettant d'effectuer les modifications sur un client et
     * prennant en parametre le nom l'adresse email le numero de telephone et
     * identifiant du client
     *
     * @param name : nom du client
     * @param email : adresse email du client
     * @param phone : numero de telephone du client
     * @param id : identifiant du client
     */
    public void modifierClient(String name, String email, String phone, int id);
    
    
     /**
     * methode permettant d'enregistrer un client pris en parametre
     *
     * @param c parametre representant un client
     */
    public void saveClient(Customer c);
}
