package com.douwe.banque.gui.admin;

import com.douwe.banque.gui.MainMenuPanel;
import com.douwe.banque.gui.common.EmptyPanel;
import com.douwe.banque.model.IServiceAccount;
import com.douwe.banque.modelImplement.ServiceAccountImpl;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Vincent Douwe<douwevincent@yahoo.fr>
 */
public class NouveauDebitPanel extends JPanel {

    private JTextField accountText;
    private JTextField amountText;
    private MainMenuPanel parent;
    IServiceAccount model = new ServiceAccountImpl();

    public NouveauDebitPanel(MainMenuPanel parentFrame) {
        setLayout(new BorderLayout(10, 10));
        this.parent = parentFrame;
        accountText = new JTextField();
        amountText = new JTextField();
        JButton btnEnregistrer = new JButton("Enrégistrer");
        JPanel haut = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lbl = new JLabel("DÉBIT DE COMPTE DANS MA BANQUE POPULAIRE");
        haut.add(lbl);
        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        add(BorderLayout.BEFORE_FIRST_LINE, haut);
        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("right:max(40dlu;p), 12dlu, 180dlu:", ""));
        builder.append("Numéro Compte", accountText);
        builder.append("Montant", amountText);
        builder.append(btnEnregistrer);
        add(BorderLayout.CENTER, builder.getPanel());
        btnEnregistrer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String account = accountText.getText();
                String amountS = amountText.getText();
                double amount;
                if ((account == null) || ("".equals(account))) {
                    JOptionPane.showMessageDialog(null, "Le numéro de compte est obligatoire");
                    return;
                }
                if ((amountS == null) || ("".equals(amountS))) {
                    JOptionPane.showMessageDialog(null, "Le montant est obligatoire");
                    return;
                }
                try {
                    amount = Double.valueOf(amountS);
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Le montant doit etre un nombre positif");
                    Logger.getLogger(NouveauDebitPanel.class.getName()).log(Level.SEVERE, null, nfe);
                    return;
                }
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(null, "Le montant doit etre un nombre positif");
                    return;
                }
                model.debiterCompte(account, amount);
                parent.setContenu(EmptyPanel.emptyPanel());
            }
        });
    }
}
