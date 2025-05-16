package warManagement;

import java.awt.Color;
import java.awt.Font;
import java.io.Serial;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JOptionPane;

import Equipment.Equipment_details;
import Mission.Mission_details;
import Personnel.Login;
import Supply.Supply_details;
import Units.Units_Interface;
import util.AbstractDetailsFrame;

public class WarManagement extends AbstractDetailsFrame {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            new WarManagement().display();
        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public WarManagement() {
        super("WarAnalyze", 773, 529);
        // ...existing UI setup code using inherited contentPane and helper methods...
        createIconLabel("/pics/PI.jpg", 28, 40, 109, 109);
        createNavButton("Personnel", Login.class, 28, 173, 128, 23);
        createIconLabel("/pics/UI.png", 186, 47, 89, 101);
        createNavButton("Units", Units_Interface.class, 188, 173, 103, 23);
        createIconLabel("/pics/MI.png", 313, 47, 109, 101);
        createNavButton("Missions", Mission_details.class, 313, 173, 109, 23);
        createIconLabel("/pics/EI.png", 466, 47, 109, 101);
        createNavButton("Equipments", Equipment_details.class, 454, 173, 147, 23);
        createIconLabel("/pics/SI.png", 615, 47, 103, 101);
        createNavButton("Supplies", Supply_details.class, 609, 173, 109, 23);
        createTextLabel("War Analysis Dashboard", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 50), Color.WHITE, 55, 195, 651, 101);
    }

    // New helper for navigation buttons
    private <T extends AbstractDetailsFrame> void createNavButton(String name, Class<T> cls, int x, int y, int w, int h) {
        createButton(name, new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20), Color.WHITE, Color.BLACK, x, y, w, h, e -> {
            try {
                T frame = cls.getDeclaredConstructor().newInstance();
                frame.display();
                dispose();
            } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
                System.err.println("Database error: " + ex.getMessage());
                JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
