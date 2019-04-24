package com.douwe.banque.gui.common;

import com.douwe.banque.model.IServiceUser;
import com.douwe.banque.modelImplement.ServiceUserImpl;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Vincent Douwe<douwevincent@yahoo.fr>
 */
public class LoginPanel extends JPanel {

    private JTextField loginText;
    private JPasswordField passwdText;
    private JButton btnLogin;
    IServiceUser model = new ServiceUserImpl();

    public LoginPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(80, 350, 80, 300));
        JPanel haut = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lbl;
        haut.add(lbl = new JLabel("<html>Entrer login et mot de passe <br/> pour profiter des services de la banque populaire"));
        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        add(BorderLayout.BEFORE_FIRST_LINE, haut);
        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("right:max(40dlu;p), 12dlu, 180dlu:", ""));
        builder.append("Login", loginText = new JTextField());
        builder.append("Mot de passe", passwdText = new JPasswordField());
        builder.append(btnLogin = new JButton("Login"));
        add(BorderLayout.CENTER, builder.getPanel());
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String username = loginText.getText();
                String passwd = new String(passwdText.getPassword());
                if ((username == null) || ("".equals(username))) {
                    JOptionPane.showMessageDialog(null, "Le login est obligatoire");
                    passwdText.setText("");
                    return;
                }
                if ("".equals(passwd)) {
                    JOptionPane.showMessageDialog(null, "Le mot de passe est obligatoire");
                    passwdText.setText("");
                    return;
                }
                int statutAth = model.authentification(username, passwd);
                if(statutAth == 1){
                     success();
                }
                else if (statutAth == 2) {
                    JOptionPane.showMessageDialog(null, "Login ou mot de passe incorrect");
                    passwdText.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Impossible de vérifier vos coordonnées");
                    passwdText.setText("");
                }
            }
        });
    }

    public void success() {
    }
}
