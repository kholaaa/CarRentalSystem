package car.rental.system;

import java.awt.*;
import javax.swing.*;

public class loading {

    JFrame frame = new JFrame();
    //constructor
    public loading() {
        // Load the image
        ImageIcon icon = new ImageIcon("c:\\CarRentalImages\\loading page 1.jpg"); 

        // Create a label with the image
        JLabel backgroundLabel = new JLabel(icon);

        // Set layout to null so we can manually resize the label
        frame.setLayout(null);

        // Get screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();

        // Resize image to fill the screen
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaledImage);
        backgroundLabel.setIcon(icon);
        backgroundLabel.setBounds(0, 0, width, height);

        // Remove title bar
        frame.setUndecorated(true);

        // Set frame size
        frame.setSize(width, height);

        // Add label
        frame.add(backgroundLabel);

        // Show full screen
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);

        // Optional: Close on any click
       // Show loading2 and close this when clicked
        backgroundLabel.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent e) {
            new loading2(frame);
            frame.dispose();
        }
          });

    }
    
    public JFrame getFrame() {
    return frame;
}


public static void main(String[] args) {
    // Start the full-screen background
    loading background = new loading();

    // Create the Timer with traditional ActionListener
   Timer timer = new Timer(1000, new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent e) {
        new loading2(background.getFrame());
        }
    });


    timer.setRepeats(false); // Only run once
    timer.start();
}


    }
