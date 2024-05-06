package com.example.capstone.driverPanel;

public class Cart {

    private String CustomerId,OfferID,OfferName,OfferQuantity,Price,Totalprice;

    public Cart(String customerId, String offerID, String offerName, String offerQuantity, String price, String totalprice) {
        CustomerId = customerId;
        OfferID = offerID;
        OfferName = offerName;
        OfferQuantity = offerQuantity;
        Price = price;
        Totalprice = totalprice;
    }

    public Cart() {
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getOfferID() {
        return OfferID;
    }

    public void setOfferID(String offerID) {
        OfferID = offerID;
    }

    public String getOfferName() {
        return OfferName;
    }

    public void setOfferName(String offerName) {
        OfferName = offerName;
    }

    public String getOfferQuantity() {
        return OfferQuantity;
    }

    public void setOfferQuantity(String offerQuantity) {
        OfferQuantity = offerQuantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getTotalprice() {
        return Totalprice;
    }

    public void setTotalprice(String totalprice) {
        Totalprice = totalprice;
    }
}