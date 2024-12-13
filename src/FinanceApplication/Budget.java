package FinanceApplication;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 The Budget class manages incomes and expenses, calculates total income, expenses,net balance, and maintains histories for income and expense entries.
 It supports saving and loading from a file for persistence.
 */
public class Budget {

    private final List<Income> incomes; // List to store all income entries
    private final List<Expense> expenses; // List to store all expense entries

    // Observable lists for tracking income and expense histories
    private transient ObservableList<String> incomeHistory;
    private transient ObservableList<String> expenseHistory;

    
     //Constructor initializes the income and expense lists and transient fields.
     
    public Budget() {
        incomes = new ArrayList<>();
        expenses = new ArrayList<>();
        initializeTransientFields(); // Initialize transient fields
    }

    
     //Initializes transient fields for income and expense history as observable lists.
     
    private void initializeTransientFields() {
        incomeHistory = FXCollections.observableArrayList();
        expenseHistory = FXCollections.observableArrayList();
    }

    
     //Adds an income entry to the list and updates the income history.
  
    public void addIncome(Income income) {
        incomes.add(income);
        if (incomeHistory != null) {
            incomeHistory.add("Source: " + income.getSource() + ", Amount: $" + String.format("%.2f", income.getAmount()));
        }
    }

     //Adds an expense entry to the list and updates the expense history.

    public void addExpense(Expense expense) {
        expenses.add(expense);
        if (expenseHistory != null) {
            expenseHistory.add("Category: " + expense.getSource() + ", Amount: $" + String.format("%.2f", expense.getAmount()));
        }
    }

   
    //Calculates the total income.
  
    public double getTotalIncome() {
        return incomes.stream().mapToDouble(Income::getAmount).sum();
    }

     //Calculates the total expenses.

    public double getTotalExpenses() {
        return expenses.stream().mapToDouble(Expense::getAmount).sum();
    }

    //Calculates income minus total expenses.

    public double getNetBalance() {
        return getTotalIncome() - getTotalExpenses();
    }

    public ObservableList<String> getIncomeHistory() {
        return incomeHistory;
    }

    public ObservableList<String> getExpenseHistory() {
        return expenseHistory;
    }

     //Saves the income and expense data to a file.

    public void saveToFile(String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Save incomes
            writer.write("INCOMES\n");
            for (Income income : incomes) {
                writer.write(income.getSource() + "," + income.getAmount() + "\n");
            }

            // Save expenses
            writer.write("EXPENSES\n");
            for (Expense expense : expenses) {
                writer.write(expense.getSource() + "," + expense.getAmount() + "\n");
            }
        }
    }

     //Loads budget data from a file and reconstructs the income and expense lists.
 
    public static Budget loadFromFile(String fileName) throws IOException {
        Budget budget = new Budget();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean isIncomeSection = false;
            boolean isExpenseSection = false;

            while ((line = reader.readLine()) != null) {
                if (line.equals("INCOMES")) {
                    isIncomeSection = true;
                    isExpenseSection = false;
                } else if (line.equals("EXPENSES")) {
                    isIncomeSection = false;
                    isExpenseSection = true;
                } else {
                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        String source = parts[0];
                        double amount = Double.parseDouble(parts[1]);

                        if (isIncomeSection) {
                            budget.addIncome(new Income(source, amount));
                        } else if (isExpenseSection) {
                            budget.addExpense(new Expense(source, amount));
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // Handle file not found scenario
        }

        return budget;
    }
}
