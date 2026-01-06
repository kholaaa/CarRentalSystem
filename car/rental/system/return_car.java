package car.rental.system;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class return_car {

    JFrame frame = new JFrame();
    JLabel backgroundLabel = new JLabel();

    JLabel carIdLabel = new JLabel("Car ID:");
    JLabel customerIdLabel = new JLabel("Customer ID:");
    JLabel returnDateLabel = new JLabel("Return Date (YYYY-MM-DD):");
    JLabel fuelLevelLabel = new JLabel("Fuel Level (%):");
    JLabel conditionLabel = new JLabel("Condition:");
    JLabel titleLable=new JLabel("RETURN CAR");
    JTextField carIdField = new JTextField();
    JTextField customerIdField = new JTextField();
    JTextField returnDateField = new JTextField();
    JTextField fuelLevelField = new JTextField();
    JTextField conditionField = new JTextField();

    JButton submitButton = new JButton("Submit");
    JButton clearButton = new JButton("Clear");
    JButton backButton = new JButton("Back");

    public return_car() {

        int width = 650;
        int height = 500;

        // Background image
        ImageIcon bgIcon = new ImageIcon("C:\\CarRentalImages\\return car.jpg");
        Image scaled = bgIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        backgroundLabel.setIcon(new ImageIcon(scaled));
        backgroundLabel.setBounds(0, 0, width, height);
        backgroundLabel.setLayout(null);

        frame.setSize(width, height);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(backgroundLabel);
        frame.setLocation(200, 120);

        Font lblFont = new Font("Arial", Font.BOLD, 15);
        
          titleLable.setBounds(210, 20, 250, 40);
        titleLable.setFont(new Font("Arial", Font.BOLD, 24));
        titleLable.setForeground(Color.white);
        backgroundLabel.add(titleLable);
        Color textColor = Color.WHITE;

        int xLabel = 40;
        int xField = 260;
        int yStart = 100;
        int yGap = 50;

        // Labels and fields
        JLabel[] labels = {carIdLabel, customerIdLabel, returnDateLabel, fuelLevelLabel, conditionLabel};
        JTextField[] fields = {carIdField, customerIdField, returnDateField, fuelLevelField, conditionField};

        for (int i = 0; i < labels.length; i++) {
            labels[i].setBounds(xLabel, yStart + i * yGap, 200, 30);
            labels[i].setFont(lblFont);
            labels[i].setForeground(textColor);
            backgroundLabel.add(labels[i]);

            fields[i].setBounds(xField, yStart + i * yGap, 300, 30);
            backgroundLabel.add(fields[i]);
        }

        // Buttons
        submitButton.setBounds(80, yStart + labels.length * yGap + 10, 120, 35);
        clearButton.setBounds(250, yStart + labels.length * yGap + 10, 120, 35);
        backButton.setBounds(420, yStart + labels.length * yGap + 10, 120, 35);

        for (JButton btn : new JButton[]{submitButton, clearButton, backButton}) {
            btn.setBackground(Color.DARK_GRAY);
            btn.setForeground(Color.WHITE);
            backgroundLabel.add(btn);
        }

        // Submit logic
        submitButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String carId = carIdField.getText().trim();
                String customerId = customerIdField.getText().trim();
                String returnDate = returnDateField.getText().trim();
                String fuelLevel = fuelLevelField.getText().trim();
                String condition = conditionField.getText().trim();

                if (carId.isEmpty() || customerId.isEmpty() || returnDate.isEmpty()
                        || fuelLevel.isEmpty() || condition.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Connection conn = null;
                PreparedStatement stmt = null;

                try {
                    conn = DBConnection.getConnection();

                    // Insert into returncar
                    String sqlInsert = "INSERT INTO returncar (fuellevel, carcondition, carID, customerID, returndate) "
                                     + "VALUES (?, ?, ?, ?, ?)";
                    stmt = conn.prepareStatement(sqlInsert);
                    stmt.setInt(1, Integer.parseInt(fuelLevel));
                    stmt.setString(2, condition);
                    stmt.setInt(3, Integer.parseInt(carId));
                    stmt.setInt(4, Integer.parseInt(customerId));
                    stmt.setDate(5, Date.valueOf(returnDate));
                    stmt.executeUpdate();
                    
                    stmt.close();

                    // Update car availability
                    String sqlUpdate = "UPDATE cars SET availability='Yes' WHERE carID=?";
                    stmt = conn.prepareStatement(sqlUpdate);
                    stmt.setInt(1, Integer.parseInt(carId));
                    stmt.executeUpdate();
                    
                    JOptionPane.showMessageDialog(frame, "Car return saved successfully!");

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    try { if (stmt != null) stmt.close(); } catch (Exception ex1) {}
                    try { if (conn != null) conn.close(); } catch (Exception ex2) {}
                }
            }
        });

        // Clear button
        clearButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                carIdField.setText("");
                customerIdField.setText("");
                returnDateField.setText("");
                fuelLevelField.setText("");
                conditionField.setText("");
            }
        });

        // Back button
        backButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
                new dashboard();
            }
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new return_car();
    }
}