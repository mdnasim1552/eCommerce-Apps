package com.example.ecommerce.Model;

public class Balance {
    private String sid,date,sellingAmount,invoiceNumber,products;

    public Balance() {
    }

    public Balance(String sid, String date, String sellingAmount, String invoiceNumber, String products) {
        this.sid = sid;
        this.date = date;
        this.sellingAmount = sellingAmount;
        this.invoiceNumber = invoiceNumber;
        this.products = products;
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

    public String getSellingAmount() {
        return sellingAmount;
    }

    public void setSellingAmount(String sellingAmount) {
        this.sellingAmount = sellingAmount;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }
}
