package car.rental.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;
import java.awt.event.MouseAdapter;

public class login {

    JFrame frame = new JFrame();

    JLabel backgroundLabel = new JLabel();
    JPanel loginPanel = new JPanel();

    JLabel titleLabel = new JLabel();
    JLabel emailLabel = new JLabel();
    JLabel passwordLabel = new JLabel();

    JTextField emailField = new JTextField();
    JPasswordField passwordField = new JPasswordField();

    JCheckBox showPassword = new JCheckBox();

    JButton loginButton = new JButton();
    JButton signupButton = new JButton();
    JButton forgotButton = new JButton();

    public login() {
        // Full screen background
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        ImageIcon bgIcon = new ImageIcon("C:\\CarRentalImages\\login.jpg"); 
        Image scaled = bgIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        backgroundLabel.setIcon(new ImageIcon(scaled));
        backgroundLabel.setBounds(0, 0, width, height);
        backgroundLabel.setLayout(null); 

        // Frame setup
        frame.setSize(width, height);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(backgroundLabel);

        // Panel setup (centered)
        loginPanel.setLayout(null);
        loginPanel.setBounds(width / 2 - 200, height / 2 - 170, 400, 340);
        loginPanel.setBackground(Color.WHITE);

        // Labels and fields
        titleLabel.setText("    LOGIN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBounds(150, 10, 120, 30);

        emailLabel.setText("EMAIL:");
        emailLabel.setBounds(40, 60, 100, 25);
        emailField.setBounds(130, 60, 220, 25);

        passwordLabel.setText("PASSWORD:");
        passwordLabel.setBounds(40, 110, 100, 25);
        passwordField.setBounds(130, 110, 220, 25);

        showPassword.setText("SHOW PASSWORD");
        showPassword.setBounds(130, 140, 150, 25);

        // Buttons
        loginButton.setText("LOGIN");
        loginButton.setBounds(40, 190, 100, 30);

        signupButton.setText("SIGN UP");
        signupButton.setBounds(250, 190, 100, 30);

        forgotButton.setText("FORGOT PASSWORD");
        forgotButton.setBounds(100, 240, 200, 30);

        // Add components to panel
        loginPanel.add(titleLabel);
        loginPanel.add(emailLabel);
        loginPanel.add(emailField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(showPassword);
        loginPanel.add(loginButton);
        loginPanel.add(signupButton);
        loginPanel.add(forgotButton);

        // Add panel to background
        backgroundLabel.add(loginPanel);

        // Event Listeners
        loginButton.addMouseListener(new LoginHandler());
        signupButton.addMouseListener(new SignupHandler());
        forgotButton.addMouseListener(new ForgotHandler());
       showPassword.addMouseListener(new ShowPasswordHandler());

        


        frame.setVisible(true);
    }

    // Event Handlers
   public class LoginHandler extends MouseAdapter {
    public void mouseClicked(MouseEvent e) {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = (Connection) DBConnection.getConnection();
            String sql = "SELECT * FROM login WHERE email = ? AND password = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password);

            rs = stmt.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(frame, "Login Successful!");
                new dashboard();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid email or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database error:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e1) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e2) {}
            try { if (conn != null) conn.close(); } catch (Exception e3) {}
        }
        
    }
}

    public class SignupHandler extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            //JOptionPane.showMessageDialog(frame, "Sign-up clicked!");
            frame.dispose();           // Close the current window
            new SignUp();
        }
    }

    public class ForgotHandler extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            new forget_password();
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
        new login();
    }
}