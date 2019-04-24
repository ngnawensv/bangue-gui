package com.douwe.banque.gui.admin;

import com.douwe.banque.data.Customer;
import com.douwe.banque.gui.MainMenuPanel;
import com.douwe.banque.model.IServiceCustomer;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Vincent Douwe<douwevincent@yahoo.fr>
 */
public class NouveauClientPanel extends JPanel {

    private JTextField nameText;
    private JTextField emailText;
    private JTextField phoneText;
    private int id = -1;
    private MainMenuPanel parent;
    IServiceCustomer model = new ServiceCustomerImpl();

    public NouveauClientPanel(MainMenuPanel parentFrame, int id) throws SQLException {
        this(parentFrame);
        this.id = id;
        Customer c = model.findCustomerById(id);
        nameText.setText(c.getName());
        emailText.setText(c.getEmailAdresse());
        phoneText.setText(c.getPhoneNumber());
    }

    public NouveauClientPanel(MainMenuPanel parentFrame) {
        this.parent = parentFrame;
        setLayout(new BorderLayout(10, 10));
        nameText = new JTextField();
        emailText = new JTextField();
        phoneText = new JTextField();
        phoneText = new JTextField();
        phoneText = new JTextField();
        JButton btnEnregistrer = new JButton("Enrégistrer");
        JPanel haut = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lbl = new JLabel("AJOUT D'UN NOUVEAU CLIENT DANS MA BANQUE POPULAIRE");
        haut.add(lbl);
        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        add(BorderLayout.BEFORE_FIRST_LINE, haut);
        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("right:max(40dlu;p), 12dlu, 180dlu:", ""));
        builder.append("Nom", nameText);
        builder.append("Adresse Email", emailText);
        builder.append("Numéro de Téléphone", phoneText);
        builder.append(btnEnregistrer);
        add(BorderLayout.CENTER, builder.getPanel());
        btnEnregistrer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (id <= 0) {
                    String name = nameText.getText();
                    String email = emailText.getText();
                    String phone = phoneText.getText();
                    if ((name == null) || ("".equals(name))) {
                        JOptionPane.showMessageDialog(null, "Le nom est obligatoire");
                        return;
                    }
                    if ((email == null) || ("".equals(email))) {
                        JOptionPane.showMessageDialog(null, "L'adresse email est obligatoire");
                        return;
                    }
                    if ((phone == null) || ("".equals(phone))) {
                        JOptionPane.showMessageDialog(null, "Le numéro de téléphone est obligatoire");
                        return;
                    }
                    Customer c = new Customer(name, email, phone);
                    model.saveClient(c);
                    try {
                        parent.setContenu(new ClientPanel(parent));
                    } catch (SQLException ex) {
                        Logger.getLogger(NouveauClientPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    String name = nameText.getText();
                    String email = emailText.getText();
                    String phone = phoneText.getText();
                    if ((name == null) || ("".equals(name))) {
                        JOptionPane.showMessageDialog(null, "Le nom est obligatoire");
                        return;
                    }
                    if ((email == null) || ("".equals(email))) {
                        JOptionPane.showMessageDialog(null, "L'adresse email est obligatoire");
                        return;
                    }
                    if ((phone == null) || ("".equals(phone))) {
                        JOptionPane.showMessageDialog(null, "Le numéro de téléphone est obligatoire");
                        return;
                    }
                    model.modifierClient(name, email, phone, id);
                    try {
                        parent.setContenu(new ClientPanel(parent));
                    } catch (SQLException ex) {
                        Logger.getLogger(NouveauClientPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }
}
