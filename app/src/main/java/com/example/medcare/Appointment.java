package com.example.medcare;

public class Appointment {

    private String appointmentId;  // Unique ID for appointment
    private String userId;          // ID of the patient/user
    private String doctorId;        // ID of the doctor
    private String doctorName;
    private String appointmentTime;
    private String location;
    private String profileImageUrl;
    private String status;          // Optional: "upcoming", "completed", etc.

    // Empty constructor for Firebase
    public Appointment() {
    }

    public Appointment(String appointmentId, String userId, String doctorId, String doctorName,
                       String appointmentTime, String location, String profileImageUrl, String status) {
        this.appointmentId = appointmentId;
        this.userId = userId;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.appointmentTime = appointmentTime;
        this.location = location;
        this.profileImageUrl = profileImageUrl;
        this.status = status;
    }

    // Getters
    public String getAppointmentId() {
        return appointmentId;
    }

    public String getUserId() {
        return userId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public String getLocation() {
        return location;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
