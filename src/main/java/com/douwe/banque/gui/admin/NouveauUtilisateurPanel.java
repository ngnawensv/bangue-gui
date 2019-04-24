package com.douwe.banque.gui.admin;

import com.douwe.banque.data.RoleType;
import com.douwe.banque.data.User;
import com.douwe.banque.gui.MainMenuPanel;
import com.douwe.banque.model.IServiceUser;
import com.douwe.banque.modelImplement.ServiceUserImpl;
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
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author Vincent Douwe<douwevincent@yahoo.fr>
 */
public class NouveauUtilisateurPanel extends JPanel {

    private JTextField loginText;
    private JPasswordField passwdText1;
    private JPasswordField passwdText2;
    private JComboBox<RoleType> role;
    private MainMenuPanel parent;
    IServiceUser model = new ServiceUserImpl();

    public NouveauUtilisateurPanel(MainMenuPanel parentFrame) {
        setLayout(new BorderLayout(20, 20));
        this.parent = parentFrame;
        JLabel lbl = new JLabel("AJOUT D'UN NOUVEAU UTILISATEUR DANS MA BANQUE POPULAIRE");
        JPanel haut = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginText = new JTextField();
        passwdText1 = new JPasswordField();
        passwdText2 = new JPasswordField();
        role = new JComboBox<RoleType>();
        JButton enregistrer = new JButton("Enregistrer");
        lbl.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        haut.add(lbl);
        add(BorderLayout.BEFORE_FIRST_LINE, haut);
        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("right:max(40dlu;p), 12dlu, 180dlu:", ""));
        builder.append("Login", loginText);
        builder.append("Mot de Passe", passwdText1);
        builder.append("Retapez mot de passe", passwdText2);
        builder.append("Role", role);
        role.addItem(null);
        role.addItem(RoleType.admin);
        role.addItem(RoleType.employee);
        builder.append(enregistrer);
        add(BorderLayout.CENTER, builder.getPanel());
        enregistrer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                    String login = loginText.getText();
                    String pwd = new String(passwdText1.getPassword());
                    String pwd2 = new String(passwdText2.getPassword());
                    RoleType ro = (RoleType) role.getSelectedItem();
                    if ((login == null) || ("".equals(login))) {
                        JOptionPane.showMessageDialog(null, "Le login est obligatoire");
                        return;
                    }
                    if ("".equals(pwd)) {
                        JOptionPane.showMessageDialog(null, "Le password est obligatoire");
                        return;
                    }
                    if (!(pwd.equals(pwd2))) {
                        JOptionPane.showMessageDialog(null, "Les mots de passe ne sont pas identiques");
                        return;
                    }
                    if (ro == null) {
                        JOptionPane.showMessageDialog(null, "Le role est obligatoire");
                        return;
                    }
                    User u = new User(login,pwd,ro);
                    model.saveUtilisateur(u);
                try {
                    parent.setContenu(new UtilisateurPanel(parent));
                } catch (SQLException ex) {
                    Logger.getLogger(NouveauUtilisateurPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
