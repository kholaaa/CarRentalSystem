package car.rental.system;

import car.rental.system.login.ShowPasswordHandler;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.*;
import java.sql.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SignUp {
    
    JFrame F=new JFrame();
    JLabel backgroundLabel = new JLabel();
    JPanel loginPanel = new JPanel();
    JLabel titleLabel = new JLabel();
    
    JLabel name = new JLabel();
    JLabel emailLabel = new JLabel();
    JLabel passwordLabel = new JLabel();
    JLabel number = new JLabel();
    
    JTextField nameField = new JTextField();
    JTextField emailField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JTextField numberField = new JTextField();
    
    JCheckBox showPassword = new JCheckBox();
    
    JButton backTologinButton = new JButton();
    JButton signupButton = new JButton();
    
    public SignUp(){
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;
        
        ImageIcon bgIcon = new ImageIcon("C:\\CarRentalImages\\signup.jpg"); 
        Image scaled = bgIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        backgroundLabel.setIcon(new ImageIcon(scaled));
        backgroundLabel.setBounds(0, 0, width, height);
        backgroundLabel.setLayout(null);
        
        // Frame setup
        F.setSize(width, height);
        F.setLayout(null);
        F.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        F.add(backgroundLabel);
        
        loginPanel.setLayout(null);
        loginPanel.setBounds(width/2-200, height/2-170, 400, 300);
        loginPanel.setBackground(Color.pink);
        
        //title  and label setup
        titleLabel.setText("    SIGNUP");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBounds(150, 10, 120, 30);
        
        name.setText("NAME:");
        name.setBounds(40, 60, 100, 25);
        nameField.setBounds(130, 60, 220, 25);
        
        emailLabel.setText("EMAIL:");
        emailLabel.setBounds(40, 100, 100, 25);
        emailField.setBounds(130, 100, 220, 25);

        passwordLabel.setText("PASSWORD:");
        passwordLabel.setBounds(40, 140, 100, 25);
        passwordField.setBounds(130, 140, 220, 25);
        
        showPassword.setText("SHOW PASSWORD");
        showPassword.setBounds(130, 170, 150, 25);
        
        number.setText("NUMBER:");
        number.setBounds(40, 210, 100, 25);
        numberField.setBounds(130, 210, 220, 25);

        //buttons
        
        backTologinButton.setText("BACK TO LOGIN");
        backTologinButton.setBounds(40, 250, 100, 30);

        signupButton.setText("SIGN UP NOW");
        signupButton.setBounds(250, 250, 100, 30);
        
        
        // Add components to panel
        loginPanel.add(titleLabel);
        loginPanel.add(name);
        loginPanel.add(nameField);
        loginPanel.add(emailLabel);
        loginPanel.add(emailField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(showPassword);
        loginPanel.add(number);
        loginPanel.add(numberField);
        loginPanel.add(backTologinButton);
        loginPanel.add(signupButton);
        
         // Add panel to background
        backgroundLabel.add(loginPanel);
        
        
        signupButton.addMouseListener(new signupHandler());
        backTologinButton.addMouseListener(new LoginHandler());
        showPassword.addMouseListener(new ShowPasswordHandler());
        
        F.setVisible(true);
    }
    
     public class signupHandler extends MouseAdapter {
    public void mouseClicked(MouseEvent e) {
        String nameText = nameField.getText();
            String emailText = emailField.getText();
            String passwordText = new String(passwordField.getPassword());
            String numberText = numberField.getText();

            if (nameText.isEmpty() || emailText.isEmpty() || passwordText.isEmpty() || numberText.isEmpty()) {
                JOptionPane.showMessageDialog(F, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Connection conn = null;
            PreparedStatement stmt = null;

            try {
                conn = DBConnection.getConnection();
                String sql = "INSERT INTO login (name, email, password, PhoneNumber) VALUES (?, ?, ?, ?)";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, nameText);
                stmt.setString(2, emailText);
                stmt.setString(3, passwordText);
                stmt.setString(4, numberText);

                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(F, "Registration successful!");
                    F.dispose();
                    new dashboard();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(F, "Database error:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                try { if (stmt != null) stmt.close(); } catch (Exception e1) {}
                try { if (conn != null) conn.close(); } catch (Exception e2) {}
            }
    }
     }
    
    public class LoginHandler extends MouseAdapter {
    public void mouseClicked(MouseEvent e) {
        F.dispose();           // Close the current window
        new login();
    }
    }

    public class ShowPasswordHandler extends MouseAdapter {
    public void mouseClicked(MouseEvent e) {
        if (showPassword.isSelected()) {
            passwordField.setEchoChar((char) 0); // Show password
        } else {
            passwordField.setEchoChar('•'); // Hide password
        }
    }
}
    
    
    
        public static void main(String[] args) {
        new SignUp();
    }
    
}