package com.douwe.banque.gui.admin;

import com.douwe.banque.data.Account;
import com.douwe.banque.data.AccountType;
import com.douwe.banque.gui.MainMenuPanel;
import com.douwe.banque.model.IServiceAccount;
import com.douwe.banque.model.IServiceOperation;
import com.douwe.banque.modelImplement.ServiceAccountImpl;
import com.douwe.banque.modelImplement.ServiceOperationImpl;
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
public class ComptePanel extends JPanel {

    private JTable compteTable;
    private DefaultTableModel tableModel;
    private JTextField nameText;
    private JTextField numberText;
    private JComboBox<AccountType> type;
    private MainMenuPanel parent;
    IServiceAccount model = new ServiceAccountImpl();
    IServiceOperation modelb = new ServiceOperationImpl();

    public ComptePanel(MainMenuPanel parentFrame) throws SQLException {
        JButton nouveauBtn;
        JButton supprimerBtn;
        JButton modifierBtn;
        JButton filtreBtn;

        this.parent = parentFrame;
        setLayout(new BorderLayout());
        JLabel lbl;
        lbl = new JLabel("LA LISTE DES COMPTES DE MA BANQUE POPULAIRE");
        nameText = new JTextField();
        numberText = new JTextField();
        type = new JComboBox<AccountType>();
        JPanel contenu = new JPanel();
        JPanel haut = new JPanel();
        JPanel bas = new JPanel();
        JPanel filtrePanel = new JPanel();
        bas.setLayout(new FlowLayout());
        nouveauBtn = new JButton("Nouveau");
        supprimerBtn = new JButton("Supprimer");
        modifierBtn = new JButton("Modifier");
        filtreBtn = new JButton("Filtrer");
        haut.setLayout(new FlowLayout(FlowLayout.CENTER));
        haut.add(lbl);
        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        add(BorderLayout.BEFORE_FIRST_LINE, haut);
        contenu.setLayout(new BorderLayout());
        filtreBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String client = nameText.getText();
                String accountNumber = numberText.getText();
                AccountType ty = (AccountType) type.getSelectedItem();
                StringBuilder query = new StringBuilder("select account.*, customer.name from account, customer where customer.id = account.customer_id and account.status = ?");
                if ((client != null) && !("".equals(client))) {
                    query.append("and name like '%");
                    query.append(client);
                    query.append("%'");
                }
                if ((accountNumber != null) && !("".equals(accountNumber))) {
                    query.append("and accountNumber like '%");
                    query.append(client);
                    query.append("%'");
                }
                if (ty != null) {
                    query.append("and type = ");
                    query.append(ty.ordinal());
                }
                tableModel.setRowCount(0);
                List<Account> listCompte;
                try {
                    listCompte = model.findAllCompteByCustomer(query.toString());
                    for (int i = 0; i < listCompte.size(); i++) {
                        tableModel.addRow(new Object[]{listCompte.get(i).getNumClient(),
                                    listCompte.get(i).getNumeroCompte(),
                                    listCompte.get(i).getSolde(),
                                    listCompte.get(i).getDateCreation(),
                                    listCompte.get(i).getType() == 0 ? AccountType.deposit.toString() : AccountType.saving.toString(),
                                    listCompte.get(i).getNom()});
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ComptePanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        nouveauBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                parent.setContenu(new NouveauComptePanel(parent));
            }
        });
        modifierBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selected = compteTable.getSelectedRow();
                if (selected >= 0) {
                    try {
                        parent.setContenu(new NouveauComptePanel(parent, (Integer) tableModel.getValueAt(selected, 0)));
                    } catch (SQLException ex) {
                        Logger.getLogger(ComptePanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Aucun compte selectionné");
                }
            }
        });
        supprimerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int selected = compteTable.getSelectedRow();
                if (selected >= 0) {
                    int resultat = modelb.saveTraitementDeleteCompte((String) tableModel.getValueAt(selected, 1), (Integer) tableModel.getValueAt(selected, 0));
                    if (resultat == 1) {
                        tableModel.removeRow(selected);
                    } else if (resultat == 2) {
                        JOptionPane.showMessageDialog(null, "Impossible de supprimer ce compte");
                    } else {
                        JOptionPane.showMessageDialog(null, "Impossible de fermer la ressource");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Aucune ligne de la table n'est selectionnee");
                }

            }
        });
        bas.add(nouveauBtn);
        bas.add(modifierBtn);
        bas.add(supprimerBtn);
        filtrePanel.setLayout(new FlowLayout());
        filtrePanel.add(new JLabel("Nom Client"));
        filtrePanel.add(nameText);
        nameText.setPreferredSize(new Dimension(100, 25));
        filtrePanel.add(new JLabel("Numero Compte"));
        filtrePanel.add(numberText);
        numberText.setPreferredSize(new Dimension(100, 25));
        filtrePanel.add(new JLabel("Type Compte"));
        filtrePanel.add(type);
        type.setPreferredSize(new Dimension(100, 25));
        type.addItem(null);
        type.addItem(AccountType.deposit);
        type.addItem(AccountType.saving);
        filtrePanel.add(filtreBtn);
        contenu.add(BorderLayout.AFTER_LAST_LINE, bas);
        contenu.add(BorderLayout.BEFORE_FIRST_LINE, filtrePanel);
        tableModel = new DefaultTableModel(new Object[]{"id", "Numero Compte", "Solde", "Date création", "Type", "Client"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        compteTable = new JTable(tableModel);
        compteTable.removeColumn(compteTable.getColumnModel().getColumn(0));
        contenu.add(BorderLayout.CENTER, new JScrollPane(compteTable));
        add(BorderLayout.CENTER, contenu);
        String requet = "select account.*, customer.name from account, customer where account.customer_id = customer.id and account.status = ?";
        List<Account> listCompte = model.findAllCompteByCustomer(requet);
        for (int i = 0; i < listCompte.size(); i++) {
            tableModel.addRow(new Object[]{listCompte.get(i).getNumClient(),
                        listCompte.get(i).getNumeroCompte(),
                        listCompte.get(i).getSolde(),
                        listCompte.get(i).getDateCreation(),
                        listCompte.get(i).getType() == 0 ? AccountType.deposit.toString() : AccountType.saving.toString(),
                        listCompte.get(i).getNom()});
        }

    }
}
