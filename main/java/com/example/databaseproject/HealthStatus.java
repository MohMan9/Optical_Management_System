package com.example.databaseproject;

public class HealthStatus {
    private int HS_id;
    private String AX;
    private String SPH;
    private String CYL;
    private int customer_id;
    private String test_eye;
    private String testdata;

    public HealthStatus(int HS_id, String AX, String SPH, String CYL, int customer_id, String test_eye, String testdata) {
        this.HS_id = HS_id;
        this.AX = AX;
        this.SPH = SPH;
        this.CYL = CYL;
        this.customer_id = customer_id;
        this.test_eye = test_eye;
        this.testdata = testdata;
    }

    // Getters and Setters
    public int getHS_id() {
        return HS_id;
    }

    public void setHS_id(int HS_id) {
        this.HS_id = HS_id;
    }

    public String getAX() {
        return AX;
    }

    public void setAX(String AX) {
        this.AX = AX;
    }

    public String getSPH() {
        return SPH;
    }

    public void setSPH(String SPH) {
        this.SPH = SPH;
    }

    public String getCYL() {
        return CYL;
    }

    public void setCYL(String CYL) {
        this.CYL = CYL;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getTest_eye() {
        return test_eye;
    }

    public void setTest_eye(String test_eye) {
        this.test_eye = test_eye;
    }

    public String getTestdata() {
        return testdata;
    }

    public void setTestdata(String testdata) {
        this.testdata = testdata;
    }
}
