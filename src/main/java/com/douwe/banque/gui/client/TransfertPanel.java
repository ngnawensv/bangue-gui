package com.douwe.banque.gui.client;

import com.douwe.banque.gui.common.UserInfo;
import com.douwe.banque.model.IServiceAccount;
import com.douwe.banque.model.IServiceOperation;
import com.douwe.banque.modelImplement.ServiceAccountImpl;
import com.douwe.banque.modelImplement.ServiceOperationImpl;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Vincent Douwe<douwevincent@yahoo.fr>
 */
public class TransfertPanel extends JPanel {

    private JComboBox<String> source;
    private JTextField destination;
    private JTextField amount;
    IServiceOperation model = new ServiceOperationImpl();
    IServiceAccount modelbd = new ServiceAccountImpl();

    public TransfertPanel() throws Exception {
        setLayout(new BorderLayout());
        Label lbl = new Label("NOUVEAU TRANSFERT DE COMPTE A COMPTE");
        JPanel pan = new JPanel(new FlowLayout(FlowLayout.CENTER));
        source = new JComboBox<String>();
        destination = new JTextField();
        amount = new JTextField();
        JButton transferBtn = new JButton("Transferer");
        pan.add(lbl);
        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        add(BorderLayout.BEFORE_FIRST_LINE, pan);
        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("right:max(40dlu;p), 12dlu, 180dlu:", ""));
        builder.append("Compte Départ", source);
        builder.append("Compte Destination", destination);
        builder.append("Montant", amount);
        builder.append(transferBtn);
        transferBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String init = (String) source.getSelectedItem();
                String dest = destination.getText();
                String amt = amount.getText();
                if ("".equalsIgnoreCase(init)) {
                    JOptionPane.showMessageDialog(null, "Le compte source est obligatoire", "Erreur", JOptionPane.ERROR_MESSAGE);
                } else if ("".equalsIgnoreCase(dest)) {
                    JOptionPane.showMessageDialog(null, "Le compte destination est obligatoire", "Erreur", JOptionPane.ERROR_MESSAGE);
                } else if ("".equalsIgnoreCase(amt)) {
                    JOptionPane.showMessageDialog(null, "Le montant est obligatoire", "Erreur", JOptionPane.ERROR_MESSAGE);
                } else {
                    int resultat = model.transfert(init, dest, amt);
                    if (resultat == 1) {
                        JOptionPane.showMessageDialog(null, "Operation de transfert realisee avec succes");
                    } else if (resultat == 2) {
                        JOptionPane.showMessageDialog(null, "Le compte " + dest + " n'existe pas", "Erreur", JOptionPane.ERROR_MESSAGE);
                    } else if (resultat == 3) {
                        JOptionPane.showMessageDialog(null, "Le compte " + init + " ne dispose pas d'un solde suffisant", "Erreur", JOptionPane.ERROR_MESSAGE);
                    } else if (resultat == 4) {
                        JOptionPane.showMessageDialog(null, "Le montant doit etre un nombre", "Erreur", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Erreur lors du transfert", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
        });
        add(BorderLayout.CENTER, builder.getPanel());
        source.addItem("");
        List<String> ListNumCompte = modelbd.findNumCompteByIdCustomer(UserInfo.getCustomerId());
        for (int i = 0; i < ListNumCompte.size(); i++) {
            source.addItem(ListNumCompte.get(i));
        }
    }
}