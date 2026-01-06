package car.rental.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import javax.swing.table.DefaultTableModel;

public class generate_report {
    

    JFrame frame = new JFrame("Rental Report");
    JLabel backgroundLabel = new JLabel();
    JTable table;
    JLabel titleLable=new JLabel("GENERATE REPORT");
    JLabel totalRevenueLabel = new JLabel();
    JLabel pendingCountLabel = new JLabel();
    JButton backButton = new JButton("Back");
    JButton refreshButton = new JButton("Refresh");

    public generate_report() {
        int width = 900;
        int height = 600;

        backgroundLabel.setBounds(0, 0, width, height);
        backgroundLabel.setLayout(null);
        backgroundLabel.setBackground(new Color(230,230,250)); // Light lavender or any color you like

        frame.setSize(width, height);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(150, 80);
        frame.add(backgroundLabel);

        // Table
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 70, 820, 300);
        backgroundLabel.add(scrollPane);
        
        titleLable.setBounds(280, 20, 250, 40);
        titleLable.setFont(new Font("Arial", Font.BOLD, 24));
        titleLable.setForeground(Color.black);
        backgroundLabel.add(titleLable);

        // Labels
        totalRevenueLabel.setBounds(50, 370, 400, 30);
        totalRevenueLabel.setForeground(Color.black);
        totalRevenueLabel.setFont(new Font("Arial", Font.BOLD, 14));
        backgroundLabel.add(totalRevenueLabel);

        pendingCountLabel.setBounds(50, 410, 400, 30);
        pendingCountLabel.setForeground(Color.black);
        pendingCountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        backgroundLabel.add(pendingCountLabel);

        // Buttons
        backButton.setBounds(150, 470, 150, 35);
        refreshButton.setBounds(350, 470, 150, 35);

        for (JButton btn : new JButton[]{backButton, refreshButton}) {
            btn.setBackground(Color.DARK_GRAY);
            btn.setForeground(Color.WHITE);
        }

        backgroundLabel.add(backButton);
        backgroundLabel.add(refreshButton);

        // Load data
        loadReportData();

        // Button actions
        backButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
                new dashboard();
            }
        });

        refreshButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                loadReportData();
            }
        });

        frame.setVisible(true);
    }

   private void loadReportData() {
    DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Booking ID", "Car ID", "Customer ID", "Entry Date", "Return Date", "Rent/Day", "Total Amount", "Status"},
            0
    );
    double totalRevenue = 0;
    int pendingCount = 0;

    try (Connection conn = DBConnection.getConnection()) {
        String query =
                "SELECT b.bookcarID, b.carID, b.customerID, b.entrydate, b.returndate, c.price_per_day, " +
                "r.returnID " +
                "FROM bookcar b " +
                "JOIN cars c ON b.carID = c.carID " +
                "LEFT JOIN returncar r ON b.carID = r.carID AND b.customerID = r.customerID";

        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            int bookingID = rs.getInt("bookcarID");
            int carID = rs.getInt("carID");
            int customerID = rs.getInt("customerID");
            Date entryDate = rs.getDate("entrydate");
            Date returnDate = rs.getDate("returndate");
            double rentPerDay = rs.getDouble("price_per_day");
            int returnID = rs.getInt("returnID");

            // Calculate total amount
            long days = ChronoUnit.DAYS.between(entryDate.toLocalDate(), returnDate.toLocalDate());
            if (days <= 0) days = 1;
            double total = rentPerDay * days;

            String status;
            if (rs.wasNull() || returnID == 0) {
                status = "Pending";
                pendingCount++;
            } else {
                status = "Returned";
                totalRevenue += total;
            }

            model.addRow(new Object[]{
                    bookingID, carID, customerID, entryDate, returnDate, rentPerDay, total, status
            });
        }
        rs.close();
        stmt.close();
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    table.setModel(model);

    totalRevenueLabel.setText("Total Revenue Earned: Rs. " + String.format("%.2f", totalRevenue));
    pendingCountLabel.setText("Pending Returns: " + pendingCount);
}


    public static void main(String[] args) {
        new generate_report();
    }
}