package com.trifecta.mada.trifecta13.other;

import android.widget.EditText;

import com.trifecta.mada.trifecta13.R;

/**
 * Created by Mada on 2/16/2017.
 */

public class StoreModel {

    private String storeId;
    private String storeTitle;
    private String storePic;
    private String language;
    private String country;
    private String Currency;
    private Boolean paypal;
    private Boolean cash;
    private Boolean creditCard;
    private String cardNumber;
    private String expireMonth;
    private String expireYear;
    private String securityNum;
    private String ballance;
    private String welcomeM;
    private String About;
    private String DeliveryPolicy;
    private String ReturnPolicy;


    public StoreModel() {
    }

    public StoreModel(String storeId, String storeTitle, String language, String country,
                      String currency, Boolean paypal, Boolean cash, Boolean creditCard, String cardNumber, String expireMonth,
                      String expireYear, String securityNum, String ballance, String welcomeM, String About, String DeliveryPolicy
                      , String ReturnPolicy, String storePic) {
        this.storeId=storeId;
        this.storeTitle = storeTitle;
        this.language = language;
        this.country = country;
        this.Currency = currency;
        this.paypal = paypal;
        this.cash = cash;
        this.creditCard = creditCard;
        this.cardNumber = cardNumber;
        this.expireMonth = expireMonth;
        this.expireYear = expireYear;
        this.securityNum = securityNum;
        this.ballance = ballance;
        this.welcomeM = welcomeM;
        this.About = About;
        this.DeliveryPolicy = DeliveryPolicy;
        this.ReturnPolicy = ReturnPolicy;
        this.storePic = storePic;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreTitle() {
        return storeTitle;
    }

    public void setStoreTitle(String storeTitle) {
        this.storeTitle = storeTitle;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public Boolean getPaypal() {
        return paypal;
    }

    public void setPaypal(Boolean paypal) {
        this.paypal = paypal;
    }

    public Boolean getCash() {
        return cash;
    }

    public void setCash(Boolean cash) {
        this.cash = cash;
    }

    public Boolean getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(Boolean creditCard) {
        this.creditCard = creditCard;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpireMonth() {
        return expireMonth;
    }

    public void setExpireMonth(String expireMonth) {
        this.expireMonth = expireMonth;
    }

    public String getExpireYear() {
        return expireYear;
    }

    public void setExpireYear(String expireYear) {
        this.expireYear = expireYear;
    }

    public String getSecurityNum() {
        return securityNum;
    }

    public void setSecurityNum(String securityNum) {
        this.securityNum = securityNum;
    }

    public String getBallance() {
        return ballance;
    }

    public void setBallance(String ballance) {
        this.ballance = ballance;
    }

    public String getWelcomeM() {
        return welcomeM;
    }

    public void setWelcomeM(String welcomeM) {
        this.welcomeM = welcomeM;
    }

    public String getAbout() {
        return About;
    }

    public void setAbout(String about) {
        About = about;
    }

    public String getDeliveryPolicy() {
        return DeliveryPolicy;
    }

    public void setDeliveryPolicy(String deliveryPolicy) {
        DeliveryPolicy = deliveryPolicy;
    }

    public String getReturnPolicy() {
        return ReturnPolicy;
    }

    public void setReturnPolicy(String returnPolicy) {
        ReturnPolicy = returnPolicy;
    }

    public String getStorePic() {
        return storePic;
    }

    public void setStorePic(String storePic) {
        this.storePic = storePic;
    }
}
