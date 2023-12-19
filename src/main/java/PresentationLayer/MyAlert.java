package PresentationLayer;

import javafx.scene.control.Alert;

public class MyAlert {

    public static void WarningAlert(String ContentText, String HeaderText) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(HeaderText);
        alert.setContentText(ContentText);
        alert.showAndWait();
    }
}
