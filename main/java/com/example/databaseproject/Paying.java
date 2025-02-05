

package com.example.databaseproject;

public class Paying {
    private int p_id;
    private String type;
    private double type_price;
    private String currency;
    private int customer_id; // New field for customer ID
    private String date; // New field for date

    public Paying(int p_id, String type, double type_price, String currency, int customer_id, String date) {
        this.p_id = p_id;
        this.type = type;
        this.type_price = type_price;
        this.currency = currency;
        this.customer_id = customer_id;
        this.date = date;
    }

    // Getters and Setters
    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getType_price() {
        return type_price;
    }

    public void setType_price(double type_price) {
        this.type_price = type_price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}