package com.douwe.banque.gui;

import com.douwe.banque.data.RoleType;
import com.douwe.banque.gui.admin.ClientPanel;
import com.douwe.banque.gui.admin.ComptePanel;
import com.douwe.banque.gui.admin.NouveauDebitPanel;
import com.douwe.banque.gui.admin.NouveauDepotPanel;
import com.douwe.banque.gui.admin.OperationsPanel;
import com.douwe.banque.gui.admin.UtilisateurPanel;
import com.douwe.banque.gui.client.MesCompteListePanel;
import com.douwe.banque.gui.client.MesOperationsListePanel;
import com.douwe.banque.gui.client.TransfertPanel;
import com.douwe.banque.gui.common.UserInfo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

/**
 *
 * @author Vincent Douwe <douwevincent@yahoo.fr>
 */
public class MainMenuPanel extends JPanel {

    private JPanel container;

    public MainMenuPanel() {
        JXHyperlink mesComptes;
        JXHyperlink mesOperations;
        JXHyperlink tranfer;
        JXHyperlink utilisateurs;
        JXHyperlink comptes;
        JXHyperlink customer;
        JXHyperlink operations;
        JXHyperlink profil;
        JXHyperlink debit;
        JXHyperlink credit;

        setLayout(new BorderLayout());
        mesComptes = new JXHyperlink();
        mesComptes.setText("Mes Comptes");
        mesComptes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    setContenu(new MesCompteListePanel());
                } catch (Exception ex) {
                    Logger.getLogger(MainMenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        mesComptes.setUnclickedColor(Color.blue);
        mesComptes.setClickedColor(Color.blue);
        mesOperations = new JXHyperlink();
        mesOperations.setText("Mes Opérations");
        mesOperations.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    setContenu(new MesOperationsListePanel());
                } catch (Exception ex) {
                    Logger.getLogger(MainMenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        mesOperations.setUnclickedColor(Color.blue);
        mesOperations.setClickedColor(Color.blue);
        tranfer = new JXHyperlink();
        tranfer.setText("Transfert");
        tranfer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    setContenu(new TransfertPanel());
                } catch (Exception ex) {
                    Logger.getLogger(MainMenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        tranfer.setUnclickedColor(Color.blue);
        tranfer.setClickedColor(Color.blue);
        utilisateurs = new JXHyperlink() {
            @Override
            public boolean isVisible() {
                return UserInfo.getRole() == RoleType.admin;
            }
        };
        utilisateurs.setText("Utilisateurs");
        utilisateurs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    setContenu(new UtilisateurPanel(MainMenuPanel.this));
                } catch (SQLException ex) {
                    Logger.getLogger(MainMenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        utilisateurs.setUnclickedColor(Color.blue);
        utilisateurs.setClickedColor(Color.blue);
        comptes = new JXHyperlink();
        comptes.setText("Comptes");
        comptes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    setContenu(new ComptePanel(MainMenuPanel.this));
                } catch (SQLException ex) {
                    Logger.getLogger(MainMenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        comptes.setUnclickedColor(Color.blue);
        comptes.setClickedColor(Color.blue);
        operations = new JXHyperlink() {
            @Override
            public boolean isVisible() {
                return UserInfo.getRole() == RoleType.admin;
            }
        };
        operations.setText("Opérations");
        operations.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    setContenu(new OperationsPanel());
                } catch (SQLException ex) {
                    Logger.getLogger(MainMenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        operations.setUnclickedColor(Color.blue);
        operations.setClickedColor(Color.blue);
        customer = new JXHyperlink();
        customer.setText("Clients");
        customer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    setContenu(new ClientPanel(MainMenuPanel.this));
                } catch (SQLException ex) {
                    Logger.getLogger(MainMenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        customer.setUnclickedColor(Color.blue);
        customer.setClickedColor(Color.blue);
        JXTaskPaneContainer menu = new JXTaskPaneContainer();
        JXTaskPane clientPane = new JXTaskPane() {
            @Override
            public boolean isVisible() {
                return UserInfo.getRole() == RoleType.customer;
            }
        };
        clientPane.setTitle("Espace client");
        clientPane.add(mesComptes);
        clientPane.add(mesOperations);
        clientPane.add(tranfer);
        menu.add(clientPane);
        JXTaskPane employee = new JXTaskPane() {
            @Override
            public boolean isVisible() {
                return UserInfo.getRole() == RoleType.employee;
            }
        };
        employee.setTitle("Espace Employé");
        debit = new JXHyperlink();
        employee.add(debit);
        debit.setText("Débit");
        debit.setUnclickedColor(Color.blue);
        debit.setClickedColor(Color.blue);
        debit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                setContenu(new NouveauDebitPanel(MainMenuPanel.this));
            }
        });
        credit = new JXHyperlink();
        employee.add(credit);
        credit.setText("Crédit");
        credit.setUnclickedColor(Color.blue);
        credit.setClickedColor(Color.blue);
        credit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                setContenu(new NouveauDepotPanel(MainMenuPanel.this));
            }
        });
        menu.add(employee);
        JXTaskPane adminPane = new JXTaskPane() {
            @Override
            public boolean isVisible() {
                return (UserInfo.getRole() == RoleType.admin) || (UserInfo.getRole() == RoleType.employee);
            }
        };
        adminPane.setTitle("Administration");
        adminPane.add(comptes);
        adminPane.add(customer);
        adminPane.add(operations);
        adminPane.add(utilisateurs);
        menu.add(adminPane);
        JXTaskPane profilPane = new JXTaskPane();
        profilPane.setTitle("Profil");
        profil = new JXHyperlink();
        profilPane.add(profil);
        profil.setText("Profil");
        profil.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JOptionPane.showMessageDialog(null, "si j'implémente tout alors que feront mes étudiants?");
            }
        });
        profil.setUnclickedColor(Color.blue);
        profil.setClickedColor(Color.blue);
        menu.add(profilPane);
        add(menu, BorderLayout.BEFORE_LINE_BEGINS);
        container = new JPanel();
        container.setLayout(new BorderLayout());
        container.setBorder(new EmptyBorder(2, 20, 20, 20));
        //container.setBackground(Color.GREEN);
        add(container, BorderLayout.CENTER);
    }

    public void setContenu(JPanel pan) {
        container.removeAll();
        container.add(BorderLayout.CENTER, pan);
        container.validate();
    }
}
