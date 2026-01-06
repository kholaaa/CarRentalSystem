package car.rental.system;

    import javax.swing.*;
    import java.awt.*;
    import java.awt.event.*;
 
public class dashboard {

    JFrame frame = new JFrame();
    JLabel backgroundLabel = new JLabel();

    JButton addCarButton = new JButton("Add car");
    JButton viewCarButton = new JButton("VIEW AVAILABLE CARS");
    JButton customerButton = new JButton("CUSTOMER DETAILS");
    JButton bookCarButton = new JButton("BOOK A CAR");
    JButton returnCarButton = new JButton("RETURN A CAR");
    JButton reportButton = new JButton("GENERATE REPORT");
    JButton logoutButton = new JButton("Logout");

    public dashboard() {
        // Get screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        // Load and scale background image
        ImageIcon bgIcon = new ImageIcon("C:\\CarRentalImages\\dashboard pic.jpg");
        Image scaled = bgIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        backgroundLabel.setIcon(new ImageIcon(scaled));
        backgroundLabel.setBounds(0, 0, width, height);
        backgroundLabel.setLayout(null); // Allow absolute positioning of buttons

        // Frame setup
        frame.setSize(width, height);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(backgroundLabel);
        
        
        // Set bounds for buttons on the right side
        int buttonWidth = 200;
        int buttonHeight = 60;
        int startY = height / 2 - 320;
        int spacing = 90;

        addCarButton.setBounds(width - buttonWidth - 50, startY, buttonWidth, buttonHeight);
        viewCarButton .setBounds(width - buttonWidth - 50, startY + spacing, buttonWidth, buttonHeight);
        customerButton .setBounds(width - buttonWidth - 50, startY + spacing*2, buttonWidth, buttonHeight);
        bookCarButton.setBounds(width - buttonWidth - 50, startY + spacing * 3, buttonWidth, buttonHeight);
        returnCarButton.setBounds(width - buttonWidth - 50, startY + spacing * 4, buttonWidth, buttonHeight);
        reportButton.setBounds(width - buttonWidth - 50, startY + spacing * 5, buttonWidth, buttonHeight);
        logoutButton.setBounds(width - buttonWidth - 50, startY + spacing * 6, buttonWidth, buttonHeight);

         // Title label
        JLabel titleLabel = new JLabel("Rental Rules");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.black);
        titleLabel.setBounds(50, startY, 400, 30);
        backgroundLabel.add(titleLabel);

// Rule 1
        JLabel rule1 = new JLabel("1. Customer must provide valid ID.");
        rule1.setFont(new Font("Arial", Font.PLAIN, 18));
        rule1.setForeground(Color.black);
        rule1.setBounds(50, startY + 50, 500, 25);
        backgroundLabel.add(rule1);

// Rule 2
        JLabel rule2 = new JLabel("2. All dues must be cleared before return.");
        rule2.setFont(new Font("Arial", Font.PLAIN, 18));
        rule2.setForeground(Color.black);
        rule2.setBounds(50, startY + 90, 500, 25);
        backgroundLabel.add(rule2);

// Rule 3
        JLabel rule3 = new JLabel("3. Late return will incur additional fees.");
        rule3.setFont(new Font("Arial", Font.PLAIN, 18));
        rule3.setForeground(Color.black);
        rule3.setBounds(50, startY + 130, 500, 25);
        backgroundLabel.add(rule3);

// Rule 4
        JLabel rule4 = new JLabel("4. Fuel must be refilled to at least 50%.");
        rule4.setFont(new Font("Arial", Font.PLAIN, 18));
        rule4.setForeground(Color.black);
        rule4.setBounds(50, startY + 170, 500, 25);
        backgroundLabel.add(rule4);
        
        // Add buttons directly to the backgroundLabel
        backgroundLabel.add(addCarButton);
        backgroundLabel.add(viewCarButton );
        backgroundLabel.add(customerButton );
        backgroundLabel.add(bookCarButton);
        backgroundLabel.add(returnCarButton);
        backgroundLabel.add(reportButton);
        backgroundLabel.add(logoutButton);
        
       addCarButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
               new add_car();
               //frame.dispose();
            }
        });
       
       viewCarButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                
                new ViewAvailableCars();
                
            }
        });
       
       customerButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                
                new customer();
                
            }
        });
       
       bookCarButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                
                new bookcar();
                
            }
        });
       
       returnCarButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                
                new return_car();
                
            }
        });
       
       reportButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                
               new generate_report();
                
            }
        });

        // Logout action
        logoutButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                
                JOptionPane.showMessageDialog(null, "Logged out.");
                frame.dispose();
                // Optionally: new login();
                
            }
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new dashboard();
    }
}