/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.douwe.banque.data;

/**
 *
 * @author Maurice PENKA
 */
public class Customer {
    //Attribits
    int id;

    
    String name;
    String emailAdresse;
    String phoneNumber;

    public Customer(String name, String emailAdresse, String phoneNumber) {
        this.name = name;
        this.emailAdresse = emailAdresse;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
    public Customer() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAdresse() {
        return emailAdresse;
    }

    public void setEmailAdresse(String emailAdresse) {
        this.emailAdresse = emailAdresse;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
