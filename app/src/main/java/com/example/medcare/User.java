package com.example.medcare;
public class User {

    private String userId;
    private String name;
    private String email;
    private String phone;
    private Integer age;
    private boolean isPatient;

    public User() {
    }

    // Full constructor
    public User(String userId, String name, String email, String phone, Integer age, Boolean isPatient) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.age = age;
        this.isPatient = isPatient;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getIsPatient() {
        return isPatient;
    }

    public void setIsPatient(Boolean isPatient) {
        this.isPatient = isPatient;
    }
}
