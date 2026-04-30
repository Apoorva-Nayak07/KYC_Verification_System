package com.banking.kyc.validator;

import com.banking.kyc.model.Customer;
import com.banking.kyc.model.KYCValidationResult;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * KYCValidator - Core validation engine for KYC verification
 * Validates all customer details against banking regulations
 */
public class KYCValidator {

    // Regex Patterns
    private static final Pattern NAME_PATTERN =
            Pattern.compile("^[A-Za-z]+(\\s[A-Za-z]+){1,4}$");

    private static final Pattern MOBILE_PATTERN =
            Pattern.compile("^[6-9]\\d{9}$");

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$");

    private static final Pattern PINCODE_PATTERN =
            Pattern.compile("^[1-9][0-9]{5}$");

    private static final Pattern AADHAAR_PATTERN =
            Pattern.compile("^[2-9]\\d{11}$");

    private static final Pattern PAN_PATTERN =
            Pattern.compile("^[A-Z]{5}[0-9]{4}[A-Z]{1}$");

    private static final Pattern PASSPORT_PATTERN =
            Pattern.compile("^[A-Z]{1}[0-9]{7}$");

    private static final Pattern VOTER_ID_PATTERN =
            Pattern.compile("^[A-Z]{3}[0-9]{7}$");

    private static final Pattern DRIVING_LICENSE_PATTERN =
            Pattern.compile("^[A-Z]{2}[0-9]{2}\\s?[0-9]{4}[0-9]{7}$");

    private static final int MINIMUM_AGE = 18;
    private static final int MAXIMUM_AGE = 100;

    /**
     * Main KYC validation method - runs all checks
     */
    public KYCValidationResult validate(Customer customer) {
        KYCValidationResult result = new KYCValidationResult();

        // Run all validation checks
        validateFullName(customer.getFullName(), result);
        validateDateOfBirth(customer.getDateOfBirth(), result);
        validateIdProof(customer.getIdProofType(), customer.getIdProofNumber(), result);
        validateAddress(customer.getAddress(), result);
        validateCity(customer.getCity(), result);
        validateState(customer.getState(), result);
        validatePinCode(customer.getPinCode(), result);
        validateMobileNumber(customer.getMobileNumber(), result);
        validateEmail(customer.getEmail(), result);
        validateOccupation(customer.getOccupation(), result);
        validateAnnualIncome(customer.getAnnualIncome(), result);

        // Set summary
        long passed = result.getPassedChecks();
        long total = result.getTotalChecks();
        if (result.isValid()) {
            result.setSummary(String.format(
                "KYC verification successful. All %d checks passed. Account creation approved.", total));
        } else {
            result.setSummary(String.format(
                "KYC verification failed. %d/%d checks passed. Please correct the errors and resubmit.", passed, total));
        }

        return result;
    }

    // ─── Individual Field Validators ───────────────────────────────────────────

    private void validateFullName(String name, KYCValidationResult result) {
        if (name == null || name.trim().isEmpty()) {
            result.addCheck("fullName", "Full Name", false, "Name is required");
            return;
        }
        name = name.trim();
        if (name.length() < 3) {
            result.addCheck("fullName", "Full Name", false,
                    "Name must be at least 3 characters long");
        } else if (name.length() > 100) {
            result.addCheck("fullName", "Full Name", false,
                    "Name must not exceed 100 characters");
        } else if (!NAME_PATTERN.matcher(name).matches()) {
            result.addCheck("fullName", "Full Name", false,
                    "Name must contain only letters and spaces, with at least first and last name");
        } else {
            result.addCheck("fullName", "Full Name", true,
                    "Valid name: " + name);
        }
    }

    private void validateDateOfBirth(String dob, KYCValidationResult result) {
        if (dob == null || dob.trim().isEmpty()) {
            result.addCheck("dateOfBirth", "Date of Birth", false, "Date of birth is required");
            return;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate birthDate = LocalDate.parse(dob, formatter);
            LocalDate today = LocalDate.now();

            if (birthDate.isAfter(today)) {
                result.addCheck("dateOfBirth", "Date of Birth", false,
                        "Date of birth cannot be in the future");
                return;
            }

            int age = Period.between(birthDate, today).getYears();
            if (age < MINIMUM_AGE) {
                result.addCheck("dateOfBirth", "Date of Birth", false,
                        String.format("Minimum age requirement is %d years. Current age: %d years",
                                MINIMUM_AGE, age));
            } else if (age > MAXIMUM_AGE) {
                result.addCheck("dateOfBirth", "Date of Birth", false,
                        String.format("Age %d exceeds maximum allowed age of %d years", age, MAXIMUM_AGE));
            } else {
                result.addCheck("dateOfBirth", "Date of Birth", true,
                        String.format("Valid date of birth. Age: %d years", age));
            }
        } catch (DateTimeParseException e) {
            result.addCheck("dateOfBirth", "Date of Birth", false,
                    "Invalid date format. Use YYYY-MM-DD format");
        }
    }

    private void validateIdProof(String idType, String idNumber, KYCValidationResult result) {
        if (idType == null || idType.trim().isEmpty()) {
            result.addCheck("idProofType", "ID Proof Type", false, "ID proof type is required");
            return;
        }
        if (idNumber == null || idNumber.trim().isEmpty()) {
            result.addCheck("idProofNumber", "ID Proof Number", false, "ID proof number is required");
            return;
        }

        idNumber = idNumber.trim().toUpperCase().replaceAll("\\s+", "");
        boolean valid;
        String message;

        switch (idType.toUpperCase()) {
            case "AADHAAR":
                valid = AADHAAR_PATTERN.matcher(idNumber).matches();
                message = valid ? "Valid Aadhaar number: XXXX-XXXX-" + idNumber.substring(8)
                        : "Invalid Aadhaar number. Must be 12 digits, starting with 2-9";
                result.addCheck("idProofNumber", "Aadhaar Card", valid, message);
                break;

            case "PAN":
                valid = PAN_PATTERN.matcher(idNumber).matches();
                message = valid ? "Valid PAN number: " + idNumber
                        : "Invalid PAN. Format: AAAAA9999A (5 letters, 4 digits, 1 letter)";
                result.addCheck("idProofNumber", "PAN Card", valid, message);
                break;

            case "PASSPORT":
                valid = PASSPORT_PATTERN.matcher(idNumber).matches();
                message = valid ? "Valid Passport number: " + idNumber
                        : "Invalid Passport number. Format: A1234567 (1 letter + 7 digits)";
                result.addCheck("idProofNumber", "Passport", valid, message);
                break;

            case "VOTER_ID":
                valid = VOTER_ID_PATTERN.matcher(idNumber).matches();
                message = valid ? "Valid Voter ID: " + idNumber
                        : "Invalid Voter ID. Format: ABC1234567 (3 letters + 7 digits)";
                result.addCheck("idProofNumber", "Voter ID", valid, message);
                break;

            case "DRIVING_LICENSE":
                valid = DRIVING_LICENSE_PATTERN.matcher(idNumber).matches();
                message = valid ? "Valid Driving License: " + idNumber
                        : "Invalid Driving License. Format: AA99 9999 9999999";
                result.addCheck("idProofNumber", "Driving License", valid, message);
                break;

            default:
                result.addCheck("idProofType", "ID Proof Type", false,
                        "Unknown ID type: " + idType + ". Use: AADHAAR, PAN, PASSPORT, VOTER_ID, DRIVING_LICENSE");
        }
    }

    private void validateAddress(String address, KYCValidationResult result) {
        if (address == null || address.trim().isEmpty()) {
            result.addCheck("address", "Address", false, "Address is required");
            return;
        }
        address = address.trim();
        if (address.length() < 10) {
            result.addCheck("address", "Address", false,
                    "Address is too short (minimum 10 characters)");
        } else if (address.length() > 300) {
            result.addCheck("address", "Address", false,
                    "Address is too long (maximum 300 characters)");
        } else {
            result.addCheck("address", "Address", true,
                    "Valid address provided");
        }
    }

    private void validateCity(String city, KYCValidationResult result) {
        if (city == null || city.trim().isEmpty()) {
            result.addCheck("city", "City", false, "City is required");
            return;
        }
        if (city.trim().length() < 2) {
            result.addCheck("city", "City", false, "City name is too short");
        } else {
            result.addCheck("city", "City", true, "Valid city: " + city.trim());
        }
    }

    private void validateState(String state, KYCValidationResult result) {
        if (state == null || state.trim().isEmpty()) {
            result.addCheck("state", "State", false, "State is required");
            return;
        }
        result.addCheck("state", "State", true, "Valid state: " + state.trim());
    }

    private void validatePinCode(String pinCode, KYCValidationResult result) {
        if (pinCode == null || pinCode.trim().isEmpty()) {
            result.addCheck("pinCode", "PIN Code", false, "PIN code is required");
            return;
        }
        pinCode = pinCode.trim();
        if (!PINCODE_PATTERN.matcher(pinCode).matches()) {
            result.addCheck("pinCode", "PIN Code", false,
                    "Invalid PIN code. Must be 6 digits and cannot start with 0");
        } else {
            result.addCheck("pinCode", "PIN Code", true, "Valid PIN code: " + pinCode);
        }
    }

    private void validateMobileNumber(String mobile, KYCValidationResult result) {
        if (mobile == null || mobile.trim().isEmpty()) {
            result.addCheck("mobileNumber", "Mobile Number", false, "Mobile number is required");
            return;
        }
        mobile = mobile.trim().replaceAll("[\\s\\-+]", "");
        if (mobile.startsWith("91") && mobile.length() == 12) {
            mobile = mobile.substring(2);
        }
        if (!MOBILE_PATTERN.matcher(mobile).matches()) {
            result.addCheck("mobileNumber", "Mobile Number", false,
                    "Invalid Indian mobile number. Must be 10 digits starting with 6-9");
        } else {
            result.addCheck("mobileNumber", "Mobile Number", true,
                    "Valid mobile number: +91-" + mobile.substring(0, 5) + "XXXXX");
        }
    }

    private void validateEmail(String email, KYCValidationResult result) {
        if (email == null || email.trim().isEmpty()) {
            result.addCheck("email", "Email Address", false, "Email address is required");
            return;
        }
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            result.addCheck("email", "Email Address", false,
                    "Invalid email format. Example: user@example.com");
        } else {
            result.addCheck("email", "Email Address", true,
                    "Valid email: " + email.trim());
        }
    }

    private void validateOccupation(String occupation, KYCValidationResult result) {
        if (occupation == null || occupation.trim().isEmpty()) {
            result.addCheck("occupation", "Occupation", false, "Occupation is required");
            return;
        }
        result.addCheck("occupation", "Occupation", true,
                "Occupation recorded: " + occupation.trim());
    }

    private void validateAnnualIncome(String income, KYCValidationResult result) {
        if (income == null || income.trim().isEmpty()) {
            result.addCheck("annualIncome", "Annual Income", false, "Annual income is required");
            return;
        }
        try {
            double amount = Double.parseDouble(income.trim().replaceAll("[,\\s]", ""));
            if (amount < 0) {
                result.addCheck("annualIncome", "Annual Income", false,
                        "Annual income cannot be negative");
            } else if (amount == 0) {
                result.addCheck("annualIncome", "Annual Income", true,
                        "No income recorded (e.g., student/homemaker)");
                result.addWarning("Zero income may require additional documentation");
            } else {
                result.addCheck("annualIncome", "Annual Income", true,
                        String.format("Valid annual income: ₹%.2f", amount));
            }
        } catch (NumberFormatException e) {
            result.addCheck("annualIncome", "Annual Income", false,
                    "Annual income must be a valid number");
        }
    }
}
