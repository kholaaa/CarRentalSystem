package car.rental.system;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.*;

public class add_car {
    

    JFrame frame = new JFrame();
    JLabel backgroundLabel = new JLabel();
    JLabel titleLable=new JLabel("ADD CAR");
    JLabel carIdLabel = new JLabel("Car ID:");
    JLabel typeLabel = new JLabel("Type:");
    JLabel colorLabel = new JLabel("Color:");
    JLabel modelLabel = new JLabel("Model:");
    JLabel rentLabel = new JLabel("Rent per Day:");

    JTextField carIdField = new JTextField();
    JTextField typeField = new JTextField();
    JTextField colorField = new JTextField();
    JTextField modelField = new JTextField();
    JTextField rentField = new JTextField();

    JButton saveButton = new JButton("Save");
    JButton clearButton = new JButton("Clear");
    JButton backButton = new JButton("Back");

    public add_car() {
       

        int width = 600;
        int height = 500;

        ImageIcon bgIcon = new ImageIcon("C:\\CarRentalImages\\add car.jpg");
        Image scaled = bgIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        backgroundLabel.setIcon(new ImageIcon(scaled));
        backgroundLabel.setBounds(0, 0, width, height);
        backgroundLabel.setLayout(null);

        frame.setSize(width, height);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(backgroundLabel);
        frame.setLocation(100, 100);

        Font lblFont = new Font("Arial", Font.BOLD, 16);
        
        titleLable.setBounds(200, 30, 250, 40);
        titleLable.setFont(new Font("Arial", Font.BOLD, 24));
        titleLable.setForeground(Color.black);
        backgroundLabel.add(titleLable);


        int xLabel = 100;
        int xField = 300;
        int yStart = 110;
        int yGap = 50;

        // Labels and Fields
        carIdLabel.setBounds(xLabel, yStart, 150, 30);
        carIdLabel.setFont(lblFont);
        backgroundLabel.add(carIdLabel);
        carIdField.setBounds(xField, yStart, 200, 30);
        backgroundLabel.add(carIdField);

        typeLabel.setBounds(xLabel, yStart + yGap, 150, 30);
        typeLabel.setFont(lblFont);
        backgroundLabel.add(typeLabel);
        typeField.setBounds(xField, yStart + yGap, 200, 30);
        backgroundLabel.add(typeField);

        colorLabel.setBounds(xLabel, yStart + 2*yGap, 150, 30);
        colorLabel.setFont(lblFont);
        backgroundLabel.add(colorLabel);
        colorField.setBounds(xField, yStart + 2*yGap, 200, 30);
        backgroundLabel.add(colorField);

        modelLabel.setBounds(xLabel, yStart + 3*yGap, 150, 30);
        modelLabel.setFont(lblFont);
        backgroundLabel.add(modelLabel);
        modelField.setBounds(xField, yStart + 3*yGap, 200, 30);
        backgroundLabel.add(modelField);

        rentLabel.setBounds(xLabel, yStart + 4*yGap, 150, 30);
        rentLabel.setFont(lblFont);
        backgroundLabel.add(rentLabel);
        rentField.setBounds(xField, yStart + 4*yGap, 200, 30);
        backgroundLabel.add(rentField);

        // Buttons
        saveButton.setBounds(100, yStart + 5*yGap + 20, 100, 35);
        clearButton.setBounds(220, yStart + 5*yGap + 20, 100, 35);
        backButton.setBounds(340, yStart + 5*yGap + 20, 100, 35);

        saveButton.setBackground(Color.DARK_GRAY);
        saveButton.setForeground(Color.WHITE);
        clearButton.setBackground(Color.DARK_GRAY);
        clearButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.DARK_GRAY);
        backButton.setForeground(Color.WHITE);

        backgroundLabel.add(saveButton);
        backgroundLabel.add(clearButton);
        backgroundLabel.add(backButton);

        // Button handlers
        clearButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                carIdField.setText("");
                typeField.setText("");
                colorField.setText("");
                modelField.setText("");
                rentField.setText("");
            }
        });

        backButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
                new dashboard();
            }
        });

        saveButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
             
                //  DB code 
        String carId = carIdField.getText();
        String model = modelField.getText();
        String type = typeField.getText();
        String color = colorField.getText();
        String rentPerDay = rentField.getText();

        if (carId.isEmpty() || model.isEmpty() || type.isEmpty() || color.isEmpty() || rentPerDay.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
         int carIdInt = 0;
        int rentPerDayInt = 0;

        try {
            carIdInt = Integer.parseInt(carId);
            rentPerDayInt = Integer.parseInt(rentPerDay);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Car ID and Rent per Day must be numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Get connection
            conn = DBConnection.getConnection();

            // SQL insert query
            String sql = "INSERT INTO cars (carid, carmodel, cartype, colour, price_per_day) VALUES (?, ?, ?, ?, ?)";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, carIdInt);
            stmt.setString(2, model);
            stmt.setString(3, type);
            stmt.setString(4, color);
            stmt.setInt(5, rentPerDayInt);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(frame, "Car saved successfully!");
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to save car.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database error:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception e1) {}
            try { if (conn != null) conn.close(); } catch (Exception e2) {}
        }
    }
});


        frame.setVisible(true);
    }
}
