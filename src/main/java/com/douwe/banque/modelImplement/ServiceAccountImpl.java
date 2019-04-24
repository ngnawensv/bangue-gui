/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.douwe.banque.modelImplement;

import com.douwe.banque.connexion.Connexion;
import com.douwe.banque.data.Account;
import com.douwe.banque.data.AccountType;
import com.douwe.banque.data.Operation;
import com.douwe.banque.gui.common.UserInfo;
import com.douwe.banque.model.IServiceAccount;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Maurice PENKA
 */
public class ServiceAccountImpl implements IServiceAccount {

    
    /**
     * methode permetant renvoyer un compte en fonction de son identifiant
     *
     * @param id : identifiant du compte
     * @return un compte
     * @throws SQLException
     */
    public Account findAccountByIdCustumer(int id) throws SQLException {
        Account a = new Account();
        if (id > 0) {
            PreparedStatement pst = null;
            ResultSet rs = null;
            try {
                pst = Connexion.getCon().prepareStatement("select account.*, customer.name  from account, customer where account.id = ? and account.customer_id = customer.id");
                pst.setInt(1, id);
                rs = pst.executeQuery();
                if (rs.next()) {
                    a.setNumeroCompte(rs.getString("accountNumber"));
                    a.setType(rs.getInt("type"));
                    a.setSolde(rs.getDouble("balance"));
                    a.setDateCreation(rs.getDate("dateCreation"));
                    a.setNumClient(rs.getInt("customer_id"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceAccountImpl.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                rs.close();
                pst.close();
            }
        }
        return a;
    }

    /**
     * methode permettant de crediter un compte donc prend en parametre le
     * numero de compte et le montant a credité
     *
     * @param numCompte : numero de compte
     * @param montant : montant à crediter
     */
    public void crediterCompte(String numCompte, double montant) {
        PreparedStatement pst1 = null;
        ResultSet rs = null;
        PreparedStatement pst2 = null;
        PreparedStatement pst3 = null;
        try {
            pst1 = Connexion.getCon().prepareStatement("select * from account where accountNumber = ?");
            pst1.setString(1, numCompte);
            rs = pst1.executeQuery();
            if (rs.next()) {
                Connexion.getCon().setAutoCommit(false);
                pst2 = Connexion.getCon().prepareStatement("update account set balance = balance + ? where accountNumber = ?");
                pst3 = Connexion.getCon().prepareStatement("insert into operations(operationType, dateOperation,description,account_id, user_id) values (?,?,?,?,?)");
                pst2.setDouble(1, montant);
                pst2.setString(2, numCompte);
                pst2.executeUpdate();
                pst3.setInt(1, Operation.debit.ordinal());
                pst3.setDate(2, new Date(new java.util.Date().getTime()));
                pst3.setString(3, "Credit du compte " + numCompte + " de " + montant);
                pst3.setInt(4, rs.getInt("id"));
                pst3.setInt(5, UserInfo.getUserId());
                pst3.executeUpdate();
                Connexion.getCon().commit();
                JOptionPane.showMessageDialog(null, "Opération réalisée avec succès");
            } else {
                JOptionPane.showMessageDialog(null, "Le compte specifier n'existe pas");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Impossible de procéder à l'opération de crédit de compte");
            Logger.getLogger(ServiceAccountImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst1.close();
                pst2.close();
                pst3.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceAccountImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    
     /**
     * methode permettant de debiter un compte donc prend en parametre le numero
     * de compte et le montant a debité
     *
     * @param numcompte : numero de compte
     * @param montant : montant a debité
     */
    public void debiterCompte(String numcompte, double montant) {
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;
        PreparedStatement pst3 = null;
        ResultSet rs = null;
        try {
            pst1 = Connexion.getCon().prepareStatement("select * from account where accountNumber = ?");
            pst1.setString(1, numcompte);
            rs = pst1.executeQuery();
            if (rs.next()) {
                if (rs.getDouble("balance") <= montant) {
                    JOptionPane.showMessageDialog(null, "Le solde du compte n'est pas suffisant pour cette opération");
                } else {
                    Connexion.getCon().setAutoCommit(false);
                    pst2 = Connexion.getCon().prepareStatement("update account set balance = balance - ? where accountNumber = ?");
                    pst3 = Connexion.getCon().prepareStatement("insert into operations(operationType, dateOperation,description,account_id, user_id) values (?,?,?,?,?)");
                    pst2.setDouble(1, montant);
                    pst2.setString(2, numcompte);
                    pst2.executeUpdate();
                    pst3.setInt(1, Operation.debit.ordinal());
                    pst3.setDate(2, new Date(new java.util.Date().getTime()));
                    pst3.setString(3, "Debit de " + montant + " du compte " + numcompte);
                    pst3.setInt(4, rs.getInt("id"));
                    pst3.setInt(5, UserInfo.getUserId());
                    pst3.executeUpdate();
                    Connexion.getCon().commit();
                    JOptionPane.showMessageDialog(null, "Opération réalisée avec succès");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Le compte specifier n'existe pas");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Impossible de procéder à l'opération de débit");
            Logger.getLogger(ServiceAccountImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (pst2 != null) {
                    pst2.close();
                }
                if (pst3 != null) {
                    pst2.close();
                }
                rs.close();
                pst1.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceAccountImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
    *  methode renvoyant la liste des comptes avec le nom des titulaires
    * et prend en parametre une requete sql
    * 
    * @param requete : requete sql
    * @return liste des comptes
    * @throws SQLException 
    */
    public List<Account> findAllCompteByCustomer(String requete) throws SQLException {
        List<Account> listCompte = new ArrayList<Account>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            Account a;
            pst = Connexion.getCon().prepareStatement(requete);
            pst.setInt(1, 0);
            rs = pst.executeQuery();
            while (rs.next()) {
                a = new Account();
                a.setNumClient(rs.getInt("id"));
                a.setDateCreation(rs.getDate("dateCreation"));
                a.setNom(rs.getString("name"));
                a.setNumeroCompte(rs.getString("accountNumber"));
                a.setSolde(rs.getDouble("balance"));
                a.setType(rs.getInt("type"));
                listCompte.add(a);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceAccountImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            rs.close();
            pst.close();
        }
        return listCompte;
    }

    
    /**
     * methode permettant de renvoyer la liste des comptes d'un client en
     * prennant en parametre son identifiant
     *
     * @param id : identifiant du client
     * @return la liste des comptes
     * @throws SQLException
     */
    public List<Account> findAllCompteByIdCustomer(int id) throws SQLException {
       List<Account> listCompte = new ArrayList<Account>();

        PreparedStatement pStmt = Connexion.getCon().prepareStatement("select * from account where customer_id=?");
        pStmt.setInt(1, id);
        ResultSet rs = pStmt.executeQuery();
        Account a;
        while (rs.next()) {
            a = new Account();
            a.setNumeroCompte(rs.getString("accountNumber"));
            a.setNumClient(rs.getInt("customer_id"));
            a.setSolde(rs.getDouble("balance"));
            a.setDateCreation(rs.getDate("dateCreation"));
            a.setType(rs.getInt("type"));
            listCompte.add(a);
        }
        rs.close();
        pStmt.close();
        return listCompte;
    }

    
    /**
     * methode permettant de retourner la liste des numero de compte de client
     * identifier par son identifiant pris en parametre
     *
     * @param id : identifiant d'un client
     * @return la liste de numero de compte d'un client
     */
    public List<String> findNumCompteByIdCustomer(int id) throws SQLException {
        List<String> ListNumCompte = new ArrayList<String>();
        PreparedStatement st2 = Connexion.getCon().prepareStatement("select accountNumber from account where customer_id=?");
        st2.setInt(1, id);
        ResultSet rs2 = st2.executeQuery();
        while (rs2.next()) {
            ListNumCompte.add(rs2.getString("accountNumber"));
        }
        rs2.close();
        st2.close();
        return ListNumCompte;
    }

    
     /**
     * methode permettant d'apporter les modification dans un compte et prennant
     * en parametre identifiant du compte le numero de compte et le type de
     * compte
     *
     * @param id : identifiant du compte
     * @param numCompte : numero de compte
     * @param type : type de compte
     */
    public void modifierCompte(int id, String numCompte, AccountType type) {
        PreparedStatement pst = null;
        try {
            pst = Connexion.getCon().prepareStatement("update account set type=? , accountNumber=? where id =?");
            pst.setInt(1, type.ordinal());
            pst.setString(2, numCompte);
            pst.setInt(3, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Impossible de mettre à jour le compte");
            Logger.getLogger(ServiceAccountImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceAccountImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    
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
    public void saveCompte(String customer, String number, AccountType type, double balance) {
        PreparedStatement pst = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            pst = Connexion.getCon().prepareStatement("select id from customer where name = ?");
            pst.setString(1, customer);
            rs = pst.executeQuery();
            if (rs.next()) {
                int customer_id = rs.getInt("id");
                st = Connexion.getCon().prepareStatement("insert into account(accountNumber,balance,dateCreation,type, customer_id) values(?,?,?,?,?)");
                st.setString(1, number);
                st.setDouble(2, balance);
                st.setDate(3, new Date(new java.util.Date().getTime()));
                st.setInt(4, type.ordinal());
                st.setInt(5, customer_id);
                st.executeUpdate();
            } else {
                JOptionPane.showMessageDialog(null, "Le client spécifié n'existe pas");
                return;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Impossible d'enregistrer le compte");
            Logger.getLogger(ServiceAccountImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceAccountImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
