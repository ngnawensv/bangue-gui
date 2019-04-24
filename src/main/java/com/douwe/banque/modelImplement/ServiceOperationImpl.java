/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.douwe.banque.modelImplement;

import com.douwe.banque.connexion.Connexion;
import com.douwe.banque.data.Operation;
import com.douwe.banque.data.Traitement;
import com.douwe.banque.gui.common.UserInfo;
import com.douwe.banque.model.IServiceOperation;
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
public class ServiceOperationImpl implements IServiceOperation{

    /**
     * methode permetant de renvoyer la liste des operations en fonction de la
     * requete pris en parametre
     *
     * @param requete
     * @return la liste des operations
     */
    public List<Traitement> findAllOperation(String requete) {
        List<Traitement> listeOperation = new ArrayList<Traitement>();
        PreparedStatement pStat = null;
        ResultSet rs = null;
        Traitement t;
        try {
            pStat = Connexion.getCon().prepareStatement(requete);
            rs = pStat.executeQuery();
            while (rs.next()) {
                t = new Traitement();
                t.setOperation(rs.getInt("operationType"));
                t.setNumCompte(rs.getString("accountNumber"));
                t.setDate(rs.getDate("dateOperation"));
                t.setDescription(rs.getString("description"));
                t.setUserName(rs.getString("username"));
                listeOperation.add(t);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Impossible de filtrer vos données");
            Logger.getLogger(ServiceOperationImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pStat.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceOperationImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listeOperation;
    }


    /**
     * methode permettant d'effectuer un transfert de montant d'un compte source
     * vers un compte destination et retourne 1 si tous se passe bien 2 si le
     * compte destination n'existe pas 3 si le compte source ne dispose pas un
     * solde suffisant 4 si le montant pris en entré n'est pas un nombre.
     *
     * @param source : compte source
     * @param destination : compte destination
     * @param montant : montant à transferé
     * @return 1 si tous se passe bien 2 si le compte destination n'existe pas 3
     * si le compte source ne dispose pas un solde suffisant 4 si le montant
     * pris en entré n'est pas
     */
    public int transfert(String source, String destination, String montant) {
        int resultat = 1;
        PreparedStatement bst = null;
        ResultSet rss = null;
        try {
            double value = Double.valueOf(montant);
            bst = Connexion.getCon().prepareStatement("select balance from account where accountNumber = ?");
            bst.setString(1, source);
            rss = bst.executeQuery();
            if (rss.next()) {
                double current = rss.getDouble("balance");
                if (current >= value) {
                    PreparedStatement outst = Connexion.getCon().prepareStatement("select * from account where accountNumber = ?");
                    outst.setString(1, destination);
                    rss = outst.executeQuery();
                    if (rss.next()) {
                        Connexion.getCon().setAutoCommit(false);
                        PreparedStatement crst = Connexion.getCon().prepareStatement("update account set balance = balance + ? where accountNumber = ?");
                        PreparedStatement dtst = Connexion.getCon().prepareStatement("update account set balance = balance - ? where accountNumber = ?");
                        PreparedStatement op1st = Connexion.getCon().prepareStatement("insert into operations (operationType, dateOperation,description,account_id,user_id) values(?,?,?,?,?)");
                        crst.setDouble(1, value);
                        crst.setString(2, destination);
                        crst.executeUpdate();
                        dtst.setDouble(1, value);
                        dtst.setString(2, source);
                        dtst.executeUpdate();
                        op1st.setInt(1, Operation.credit.ordinal());
                        op1st.setDate(2, new java.sql.Date(new java.util.Date().getTime()));
                        op1st.setString(3, "Credit du compte " + destination + " du montant " + montant);
                        op1st.setInt(4, rss.getInt("id"));
                        op1st.setInt(5, UserInfo.getUserId());
                        op1st.executeUpdate();
                        op1st.setInt(1, Operation.debit.ordinal());
                        op1st.setDate(2, new java.sql.Date(new java.util.Date().getTime()));
                        op1st.setString(3, "Debit du compte " + source + " du montant " + montant);
                        op1st.setInt(4, rss.getInt("id"));
                        op1st.setInt(5, UserInfo.getUserId());
                        op1st.executeUpdate();
                        Connexion.getCon().commit();
                        crst.close();
                        dtst.close();
                        op1st.close();
                        resultat = 1;
                    } else {
                        resultat = 2;
                    }
                    outst.close();
                } else {
                    resultat = 3;
                }
            }
        } catch (NumberFormatException ps) {
            Logger.getLogger(ServiceOperationImpl.class.getName()).log(Level.SEVERE, null, ps);
            resultat = 4;
        } catch (SQLException ex) {
            resultat = 5;
            Logger.getLogger(ServiceOperationImpl.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Connexion.getCon().rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(ServiceOperationImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                rss.close();
                bst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceOperationImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return resultat;
    }

    /**
     * methode permettant d'enregistrer le operations de 
     * suppression de comptes bancaire
     * 
     * @param numeroCompte: numero de compte
     * @param id identifiant du compte
     * @return 1 si tous se passe bien 2 si Impossible de supprimer ce compte
     * et 3 si Impossible de fermer la ressource
     */
    public int saveTraitementDeleteCompte(String numeroCompte, int id) {
        PreparedStatement pst = null;
        int resultat = 1;
        try {
            System.out.println("fdddfdf " + numeroCompte);
            Connexion.getCon().setAutoCommit(false);
            pst = Connexion.getCon().prepareStatement("update account set status = ? where accountNumber = ?");
            pst.setInt(1, 1);
            pst.setString(2, numeroCompte);
            PreparedStatement st = Connexion.getCon().prepareStatement("insert into operations(operationType,dateOperation,description,account_id, user_id) values(?,?,?,?,?)");
            st.setInt(1, Operation.cloture.ordinal());
            st.setDate(2, new Date(new java.util.Date().getTime()));
            st.setString(3, "Cloture du compte " + numeroCompte);
            st.setInt(4, id);
            st.setInt(5, 1);
            st.executeUpdate();
            pst.executeUpdate();
            Connexion.getCon().commit();
            resultat = 1;
        } catch (SQLException ex) {
            Logger.getLogger(ServiceOperationImpl.class.getName()).log(Level.SEVERE, null, ex);
            resultat = 2;
        } finally {
            try {
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceOperationImpl.class.getName()).log(Level.SEVERE, null, ex);
                resultat = 3;
            }
        }
        return resultat;
    }
}
