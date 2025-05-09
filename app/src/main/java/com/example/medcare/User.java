package com.example.medcare;
public class User {

    private String userId;
    private String name;
    private String email;
    private String phone;
    private Integer age;
    private String role;

    private String gender;
    private String profileImageUrl;
    private String address;
    private String bloodGroup;

    public User() {
    }

    public User(String userId, String name, String email, String phone, Integer age, String role, String gender, String profileImageUrl, String address, String bloodGroup) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.age = age;
        this.role = role;
        this.gender = gender;
        this.profileImageUrl = profileImageUrl;
        this.address = address;
        this.bloodGroup = bloodGroup;
    }

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
}
