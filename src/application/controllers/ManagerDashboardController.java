package application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class ManagerDashboardController {
    String current = "dashboard";
    
    @FXML
    private AnchorPane addMember;

    @FXML
    private AnchorPane dashboard;

    private void disableNode() {
        if (current.equals("dashboard")) dashboard.setVisible(false);
        if (current.equals("addMember")) addMember.setVisible(false);
    }

    @FXML
    private void switchToDashboard(ActionEvent event) {
        disableNode();
        current = "dashboard";
        dashboard.setVisible(true);
    }

    @FXML
    private void switchToAddMember(ActionEvent event) {
        disableNode();
        current = "addMember";
        addMember.setVisible(true);
    }
}
