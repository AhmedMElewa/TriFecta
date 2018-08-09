package com.trifecta.mada.trifecta13.other;

import android.widget.EditText;

/**
 * Created by Mada on 2/23/2017.
 */

public class OrderModel {
    private String prId;
    private String pushId;
    private String buyerId;
    private String sellerId;
    private String Total;
    private String Commission;
    private String paymentMethod;
    private String orDate;
    private Boolean buyerConfirm;
    private Boolean sellerConfirm;
    private String prQuantity;
    private String prPrice;
    private String prPic;
    private String prName;
    private Boolean paid;
    private String buyerName;
    private String buyerStreet;
    private String buyerHomeNum;
    private String buyerCity;
    private String buyerPhone;
    private String buyerNote;
    private boolean notifyOrder;
    private String shipping;

    public OrderModel() {
    }

    public OrderModel(String prId, String pushId, String buyerId, String sellerId, String total,
                      String commission, String paymentMethod, String orDate, Boolean buyerConfirm,
                      Boolean sellerConfirm, String prQuantity, String prPrice, String prPic, String prName,
                      String buyerName, String buyerStreet, String buyerHomeNum, String buyerCity, String buyerPhone,
                      String buyerNote, Boolean paid,boolean notifyOrder, String shipping) {
        this.prId = prId;
        this.pushId = pushId;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        Total = total;
        Commission = commission;
        this.paymentMethod = paymentMethod;
        this.orDate = orDate;
        this.buyerConfirm = buyerConfirm;
        this.sellerConfirm = sellerConfirm;
        this.prQuantity = prQuantity;
        this.prPrice = prPrice;
        this.prPic = prPic;
        this.prName = prName;
        this.buyerName = buyerName;
        this.buyerStreet = buyerStreet;
        this.buyerHomeNum = buyerHomeNum;
        this.buyerCity = buyerCity;
        this.buyerPhone = buyerPhone;
        this.buyerNote = buyerNote;
        this.paid = paid;
        this.notifyOrder= notifyOrder;
        this.shipping = shipping;
    }

    public String getPrId() {
        return prId;
    }

    public void setPrId(String prId) {
        this.prId = prId;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getCommission() {
        return Commission;
    }

    public void setCommission(String commission) {
        Commission = commission;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getOrDate() {
        return orDate;
    }

    public void setOrDate(String orDate) {
        this.orDate = orDate;
    }

    public Boolean getBuyerConfirm() {
        return buyerConfirm;
    }

    public void setBuyerConfirm(Boolean buyerConfirm) {
        this.buyerConfirm = buyerConfirm;
    }

    public Boolean getSellerConfirm() {
        return sellerConfirm;
    }

    public void setSellerConfirm(Boolean sellerConfirm) {
        this.sellerConfirm = sellerConfirm;
    }

    public String getPrQuantity() {
        return prQuantity;
    }

    public void setPrQuantity(String prQuantity) {
        this.prQuantity = prQuantity;
    }

    public String getPrPrice() {
        return prPrice;
    }

    public void setPrPrice(String prPrice) {
        this.prPrice = prPrice;
    }

    public String getPrPic() {
        return prPic;
    }

    public void setPrPic(String prPic) {
        this.prPic = prPic;
    }

    public String getPrName() {
        return prName;
    }

    public void setPrName(String prName) {
        this.prName = prName;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerStreet() {
        return buyerStreet;
    }

    public void setBuyerStreet(String buyerStreet) {
        this.buyerStreet = buyerStreet;
    }

    public String getBuyerHomeNum() {
        return buyerHomeNum;
    }

    public void setBuyerHomeNum(String buyerHomeNum) {
        this.buyerHomeNum = buyerHomeNum;
    }

    public String getBuyerCity() {
        return buyerCity;
    }

    public void setBuyerCity(String buyerCity) {
        this.buyerCity = buyerCity;
    }

    public String getBuyerPhone() {
        return buyerPhone;
    }

    public void setBuyerPhone(String buyerPhone) {
        this.buyerPhone = buyerPhone;
    }

    public String getBuyerNote() {
        return buyerNote;
    }

    public void setBuyerNote(String buyerNote) {
        this.buyerNote = buyerNote;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public boolean isNotifyOrder() {
        return notifyOrder;
    }

    public void setNotifyOrder(boolean notifyOrder) {
        this.notifyOrder = notifyOrder;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }
}