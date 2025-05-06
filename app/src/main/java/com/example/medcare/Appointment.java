package com.example.medcare;

public class Appointment {
    private String id;                  // Appointment ID
    private String userId;             // Patient/User ID
    private String medicId;            // Doctor/Medic ID
    private String userName;          // user's name
    private String profileImageUrl;    // Doctor's profile image
    private String date;               // Date of appointment
    private String time;               // Time of appointment
    private String location;           // Location of appointment
    private String notes;              // Notes from patient
    private String medicName;
    private double fees;               // Consultation fees
    private String status;             // "pending", "confirmed", etc.

    public Appointment() {
        // Required for Firebase
    }

    public Appointment(String id, String userId, String medicId, String userName, String profileImageUrl,
                       String date, String time, String location, String notes, double fees, String status,
                       String medicName) {
        this.id = id;
        this.userId = userId;
        this.medicId = medicId;
        this.userName = userName;
        this.profileImageUrl = profileImageUrl;
        this.date = date;
        this.time = time;
        this.location = location;
        this.notes = notes;
        this.medicName = medicName;
        this.fees = fees;
        this.status = status;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getMedicId() {
        return medicId;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public String getNotes() {
        return notes;
    }

    public double getFees() {
        return fees;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setMedicId(String medicId) {
        this.medicId = medicId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setFees(double fees) {
        this.fees = fees;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMedicName() {
        return medicName;
    }

    public void setMedicName(String medicName) {
        this.medicName = medicName;
    }
}
