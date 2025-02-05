package com.example.databaseproject;

public class Sale {
    private int sale_number;
    private int quantity;
    private String period;
    private double s_rate;
    private int product_id;

    public Sale(int sale_number, int quantity, String period, double s_rate, int product_id) {
        this.sale_number = sale_number;
        this.quantity = quantity;
        this.period = period;
        this.s_rate = s_rate;
        this.product_id = product_id;
    }

    // Getters and Setters
    public int getSale_number() {
        return sale_number;
    }

    public void setSale_number(int sale_number) {
        this.sale_number = sale_number;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public double getS_rate() {
        return s_rate;
    }

    public void setS_rate(double s_rate) {
        this.s_rate = s_rate;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }
}
