package FinanceApplication;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {

    @FXML private Label totalIncomeLabel;
    @FXML private Label totalExpensesLabel;
    @FXML private Label netBalanceLabel;
    @FXML private ListView<String> incomeListView;
    @FXML private ListView<String> expenseListView;
    @FXML private TextField paymentDescriptionField;
    @FXML private DatePicker paymentDatePicker;
    @FXML private ListView<String> upcomingPaymentsList;
    @FXML private TextField paymentAmountField;
    @FXML private TextField nextPaymentDateField;
    @FXML private ComboBox<String> sortComboBox;
    @FXML private CheckBox recurringCheckBox;
    @FXML private AnchorPane rootPane;
    @FXML
    private ProgressBar budgetGoalProgressBar;
    @FXML
    private TextField budgetGoalField;
    @FXML
    private Label budgetGoalLabel;
    @FXML
    private Button setBudgetGoalButton;
    private double monthlyBudgetGoal = 0.0;


    private ObservableList<String> upcomingPayments;
    private Budget budget;

    private final Settings settings = Settings.getInstance();
    @FXML
    public void initialize() {

        //load budget_data
        if (budget == null) {
            try {
                budget = Budget.loadFromFile("budget_data.txt");
            } catch (IOException e) {
                budget = new Budget();
                System.err.println("Failed to load budget data. Initializing with an empty budget.");
            }
        }

        //Init upcoming payments and progress bar
        upcomingPayments = FXCollections.observableArrayList(loadPaymentsFromFile());
        if (upcomingPaymentsList != null) {
            upcomingPaymentsList.setItems(upcomingPayments);
        }

        budgetGoalProgressBar.setProgress(0);
        budgetGoalLabel.setText("No goal set");

        loadBudgetGoal();

        updateDashboard();
        updateNextPaymentDate();
        bindSettingsToUI();

        sortComboBox.setItems(FXCollections.observableArrayList("Date", "Amount"));
        sortComboBox.setOnAction(e -> sortPayments());
    }

    public void setBudget(Budget budget) {
        this.budget = budget != null ? budget : new Budget();
        updateDashboard();
    }

    private void updateDashboard() {
        if (budget == null) {
            budget = new Budget();
        }

        totalIncomeLabel.setText(formatCurrency(budget.getTotalIncome()));
        totalExpensesLabel.setText(formatCurrency(budget.getTotalExpenses()));

        double netBalance = budget.getTotalIncome() - budget.getTotalExpenses();
        netBalanceLabel.setText(formatCurrency(netBalance));

        if (incomeListView != null) {
            incomeListView.setItems(FXCollections.observableArrayList(budget.getIncomeHistory()));
        }
        if (expenseListView != null) {
            expenseListView.setItems(FXCollections.observableArrayList(budget.getExpenseHistory()));
        }
        updateBudgetGoalProgress();
    }

    @FXML
    public void handleSetBudgetGoal() {
        //parse data if successful
        String goalText = budgetGoalField.getText().trim();
        try {
            double goal = Double.parseDouble(goalText);
            if (goal <= 0) {
                showAlert("Input Error", "Budget goal must be a positive number.");
                return;
            }

            monthlyBudgetGoal = goal;
            saveBudgetGoal(goal); // Save the goal to a file
            updateBudgetGoalProgress();
            budgetGoalLabel.setText("Goal: " + formatCurrency(monthlyBudgetGoal));
            showAlert("Success", "Budget goal set successfully.");
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter a valid number for the budget goal.");
        }
    }
    
    private void updateBudgetGoalProgress() {
        if (monthlyBudgetGoal > 0) {
            // Calculate progress as the spent amount relative to the budget goal
            double spentAmount = budget.getTotalExpenses();
            double progress = spentAmount / monthlyBudgetGoal;

            // Update the progress bar, ensuring progress stays between 0 and 1
            budgetGoalProgressBar.setProgress(Math.max(0, Math.min(progress, 1.0)));

            // Update the goal label with remaining and total goal info
            double remainingBalance = Math.max(0, monthlyBudgetGoal - spentAmount);
            budgetGoalLabel.setText("Goal: " + formatCurrency(monthlyBudgetGoal) +
                    " | Remaining: " + formatCurrency(remainingBalance));
        } else {
            // Reset progress bar if no goal is set
            budgetGoalProgressBar.setProgress(0);
            budgetGoalLabel.setText("No goal set");
        }
    }



    
    private void saveBudgetGoal(double goal) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("budget_goal.txt"))) {
            writer.write(String.valueOf(goal));
        } catch (IOException e) {
            System.err.println("Failed to save budget goal.");
        }
    }

    private void loadBudgetGoal() {
        File file = new File("budget_goal.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = reader.readLine();
                monthlyBudgetGoal = Double.parseDouble(line);
                budgetGoalLabel.setText("Goal: " + formatCurrency(monthlyBudgetGoal));
                updateBudgetGoalProgress();
            } catch (IOException | NumberFormatException e) {
                System.err.println("Failed to load budget goal. Initializing with no goal.");
            }
        }
    }

    private void bindSettingsToUI() {
        // Bind currency updates
        settings.currencyProperty().addListener((obs, oldVal, newVal) -> updateDashboard());

        // Bind theme updates
        settings.isLightModeProperty().addListener((obs, oldVal, isLightMode) -> applyTheme(isLightMode));

        // Apply the current theme
        applyTheme(settings.isLightMode());
        refreshUpcomingPayments();
    }

    private void applyTheme(boolean isLightMode) {
        if (rootPane == null) {
            System.err.println("RootPane is not initialized yet.");
            return;
        }

        String lightTheme =  "-fx-background-color: #0a0f23; -fx-text-fill: white;" ;
        String darkTheme = "-fx-background-color: white; -fx-text-fill: black;";

        // Apply the appropriate theme
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


    
    private void sortPayments() {
        String sortBy = sortComboBox.getValue();
        if (sortBy == null) return;

        if ("Date".equals(sortBy)) {
            upcomingPayments.sort((p1, p2) -> {
                try {
                    LocalDate date1 = LocalDate.parse(p1.split("Due: ")[1].split(",")[0]);
                    LocalDate date2 = LocalDate.parse(p2.split("Due: ")[1].split(",")[0]);
                    return date1.compareTo(date2);
                } catch (Exception e) {
                    return 0;
                }
            });
        } else if ("Amount".equals(sortBy)) {
            upcomingPayments.sort((p1, p2) -> {
                try {
                    double amount1 = Double.parseDouble(p1.split("Amount: ")[1].replace("$", ""));
                    double amount2 = Double.parseDouble(p2.split("Amount: ")[1].replace("$", ""));
                    return Double.compare(amount1, amount2);
                } catch (Exception e) {
                    return 0;
                }
            });
        }
        upcomingPaymentsList.setItems(upcomingPayments);
    }

    private void updateNextPaymentDate() {
        if (upcomingPayments == null || upcomingPayments.isEmpty()) {
            nextPaymentDateField.setText("No Payments Scheduled");
            return;
        }

        LocalDate nearestDate = null;

        for (String payment : upcomingPayments) {
            try {
                String dueDateString = payment.split("Due: ")[1].split(",")[0];
                LocalDate dueDate = LocalDate.parse(dueDateString);

                if (nearestDate == null || dueDate.isBefore(nearestDate)) {
                    nearestDate = dueDate;
                }
            } catch (Exception e) {
                System.err.println("Error parsing payment date: " + payment);
            }
        }

        nextPaymentDateField.setText(nearestDate != null ? nearestDate.toString() : "No Payments Scheduled");
    }
    
    @FXML
    public void handleDeletePayment() {
        String selectedPayment = upcomingPaymentsList.getSelectionModel().getSelectedItem();

        if (selectedPayment == null) {
            showAlert("Selection Error", "Please select a payment to delete.");
            return;
        }
        upcomingPayments.remove(selectedPayment);
        savePaymentsToFile(upcomingPayments);
        updateNextPaymentDate();

        showAlert("Success", "Selected payment deleted successfully.");
    }


    @FXML
    public void handleSavePayment() {
        String description = paymentDescriptionField.getText().trim();
        String amountText = paymentAmountField.getText().trim();
        if (description.isEmpty() || paymentDatePicker.getValue() == null || amountText.isEmpty()) {
            showAlert("Input Error", "All fields (description, due date, and amount) are required.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            if (amount < 0) {
                showAlert("Input Error", "Amount cannot be negative.");
                return;
            }

            String dueDate = paymentDatePicker.getValue().toString();
            String paymentEntry = "Description: " + description + ", Due: " + dueDate + ", Amount: " + formatCurrency(amount);

            upcomingPayments.add(paymentEntry);

            if (recurringCheckBox.isSelected()) {
                LocalDate initialDueDate = paymentDatePicker.getValue();
                for (int i = 1; i <= 12; i++) { 
                    LocalDate nextDate = initialDueDate.plusMonths(i);
                    String recurringEntry = "Description: " + description + ", Due: " + nextDate + ", Amount: " + formatCurrency(amount);
                    upcomingPayments.add(recurringEntry);
                }
            }

            savePaymentsToFile(upcomingPayments);
            refreshUpcomingPayments(); // Refresh the list with updated formatting

            // Clear input fields
            paymentDescriptionField.clear();
            paymentDatePicker.setValue(null);
            paymentAmountField.clear();
            recurringCheckBox.setSelected(false);

            showAlert("Success", "Payment saved successfully.");
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Amount must be a valid number.");
        }
    }


    private void refreshUpcomingPayments() {
        // Create a new formatted list using the current currency
        ObservableList<String> formattedPayments = FXCollections.observableArrayList();

        for (String payment : upcomingPayments) {
            try {
                // Extract and reformat the amount dynamically
                String[] parts = payment.split(", Amount: ");
                String descriptionAndDate = parts[0];
                double amount = Double.parseDouble(parts[1].replaceAll("[^\\d.]", "")); // Parse numeric amount
                formattedPayments.add(descriptionAndDate + ", Amount: " + formatCurrency(amount));
            } catch (Exception e) {
                System.err.println("Error parsing payment: " + payment);
                formattedPayments.add(payment); // Keep the original in case of errors
            }
        }

        // Update the ListView
        upcomingPaymentsList.setItems(formattedPayments);
    }

    
    

    private void savePaymentsToFile(ObservableList<String> payments) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("upcoming_payments.txt"))) {
            for (String payment : payments) {
                writer.write(payment);
                writer.newLine();
            }
        } catch (IOException e) {
            showAlert("Error", "Failed to save payments.");
        }
    }

    private List<String> loadPaymentsFromFile() {
        List<String> payments = new ArrayList<>();
        File file = new File("upcoming_payments.txt");

        if (!file.exists()) {
            System.out.println("Payments file does not exist. Initializing empty list.");
            return payments;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                payments.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading payments file. Initializing empty list.");
            e.printStackTrace();
        }

        return payments;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadView(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            if (fxmlFile.equals("Dashboard.fxml")) {
                DashboardController controller = loader.getController();
                controller.setBudget(this.budget);
            }

            Stage primaryStage = (Stage) netBalanceLabel.getScene().getWindow();
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle(title);
        } catch (IOException e) {
            showAlert("Error", "Unable to load " + title + " view.");
            e.printStackTrace();
        }
    }

    @FXML
    public void goToDashboard() {
        loadView("Dashboard.fxml", "Dashboard");
    }

    @FXML
    public void goToDictionary() {
        loadView("Dictionary.fxml", "Dictionary");
    }

    @FXML
    public void goToSettings() {
        loadView("Settings.fxml", "Settings");
    }

    @FXML
    public void goToIncome() {
        loadView("Income.fxml", "Manage Income");
    }

    @FXML
    public void goToExpense() {
        loadView("Expense.fxml", "Manage Expenses");
    }

    @FXML
    public void goToCalc() {
        loadView("Calc.fxml", "Quick Calc");
    }
}
