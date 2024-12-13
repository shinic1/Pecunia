package FinanceApplication;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FinancialDictionary {
    private Map<String, String> terms;

    public FinancialDictionary() {
        terms = new HashMap<>(); 
        // Financial Terms
        terms.put("Income", "Money received regularly for work, through investments, or other sources.");
        terms.put("Expense", "Money spent on goods, services, or bills.");
        terms.put("Budget", "A plan for managing income and expenses within a set period.");
        terms.put("Savings", "Money set aside for future use rather than spent immediately.");
        terms.put("Debt", "Money owed to another person or institution, typically with interest.");
        terms.put("Interest", "The cost of borrowing money or the earnings from lending money.");
        terms.put("Principal", "The original sum of money borrowed or invested, excluding interest.");
        terms.put("Investment", "The act of allocating money or resources to an asset with the expectation of generating a return over time.");

        // Investment Terms
        terms.put("Asset", "Anything of value owned, such as cash, property, or stocks.");
        terms.put("Liability", "Financial obligations or debts owed to others.");
        terms.put("Equity", "The value of an asset minus liabilities, often referring to ownership in a company.");
        terms.put("Stock", "A share in the ownership of a company, representing a claim on part of its assets and earnings.");
        terms.put("Bond", "A fixed-income investment where an investor loans money to an entity for a defined period at a fixed interest rate.");
        terms.put("MutualFund", "An investment vehicle pooling money from many investors to buy a diversified portfolio of securities.");
        terms.put("EFT", "A collection of securities traded on a stock exchange, similar to stocks.");
        terms.put("Portfolio", "A collection of financial investments like stocks, bonds, and cash equivalents.");
        terms.put("Dividend", "A portion of a company's earnings distributed to shareholders.");

        // Banking Terms
        terms.put("CheckingAccount", "A bank account allowing easy access to funds for daily transactions.");
        terms.put("SavingsAccount", "A bank account that earns interest on deposited money.");
        terms.put("CertificateofDeposit", "A savings account with a fixed interest rate and fixed term of withdrawal.");
        terms.put("Overdraft", "Spending more money than is available in an account, resulting in a negative balance.");
        terms.put("APR", "The annualized cost of borrowing money, including interest and fees.");
        terms.put("APY", "The effective annual rate of return, taking into account the effect of compounding interest.");

        // Credit and Loans
        terms.put("CreditScore", "A numerical representation of a person's creditworthiness based on their credit history.");
        terms.put("CreditReport", "A record of an individual's credit history, including loans and payment behavior.");
        terms.put("Mortgage", "A loan secured by real estate property, paid back in installments.");
        terms.put("Refinancing", "Replacing an existing loan with a new loan, often to secure better terms.");
        terms.put("Collateral", " An asset pledged as security for a loan, which the lender can seize if the loan is not repaid.");
        
        // Tax Terms
        terms.put("TaxableIncome", "The portion of income subject to taxation after deductions and exemptions.");
        terms.put("Deduction", "An expense subtracted from gross income to reduce taxable income.");
        terms.put("TaxCredit", "A direct reduction in the amount of tax owed, often for specific activities or demographics.");
        terms.put("CapitalGainsTax", "A tax on profits from the sale of assets like stocks or property.");
        terms.put("EstateTax", "A tax on the transfer of an estate after someone dies.");

        // Retirement Terms
        terms.put("401(k)", "A retirement savings plan sponsored by an employer, allowing tax-deferred contributions.");
        terms.put("IRA", "A tax-advantaged account for individual retirement savings.");
        terms.put("RothIRA", "A retirement account allowing tax-free withdrawals after retirement but funded with after-tax dollars.");
        terms.put("Pension", "A retirement plan that pays a fixed income based on employment and salary history.");

        // Economic Terms
        terms.put("Inflation", "The rate at which the general price level of goods and services rises, reducing purchasing power.");
        terms.put("Deflation", "A decrease in the general price level of goods and services, increasing purchasing power.");
        terms.put("Recession", "A period of economic decline, typically defined by a fall in GDP for two consecutive quarters.");
        terms.put("GDP", "The total value of goods and services produced in a country over a specific period.");
        
        // Insurance Terms
        terms.put("Premium", "The cost paid for an insurance policy, typically on a monthly or annual basis.");
        terms.put("Deductible", "The amount an insured person must pay out of pocket before insurance coverage begins.");
        terms.put("Coverage", "The scope of protection provided by an insurance policy.");
        terms.put("Claim", "A request made to an insurance company for payment after an insured event.");

        // Advanced Terms
        terms.put("Hedge", "An investment to reduce the risk of adverse price movements in an asset.");
        terms.put("Leverage", "The use of borrowed funds to increase potential returns on investment.");
        terms.put("Liquidity", "The ease with which an asset can be converted into cash without affecting its market price.");
        terms.put("Yield", "The income generated by an investment, expressed as a percentage of the investment's cost.");
        terms.put("CapitalExpenditure", "Funds used by a company to acquire or upgrade physical assets.");
        terms.put("Depreciation", "The reduction in the value of an asset over time due to wear and tear.");
    }

    public String getDefinition(String term) {
        return terms.getOrDefault(term, "Term not found"); 
    }
    public Set<String> getTerms() {
        return terms.keySet();
    }

    public void addTerm(String term, String definition) {
        terms.put(term, definition);
    }

    public void removeTerm(String term) {
        terms.remove(term);
    }
}
