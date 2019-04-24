/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.douwe.banque.modelImplement;

import com.douwe.banque.connexion.Connexion;
import com.douwe.banque.data.Customer;
import com.douwe.banque.data.RoleType;
import com.douwe.banque.model.IServiceCustomer;
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
public class ServiceCustomerImpl implements IServiceCustomer{

    /**
     * methode retournant le nombre de lignes detectés
     *
     * @param id identifiant du client
     * @return le nombre de lignes
     */
    public int modifierCustomerById(int id) {
        PreparedStatement psmt = null;
        int line = 0;
        try {
            psmt = Connexion.getCon().prepareStatement("update customer set status = ? where id = ?");
            psmt.setInt(1, 1);
            psmt.setInt(2, id);
            line = psmt.executeUpdate();
            if (line > 0) {
                return line;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceCustomerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                psmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceCustomerImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return line;
    }


    /**
     * methode renvoyant la liste de tous les clients
     *
     * @return la liste de tous les clients
     */
    public List<Customer> findAllCustomer() throws SQLException {
        List<Customer> listClient = new ArrayList<Customer>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Customer c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from customer where status = ?");
            pst.setInt(1, 0);
            rs = pst.executeQuery();
            while (rs.next()) {
                c = new Customer();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setEmailAdresse(rs.getString("emailAddress"));
                c.setPhoneNumber(rs.getString("phoneNumber"));
                listClient.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceCustomerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            rs.close();
            pst.close();
        }
        return listClient;
    }

    /**
     * methode renvoyant un client a parti de son identifiant
     *
     * @param id : identifiant du client
     * @return un client
     * @throws SQLException
     */
    public Customer findCustomerById(int id) throws SQLException {
        Customer c = new Customer();
        if (id > 0) {
            ResultSet rs = null;
            PreparedStatement ps = null;
            try {
                ps = Connexion.getCon().prepareStatement("select * from customer where id = ?");
                ps.setInt(1, id);
                rs = ps.executeQuery();
                if (rs.next()) {
                    c.setName(rs.getString("name"));
                    c.setEmailAdresse(rs.getString("emailAddress"));
                    c.setPhoneNumber(rs.getString("phoneNumber"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceCustomerImpl.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                rs.close();
                ps.close();
            }
        }
        return c;
    }

    /**
     * methode permettant de filter la liste des clients en fonction du nom pris
     * en parametre
     *
     * @param nom : nom des clients
     * @return la liste des clients
     */
    public List<Customer> findCustomerByName(String nom) {
        List<Customer> listClient = new ArrayList<Customer>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Customer c;
        try {
            pst = Connexion.getCon().prepareStatement("select * from customer where status = ? and name like ?");
            pst.setInt(1, 0);
            pst.setString(2, "%" + nom + "%");
            rs = pst.executeQuery();

            while (rs.next()) {
                c = new Customer();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setEmailAdresse(rs.getString("emailAddress"));
                c.setPhoneNumber("phoneNumber");
                listClient.add(c);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Impossible de filtrer vos données");
            Logger.getLogger(ServiceCustomerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceCustomerImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listClient;
    }


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
    public void modifierClient(String name, String email, String phone, int id) {
        PreparedStatement pst = null;
        try {
            pst = Connexion.getCon().prepareStatement("update customer set name =?, emailAddress=?, phoneNumber=? where id = ?");
            pst.setString(1, name);
            pst.setString(2, email);
            pst.setString(3, phone);
            pst.setInt(4, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Impossible de mettre a jour votre compte");
            Logger.getLogger(ServiceCustomerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceCustomerImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    
    /**
     * methode permettant d'enregistrer un client pris en parametre
     *
     * @param c parametre representant un client
     */
    public void saveClient(Customer c) {
        PreparedStatement pst = null;
        PreparedStatement st = null;
        ResultSet rrr = null;
        try {
            Connexion.getCon().setAutoCommit(false);
            pst = Connexion.getCon().prepareStatement("insert into customer(name,emailAddress, phoneNumber, user_id) values (?,?,?,?)");
            st = Connexion.getCon().prepareStatement("insert into users(username, passwd, role) values (?,?,?)");
            String val = c.getName().replaceAll(" ", "").toLowerCase();
            st.setString(1, val);
            st.setString(2, "admin");
            st.setInt(3, RoleType.customer.ordinal());
            st.executeUpdate();
            rrr = st.getGeneratedKeys();
            if (rrr.next()) {
                pst.setString(1, c.getName());
                pst.setString(2, c.getEmailAdresse());
                pst.setString(3, c.getPhoneNumber());
                pst.setInt(4, rrr.getInt(1));
                pst.executeUpdate();
                Connexion.getCon().commit();
            } else {
                Connexion.getCon().rollback();
            }
            JOptionPane.showMessageDialog(null, "Un compte avec login " + val + " et mot de passe 'admin' a été créé");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Impossible de créer le compte");
            Logger.getLogger(ServiceCustomerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rrr.close();
                st.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceCustomerImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
