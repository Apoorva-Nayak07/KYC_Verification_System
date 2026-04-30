package com.banking.kyc.controller;

import com.banking.kyc.model.BankAccount;
import com.banking.kyc.model.Customer;
import com.banking.kyc.model.KYCValidationResult;
import com.banking.kyc.service.KYCService;

import java.util.Map;
import java.util.Scanner;

/**
 * KYCController - Console-based UI controller for the KYC Banking System
 * Handles user interactions via command-line interface
 */
public class KYCController {

    private static final KYCService kycService = new KYCService();
    private static final Scanner scanner = new Scanner(System.in);

    private static final String BANNER = """
            ╔══════════════════════════════════════════════════════════════╗
            ║          SECURE BANK - KYC VERIFICATION SYSTEM              ║
            ║          Know Your Customer | Account Opening Portal        ║
            ╚══════════════════════════════════════════════════════════════╝
            """;

    public static void main(String[] args) {
        System.out.println(BANNER);
        boolean running = true;

        while (running) {
            printMainMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> startKYCProcess();
                case 2 -> viewCustomerStatus();
                case 3 -> viewAllCustomers();
                case 4 -> viewStatistics();
                case 5 -> runDemoKYC();
                case 6 -> {
                    System.out.println("\nThank you for using SecureBank KYC System. Goodbye!");
                    running = false;
                }
                default -> System.out.println("\n[ERROR] Invalid option. Please select 1-6.\n");
            }
        }
        scanner.close();
    }

    private static void printMainMenu() {
        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│           MAIN MENU                  │");
        System.out.println("├─────────────────────────────────────┤");
        System.out.println("│  1. Start New KYC Verification       │");
        System.out.println("│  2. Check Customer KYC Status        │");
        System.out.println("│  3. View All Customers               │");
        System.out.println("│  4. View KYC Statistics              │");
        System.out.println("│  5. Run Demo (Sample Data)           │");
        System.out.println("│  6. Exit                             │");
        System.out.println("└─────────────────────────────────────┘");
    }

    // ─── KYC Process ──────────────────────────────────────────────────────────

    private static void startKYCProcess() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║              NEW CUSTOMER KYC VERIFICATION                   ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println("\nPlease provide the following details:\n");

        Customer customer = new Customer();

        // Personal Details
        System.out.println("--- PERSONAL INFORMATION ---");
        customer.setFullName(getStringInput("Full Name (First Last): "));
        customer.setDateOfBirth(getStringInput("Date of Birth (YYYY-MM-DD): "));

        // ID Proof
        System.out.println("\n--- IDENTITY PROOF ---");
        System.out.println("Supported ID Types: AADHAAR, PAN, PASSPORT, VOTER_ID, DRIVING_LICENSE");
        customer.setIdProofType(getStringInput("ID Proof Type: ").toUpperCase());
        customer.setIdProofNumber(getStringInput("ID Proof Number: "));

        // Address
        System.out.println("\n--- ADDRESS DETAILS ---");
        customer.setAddress(getStringInput("Street Address: "));
        customer.setCity(getStringInput("City: "));
        customer.setState(getStringInput("State: "));
        customer.setPinCode(getStringInput("PIN Code: "));

        // Contact
        System.out.println("\n--- CONTACT INFORMATION ---");
        customer.setMobileNumber(getStringInput("Mobile Number (10 digits): "));
        customer.setEmail(getStringInput("Email Address: "));

        // Financial
        System.out.println("\n--- FINANCIAL DETAILS ---");
        customer.setOccupation(getStringInput("Occupation: "));
        customer.setAnnualIncome(getStringInput("Annual Income (in ₹): "));

        // Process KYC
        System.out.println("\n[PROCESSING] Submitting KYC verification...");
        System.out.println("━".repeat(62));

        KYCService.KYCProcessResult result = kycService.processKYC(customer);
        displayKYCResult(result);
    }

    private static void displayKYCResult(KYCService.KYCProcessResult result) {
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                  KYC VERIFICATION RESULT                    ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");

        Customer customer = result.getCustomer();
        KYCValidationResult validation = result.getValidationResult();

        System.out.printf("%-25s : %s%n", "Customer ID", customer.getCustomerId());
        System.out.printf("%-25s : %s%n", "Customer Name", customer.getFullName());
        System.out.printf("%-25s : %s%n", "ID Proof Type", customer.getIdProofType());
        System.out.printf("%-25s : %s%n", "Registration Time", customer.getRegistrationTime());
        System.out.println("─".repeat(62));

        // Validation Checks
        System.out.println("\nVALIDATION CHECKS:");
        for (KYCValidationResult.ValidationCheck check : validation.getChecks()) {
            String status = check.isPassed() ? "✓" : "✗";
            System.out.printf("  %s %-22s : %s%n", status, check.getDisplayName(), check.getMessage());
        }

        System.out.println("\n" + "═".repeat(62));
        System.out.printf("Checks Passed: %d / %d%n", validation.getPassedChecks(), validation.getTotalChecks());
        System.out.println("Summary      : " + validation.getSummary());

        if (result.isSuccess()) {
            BankAccount account = result.getAccount();
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║   ✓  KYC VERIFIED - ACCOUNT CREATED  ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.printf("%-25s : %s%n", "Account Number", account.getAccountNumber());
            System.out.printf("%-25s : %s%n", "Account Type", account.getAccountType().getDisplayName());
            System.out.printf("%-25s : %s%n", "IFSC Code", account.getIfscCode());
            System.out.printf("%-25s : %s%n", "Branch", account.getBranchName());
            System.out.printf("%-25s : %s%n", "Account Status", account.getStatus());
            System.out.printf("%-25s : %s%n", "Opening Balance", "₹0.00");
            System.out.printf("%-25s : %s%n", "Created At", account.getCreatedAt());
        } else {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║   ✗  KYC REJECTED                    ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.println("Reason: " + result.getMessage());
            System.out.println("\nFailed Checks:");
            validation.getChecks().stream()
                    .filter(c -> !c.isPassed())
                    .forEach(c -> System.out.println("  - " + c.getDisplayName() + ": " + c.getMessage()));
            System.out.println("\nPlease correct the above and resubmit.");
        }
        System.out.println("═".repeat(62));
    }

    // ─── Status Lookup ────────────────────────────────────────────────────────

    private static void viewCustomerStatus() {
        System.out.println("\n--- CHECK KYC STATUS ---");
        String customerId = getStringInput("Enter Customer ID: ");
        kycService.getCustomer(customerId).ifPresentOrElse(
                customer -> {
                    System.out.println("\n  Customer   : " + customer.getFullName());
                    System.out.println("  ID         : " + customer.getCustomerId());
                    System.out.println("  KYC Status : " + customer.getKycStatus());
                    if (customer.getAccountNumber() != null) {
                        System.out.println("  Account No : " + customer.getAccountNumber());
                    }
                    if (customer.getRejectionReason() != null) {
                        System.out.println("  Reason     : " + customer.getRejectionReason());
                    }
                },
                () -> System.out.println("[ERROR] Customer not found: " + customerId)
        );
    }

    // ─── Customer List ────────────────────────────────────────────────────────

    private static void viewAllCustomers() {
        System.out.println("\n--- ALL CUSTOMERS ---");
        var customers = kycService.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers registered yet.");
            return;
        }
        System.out.printf("%-30s %-25s %-15s %-20s%n",
                "Name", "Customer ID", "KYC Status", "Account Number");
        System.out.println("─".repeat(95));
        for (Customer c : customers) {
            System.out.printf("%-30s %-25s %-15s %-20s%n",
                    c.getFullName(),
                    c.getCustomerId(),
                    c.getKycStatus(),
                    c.getAccountNumber() != null ? c.getAccountNumber() : "N/A");
        }
    }

    // ─── Statistics ───────────────────────────────────────────────────────────

    private static void viewStatistics() {
        System.out.println("\n--- KYC STATISTICS ---");
        Map<String, Object> stats = kycService.getStatistics();
        System.out.println("  Total Applications   : " + stats.get("total"));
        System.out.println("  ✓ Verified           : " + stats.get("verified"));
        System.out.println("  ✗ Rejected           : " + stats.get("rejected"));
        System.out.println("  ⏳ Pending            : " + stats.get("pending"));
    }

    // ─── Demo ─────────────────────────────────────────────────────────────────

    private static void runDemoKYC() {
        System.out.println("\n[DEMO] Running KYC with sample data...\n");

        // Valid customer
        Customer validCustomer = new Customer(
                "Rahul Kumar Sharma", "1990-05-15", "AADHAAR",
                "234567890123", "123 MG Road, Sector 5",
                "Bengaluru", "Karnataka", "560001",
                "9876543210", "rahul.sharma@email.com",
                "Software Engineer", "850000"
        );

        System.out.println("=== DEMO 1: Valid Customer ===");
        KYCService.KYCProcessResult result1 = kycService.processKYC(validCustomer);
        displayKYCResult(result1);

        // Invalid customer
        Customer invalidCustomer = new Customer(
                "R", "2010-01-01", "PAN",
                "INVALID123", "123",
                "Mumbai", "Maharashtra", "00000",
                "1234567890", "not-an-email",
                "Student", "-5000"
        );

        System.out.println("\n=== DEMO 2: Invalid Customer ===");
        KYCService.KYCProcessResult result2 = kycService.processKYC(invalidCustomer);
        displayKYCResult(result2);
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        try {
            int val = Integer.parseInt(scanner.nextLine().trim());
            return val;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
