package com.example.databaseproject;

public class Customer {
    private int c_id;
    private String first;
    private String mid;
    private String last;
    private String dateOfBirth;
    private String address;
    private String gender;
    private String email;
    private String phoneNumber;

    public Customer(int c_id, String first, String mid, String last, String dateofbirth, String address, String gender, String email, String phonenumber1) {
        this.c_id = c_id;
        this.first = first;
        this.mid = mid;
        this.last = last;
        this.address = address;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phonenumber1;
        this.dateOfBirth = dateofbirth;
    }


    public Customer( String first, String mid, String last, String dateofbirth, String address, String gender, String email, String phonenumber1) {

        this.first = first;
        this.mid = mid;
        this.last = last;
        this.address = address;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phonenumber1;
        this.dateOfBirth = dateofbirth;
    }

    public int getC_id() {
        return c_id;
    }

    public void setC_id(int c_id) {
        this.c_id = c_id;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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
}