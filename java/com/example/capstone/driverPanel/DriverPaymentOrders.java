package com.example.capstone.driverPanel;

public class DriverPaymentOrders {

    private String CustomerId,OfferId,OfferName,OfferPrice,OfferQuantity,RandomUID,TotalPrice,UserId;

    public DriverPaymentOrders(String customerId, String offerId, String offerName, String offerPrice, String offerQuantity, String randomUID, String totalPrice, String userId) {
        CustomerId = customerId;
        OfferId = offerId;
        OfferName = offerName;
        OfferPrice = offerPrice;
        OfferQuantity = offerQuantity;
        RandomUID = randomUID;
        TotalPrice = totalPrice;
        UserId = userId;
    }

    public DriverPaymentOrders()
    {

    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getOfferId() {
        return OfferId;
    }

    public void setOfferId(String offerId) {
        OfferId = offerId;
    }

    public String getOfferName() {
        return OfferName;
    }

    public void setOfferName(String offerName) {
        OfferName = offerName;
    }

    public String getOfferPrice() {
        return OfferPrice;
    }

    public void setOfferPrice(String offerPrice) {
        OfferPrice = offerPrice;
    }

    public String getOfferQuantity() {
        return OfferQuantity;
    }

    public void setOfferQuantity(String offerQuantity) {
        OfferQuantity = offerQuantity;
    }

    public String getRandomUID() {
        return RandomUID;
    }

    public void setRandomUID(String randomUID) {
        RandomUID = randomUID;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}