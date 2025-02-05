package com.example.databaseproject;

public class Product {
    private int id;
    private double price;
    private String brand;
    private String color;
    private int stock_id;

    public Product(int id, double price, String brand, String color, int stock_id) {
        this.id = id;
        this.price = price;
        this.brand = brand;
        this.color = color;
        this.stock_id = stock_id;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public int getStock_id() { return stock_id; }
    public void setStock_id(int stock_id) { this.stock_id = stock_id; }
}
