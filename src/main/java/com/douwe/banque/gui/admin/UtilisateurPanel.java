package com.douwe.banque.gui.admin;

import com.douwe.banque.data.RoleType;
import com.douwe.banque.data.User;
import com.douwe.banque.gui.MainMenuPanel;
import com.douwe.banque.model.IServiceUser;
import com.douwe.banque.modelImplement.ServiceUserImpl;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Vincent Douwe<douwevincent@yahoo.fr>
 */
public class UtilisateurPanel extends JPanel {

    private JTextField loginText;
    private JComboBox<RoleType> role;
    private JTable utilisateurTable;
    private DefaultTableModel tableModel;
    private MainMenuPanel parent;
    IServiceUser model = new ServiceUserImpl();

    public UtilisateurPanel(MainMenuPanel parentFrame) throws SQLException {
        JButton nouveauBtn;
        JButton supprimerBtn;
        JButton initialiserPasswdBtn;
        JButton filtreBtn;

        setLayout(new BorderLayout());
        this.parent = parentFrame;
        JLabel lbl = new JLabel("LA LISTE DES UTILISATEURS DE MA BANQUE POPULAIRE");
        JPanel haut = new JPanel();
        JPanel contenu = new JPanel();
        JPanel bas = new JPanel();
        JPanel filtrePanel = new JPanel();
        nouveauBtn = new JButton("Nouveau");
        supprimerBtn = new JButton("Supprimer");
        initialiserPasswdBtn = new JButton("Reinitialiser Mot de Passe");
        filtreBtn = new JButton("Filtrer");
        loginText = new JTextField();
        role = new JComboBox<RoleType>();
        haut.setLayout(new FlowLayout(FlowLayout.CENTER));
        haut.add(lbl);
        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        add(BorderLayout.BEFORE_FIRST_LINE, haut);
        contenu.setLayout(new BorderLayout());
        bas.setLayout(new FlowLayout());
        filtreBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String username = loginText.getText();
                RoleType roleN = (RoleType) role.getSelectedItem();
                StringBuilder query = new StringBuilder("select * from users where status = ?");
                if ((username != null) && !("".equals(username))) {
                    query.append("and username like '%");
                    query.append(username);
                    query.append("%'");
                }
                if (roleN != null) {
                    query.append("and role = ");
                    query.append(roleN.ordinal());
                }
                tableModel.setRowCount(0);
                try {
                    List<User> listUsers = model.findAllUserSimple(query.toString());
                    for (int i = 0; i < listUsers.size(); i++) {
                        tableModel.addRow(new Object[]{listUsers.get(i).getId(),
                                    listUsers.get(i).getUserName(),
                                    listUsers.get(i).getRole()});
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(UtilisateurPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        nouveauBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                parent.setContenu(new NouveauUtilisateurPanel(parent));
            }
        });
        initialiserPasswdBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selected = utilisateurTable.getSelectedRow();
                if (selected >= 0) {
                    model.reinitialiserPassWord((Integer) tableModel.getValueAt(selected, 0));
                } else {
                    JOptionPane.showMessageDialog(null, "Aucun utilisateur n'est selectionné");
                }
            }
        });
        supprimerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selected = utilisateurTable.getSelectedRow();
                if (selected >= 0) {
                    if (model.deleteUserById((Integer) tableModel.getValueAt(selected, 0)) > 0) {
                        tableModel.removeRow(selected);
                    } else {
                        JOptionPane.showMessageDialog(null, "Impossible de supprimer cet utilisateur");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Aucun utilisateur n'est selectionné");
                }
            }
        });
        bas.add(nouveauBtn);
        bas.add(initialiserPasswdBtn);
        bas.add(supprimerBtn);
        filtrePanel.setLayout(new FlowLayout());
        filtrePanel.add(new JLabel("Nom Client"));
        filtrePanel.add(loginText);
        loginText.setPreferredSize(new Dimension(100, 25));
        filtrePanel.add(new JLabel("Type Compte"));
        filtrePanel.add(role);
        role.setPreferredSize(new Dimension(100, 25));
        role.addItem(null);
        role.addItem(RoleType.customer);
        role.addItem(RoleType.employee);
        role.addItem(RoleType.admin);
        filtrePanel.add(filtreBtn);
        contenu.add(BorderLayout.AFTER_LAST_LINE, bas);
        contenu.add(BorderLayout.BEFORE_FIRST_LINE, filtrePanel);
        tableModel = new DefaultTableModel(new Object[]{"id", "Login", "Role"}, 0);
        utilisateurTable = new JTable(tableModel);
        utilisateurTable.removeColumn(utilisateurTable.getColumnModel().getColumn(0));
        contenu.add(BorderLayout.CENTER, new JScrollPane(utilisateurTable));
        add(BorderLayout.CENTER, contenu);
        String requete = "select * from users where status = ?";
        List<User> listUsers = model.findAllUserSimple(requete);
        for (int i = 0; i < listUsers.size(); i++) {
            tableModel.addRow(new Object[]{listUsers.get(i).getId(),
                        listUsers.get(i).getUserName(),
                        listUsers.get(i).getRole()});
        }
    }
}