package com.banking.kyc.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Customer Model - Represents a bank customer with KYC details
 */
public class Customer {

    private String customerId;
    private String fullName;
    private String dateOfBirth;
    private String idProofType;
    private String idProofNumber;
    private String address;
    private String city;
    private String state;
    private String pinCode;
    private String mobileNumber;
    private String email;
    private String occupation;
    private String annualIncome;
    private KYCStatus kycStatus;
    private String registrationTime;
    private String accountNumber;
    private String rejectionReason;

    public enum KYCStatus {
        PENDING, VERIFIED, REJECTED, UNDER_REVIEW
    }

    public Customer() {
        this.kycStatus = KYCStatus.PENDING;
        this.registrationTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    public Customer(String fullName, String dateOfBirth, String idProofType,
                    String idProofNumber, String address, String city,
                    String state, String pinCode, String mobileNumber,
                    String email, String occupation, String annualIncome) {
        this();
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.idProofType = idProofType;
        this.idProofNumber = idProofNumber;
        this.address = address;
        this.city = city;
        this.state = state;
        this.pinCode = pinCode;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.occupation = occupation;
        this.annualIncome = annualIncome;
    }

    // Getters and Setters
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getIdProofType() { return idProofType; }
    public void setIdProofType(String idProofType) { this.idProofType = idProofType; }

    public String getIdProofNumber() { return idProofNumber; }
    public void setIdProofNumber(String idProofNumber) { this.idProofNumber = idProofNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getPinCode() { return pinCode; }
    public void setPinCode(String pinCode) { this.pinCode = pinCode; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }

    public String getAnnualIncome() { return annualIncome; }
    public void setAnnualIncome(String annualIncome) { this.annualIncome = annualIncome; }

    public KYCStatus getKycStatus() { return kycStatus; }
    public void setKycStatus(KYCStatus kycStatus) { this.kycStatus = kycStatus; }

    public String getRegistrationTime() { return registrationTime; }
    public void setRegistrationTime(String registrationTime) { this.registrationTime = registrationTime; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId='" + customerId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", idProofType='" + idProofType + '\'' +
                ", idProofNumber='" + idProofNumber + '\'' +
                ", kycStatus=" + kycStatus +
                ", accountNumber='" + accountNumber + '\'' +
                '}';
    }
}
