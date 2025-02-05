package com.example.databaseproject;

public class Employee {
    private int SSN;
    private String name;
    private String dateOfBirth;
    private String dateOfJoin;
    private int rate;
    private String email;
    private String phoneNumber;
    private String address;
    private String type;  // New attribute

    // Constructor
    public Employee(int SSN, String name, String dateOfBirth, String dateOfJoin, int rate, String address, String email, String phoneNumber, String type) {
        this.SSN = SSN;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.dateOfJoin = dateOfJoin;
        this.rate = rate;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.type = type;
    }

    // Getter and Setter methods
    public int getSSN() {
        return SSN;
    }

    public void setSSN(int SSN) {
        this.SSN = SSN;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDateOfJoin() {
        return dateOfJoin;
    }

    public void setDateOfJoin(String dateOfJoin) {
        this.dateOfJoin = dateOfJoin;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
