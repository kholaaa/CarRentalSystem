package car.rental.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.HashMap;

public class bookcar {

    JFrame frame = new JFrame("Book Car");
    JLabel backgroundLabel = new JLabel();
    JLabel titleLable = new JLabel("BOOK CAR");
    JLabel customerIdLabel = new JLabel("Customer ID:");
    JLabel carNameLabel = new JLabel("Car Name:");
    JLabel entryDateLabel = new JLabel("Entry Date (YYYY-MM-DD):");
    JLabel returnDateLabel = new JLabel("Return Date (YYYY-MM-DD):");

    JTextField customerIdField = new JTextField();
    JComboBox<String> carNameComboBox = new JComboBox<>();
    JTextField entryDateField = new JTextField();
    JTextField returnDateField = new JTextField();

    JButton submitButton = new JButton("Submit");
    JButton clearButton = new JButton("Clear");
    JButton backButton = new JButton("Back");
    JButton chalanButton = new JButton("CHALAN");

    // This map holds car names -> carIDs
    HashMap<String, Integer> carNameToId = new HashMap<>();

    public bookcar() {
        int width = 650;
        int height = 450;

        ImageIcon bgIcon = new ImageIcon("C:\\CarRentalImages\\book car.jpg");
        Image scaled = bgIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        backgroundLabel.setIcon(new ImageIcon(scaled));
        backgroundLabel.setBounds(0, 0, width, height);
        backgroundLabel.setLayout(null);

        frame.setSize(width, height);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(200, 120);
        frame.add(backgroundLabel);

        Font lblFont = new Font("Arial", Font.BOLD, 14);
        titleLable.setBounds(250, 20, 250, 40);
        titleLable.setFont(new Font("Arial", Font.BOLD, 24));
        titleLable.setForeground(Color.WHITE);
        backgroundLabel.add(titleLable);

        int xLabel = 80;
        int xField = 260;
        int yStart = 100;
        int yGap = 50;

        // Labels and fields
        JLabel[] labels = {customerIdLabel, carNameLabel, entryDateLabel, returnDateLabel};
        for (JLabel label : labels) {
            label.setForeground(Color.white);
        }
        Component[] fields = {customerIdField, carNameComboBox, entryDateField, returnDateField};

        for (int i = 0; i < labels.length; i++) {
            labels[i].setBounds(xLabel, yStart + i * yGap, 180, 25);
            labels[i].setFont(lblFont);
            backgroundLabel.add(labels[i]);

            fields[i].setBounds(xField, yStart + i * yGap, 220, 25);
            backgroundLabel.add(fields[i]);
        }

        // Load available cars
        loadAvailableCarNames();

        // Buttons
        submitButton.setBounds(xLabel, yStart + 4 * yGap + 20, 100, 30);
        clearButton.setBounds(xLabel + 140, yStart + 4 * yGap + 20, 100, 30);
        backButton.setBounds(xLabel + 280, yStart + 4 * yGap + 20, 100, 30);
        chalanButton.setBounds(xLabel + 420, yStart + 4 * yGap + 20, 100, 30);

        backgroundLabel.add(submitButton);
        backgroundLabel.add(clearButton);
        backgroundLabel.add(backButton);
        backgroundLabel.add(chalanButton);

        Color btnColor = new Color(50, 50, 50);
        Color textColor = Color.WHITE;
        for (JButton btn : new JButton[]{submitButton, clearButton, backButton, chalanButton}) {
            btn.setBackground(btnColor);
            btn.setForeground(textColor);
        }

        // Clear button
        clearButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                customerIdField.setText("");
                entryDateField.setText("");
                returnDateField.setText("");
                if (carNameComboBox.getItemCount() > 0) {
                    carNameComboBox.setSelectedIndex(0);
                }
            }
        });

        // Back button
        backButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
                new dashboard();
            }
        });

        // Submit button - with overlap check in Java
        submitButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String cidText = customerIdField.getText().trim();
                String carName = (String) carNameComboBox.getSelectedItem();
                String entry = entryDateField.getText().trim();
                String ret = returnDateField.getText().trim();

                if (cidText.isEmpty() || carName == null || entry.isEmpty() || ret.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Integer carIdObj = carNameToId.get(carName);
                if (carIdObj == null) {
                    JOptionPane.showMessageDialog(frame, "Invalid car selected.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int carID = carIdObj.intValue();

                // Parse customer ID safely
                int customerID;
                try {
                    customerID = Integer.parseInt(cidText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Customer ID must be a number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Parse dates
                java.sql.Date newEntryDate, newReturnDate;
                try {
                    newEntryDate = java.sql.Date.valueOf(entry);
                    newReturnDate = java.sql.Date.valueOf(ret);

                    if (!newReturnDate.after(newEntryDate)) {
                        JOptionPane.showMessageDialog(frame, "Return date must be after entry date.", "Invalid Date", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid date format. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // === CHECK FOR OVERLAPPING BOOKINGS IN JAVA ===
                boolean hasOverlap = false;
                try (Connection conn = DBConnection.getConnection()) {
                    String query = "SELECT entrydate, returndate FROM bookcar WHERE carID = ?";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setInt(1, carID);
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                        java.sql.Date existingEntry = rs.getDate("entrydate");
                        java.sql.Date existingReturn = rs.getDate("returndate");

                        // Overlap logic: two ranges overlap if:
                        // newEntry <= existingReturn AND newReturn >= existingEntry
                        if (newEntryDate.compareTo(existingReturn) <= 0 && newReturnDate.compareTo(existingEntry) >= 0) {
                            hasOverlap = true;
                            break;
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error checking car availability: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (hasOverlap) {
                    JOptionPane.showMessageDialog(frame,
                            "This car is already booked for the selected dates.\n" +
                                    "Please choose different dates or another car.",
                            "Booking Conflict", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // === NO OVERLAP → PROCEED WITH BOOKING ===
                try (Connection conn = DBConnection.getConnection()) {
                    String insert = "INSERT INTO bookcar (carID, customerID, entrydate, returndate) VALUES (?, ?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(insert);
                    stmt.setInt(1, carID);
                    stmt.setInt(2, customerID);
                    stmt.setDate(3, newEntryDate);
                    stmt.setDate(4, newReturnDate);

                    int result = stmt.executeUpdate();

                    if (result > 0) {
                        // Update car availability
                        PreparedStatement update = conn.prepareStatement("UPDATE cars SET Availability = 'No' WHERE carID = ?");
                        update.setInt(1, carID);
                        update.executeUpdate();

                        JOptionPane.showMessageDialog(frame, "Booking Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

                        // Clear fields
                        customerIdField.setText("");
                        entryDateField.setText("");
                        returnDateField.setText("");
                        if (carNameComboBox.getItemCount() > 0) {
                            carNameComboBox.setSelectedIndex(0);
                        }

                        loadAvailableCarNames(); // Refresh available cars
                    } else {
                        JOptionPane.showMessageDialog(frame, "Booking Failed!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error saving booking: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Chalan button (unchanged, but with better validation)
        chalanButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String cid = customerIdField.getText().trim();
                String carName = (String) carNameComboBox.getSelectedItem();
                String entry = entryDateField.getText().trim();
                String ret = returnDateField.getText().trim();

                if (cid.isEmpty() || carName == null || entry.isEmpty() || ret.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all fields before generating chalan.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Integer carIdObj = carNameToId.get(carName);
                if (carIdObj == null) {
                    JOptionPane.showMessageDialog(frame, "Invalid car selected.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int carID = carIdObj.intValue();

                try {
                    java.sql.Date entryDate = java.sql.Date.valueOf(entry);
                    java.sql.Date returnDate = java.sql.Date.valueOf(ret);
                    if (!returnDate.after(entryDate)) {
                        JOptionPane.showMessageDialog(frame, "Return date must be after entry date.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid date format. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try (Connection conn = DBConnection.getConnection()) {
                    String sqlPrice = "SELECT price_per_day FROM cars WHERE carID = ?";
                    PreparedStatement stmt = conn.prepareStatement(sqlPrice);
                    stmt.setInt(1, carID);
                    ResultSet rs = stmt.executeQuery();
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(frame, "Price information not found.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int pricePerDay = rs.getInt("price_per_day");

                    java.sql.Date entryDate = java.sql.Date.valueOf(entry);
                    java.sql.Date returnDate = java.sql.Date.valueOf(ret);
                    long diffMillis = returnDate.getTime() - entryDate.getTime();
                    long diffDays = (diffMillis / (1000 * 60 * 60 * 24)) + 1;

                    long totalRent = diffDays * pricePerDay;

                    String message = "=== Car Rental Chalan ===\n\n"
                            + "Customer ID: " + cid + "\n"
                            + "Car Name: " + carName + "\n"
                            + "Entry Date: " + entry + "\n"
                            + "Return Date: " + ret + "\n"
                            + "Price per Day: Rs. " + pricePerDay + "\n"
                            + "Total Days: " + diffDays + "\n"
                            + "------------------------\n"
                            + "Total Rent: Rs. " + totalRent + "\n\n"
                            + "Thank you for choosing us!";

                    JOptionPane.showMessageDialog(frame, message, "Rental Chalan", JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.setVisible(true);
    }

    // Load available cars into combo box
    private void loadAvailableCarNames() {
        carNameComboBox.removeAllItems();
        carNameToId.clear();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT carID, carType FROM cars WHERE Availability = 'yes'");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("carType");
                int id = rs.getInt("carID");
                carNameComboBox.addItem(name);
                carNameToId.put(name, id);
            }

            if (carNameComboBox.getItemCount() == 0) {
                carNameComboBox.addItem("No cars available");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading cars: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new bookcar();
    }
}