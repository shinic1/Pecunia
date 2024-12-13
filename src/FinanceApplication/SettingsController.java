package FinanceApplication;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import javafx.collections.FXCollections;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsController {

    @FXML
    private ComboBox<String> currencyComboBox;
    @FXML
    private RadioButton lightModeRadio;
    @FXML
    private RadioButton darkModeRadio;
    @FXML
    private AnchorPane rootPane;

    private final Settings settings = Settings.getInstance();

    @FXML
    public void initialize() {
    	
        currencyComboBox.setItems(FXCollections.observableArrayList("USD", "EUR", "GBP", "JPY", "AUD"));
        loadSettings();

        // Ensure mutual exclusivity for theme mode buttons
        lightModeRadio.setOnAction(event -> {
            if (lightModeRadio.isSelected()) {
                darkModeRadio.setSelected(false);
                settings.setLightMode(true);
                applyTheme(true);
            }
        });

        darkModeRadio.setOnAction(event -> {
            if (darkModeRadio.isSelected()) {
                lightModeRadio.setSelected(false);
                settings.setLightMode(false);
                applyTheme(false);
            }
        });

        currencyComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                settings.setCurrency(newVal);
            }
        });
    }


    private void loadSettings() {
        if (settings.getCurrency() == null) {
            settings.setCurrency("USD");
        }

        currencyComboBox.getSelectionModel().select(settings.getCurrency());

        // Set default theme to dark mode if none is set
        if (!settings.isLightMode()) {
            settings.setLightMode(false); // Ensure dark mode is the default
        }

        darkModeRadio.setSelected(!settings.isLightMode());
        lightModeRadio.setSelected(settings.isLightMode());
        applyTheme(settings.isLightMode()); // Apply the correct theme
    }
    
    private void applyTheme(boolean isLightMode) {
        if (rootPane == null) {
            System.err.println("RootPane is not initialized yet.");
            return;
        }

        String darkTheme = "-fx-background-color: white; -fx-text-fill: black;";
        String lightTheme = "-fx-background-color: #0a0f23; -fx-text-fill: white;";

        rootPane.setStyle(isLightMode ? lightTheme : darkTheme);
    }

    @FXML
    public void handleSaveSettings() {
        settings.setCurrency(currencyComboBox.getSelectionModel().getSelectedItem());
        settings.setLightMode(lightModeRadio.isSelected());

        System.out.println("Settings Saved:");
        System.out.println("Currency: " + settings.getCurrency());
        System.out.println("Theme Mode: " + (settings.isLightMode() ? "Light" : "Dark"));
        goToDashboard();
    }


    @FXML
    public void handleCancel() {
        goToDashboard();
    }

    private void goToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Parent root = loader.load();
            Stage currentStage = (Stage) currencyComboBox.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Dashboard");
        } catch (IOException e) {
            System.err.println("Error: Unable to load Dashboard.fxml.");
            e.printStackTrace();
        }
    }
}
