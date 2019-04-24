package com.douwe.banque.gui.client;

import com.douwe.banque.data.Account;
import com.douwe.banque.data.AccountType;
import com.douwe.banque.gui.common.UserInfo;
import com.douwe.banque.model.IServiceAccount;
import com.douwe.banque.modelImplement.ServiceAccountImpl;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Vincent Douwe<douwevincent@yahoo.fr>
 */
public class MesCompteListePanel extends JPanel {

    IServiceAccount modelbd = new ServiceAccountImpl();

    public MesCompteListePanel() throws Exception {
        JTable compteTable;
        DefaultTableModel model;
        setLayout(new BorderLayout(10, 10));
        Label lbl = new Label("LA LISTE DE MES COMPTES");
        JPanel pan = new JPanel(new FlowLayout(FlowLayout.CENTER));
        model = new DefaultTableModel(new String[]{"No Compte", "Type Compte", "Balance"}, 0);
        compteTable = new JTable(model);
        JPanel btnPanel = new JPanel();
        pan.add(lbl);
        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        add(BorderLayout.BEFORE_FIRST_LINE, pan);
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        add(BorderLayout.CENTER, new JScrollPane(compteTable));
        List<Account> listCompte = modelbd.findAllCompteByIdCustomer(UserInfo.getCustomerId());
        for (int i = 0; i < listCompte.size(); i++) {
            model.addRow(new Object[]{listCompte.get(i).getNumeroCompte(), AccountType.values()[listCompte.get(i).getType()], listCompte.get(i).getSolde()});
        }
    }
}
