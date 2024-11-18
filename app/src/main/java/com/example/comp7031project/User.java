package com.example.comp7031project;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String address;
    private String photoPath;
    private String status;
    private boolean isFocused;

    public User(int id, String firstName, String lastName, String address, String photoPath, String status, boolean isFocused) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.photoPath = photoPath;
        this.status = status;
        this.isFocused = isFocused;
    }

    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getAddress() { return address; }
    public String getPhotoPath() { return photoPath; }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isFocused() {
        return isFocused;
    }

    public void setFocused(boolean focused) {
        isFocused = focused;
    }

    public String getFullInfo() {
        return firstName + " " + lastName + " " + address;
    }
}
