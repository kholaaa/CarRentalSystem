
package car.rental.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class forget_password {
    

    JFrame frame = new JFrame("Forget Password");
    JLabel backgroundLabel = new JLabel();

    JPanel panel = new JPanel();

    JLabel titleLabel = new JLabel("FORGOT PASSWORD");

    JLabel nameLabel = new JLabel("Name:");
    JLabel emailLabel = new JLabel("Email:");
    JLabel phoneLabel = new JLabel("Phone Number:");
    JLabel newPasswordLabel = new JLabel("New Password:");
    JLabel confirmPasswordLabel = new JLabel("Confirm Password:");

    JTextField nameField = new JTextField();
    JTextField emailField = new JTextField();
    JTextField phoneField = new JTextField();
    JPasswordField newPasswordField = new JPasswordField();
    JPasswordField confirmPasswordField = new JPasswordField();

    JButton submitButton = new JButton("Submit");
    JButton cancelButton = new JButton("Cancel");
    JButton backButton = new JButton("Back to Login");

    public forget_password() {

         Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        // Full background image
        ImageIcon bgIcon = new ImageIcon("C:\\CarRentalImages\\forget.jpg");
        Image scaled = bgIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        backgroundLabel.setIcon(new ImageIcon(scaled));
        backgroundLabel.setBounds(0, 0, width, height);

        frame.setSize(width, height);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.add(backgroundLabel);

        // Panel in the middle
       int panelWidth = 500;
       int panelHeight = 420;
       panel.setLayout(null);
       panel.setOpaque(true);
       panel.setBackground(Color.WHITE);
       panel.setBounds((width - panelWidth) / 2, (height - panelHeight) / 2, panelWidth, panelHeight);
       backgroundLabel.add(panel);
        
       

        Font lblFont = new Font("Arial", Font.BOLD, 14);
        Font titleFont = new Font("Arial", Font.BOLD, 20);

        // Title
        titleLabel.setBounds(140, 20, 250, 30);
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel);
        
        // Labels and Fields positions
        int xLabel = 40;
        int xField = 200;
        int yStart = 70;
        int yGap = 50;

        JLabel[] labels = {nameLabel, emailLabel, phoneLabel, newPasswordLabel, confirmPasswordLabel};
        Component[] fields = {nameField, emailField, phoneField, newPasswordField, confirmPasswordField};

        for (JLabel label : labels) {
            label.setFont(lblFont);
            label.setForeground(Color.black);
        }

        for (int i = 0; i < labels.length; i++) {
            labels[i].setBounds(xLabel, yStart + i * yGap, 150, 25);
            fields[i].setBounds(xField, yStart + i * yGap, 220, 25);
            panel.add(labels[i]);
            panel.add(fields[i]);
        }

        // Buttons
        submitButton.setBounds(40, yStart + 5 * yGap + 10, 120, 35);
        cancelButton.setBounds(180, yStart + 5 * yGap + 10, 120, 35);
        backButton.setBounds(320, yStart + 5 * yGap + 10, 140, 35);

        Color btnColor = new Color(70, 70, 70);
        Color textColor = Color.WHITE;

        for (JButton btn : new JButton[]{submitButton, cancelButton, backButton}) {
            btn.setBackground(btnColor);
            btn.setForeground(textColor);
        }

        panel.add(submitButton);
        panel.add(cancelButton);
        panel.add(backButton);

        // Submit logic
        submitButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String phone = phoneField.getText().trim();
                String newPassword = new String(newPasswordField.getPassword()).trim();
                String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

                if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(frame, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try (Connection conn = DBConnection.getConnection()) {
                    String query = "SELECT * FROM login WHERE name=? AND email=? AND PhoneNumber=?";
                    PreparedStatement checkStmt = conn.prepareStatement(query);
                    checkStmt.setString(1, name);
                    checkStmt.setString(2, email);
                    checkStmt.setString(3, phone);
                    ResultSet rs = checkStmt.executeQuery();

                    if (rs.next()) {
                        String update = "UPDATE login SET password=? WHERE name=? AND email=? AND PhoneNumber=?";
                        PreparedStatement updateStmt = conn.prepareStatement(update);
                        updateStmt.setString(1, newPassword);
                        updateStmt.setString(2, name);
                        updateStmt.setString(3, email);
                        updateStmt.setString(4, phone);
                        int rows = updateStmt.executeUpdate();

                        if (rows > 0) {
                            JOptionPane.showMessageDialog(frame, "Password updated successfully!");
                            frame.dispose();
                            new login();
                        } else {
                            JOptionPane.showMessageDialog(frame, "Failed to update password.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "No user found with provided details.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Cancel button clears fields
        cancelButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                nameField.setText("");
                emailField.setText("");
                phoneField.setText("");
                newPasswordField.setText("");
                confirmPasswordField.setText("");
            }
        });

        // Back to Login
        backButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
                new login();
            }
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new forget_password();
    }
}