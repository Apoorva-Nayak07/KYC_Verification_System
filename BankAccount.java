package com.banking.kyc.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * BankAccount - Represents a bank account created after successful KYC
 */
public class BankAccount {

    private String accountNumber;
    private String customerId;
    private AccountType accountType;
    private double balance;
    private String createdAt;
    private AccountStatus status;
    private String ifscCode;
    private String branchName;

    public enum AccountType {
        SAVINGS("Savings Account"),
        CURRENT("Current Account"),
        FIXED_DEPOSIT("Fixed Deposit"),
        RECURRING_DEPOSIT("Recurring Deposit");

        private final String displayName;
        AccountType(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
    }

    public enum AccountStatus {
        ACTIVE, INACTIVE, FROZEN, CLOSED
    }

    public BankAccount(String customerId, AccountType accountType) {
        this.customerId = customerId;
        this.accountType = accountType;
        this.balance = 0.0;
        this.status = AccountStatus.ACTIVE;
        this.ifscCode = "BNKY0001234";
        this.branchName = "Main Branch";
        this.createdAt = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    // Getters and Setters
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getCustomerId() { return customerId; }
    public AccountType getAccountType() { return accountType; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public String getCreatedAt() { return createdAt; }
    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }

    public String getIfscCode() { return ifscCode; }
    public String getBranchName() { return branchName; }

    @Override
    public String toString() {
        return "BankAccount{" +
                "accountNumber='" + accountNumber + '\'' +
                ", accountType=" + accountType.getDisplayName() +
                ", balance=" + balance +
                ", status=" + status +
                '}';
    }
}
