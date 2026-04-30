package com.banking.kyc.service;

import com.banking.kyc.model.BankAccount;
import com.banking.kyc.model.Customer;
import com.banking.kyc.model.KYCValidationResult;
import com.banking.kyc.util.IDGenerator;
import com.banking.kyc.validator.KYCValidator;

import java.util.*;
import java.util.stream.Collectors;

/**
 * KYCService - Core business logic for KYC verification and account management
 */
public class KYCService {

    private final KYCValidator validator;
    private final Map<String, Customer> customerDatabase;
    private final Map<String, BankAccount> accountDatabase;

    public KYCService() {
        this.validator = new KYCValidator();
        this.customerDatabase = new LinkedHashMap<>();
        this.accountDatabase = new LinkedHashMap<>();
    }

    /**
     * Process KYC for a new customer
     * @return KYCProcessResult containing validation result and customer info
     */
    public KYCProcessResult processKYC(Customer customer) {
        System.out.println("\n[KYC SERVICE] Processing KYC for: " + customer.getFullName());

        // Step 1: Generate Customer ID
        String customerId = IDGenerator.generateCustomerId();
        customer.setCustomerId(customerId);

        // Step 2: Run validation
        KYCValidationResult validationResult = validator.validate(customer);
        validationResult.printReport();

        // Step 3: Set KYC status based on validation
        if (validationResult.isValid()) {
            customer.setKycStatus(Customer.KYCStatus.VERIFIED);

            // Step 4: Create bank account on successful KYC
            BankAccount account = createBankAccount(customer);
            customer.setAccountNumber(account.getAccountNumber());

            // Step 5: Store in database
            customerDatabase.put(customerId, customer);
            accountDatabase.put(account.getAccountNumber(), account);

            System.out.println("[KYC SERVICE] ✓ KYC VERIFIED - Account created: " + account.getAccountNumber());
            return new KYCProcessResult(true, customer, account, validationResult,
                    "KYC verification successful! Account created successfully.");
        } else {
            customer.setKycStatus(Customer.KYCStatus.REJECTED);
            customer.setRejectionReason(String.join("; ", validationResult.getErrors()));

            // Still store customer with rejected status
            customerDatabase.put(customerId, customer);

            String reason = validationResult.getChecks().stream()
                    .filter(c -> !c.isPassed())
                    .map(c -> c.getDisplayName() + ": " + c.getMessage())
                    .collect(Collectors.joining(", "));

            System.out.println("[KYC SERVICE] ✗ KYC REJECTED - Reason: " + reason);
            return new KYCProcessResult(false, customer, null, validationResult,
                    "KYC verification failed. Please correct the errors and resubmit.");
        }
    }

    /**
     * Create a new bank account for a verified customer
     */
    private BankAccount createBankAccount(Customer customer) {
        BankAccount account = new BankAccount(customer.getCustomerId(), BankAccount.AccountType.SAVINGS);
        String accountNumber = IDGenerator.generateAccountNumber();
        account.setAccountNumber(accountNumber);
        account.setBalance(0.0);
        return account;
    }

    /**
     * Look up a customer by ID
     */
    public Optional<Customer> getCustomer(String customerId) {
        return Optional.ofNullable(customerDatabase.get(customerId));
    }

    /**
     * Get all customers
     */
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customerDatabase.values());
    }

    /**
     * Get KYC statistics
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new LinkedHashMap<>();
        List<Customer> customers = getAllCustomers();
        stats.put("total", customers.size());
        stats.put("verified", customers.stream()
                .filter(c -> c.getKycStatus() == Customer.KYCStatus.VERIFIED).count());
        stats.put("rejected", customers.stream()
                .filter(c -> c.getKycStatus() == Customer.KYCStatus.REJECTED).count());
        stats.put("pending", customers.stream()
                .filter(c -> c.getKycStatus() == Customer.KYCStatus.PENDING).count());
        return stats;
    }

    // ─── Inner Result Class ──────────────────────────────────────────────────

    public static class KYCProcessResult {
        private final boolean success;
        private final Customer customer;
        private final BankAccount account;
        private final KYCValidationResult validationResult;
        private final String message;

        public KYCProcessResult(boolean success, Customer customer, BankAccount account,
                                KYCValidationResult validationResult, String message) {
            this.success = success;
            this.customer = customer;
            this.account = account;
            this.validationResult = validationResult;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public Customer getCustomer() { return customer; }
        public BankAccount getAccount() { return account; }
        public KYCValidationResult getValidationResult() { return validationResult; }
        public String getMessage() { return message; }
    }
}
