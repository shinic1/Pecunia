package FinanceApplication;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LoginController {

    @FXML
    private TextField usernameField; 
    @FXML
    private PasswordField passwordField; 

    private static final String USER_FILE = "users.txt"; 
    private Map<String, String> users = new HashMap<>();

    @FXML
    public void initialize() {
        loadUsers();
    }

    @FXML
    public void handleLogin() {
        //error handling if input is invalid
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Username and password cannot be empty.");
            return;
        }

        if (users.containsKey(username) && users.get(username).equals(password)) {
            loadMainView();
        } else {
            showError("Invalid username or password.");
        }
    }

    @FXML
    public void handleRegister() {
        //error handling if input is invalid
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Username and password cannot be empty.");
            return;
        }

        if (users.containsKey(username)) {
            showError("Username already exists. Please choose a different username.");
            return;
        }

        users.put(username, password);
        saveUsers();
        showInfo("User registered successfully!");
    }

    private void loadUsers() {
        //Search in text file for existing users, if found load user
        File file = new File(USER_FILE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        users.put(parts[0], parts[1]);
                    }
                }
            } catch (IOException e) {
                showError("Failed to load user data.");
                e.printStackTrace();
            }
        }
    }

    private void saveUsers() {
        //Save user data after registration is successful
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (Map.Entry<String, String> entry : users.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            showError("Failed to save user data.");
            e.printStackTrace();
        }
    }

    private void loadMainView() {
        //Load main view after user successfully logged in
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow(); 
            stage.setScene(new Scene(root));
            stage.setTitle("Finance Application");
        } catch (IOException e) {
            showError("Unable to load main application view.");
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
