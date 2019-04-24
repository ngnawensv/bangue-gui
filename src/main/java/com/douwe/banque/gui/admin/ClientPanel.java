package com.douwe.banque.gui.admin;

import com.douwe.banque.data.Customer;
import com.douwe.banque.gui.MainMenuPanel;
import com.douwe.banque.model.IServiceCustomer;
import com.douwe.banque.modelImplement.ServiceCustomerImpl;
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
public class ClientPanel extends JPanel {

    private JTable clientTable;
    private DefaultTableModel tableModel;
    private JTextField nameText;
    private MainMenuPanel parent;
    IServiceCustomer model = new ServiceCustomerImpl();

    public ClientPanel(MainMenuPanel parentFrame) throws SQLException {
        JButton nouveauBtn;
        JButton supprimerBtn;
        JButton modifierBtn;
        JButton filtreBtn;


        setLayout(new BorderLayout());
        this.parent = parentFrame;
        JLabel lbl;
        JPanel bas = new JPanel();
        JPanel haut = new JPanel();
        JPanel filtrePanel = new JPanel();
        nameText = new JTextField();
        nouveauBtn = new JButton("Nouveau");
        supprimerBtn = new JButton("Supprimer");
        modifierBtn = new JButton("Modifier");
        filtreBtn = new JButton("Filtrer");
        JPanel contenu = new JPanel();
        lbl = new JLabel("LA LISTE DES CLIENTS DE MA BANQUE POPULAIRE");
        haut.setLayout(new FlowLayout(FlowLayout.CENTER));
        haut.add(lbl);
        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        add(BorderLayout.BEFORE_FIRST_LINE, haut);
        contenu.setLayout(new BorderLayout());
        bas.setLayout(new FlowLayout());
        filtreBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String name = nameText.getText();
                tableModel.setRowCount(0);
                List<Customer> listClient = model.findCustomerByName(name);
                for (int i = 0; i < listClient.size(); i++) {
                    tableModel.addRow(new Object[]{listClient.get(i).getId(),
                                listClient.get(i).getName(),
                                listClient.get(i).getEmailAdresse(),
                                listClient.get(i).getPhoneNumber()
                            });
                }
            }
        });
        nouveauBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                parent.setContenu(new NouveauClientPanel(parent));
            }
        });
        modifierBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selected = clientTable.getSelectedRow();
                if (selected >= 0) {
                    try {
                        parent.setContenu(new NouveauClientPanel(parent, (Integer) tableModel.getValueAt(selected, 0)));
                    } catch (SQLException ex) {
                        Logger.getLogger(ClientPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Aucun client n'est selectionné");
                }
            }
        });
        supprimerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selected = clientTable.getSelectedRow();
                if (selected >= 0) {
                    if (model.modifierCustomerById((Integer) tableModel.getValueAt(selected, 0)) > 0) {
                        tableModel.removeRow(selected);
                    } else {
                        JOptionPane.showMessageDialog(null, "Erreur lors de la suppression du client " + tableModel.getValueAt(selected, 1));
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Aucune donnée n'est selectionnée");
                }
            }
        });
        bas.add(nouveauBtn);
        bas.add(modifierBtn);
        bas.add(supprimerBtn);
        filtrePanel.setLayout(new FlowLayout());
        filtrePanel.add(new JLabel("Nom"));
        filtrePanel.add(nameText);
        nameText.setPreferredSize(new Dimension(100, 25));
        filtrePanel.add(filtreBtn);
        contenu.add(BorderLayout.AFTER_LAST_LINE, bas);
        contenu.add(BorderLayout.BEFORE_FIRST_LINE, filtrePanel);
        tableModel = new DefaultTableModel(new Object[]{"id", "Nom", "Adresse Email", "Téléphone"}, 0);
        clientTable = new JTable(tableModel);
        clientTable.removeColumn(clientTable.getColumnModel().getColumn(0));
        contenu.add(BorderLayout.CENTER, new JScrollPane(clientTable));
        add(BorderLayout.CENTER, contenu);
        List<Customer> listClient = model.findAllCustomer();
        for (int i = 0; i < listClient.size(); i++) {
            tableModel.addRow(new Object[]{listClient.get(i).getId(),
                        listClient.get(i).getName(),
                        listClient.get(i).getEmailAdresse(),
                        listClient.get(i).getPhoneNumber()
                    });
        }
    }
}
