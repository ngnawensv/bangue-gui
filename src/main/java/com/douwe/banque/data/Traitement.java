/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.douwe.banque.data;

import java.util.Date;

/**
 *
 * @author Maurice PENKA
 */
public class Traitement {
    //Attributs
    String numCompte;
    String userName;
    int operation;
    Date date;
    String description;

    public Traitement() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNumCompte() {
        return numCompte;
    }

    public Traitement(String numCompte, String userName, int operation, Date date, String description) {
        this.numCompte = numCompte;
        this.userName = userName;
        this.operation = operation;
        this.date = date;
        this.description = description;
    }

    public void setNumCompte(String numCompte) {
        this.numCompte = numCompte;
    }
    
    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
