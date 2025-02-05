package com.example.databaseproject;

public class Stock {
    private int ST_id;
    private String name;
    private int size;




    public Stock(int ST_id, String shname, int size) {
        this.ST_id = ST_id;
        this.name = shname;
        this.size = size;

    }

    public Stock( String shname, int size) {

        this.name = shname;
        this.size = size;

    }


    // getters and setters for all fields
    public int getST_id() { return ST_id; }
    public void setST_id(int ST_id) { this.ST_id = ST_id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }


}

