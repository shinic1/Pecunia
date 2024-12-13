package FinanceApplication;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;

public class BudgetController {

    @FXML private Label totalIncomeLabel;
    @FXML private Label totalExpensesLabel;
    @FXML private Label netBalanceLabel;

    @FXML private TextField incomeSourceField;
    @FXML private TextField incomeAmountField;
    @FXML private TextField expenseCategoryField;
    @FXML private TextField expenseAmountField;

    @FXML private ListView<String> incomeList;
    @FXML private ListView<String> expenseList;

    private Budget budget;

    public void setBudget(Budget budget) {
        this.budget = budget != null ? budget : new Budget();
        updateDashboard();
    }

    //Updates dashboard
    private void updateDashboard() {
        if (budget == null) return;

        totalIncomeLabel.setText("$" + String.format("%.2f", budget.getTotalIncome()));
        totalExpensesLabel.setText("$" + String.format("%.2f", budget.getTotalExpenses()));
        netBalanceLabel.setText("$" + String.format("%.2f", budget.getNetBalance()));

        incomeList.setItems(FXCollections.observableArrayList(budget.getIncomeHistory()));
        expenseList.setItems(FXCollections.observableArrayList(budget.getExpenseHistory()));
    }

    @FXML
    //Parses data when income is added
    public void handleAddIncome() {
        String source = incomeSourceField.getText().trim();
        String amountText = incomeAmountField.getText().trim();
        if (source.isEmpty() || !validateAmountInput(amountText, "Income")) return;

        double amount = Double.parseDouble(amountText);
        budget.addIncome(new Income(source, amount));
        updateDashboard();
    }

    //parses data when expense is added
    @FXML
    public void handleAddExpense() {
        String category = expenseCategoryField.getText().trim();
        String amountText = expenseAmountField.getText().trim();
        if (category.isEmpty() || !validateAmountInput(amountText, "Expense")) return;

        double amount = Double.parseDouble(amountText);
        budget.addExpense(new Expense(category, amount));
        updateDashboard();
    }
    
    @FXML
    public void handleRefresh() {
        System.out.println("Refresh button clicked.");

        // Clear all input fields
        incomeSourceField.clear();
        incomeAmountField.clear();
        expenseCategoryField.clear();
        expenseAmountField.clear();

        // Clear the labels
        totalIncomeLabel.setText("$0.00");
        totalExpensesLabel.setText("$0.00");
        netBalanceLabel.setText("$0.00");

        // Clear the ListViews
        incomeList.setItems(FXCollections.observableArrayList());
        expenseList.setItems(FXCollections.observableArrayList());
}


  @FXML
    public void handleSave() {
        try {
            budget.saveToFile("budget_data.txt");
            showAlert("Success", "Data saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save data.");
        }
    }

    @FXML
    public void handleLoad() {
        try {
            budget = Budget.loadFromFile("budget_data.txt");
            updateDashboard();
            showAlert("Success", "Data loaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load data.");
        }
    }

    //input validation handling
    private boolean validateAmountInput(String input, String type) {
        try {
            double value = Double.parseDouble(input);
            if (value <= 0) {
                showAlert("Validation Error", type + " amount must be greater than zero.");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            showAlert("Validation Error", type + " amount must be a valid number.");
            return false;
        }
    }

    //Used alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
