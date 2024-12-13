package FinanceApplication;
public class Income {
    private String source; 
    private double amount; 

    public Income(String source, double amount) {
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
    //toString method for income
    public String toString() {
        return "Source: " + source + ", Amount: $" + String.format("%.2f", amount);
    }
}
