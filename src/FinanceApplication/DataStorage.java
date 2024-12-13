package FinanceApplication;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class DataStorage {

    //Save income data to income_data.txt
    public static void saveIncomeData(List<Income> incomes) {
        String filePath = Paths.get("income_data.txt").toAbsolutePath().toString();
        boolean append = Files.exists(Paths.get(filePath)); 
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, append))) {
            for (Income income : incomes) {
                writer.write("Source: " + income.getSource() + ", Amount: $" + income.getAmount());
                writer.newLine();
            }
            System.out.println("Income data saved to " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving income data: " + e.getMessage());
        }
    }

    //Save income data to expense_data.txt
    public static void saveExpenseData(List<Expense> expenses) {
        String filePath = Paths.get("expense_data.txt").toAbsolutePath().toString();
        boolean append = Files.exists(Paths.get(filePath));  
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, append))) {
            for (Expense expense : expenses) {
                writer.write("Category: " + expense.getSource() + ", Amount: $" + expense.getAmount());
                writer.newLine();
            }
            System.out.println("Expense data saved to " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving expense data: " + e.getMessage());
        }
    }
}
