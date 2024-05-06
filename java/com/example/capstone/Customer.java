package com.example.capstone;

public class Customer {

    private String Area, City, ConfirmPassword, Emailid, Fname, House, Lname, Mobile, Password, Postcode, State, Suburban;

    // Press Alt+Insert


    public Customer(String area, String city, String confirmPassword, String emailid, String fname, String house, String lname, String mobile, String password, String postcode, String state, String suburban) {
        this.Area = area;
        City = city;
        ConfirmPassword = confirmPassword;
        Emailid = emailid;
        Fname = fname;
        House = house;
        Lname = lname;
        Mobile = mobile;
        Password = password;
        Postcode = postcode;
        State = state;
        Suburban = suburban;
    }
    public Customer() {
    }

    public String getArea() {
        return Area;
    }

    public String getCity() {
        return City;
    }

    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public String getEmailid() {
        return Emailid;
    }

    public String getFname() {
        return Fname;
    }

    public String getHouse() {
        return House;
    }

    public String getLname() {
        return Lname;
    }

    public String getMobile() {
        return Mobile;
    }

    public String getPassword() {
        return Password;
    }

    public String getPostcode() {
        return Postcode;
    }

    public String getState() {
        return State;
    }

    public String getSuburban() {
        return Suburban;
    }
}