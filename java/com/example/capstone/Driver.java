package com.example.capstone;
public class Driver {

    private String City, FirstName, LastName, Password, ConfirmPassword, EmailId, Mobileno, State, Area, LocalAddress, Suburban;

    public Driver() {
    }
    // Press Alt+insert


    public Driver(String city, String firstName, String lastName, String password, String confirmPassword, String emailId, String mobileno, String state, String area, String localAddress, String suburban) {
        this.City = city;
        FirstName = firstName;
        LastName = lastName;
        Password = password;
        ConfirmPassword = confirmPassword;
        EmailId = emailId;
        Mobileno = mobileno;
        State = state;
        Area = area;
        LocalAddress = localAddress;
        Suburban = suburban;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        ConfirmPassword = confirmPassword;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getMobileNo() {
        return Mobileno;
    }

    public void setMobileNo(String mobileno) {
        Mobileno = mobileno;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getLocalAddress() {
        return LocalAddress;
    }

    public void setLocalAddress(String localAddress) {
        LocalAddress = localAddress;
    }

    public String getSuburban() {
        return Suburban;
    }

    public void setSuburban(String suburban) {
        Suburban = suburban;
    }
}