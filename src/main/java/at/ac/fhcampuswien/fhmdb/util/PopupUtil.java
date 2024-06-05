package at.ac.fhcampuswien.fhmdb.util;

import at.ac.fhcampuswien.fhmdb.exception.ApplicationException;
import javafx.scene.control.Alert;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PopupUtil {
    private static void showError(String title, String message, String details) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(details);
        alert.showAndWait();
    }

    private static void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showPopup(ApplicationException e) {
        String title = "An error occurred: " + e.getType();
        String message = "Error Code: " + e.getErrorCode();
        String details = e.getMessage();
        PopupUtil.showError(title, message, details);
    }

    public static void showPopup(String message) {
        PopupUtil.showMessage("Watchlist", message);
    }
}