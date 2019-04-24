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
public class Account {

    
    //Attributs
    String numeroCompte;
    int numClient;
    double solde;
    Date dateCreation;
    int type;
    String nom;

    public Account() {
    }

    public Account(String numeroCompte, double solde, Date dateCreation, int type) {
        this.numeroCompte = numeroCompte;
        this.solde = solde;
        this.dateCreation = dateCreation;
        this.type = type;
    }

    public String getNumeroCompte() {
        return numeroCompte;
    }

    public void setNumeroCompte(String numeroCompte) {
        this.numeroCompte = numeroCompte;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
    public int getNumClient() {
        return numClient;
    }

    public void setNumClient(int numClient) {
        this.numClient = numClient;
    }
    
     public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
