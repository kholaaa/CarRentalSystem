package car.rental.system;

import java.awt.*;
import javax.swing.*;

public class loading2 {

    JFrame frame = new JFrame();

    public loading2(JFrame backgroundFrame) {
        frame.setUndecorated(true);
        frame.setSize(400, 350);
        frame.setLocationRelativeTo(backgroundFrame);
        frame.setLayout(new BorderLayout());

        // Panel with background color
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(0, 0, 0, 180));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Image (logo)
        ImageIcon logoIcon = new ImageIcon("c:\\CarRentalImages\\loading2..pic.jpg");
        Image scaledLogo = logoIcon.getImage().getScaledInstance(350, 250, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title label
        JLabel titleLabel = new JLabel("CAR RENTAL SYSTEM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Loading label
        JLabel loadingLabel = new JLabel("LOADING...");
        loadingLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        loadingLabel.setForeground(Color.WHITE);
        loadingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(logoLabel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(loadingLabel);
        contentPanel.add(Box.createVerticalStrut(20));

        frame.add(contentPanel, BorderLayout.CENTER);
        frame.setVisible(true);

        // Optional: auto-close after 3 seconds
       Timer timer = new Timer(3000, new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent e) {
        frame.dispose();
        //backgroundFrame.dispose();
        new login();
    }
});
timer.setRepeats(false);
timer.start();

    }
    
}