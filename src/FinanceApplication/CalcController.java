package FinanceApplication;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CalcController {

    @FXML
    private Label calculationLabel;
    @FXML
    private Label totalIncomeLabel;
    @FXML
    private AnchorPane rootPane;

    private StringBuilder currentInput = new StringBuilder();
    private double currentResult = 0; 
    private String operator = ""; 
    
    private final Settings settings = Settings.getInstance();
    
    @FXML
    private void initialize() {
        bindSettingsToUI();
    }

    private void appendNumber(int number) {
        currentInput.append(number);
        updateDisplay();
    }

    private void setOperator(String operator) {
        if (currentInput.length() > 0) {
            double currentOperand = Double.parseDouble(currentInput.toString());
            if (!this.operator.isEmpty()) {
                calculateIntermediateResult(currentOperand);
            } else {
                currentResult = currentOperand; 
            }
            this.operator = operator;
            currentInput.setLength(0); 
            updateCalculationLabel(); 
        } else if (!this.operator.isEmpty()) {
            this.operator = operator;
            updateCalculationLabel();
        } else {
            showError("Please enter a number before selecting an operator.");
        }
    }

    //Addition, subtraction, multiplication and division with error handling
    private void calculateIntermediateResult(double currentOperand) {
        switch (operator) {
            case "+":
                currentResult += currentOperand;
                break;
            case "-":
                currentResult -= currentOperand;
                break;
            case "*":
                currentResult *= currentOperand;
                break;
            case "/":
                if (currentOperand == 0) {
                    showError("Cannot divide by zero.");
                    return;
                }
                currentResult /= currentOperand;
                break;
            default:
                showError("Unknown operator.");
                return;
        }
        updateCalculationLabel();
        updateDisplay();
    }

    //calculates results
    private void calculateResult() {
        if (currentInput.length() > 0 && !operator.isEmpty()) {
            double currentOperand = Double.parseDouble(currentInput.toString());
            calculateIntermediateResult(currentOperand);
            operator = ""; 
            currentInput.setLength(0);
            currentInput.append(currentResult); 
            updateCalculationLabel();
            updateDisplay();
        } else {
            showError("Incomplete operation. Please enter a number.");
        }
    }

    //clears input when button is pressed
    private void clearInput() {
        currentInput.setLength(0);
        currentResult = 0;
        operator = "";
        calculationLabel.setText(""); 
        updateDisplay();
    }

    private void updateCalculationLabel() {
        if (!operator.isEmpty() && currentInput.length() == 0) {
            calculationLabel.setText(currentResult + " " + operator);
        } else if (!operator.isEmpty()) {
            calculationLabel.setText(currentResult + " " + operator + " " + currentInput);
        } else {
            calculationLabel.setText(currentInput.toString());
        }
    }
    
    private void bindSettingsToUI() {
        // Listen for theme changes
        settings.isLightModeProperty().addListener((obs, oldVal, isLightMode) -> applyTheme(isLightMode));
        applyTheme(settings.isLightMode());

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


    private void updateDisplay() {
        totalIncomeLabel.setText(currentInput.length() > 0 ? currentInput.toString() : String.valueOf(currentResult));
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleNum1() {
        appendNumber(1);
    }

    @FXML
    private void handleNum2() {
        appendNumber(2);
    }

    @FXML
    private void handleNum3() {
        appendNumber(3);
    }

    @FXML
    private void handleNum4() {
        appendNumber(4);
    }

    @FXML
    private void handleNum5() {
        appendNumber(5);
    }

    @FXML
    private void handleNum6() {
        appendNumber(6);
    }

    @FXML
    private void handleNum7() {
        appendNumber(7);
    }

    @FXML
    private void handleNum8() {
        appendNumber(8);
    }

    @FXML
    private void handleNum9() {
        appendNumber(9);
    }

    @FXML
    private void handleNum0() {
        appendNumber(0);
    }

   
    @FXML
    private void handlePlus() {
        setOperator("+");
    }

    @FXML
    private void handleMinus() {
        setOperator("-");
    }

    @FXML
    private void handleMultiply() {
        setOperator("*");
    }

    @FXML
    private void handleDivide() {
        setOperator("/");
    }

    @FXML
    private void handleEqual() {
        calculateResult();
    }

    @FXML
    private void handleDel() {
        clearInput();
    }

    private void loadView(String fxmlFile, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage primaryStage = (Stage) totalIncomeLabel.getScene().getWindow(); 
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle(title);
        } catch (IOException e) {
            showError("Unable to load " + title + " view.");
            e.printStackTrace(); 
        }
    }

    @FXML
    public void goToCalc() {
        loadView("Calc.fxml", "Quick Calc");
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
    public void goToDictionary() {
        loadView("Dictionary.fxml", "Financial Dictionary");
    }

    @FXML
    public void goToDashboard() {
        loadView("Dashboard.fxml", "Dashboard");
    }

    @FXML
    public void goToSettings() {
        loadView("Settings.fxml", "Settings");
    }
}
