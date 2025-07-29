package com.planwise.model;

public class ContactMessage {
    private String name;
    private String email;
    private String phone;
    private String address;
    private String message;

    // Constructors, Getters and Setters
    public ContactMessage() {}

    public ContactMessage(String name, String email, String phone, String address, String message) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.message = message;
    }

    // getters and setters for all fields
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}