package com.example.ecommerce.Model;

public class DelivererBalance {
    private String sid,date,deliveryAmount,invoiceNumber;

    public DelivererBalance() {
    }

    public DelivererBalance(String sid, String date, String deliveryAmount, String invoiceNumber) {
        this.sid = sid;
        this.date = date;
        this.deliveryAmount = deliveryAmount;
        this.invoiceNumber = invoiceNumber;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDeliveryAmount() {
        return deliveryAmount;
    }

    public void setDeliveryAmount(String deliveryAmount) {
        this.deliveryAmount = deliveryAmount;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }
}
