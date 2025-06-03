package com.orebi.dto;

import com.orebi.entity.User;

public class UserDTO {
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String role;
    private boolean isActive;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this.isActive = user.isActive();
        this.userId = user.getUserId();
        this.address = user.getAddress();
        this.name = user.getName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.role = user.getRole() != null ? user.getRole().getRoleName() : null;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

}
