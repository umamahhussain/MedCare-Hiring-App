package com.example.medcare;

public class Appointments {
    private String id;
    private String userId;
    private String medicId;
    private String medicName;
    private String date;
    private String time;
    private String notes;
    private double fees;
    private String status; // e.g. "pending", "confirmed", "cancelled"

    public Appointments() {
        // Default constructor required for Firebase
    }

    public Appointments(String id, String userId, String medicId, String medicName, String date, String time, String notes, double fees, String status) {
        this.id = id;
        this.userId = userId;
        this.medicId = medicId;
        this.medicName = medicName;
        this.date = date;
        this.time = time;
        this.notes = notes;
        this.fees = fees;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMedicId() {
        return medicId;
    }

    public void setMedicId(String medicId) {
        this.medicId = medicId;
    }

    public String getMedicName() {
        return medicName;
    }

    public void setMedicName(String medicName) {
        this.medicName = medicName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public double getFees() {
        return fees;
    }

    public void setFees(double fees) {
        this.fees = fees;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
