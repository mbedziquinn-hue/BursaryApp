package com.example.bursaryapp;

public class AppliedBursary {
    private String name;
    private String status;
    private String appliedDate;

    // Required empty constructor for Firebase
    public AppliedBursary() {}

    public AppliedBursary(String name, String status, String appliedDate) {
        this.name = name;
        this.status = status;
        this.appliedDate = appliedDate;
    }

    // Getters
    public String getName() { return name; }
    public String getStatus() { return status; }
    public String getAppliedDate() { return appliedDate; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setStatus(String status) { this.status = status; }
    public void setAppliedDate(String appliedDate) { this.appliedDate = appliedDate; }
}