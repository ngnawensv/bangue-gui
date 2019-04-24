package com.douwe.banque.gui.client;

import com.douwe.banque.data.Operation;
import com.douwe.banque.data.Traitement;
import com.douwe.banque.gui.common.UserInfo;
import com.douwe.banque.model.IServiceAccount;
import com.douwe.banque.model.IServiceOperation;
import com.douwe.banque.modelImplement.ServiceAccountImpl;
import com.douwe.banque.modelImplement.ServiceOperationImpl;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXDatePicker;

/**
 *
 * @author Vincent Douwe<douwevincent@yahoo.fr>
 */
public class MesOperationsListePanel extends JPanel {

    private JComboBox<String> comptes;
    private JComboBox<String> operations;
    private JXDatePicker startDate;
    private JXDatePicker endDate;
    private DefaultTableModel tableModel;
    IServiceOperation model = new ServiceOperationImpl();
    IServiceAccount modelbd = new ServiceAccountImpl();

    public MesOperationsListePanel() throws Exception {
        JTable operationTable;
        JButton filtreBtn;
        setLayout(new BorderLayout());
        Label lbl = new Label("LA LISTE DE MES OPERATIONS");
        JPanel hautPanel = new JPanel(new GridLayout(2, 1));
        JPanel pan = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel filtrePanel = new JPanel();
        filtreBtn = new JButton("Filtrer");
        comptes = new JComboBox<String>();
        operations = new JComboBox<String>();
        startDate = new JXDatePicker();
        endDate = new JXDatePicker();
        pan.add(lbl);
        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        hautPanel.add(pan);
        filtrePanel.setLayout(new FlowLayout());
        operations.addItem("");
        operations.addItem(Operation.credit.toString());
        operations.addItem(Operation.debit.toString());
        operations.addItem(Operation.transfer.toString());
        operations.addItem(Operation.cloture.toString());
        filtrePanel.add(new JLabel("Compte"));
        filtrePanel.add(comptes);
        filtrePanel.add(new JLabel("Opération"));
        filtrePanel.add(operations);
        filtrePanel.add(new JLabel("Date Début"));
        filtrePanel.add(startDate);
        filtrePanel.add(new JLabel("Date Fin"));
        filtrePanel.add(endDate);
        filtrePanel.add(filtreBtn);
        hautPanel.add(filtrePanel);
        tableModel = new DefaultTableModel(new Object[]{"Opération", "Compte", "Date", "Utilisateur", "Description"}, 0);
        operationTable = new JTable(tableModel);
        add(BorderLayout.BEFORE_FIRST_LINE, hautPanel);
        add(BorderLayout.CENTER, new JScrollPane(operationTable));
        filtreBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String selectedCompte = (String) comptes.getSelectedItem();
                String selectedOperation = (String) operations.getSelectedItem();
                Date debut = startDate.getDate();
                Date fin = endDate.getDate();
                StringBuilder builder = new StringBuilder("select operations.*, account.accountNumber, users.username from operations, account, users where operations.account_id=account.id and operations.user_id=users.id");
                if ((selectedCompte != null) && !("".equals(selectedCompte))) {
                    builder.append(" and accountNumber = '");
                    builder.append(selectedCompte);
                    builder.append("'");
                }
                if ((selectedOperation != null) && !("".equals(selectedOperation))) {
                    System.out.println("ddddd  ddd dd d " + selectedOperation);
                    int index = Operation.valueOf(selectedOperation).ordinal();
                    builder.append(" and operationType = ");
                    builder.append(index);
                }
                if (debut != null) {
                    builder.append(" and dateOperation >= '");
                    builder.append(debut.getTime());
                    builder.append("'");
                }
                if (fin != null) {
                    builder.append(" and dateOperation <= '");
                    builder.append(fin.getTime());
                    builder.append("'");
                }
                tableModel.setNumRows(0);
                List<Traitement> listeOperation = model.findAllOperation(builder.toString());
                for (int i = 0; i < listeOperation.size(); i++) {
                    tableModel.addRow(new Object[]{Operation.values()[listeOperation.get(i).getOperation()],
                                listeOperation.get(i).getNumCompte(),
                                listeOperation.get(i).getDate(),
                                listeOperation.get(i).getUserName(),
                                listeOperation.get(i).getNumCompte()});
                }
            }
        });
        comptes.addItem("");
        List<String> ListNumCompte = modelbd.findNumCompteByIdCustomer(UserInfo.getCustomerId());
        for (int i = 0; i < ListNumCompte.size(); i++) {
            comptes.addItem(ListNumCompte.get(i));
        }
        
        String requete = "select operations.*, account.accountNumber, users.username from operations, account, users where operations.account_id=account.id and operations.user_id=users.id";
        List<Traitement> listeOperation = model.findAllOperation(requete);
        for (int i = 0; i < listeOperation.size(); i++) {
            tableModel.addRow(new Object[]{Operation.values()[listeOperation.get(i).getOperation()],
                        listeOperation.get(i).getNumCompte(),
                        listeOperation.get(i).getDate(),
                        listeOperation.get(i).getUserName(),
                        listeOperation.get(i).getNumCompte()});
        }
    }
}
