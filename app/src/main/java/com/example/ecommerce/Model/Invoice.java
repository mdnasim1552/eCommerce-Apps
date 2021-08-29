package com.example.ecommerce.Model;

public class Invoice {
    String filename,fileurl,deliveryMansUserId,CurrentDateCurrentTime,customersUserID;

    public Invoice() {
    }

    public Invoice(String filename, String fileurl, String deliveryMansUserId, String currentDateCurrentTime, String customersUserID) {
        this.filename = filename;
        this.fileurl = fileurl;
        this.deliveryMansUserId = deliveryMansUserId;
        CurrentDateCurrentTime = currentDateCurrentTime;
        this.customersUserID = customersUserID;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    public String getDeliveryMansUserId() {
        return deliveryMansUserId;
    }

    public void setDeliveryMansUserId(String deliveryMansUserId) {
        this.deliveryMansUserId = deliveryMansUserId;
    }

    public String getCurrentDateCurrentTime() {
        return CurrentDateCurrentTime;
    }

    public void setCurrentDateCurrentTime(String currentDateCurrentTime) {
        CurrentDateCurrentTime = currentDateCurrentTime;
    }

    public String getCustomersUserID() {
        return customersUserID;
    }

    public void setCustomersUserID(String customersUserID) {
        this.customersUserID = customersUserID;
    }
}
