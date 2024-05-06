package com.example.capstone;

public class OfferDetails {

    public String License,Quantity,Price,Description,ImageURL,RandomUID,Customerid;
    // Alt+insert

    public OfferDetails(String license, String Days, String price, String description, String imageURL, String randomUID, String customerid) {
        License = license;
        Quantity = Days;
        Price = price;
        Description = description;
        ImageURL = imageURL;
        RandomUID = randomUID;
        Customerid = customerid;
    }
}