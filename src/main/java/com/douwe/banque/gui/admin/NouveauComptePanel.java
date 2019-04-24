package com.douwe.banque.gui.admin;

import com.douwe.banque.connexion.Connexion;
import com.douwe.banque.data.Account;
import com.douwe.banque.data.AccountType;
import com.douwe.banque.data.Customer;
import com.douwe.banque.gui.MainMenuPanel;
import com.douwe.banque.model.IServiceAccount;
import com.douwe.banque.model.IServiceCustomer;
import com.douwe.banque.modelImplement.ServiceAccountImpl;
import com.douwe.banque.modelImplement.ServiceCustomerImpl;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Vincent Douwe<douwevincent@yahoo.fr>
 */
public class NouveauComptePanel extends JPanel {

    private JTextField numberText;
    private JTextField balanceText;
    private JComboBox<AccountType> typeText;
    private JTextField customerText;
    private JButton btnEnregistrer;
    private int id = -1;
    private MainMenuPanel parent;
    IServiceAccount model = new ServiceAccountImpl();
    IServiceCustomer modelb = new ServiceCustomerImpl();

    public NouveauComptePanel(MainMenuPanel parentFrame, int account_id) throws SQLException {
        this(parentFrame);
        this.id = account_id;
        Account a = model.findAccountByIdCustumer(id);
        numberText.setText(a.getNumeroCompte());
        balanceText.setText("" + a.getSolde());
        balanceText.setEnabled(false);
        customerText.setEnabled(false);
        Customer c = modelb.findCustomerById(a.getNumClient());
        customerText.setText(c.getName());
    }

    public NouveauComptePanel(MainMenuPanel parentFrame) {
        Connexion.instance();
        this.parent = parentFrame;
        setLayout(new BorderLayout(10, 10));
        numberText = new JTextField();
        balanceText = new JTextField();
        typeText = new JComboBox<AccountType>();
        customerText = new JTextField();
        btnEnregistrer = new JButton("Enrégistrer");
        JPanel haut = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lbl;
        lbl = new JLabel("AJOUT D'UN NOUVEAU COMPTE DANS MA BANQUE POPULAIRE");
        haut.add(lbl);
        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        add(BorderLayout.BEFORE_FIRST_LINE, haut);
        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("right:max(40dlu;p), 12dlu, 180dlu:", ""));
        builder.append("Numéro Compte", numberText);
        builder.append("Solde initial", balanceText);
        builder.append("Type de Compte", typeText);
        typeText.addItem(AccountType.deposit);
        typeText.addItem(AccountType.saving);
        builder.append("Titulaire", customerText);
        builder.append(btnEnregistrer);
        add(BorderLayout.CENTER, builder.getPanel());
        btnEnregistrer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (id > 0) {
                    String number = numberText.getText();
                    AccountType type = (AccountType) typeText.getSelectedItem();
                    if ((number == null) || ("".equals(number))) {
                        JOptionPane.showMessageDialog(null, "Le numéro du compte n'est pas specifie");
                        return;
                    }
                    if (type == null) {
                        JOptionPane.showMessageDialog(null, "Le type du compte n'est pas specifie");
                        return;
                    }
                    model.modifierCompte(id, number, type);
                    try {
                        parent.setContenu(new ComptePanel(parent));
                    } catch (SQLException ex) {
                        Logger.getLogger(NouveauComptePanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    String customer = customerText.getText();
                    String number = numberText.getText();
                    String balanceT = balanceText.getText();
                    AccountType type = (AccountType) typeText.getSelectedItem();
                    double balance;
                    if ((customer == null) || ("".equals(customer))) {
                        JOptionPane.showMessageDialog(null, "Le nom du client n'est pas specifie");
                        return;
                    }
                    if ((number == null) || ("".equals(number))) {
                        JOptionPane.showMessageDialog(null, "Le numéro du compte n'est pas specifie");
                        return;
                    }
                    if ((balanceT == null) || ("".equals(balanceT))) {
                        JOptionPane.showMessageDialog(null, "Le solde initial du compte n'est pas specifie");
                        return;
                    }
                    if (type == null) {
                        JOptionPane.showMessageDialog(null, "Le type du compte n'est pas specifie");
                        return;
                    }
                    try {
                        balance = Double.valueOf(balanceT);
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(null, "Le solde compte doit être un nombre positif");
                        Logger.getLogger(NouveauComptePanel.class.getName()).log(Level.SEVERE, null, nfe);
                        return;
                    }
                    if (balance < 0) {
                        JOptionPane.showMessageDialog(null, "Le solde compte doit être un nombre positif");
                        return;
                    }
                    model.saveCompte(customer, number, type, balance);
                    try {
                        parent.setContenu(new ComptePanel(parent));
                    } catch (SQLException ex) {
                        Logger.getLogger(NouveauComptePanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }
}
