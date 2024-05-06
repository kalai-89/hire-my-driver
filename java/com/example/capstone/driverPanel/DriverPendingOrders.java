package com.example.capstone.driverPanel;

public class DriverPendingOrders {

    private String CustomerId, OfferID, OfferName, OfferQuantity, Price, TotalPrice;

    public DriverPendingOrders(String offerID, String offerName, String offerQuantity, String price, String totalPrice, String customerId) {
        CustomerId = customerId;
        OfferID = offerID;
        OfferName = offerName;
        OfferQuantity = offerQuantity;
        Price = price;
        TotalPrice = totalPrice;

    }

    public DriverPendingOrders() {

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

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }


}