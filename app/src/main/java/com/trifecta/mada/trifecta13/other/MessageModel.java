package com.trifecta.mada.trifecta13.other;

/**
 * Created by Mada on 3/18/2017.
 */

public class MessageModel {

    private String senderId;
    private String receiverId;
    private String subject;
    private String Message;
    private String receiverName;
    private String SenderName;
    private String pushId;
    private String date;
    private String time;

    public MessageModel() {
    }

    public MessageModel(String senderId, String receiverId, String subject, String message, String receiverName, String senderName, String pushId, String date, String time) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.subject = subject;
        Message = message;
        this.receiverName = receiverName;
        SenderName = senderName;
        this.pushId = pushId;
        this.date = date;
        this.time = time;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getSenderName() {
        return SenderName;
    }

    public void setSenderName(String senderName) {
        SenderName = senderName;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
