/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.douwe.banque.modelImplement;

import com.douwe.banque.connexion.Connexion;
import com.douwe.banque.data.RoleType;
import com.douwe.banque.data.User;
import com.douwe.banque.gui.common.UserInfo;
import com.douwe.banque.model.IServiceUser;
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
public class ServiceUserImpl implements IServiceUser{

   /**
     * methode permettant supprimer les utilisateurs et retoune le nombre de
     * lignes supprimés
     *
     * @param id : identifiant utilisateur
     * @return le nombre de lignes detectés
     */
    public int deleteUserById(int id) {
        int line = 0;
        PreparedStatement pst = null;
        try {
            pst = Connexion.getCon().prepareStatement("update users set status = ? where id = ?");
            pst.setInt(1, 1);
            pst.setInt(2, id);
            pst.executeUpdate();
            line = pst.executeUpdate();
            if (line > 0) {
                return line;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceUserImpl.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        } finally {
            try {
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceUserImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return line;
    }

   /**
     * methode permettant de renvoyer la liste de tous les utilisateurs prennant
     * en parametre une requete sql
     *
     * @param requete : requete sql
     * @return la liste de tous les utilisateurs
     * @throws SQLException
     */
    public List<User> findAllUserSimple(String requete) throws SQLException {
        List<User> listUser = new ArrayList<User>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        User u;
        try {
            pst = Connexion.getCon().prepareStatement(requete);
            pst.setInt(1, 0);
            rs = pst.executeQuery();
            while (rs.next()) {
                u = new User();
                u.setId(rs.getInt("id"));
                u.setPassWord(rs.getString("passwd"));
                u.setRole(RoleType.values()[rs.getInt("role")]);
                u.setUserName(rs.getString("username"));
                listUser.add(u);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceUserImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            rs.close();
            pst.close();
        }
        return listUser;
    }

    /**
     * methode permettant de reinitialiser le mot de passe
     *
     * @param id : identifiant utilisateur
     */
    public void reinitialiserPassWord(int id) {
        PreparedStatement pst = null;
        try {
            pst = Connexion.getCon().prepareStatement("update users set passwd = ? where id = ?");
            pst.setString(1, "admin");
            pst.setInt(2, id);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Le mot de passe est reinitialisé à 'admin'");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Impossible de reinitialiser le mot de passe");
            Logger.getLogger(ServiceUserImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceUserImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


    /**
     * methode permettant d'enregistrer un utilisateur pris en parametre
     *
     * @param u parametre representant un utilisateur
     */
    public void saveUtilisateur(User u) {
        PreparedStatement pst = null;
        try {
            pst = Connexion.getCon().prepareStatement("insert into users (username,passwd,role) values(?,?,?)");
            pst.setString(1, u.getUserName().toLowerCase());
            pst.setString(2, u.getPassWord());
            pst.setInt(3, u.getRole().ordinal());
            pst.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Impossible de créer le compte");
            Logger.getLogger(ServiceUserImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceUserImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

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
    public int authentification(String login, String password) {
        PreparedStatement pst = null;
        PreparedStatement pp = null;
        ResultSet rs = null;
        ResultSet dd = null;
        try {
            pst = Connexion.getCon().prepareStatement("select * from users where username = ? and status = ?");
            pst.setString(1, login.toLowerCase());
            pst.setInt(2, 0);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (password.equals(rs.getString("passwd"))) {
                    UserInfo.setUsername(login);
                    UserInfo.setRole(RoleType.values()[rs.getInt("role")]);
                    UserInfo.setUserId(rs.getInt("id"));
                    UserInfo.setLogged(true);
                    if (UserInfo.getRole().equals(RoleType.customer)) {
                        pp = Connexion.getCon().prepareStatement("select customer.id from users, customer where users.id = customer.user_id and username = ?");
                        pp.setString(1, login.toLowerCase());
                        dd = pp.executeQuery();
                        if (dd.next()) {
                            UserInfo.setCustomerId(dd.getInt(1));
                        }
                    }
                    return 1;
                } else {
                    return 2;
                }
            } else {
                return 2;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceUserImpl.class.getName()).log(Level.SEVERE, null, ex);
            return 3;
        } finally {
            try {
                if (dd != null) {
                    dd.close();
                }
                if (pp != null) {
                    pp.close();
                }
                rs.close();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiceUserImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
