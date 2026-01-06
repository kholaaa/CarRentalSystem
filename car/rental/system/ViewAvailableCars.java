package car.rental.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ViewAvailableCars {

    JFrame frame = new JFrame("Available Cars");
    JLabel backgroundLabel = new JLabel();
    JTable carsTable;
    JButton backButton = new JButton("Back");
    JLabel titleLable=new JLabel("VIEW AVAILABLE CARS");

    public ViewAvailableCars() {
        // Frame size
        int width = 610;
        int height = 470;

        // Background image (optional)
        ImageIcon bgIcon = new ImageIcon("C:\\CarRentalImages\\Available cars.jpg");
        Image scaled = bgIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        backgroundLabel.setIcon(new ImageIcon(scaled));
        backgroundLabel.setBounds(0, 0, width, height);
        backgroundLabel.setLayout(null);
        
        titleLable.setBounds(150, 30, 300, 40);
        titleLable.setFont(new Font("Arial", Font.BOLD, 24));
        titleLable.setForeground(Color.black);
        backgroundLabel.add(titleLable);

        frame.setSize(width, height);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(backgroundLabel);
        frame.setLocation(50, 100);

        // Table model setup
        String[] columnNames = {"Car ID", "Model", "Type", "Color", "price_per_day","Availability"};
        Object[][] data = fetchCarsData();

        carsTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(carsTable);
        scrollPane.setPreferredSize(new Dimension(300,300));
        scrollPane.setBounds(50, 80, 500, 250);
        backgroundLabel.add(scrollPane);

        // Back button
        backButton.setBounds(250, 370, 100, 35);
        backButton.setBackground(Color.DARK_GRAY);
        backButton.setForeground(Color.WHITE);

        backButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
                new dashboard();
            }
        });

        backgroundLabel.add(backButton);

        frame.setVisible(true);
    }

    // Fetch cars data from DB
    private Object[][] fetchCarsData() {
        Object[][] data = new Object[0][];
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT carid, carmodel, cartype, colour, price_per_day,Availability FROM cars";
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);

            // Count rows
            rs.last();
            int rowCount = rs.getRow();
            rs.beforeFirst();
            

            data = new Object[rowCount][6];
            int i = 0;
            while (rs.next()) {
                data[i][0] = rs.getInt("carid");
                data[i][1] = rs.getString("carmodel");
                data[i][2] = rs.getString("cartype");
                data[i][3] = rs.getString("colour");
                data[i][4] = rs.getInt("price_per_day");
                data[i][5] = rs.getString("availability");
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database error:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        return data;
    }

    public static void main(String[] args) {
        new ViewAvailableCars();
    }
}