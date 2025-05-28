package com.warManagementGUI;

import com.warManagementGUI.Equipment.Equipment_details;
import com.warManagementGUI.Mission.Mission_details;
import com.warManagementGUI.Personnel.Login;
import com.warManagementGUI.Supply.Supply_details;
import com.warManagementGUI.Units.Units_Interface;
import com.warManagementGUI.util.AbstractDetailsStage;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class WarManagement extends AbstractDetailsStage {
    
    public WarManagement() {
        super("WarAnalyze", 773, 529);
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
        createTextLabel("War Analysis Dashboard", Font.font("Times New Roman", FontWeight.BOLD, 50), Color.WHITE, 55, 195, 651, 101);
        createIconLabel("/pics/quote1.jpg", 150, 300, 450, 180); // Added quote1.jpg image to dashboard
    }
}
