package FinanceApplication;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class IncomeController {

    @FXML
    private TextField sourceField; 
    @FXML
    private TextField amountField; 
    @FXML
    private ListView<String> incomeList;
    @FXML
    private AnchorPane rootPane; 

    private Budget budget;
    
    private final Settings settings = Settings.getInstance();

    public void setBudget(Budget budget) {
        this.budget = budget != null ? budget : new Budget();
        goToDashboard();
    }
    
    @FXML
    private void initialize() {
        try {
            budget = Budget.loadFromFile("budget_data.txt");
        } catch (IOException e) {
            budget = new Budget();
        }
        refreshIncomeList();
        bindSettingsToUI();
    }

    @FXML
    public void handleAddIncome() {
        String source = sourceField.getText().trim();
        String amountText = amountField.getText().trim();

        if (source.isEmpty()) {
            showAlert("Input Error", "Please enter a source for the income.");
            return;
        }

        if (amountText.isEmpty()) {
            showAlert("Input Error", "Please enter an amount for the income.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                showAlert("Input Error", "Amount must be a positive number.");
                return;
            }

            Income income = new Income(source, amount);
            budget.addIncome(income);

            refreshIncomeList();

            sourceField.clear();
            amountField.clear();

            budget.saveToFile("budget_data.txt");

            showAlert("Success", "Income added successfully.");
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Amount must be a valid number.");
        } catch (IOException e) {
            showAlert("Save Error", "Failed to save income. Please try again.");
        }
    }

    private void refreshIncomeList() {
        ObservableList<String> incomeHistory = FXCollections.observableArrayList();
        for (String income : budget.getIncomeHistory()) {
            try {
                String[] parts = income.split(", Amount: ");
                String description = parts[0];
                double amount = Double.parseDouble(parts[1].replaceAll("[^\\d.]", ""));
                incomeHistory.add(description + ", Amount: " + formatCurrency(amount));
            } catch (Exception e) {
                incomeHistory.add(income); // Keep the original in case of errors
            }
        }
        incomeList.setItems(incomeHistory);
    }
    
    private void bindSettingsToUI() {
        // Listen for currency changes and refresh the income list
        settings.currencyProperty().addListener((obs, oldVal, newVal) -> refreshIncomeList());

        // Listen for theme changes and apply them
        settings.isLightModeProperty().addListener((obs, oldVal, isLightMode) -> applyTheme(isLightMode));
        applyTheme(settings.isLightMode()); // Apply the current theme on initialization
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

    private String formatCurrency(double amount) {
        String currencySymbol;
        String currency = settings.getCurrency();

        if ("EUR".equals(currency)) {
            currencySymbol = "€";
        } else if ("GBP".equals(currency)) {
            currencySymbol = "£";
        } else if ("JPY".equals(currency)) {
            currencySymbol = "¥";
        } else if ("AUD".equals(currency)) {
            currencySymbol = "A$";
        } else {
            currencySymbol = "$"; // Default to USD
        }

        return currencySymbol + String.format("%.2f", amount);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void goToDashboard() {
        navigateTo("Dashboard.fxml", "Dashboard");
    }

    @FXML
    public void goToIncome() {
        navigateTo("Income.fxml", "Manage Income");
    }

    @FXML
    public void goToExpense() {
        navigateTo("Expense.fxml", "Manage Expenses");
    }

    @FXML
    public void goToDictionary() {
        navigateTo("Dictionary.fxml", "Financial Dictionary");
    }

    @FXML
    public void goToCalc() {
        navigateTo("Calc.fxml", "Quick Calc");
    }

    @FXML
    public void goToSettings() {
        navigateTo("Settings.fxml", "Settings");
    }

    @FXML
    public void navigateTo(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FinanceApplication/" + fxmlFile));
            Parent root = loader.load();

            if (fxmlFile.equals("Dashboard.fxml")) {
                DashboardController controller = loader.getController();
                if (controller != null) {
                    controller.setBudget(this.budget); 
                } else {
                    System.out.println("Error: DashboardController is null.");
                }
            }

            Stage primaryStage = (Stage) incomeList.getScene().getWindow();
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle(title);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Unable to load " + title + ".");
        }
    }


}
