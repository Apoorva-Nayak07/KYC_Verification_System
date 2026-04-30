<<<<<<< HEAD
# KYC_Verification_System
=======
# 🏛️ SecureBank — KYC Verification System

A complete **Java-based Banking System** that implements a KYC (Know Your Customer) verification
process, with a polished **HTML/CSS/JavaScript** frontend.

---

## 📁 Project Structure

```
kyc-banking-system/
├── pom.xml
├── README.md
└── src/
    └── main/
        ├── java/com/banking/kyc/
        │   ├── model/
        │   │   ├── Customer.java              ← Customer entity with KYC fields
        │   │   ├── KYCValidationResult.java   ← Validation result model
        │   │   └── BankAccount.java           ← Bank account created on KYC pass
        │   ├── validator/
        │   │   └── KYCValidator.java          ← Core KYC validation engine (regex + rules)
        │   ├── service/
        │   │   └── KYCService.java            ← Business logic & in-memory storage
        │   ├── controller/
        │   │   └── KYCController.java         ← Console UI controller (main entry point)
        │   └── util/
        │       └── IDGenerator.java           ← Customer ID & account number generator
        └── resources/
            └── static/
                └── index.html                 ← Full-featured HTML/CSS/JS frontend
```

---

## 🔍 KYC Validation Rules

| Field            | Validation Rule                                              |
|------------------|--------------------------------------------------------------|
| Full Name        | Min 3 chars, letters + spaces only, at least 2 words        |
| Date of Birth    | Age must be between 18–100 years                            |
| Aadhaar Card     | 12 digits, must start with 2–9                              |
| PAN Card         | Format: `AAAAA9999A` (5 letters + 4 digits + 1 letter)      |
| Passport         | Format: `A1234567` (1 letter + 7 digits)                    |
| Voter ID         | Format: `ABC1234567` (3 letters + 7 digits)                 |
| Driving License  | Format: `KA0120249999999`                                   |
| Address          | 10–300 characters                                            |
| PIN Code         | 6 digits, cannot start with 0                               |
| Mobile Number    | 10 digits, must start with 6–9 (Indian format)              |
| Email            | Standard email format `user@domain.com`                     |
| Annual Income    | Must be a non-negative number                               |

---

## 🚀 How to Run

### Java Console Application

**Prerequisites:** Java 17+, Maven 3.8+

```bash
# 1. Compile the project
mvn clean compile

# 2. Run the application
mvn exec:java -Dexec.mainClass="com.banking.kyc.controller.KYCController"

# OR build and run JAR
mvn clean package
java -jar target/kyc-banking-system.jar
```

### HTML/CSS Frontend

Simply open `src/main/resources/static/index.html` in any web browser.
No server required — it runs entirely in the browser.

---

## 📊 System Flow

```
Customer Input
     │
     ▼
KYCController  ──────────────►  KYCService
(Console/Web UI)                    │
                                    ▼
                              KYCValidator
                                    │
                        ┌───────────┴───────────┐
                        │                       │
                   VERIFIED ✓              REJECTED ✗
                        │
                        ▼
                  BankAccount (created)
                        │
                        ▼
                  CustomerDatabase (stored)
```

---

## 🌟 Features

- **12 validation checks** covering all KYC fields
- **5 ID proof types**: Aadhaar, PAN, Passport, Voter ID, Driving License
- **Automatic account number generation** on successful KYC
- **Customer ID generation** (format: `CUST-YYYYMMDD-XXXXXX`)
- **In-memory database** for storing verified/rejected customers
- **Console menu system** with demo data runner
- **HTML frontend** with real-time validation, tabs, and progress bar

---

## 💻 Sample Output (Console)

```
╔══════════════════════════════════════════════════════════════╗
║          SECURE BANK - KYC VERIFICATION SYSTEM              ║
╚══════════════════════════════════════════════════════════════╝

========================================
       KYC VALIDATION REPORT
========================================
Overall Status: ✓ VERIFIED
Checks: 12/12 passed
----------------------------------------
[✓ PASS] Full Name          : Valid name: Rahul Kumar Sharma
[✓ PASS] Date of Birth      : Valid. Age: 34 years
[✓ PASS] Aadhaar Card       : Valid Aadhaar: XXXX-XXXX-0123
[✓ PASS] Address            : Valid address provided
...
========================================

Account Number : 123456781234567
Account Type   : Savings Account
IFSC Code      : BNKY0001234
```

---

## 🎨 Frontend Features

- **Gold & Navy** luxury banking aesthetic
- **Live validation** with error messages
- **3-tab result view**: Checks, Account Details, Summary
- **Animated progress bar** showing KYC score
- **Virtual bank card** displaying account details
- **Responsive** for desktop and mobile

---

## 📋 Java Classes Overview

| Class | Role |
|---|---|
| `Customer` | Entity holding all KYC fields, status, account number |
| `KYCValidationResult` | Aggregates check results, errors, warnings |
| `BankAccount` | Created after successful KYC; has account number, IFSC, type |
| `KYCValidator` | Runs 12 regex/rule-based checks on a Customer |
| `KYCService` | Orchestrates validation, account creation, and in-memory DB |
| `KYCController` | Console UI: menus, input, result display, demo runner |
| `IDGenerator` | Generates unique Customer IDs and account numbers |

---

*Built with Java 17 · No external dependencies · Pure HTML/CSS/JS frontend*
>>>>>>> 1f80243 (Initial commit)
