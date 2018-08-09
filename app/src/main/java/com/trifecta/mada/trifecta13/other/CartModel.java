package com.trifecta.mada.trifecta13.other;

/**
 * Created by Mada on 2/23/2017.
 */

public class CartModel {
    private String crId;
    private String crName;
    private String uid;
    private String pushId;
    private String crPrice;
    private String crQuantity;
    private String crPic;

    public CartModel() {
    }

    public CartModel(String crId, String crName, String uid, String pushId, String crPrice, String crQuantity, String crPic) {
        this.crId = crId;
        this.crName = crName;
        this.uid = uid;
        this.pushId = pushId;
        this.crPrice = crPrice;
        this.crQuantity = crQuantity;
        this.crPic = crPic;
    }

    public String getCrId() {
        return crId;
    }

    public void setCrId(String crId) {
        this.crId = crId;
    }

    public String getCrName() {
        return crName;
    }

    public void setCrName(String crName) {
        this.crName = crName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getCrPrice() {
        return crPrice;
    }

    public void setCrPrice(String crPrice) {
        this.crPrice = crPrice;
    }

    public String getCrQuantity() {
        return crQuantity;
    }

    public void setCrQuantity(String crQuantity) {
        this.crQuantity = crQuantity;
    }

    public String getCrPic() {
        return crPic;
    }

    public void setCrPic(String crPic) {
        this.crPic = crPic;
    }
}
