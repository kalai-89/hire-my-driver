package com.example.capstone.customerPanel;

public class OfferDetails {

    public String Offers,Quantity,Price,Description,ImageURL,RandomUID,CustomerId;

    public OfferDetails(String offers, String quantity, String price, String description, String imageURL,String randomUID,String customerId) {
        Offers = offers;
        Quantity = quantity;
        Price = price;
        Description = description;
        ImageURL = imageURL;
        RandomUID=randomUID;
        CustomerId=customerId;
    }

}