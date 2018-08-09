package com.trifecta.mada.trifecta13.other;

/**
 * Created by Mada on 3/12/2017.
 */

public class WishModel {

    private String prName;
    private String prPic;
    private String prMade;
    private String PrDate;
    private String prCategory;
    private String prQuantity;
    private String prRenewal;
    private String prType;
    private String prDescription;
    private String prTags;
    private String prMaterials;
    private String prLocation;
    private String prProcessTime;
    private String prHomeCountryO;
    private String prOtherCountryO;
    private String uid;
    private String prId;
    private String prPrice;
    private String WishId;

    public WishModel() {
    }

    public WishModel(String prName, String prPic, String prMade, String prDate, String prCategory,
                        String prQuantity, String prRenewal, String prType, String prDescription, String prTags,
                        String prMaterials, String prLocation, String prProcessTime, String prHomeCountryO,
                        String prOtherCountryO, String uid, String prId, String prPrice, String WishId) {
        this.prName = prName;
        this.prPic = prPic;
        this.prMade = prMade;
        PrDate = prDate;
        this.prCategory = prCategory;
        this.prQuantity = prQuantity;
        this.prRenewal = prRenewal;
        this.prType = prType;
        this.prDescription = prDescription;
        this.prTags = prTags;
        this.prMaterials = prMaterials;
        this.prLocation = prLocation;
        this.prProcessTime = prProcessTime;
        this.prHomeCountryO = prHomeCountryO;
        this.prOtherCountryO = prOtherCountryO;
        this.uid = uid;
        this.prId = prId;
        this.prPrice = prPrice;
        this.WishId = WishId;
    }

    public String getWishId() {
        return WishId;
    }

    public void setWishId(String wishId) {
        WishId = wishId;
    }

    public String getPrName() {
        return prName;
    }

    public void setPrName(String prName) {
        this.prName = prName;
    }

    public String getPrPic() {
        return prPic;
    }

    public void setPrPic(String prPic) {
        this.prPic = prPic;
    }

    public String getPrMade() {
        return prMade;
    }

    public void setPrMade(String prMade) {
        this.prMade = prMade;
    }

    public String getPrDate() {
        return PrDate;
    }

    public void setPrDate(String prDate) {
        PrDate = prDate;
    }

    public String getPrCategory() {
        return prCategory;
    }

    public void setPrCategory(String prCategory) {
        this.prCategory = prCategory;
    }

    public String getPrQuantity() {
        return prQuantity;
    }

    public void setPrQuantity(String prQuantity) {
        this.prQuantity = prQuantity;
    }

    public String getPrRenewal() {
        return prRenewal;
    }

    public void setPrRenewal(String prRenewal) {
        this.prRenewal = prRenewal;
    }

    public String getPrType() {
        return prType;
    }

    public void setPrType(String prType) {
        this.prType = prType;
    }

    public String getPrDescription() {
        return prDescription;
    }

    public void setPrDescription(String prDescription) {
        this.prDescription = prDescription;
    }

    public String getPrTags() {
        return prTags;
    }

    public void setPrTags(String prTags) {
        this.prTags = prTags;
    }

    public String getPrMaterials() {
        return prMaterials;
    }

    public void setPrMaterials(String prMaterials) {
        this.prMaterials = prMaterials;
    }

    public String getPrLocation() {
        return prLocation;
    }

    public void setPrLocation(String prLocation) {
        this.prLocation = prLocation;
    }

    public String getPrProcessTime() {
        return prProcessTime;
    }

    public void setPrProcessTime(String prProcessTime) {
        this.prProcessTime = prProcessTime;
    }

    public String getPrHomeCountryO() {
        return prHomeCountryO;
    }

    public void setPrHomeCountryO(String prHomeCountryO) {
        this.prHomeCountryO = prHomeCountryO;
    }


    public String getPrOtherCountryO() {
        return prOtherCountryO;
    }

    public void setPrOtherCountryO(String prOtherCountryO) {
        this.prOtherCountryO = prOtherCountryO;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPrId() {
        return prId;
    }

    public void setPrId(String prId) {
        this.prId = prId;
    }

    public String getPrPrice() {
        return prPrice;
    }

    public void setPrPrice(String prPrice) {
        this.prPrice = prPrice;
    }
}
