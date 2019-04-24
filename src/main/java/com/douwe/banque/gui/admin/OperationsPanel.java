package com.douwe.banque.gui.admin;

import com.douwe.banque.data.Operation;
import com.douwe.banque.data.Traitement;
import com.douwe.banque.model.IServiceOperation;
import com.douwe.banque.modelImplement.ServiceOperationImpl;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXDatePicker;

/**
 *
 * @author Vincent Douwe<douwevincent@yahoo.fr>
 */
public class OperationsPanel extends JPanel {

    private DefaultTableModel tableModel;
    private JTextField clientText;
    private JTextField compteText;
    private JComboBox<Operation> type;
    private JXDatePicker startDate;
    private JXDatePicker endDate;
    IServiceOperation model = new ServiceOperationImpl();

    public OperationsPanel() throws SQLException {

        JTable operationTable;
        JButton filtreBtn;
        setLayout(new BorderLayout());
        JLabel lbl;
        JPanel haut = new JPanel();
        lbl = new JLabel("LA LISTE DES OPERATIONS DE MA BANQUE POPULAIRE");
        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        JPanel contenu = new JPanel();
        JPanel filtrePanel = new JPanel();
        filtreBtn = new JButton("Filtrer");
        clientText = new JTextField();
        compteText = new JTextField();
        type = new JComboBox<Operation>();
        startDate = new JXDatePicker();
        endDate = new JXDatePicker();
        haut.setLayout(new FlowLayout(FlowLayout.CENTER));
        haut.add(lbl);
        add(BorderLayout.BEFORE_FIRST_LINE, haut);
        contenu.setLayout(new BorderLayout());
        filtreBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String selectedCompte = compteText.getText();
                Operation selectedOperation = (Operation) type.getSelectedItem();
                String client = clientText.getText();
                Date debut = startDate.getDate();
                Date fin = endDate.getDate();
                StringBuilder builder = new StringBuilder("select operations.*, account.accountNumber, users.username from operations, account, users where operations.account_id=account.id and operations.user_id=users.id");
                if ((selectedCompte != null) && !("".equals(selectedCompte))) {
                    builder.append(" and accountNumber like '%");
                    builder.append(selectedCompte);
                    builder.append("%'");
                }
                if ((client != null) && !("".equals(client))) {
                    builder.append(" and username like '%");
                    builder.append(selectedCompte);
                    builder.append("%'");
                }
                if (selectedOperation != null) {
                    int index = selectedOperation.ordinal();
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
        filtrePanel.setLayout(new FlowLayout());
        filtrePanel.add(new JLabel("Nom Client"));
        filtrePanel.add(clientText);
        clientText.setPreferredSize(new Dimension(100, 25));
        filtrePanel.add(new JLabel("Numero Compte"));
        filtrePanel.add(compteText);
        compteText.setPreferredSize(new Dimension(100, 25));
        filtrePanel.add(new JLabel("Type Compte"));
        filtrePanel.add(type);
        type.setPreferredSize(new Dimension(100, 25));
        type.addItem(null);
        type.addItem(Operation.cloture);
        type.addItem(Operation.credit);
        type.addItem(Operation.debit);
        type.addItem(Operation.transfer);
        filtrePanel.add(new JLabel("Début"));
        filtrePanel.add(startDate);
        filtrePanel.add(new JLabel("Fin"));
        filtrePanel.add(endDate);
        filtrePanel.add(filtreBtn);
        contenu.add(BorderLayout.BEFORE_FIRST_LINE, filtrePanel);
        tableModel = new DefaultTableModel(new Object[]{"Opération", "Compte", "Date", "Utilisateur", "Description"}, 0);
        operationTable = new JTable(tableModel);
        contenu.add(BorderLayout.CENTER, new JScrollPane(operationTable));
        add(BorderLayout.CENTER, contenu);
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