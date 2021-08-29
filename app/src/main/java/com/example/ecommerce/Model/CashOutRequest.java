package com.example.ecommerce.Model;

public class CashOutRequest {
    private String date,amount,sid,transactionid,key;

    public CashOutRequest() {
    }

    public CashOutRequest(String date, String amount, String sid, String transactionid, String key) {
        this.date = date;
        this.amount = amount;
        this.sid = sid;
        this.transactionid = transactionid;
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
