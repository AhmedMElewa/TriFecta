package com.trifecta.mada.trifecta13.other;

/**
 * Created by Mada on 2/25/2017.
 */

public class ReviewModel {

    private String pushId;
    private String uid;
    private String buyerName;
    private String message;
    private String rating;

    public ReviewModel() {
    }

    public ReviewModel(String pushId, String uid, String buyerName, String message, String rating) {
        this.pushId = pushId;
        this.uid = uid;
        this.buyerName = buyerName;
        this.message = message;
        this.rating = rating;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
