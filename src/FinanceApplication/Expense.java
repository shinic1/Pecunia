package FinanceApplication;

public class Expense {
  
    private String source;  
    private double amount;  

    public Expense(String source, double amount) {
        this.source = source;
        this.amount = amount;
    }

    public String getSource() {
        return source;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    //toString method for getting the input into the right format
    public String toString() {
        return "Source: " + source + ", Amount: $" + String.format("%.2f", amount);
    }
}