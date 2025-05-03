package com.example.medcare;

public class Medic {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String specialization;
    private String role; // "Nurse", "Physiotherapist"
    private int experience; // in years
    private double fees;
    private double rating;
    private boolean available;
    private String pictureURL;

    public Medic(){

    }



    public Medic(String id, String name, String email, String phone, String role, int experience,
                 double fees, double rating, boolean available, String specialization, String pictureURL) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.experience = experience;
        this.fees = fees;
        this.rating = rating;
        this.available = available;
        this.specialization = specialization;
        this.pictureURL = pictureURL;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public double getFees() {
        return fees;
    }

    public void setFees(double fees) {
        this.fees = fees;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }
}