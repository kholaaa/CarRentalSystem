package car.rental.system;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class customer {

    JFrame frame = new JFrame();
    JLabel backgroundLabel = new JLabel();
    JLabel titleLable=new JLabel("CUSTOMER DETAILS");
    JLabel idLabel = new JLabel("Customer ID:");
    JLabel nameLabel = new JLabel("Customer Name:");
    JLabel numberLabel = new JLabel("Customer Number:");

    JTextField idField = new JTextField();
    JTextField nameField = new JTextField();
    JTextField numberField = new JTextField();

    JButton saveButton = new JButton("Save");
    JButton clearButton = new JButton("Clear");
    JButton backButton = new JButton("Back");

    public customer() {

        // Frame size
        int width = 600;
        int height = 400;

        // Background image
        ImageIcon bgIcon = new ImageIcon("C:\\CarRentalImages\\customer.jpg");
        Image scaled = bgIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        backgroundLabel.setIcon(new ImageIcon(scaled));
        backgroundLabel.setBounds(0, 0, width, height);
        backgroundLabel.setLayout(null);

        frame.setSize(width, height);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(backgroundLabel);
        frame.setLocation(200, 150);

        Font lblFont = new Font("Arial", Font.BOLD, 16);
        
        titleLable.setBounds(150, 20, 250, 40);
        titleLable.setFont(new Font("Arial", Font.BOLD, 24));
        titleLable.setForeground(Color.black);
        backgroundLabel.add(titleLable);

        int xLabel = 50;
        int xField = 220;
        int yStart = 100;
        int yGap = 50;

        // Labels and text fields
        idLabel.setBounds(xLabel, yStart, 150, 30);
        idLabel.setFont(lblFont);
        backgroundLabel.add(idLabel);
        idLabel.setForeground(Color.white);
        idField.setBounds(xField, yStart, 250, 30);
        backgroundLabel.add(idField);

        nameLabel.setBounds(xLabel, yStart + yGap, 150, 30);
        nameLabel.setFont(lblFont);
        backgroundLabel.add(nameLabel);
        nameLabel.setForeground(Color.white);
        nameField.setBounds(xField, yStart + yGap, 250, 30);
        backgroundLabel.add(nameField);

        numberLabel.setBounds(xLabel, yStart + 2 * yGap, 180, 30);
        numberLabel.setFont(lblFont);
        backgroundLabel.add(numberLabel);
        numberLabel.setForeground(Color.white);
        numberField.setBounds(xField, yStart + 2 * yGap, 250, 30);
        backgroundLabel.add(numberField);

        // Buttons
        saveButton.setBounds(60, yStart + 3 * yGap + 20, 120, 35);
        clearButton.setBounds(200, yStart + 3 * yGap + 20, 120, 35);
        backButton.setBounds(340, yStart + 3 * yGap + 20, 120, 35);

        saveButton.setBackground(Color.DARK_GRAY);
        saveButton.setForeground(Color.WHITE);
        clearButton.setBackground(Color.DARK_GRAY);
        clearButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.DARK_GRAY);
        backButton.setForeground(Color.WHITE);

        backgroundLabel.add(saveButton);
        backgroundLabel.add(clearButton);
        backgroundLabel.add(backButton);

        // Button handlers with MouseAdapter
        saveButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String id = idField.getText();
                String name = nameField.getText();
                String number = numberField.getText();

                if (id.isEmpty() || name.isEmpty() || number.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Connection conn = null;
                PreparedStatement stmt = null;

                try {
                    conn = DBConnection.getConnection();
                    String sql = "INSERT INTO customer (customerID, customername, customernumber) VALUES (?, ?, ?)";
                    stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, Integer.parseInt(id));
                    stmt.setString(2, name);
                    stmt.setString(3, number);

                    int rows = stmt.executeUpdate();
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(frame, "Customer saved successfully!");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Database error:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    try { if (stmt != null) stmt.close(); } catch (Exception ex1) {}
                    try { if (conn != null) conn.close(); } catch (Exception ex2) {}
                }
            }
        });

        clearButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                idField.setText("");
                nameField.setText("");
                numberField.setText("");
            }
        });

        backButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
                new dashboard(); // Make sure you have a Dashboard class
            }
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new customer();
    }
}