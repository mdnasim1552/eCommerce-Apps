package com.example.ecommerce.Model;

public class Deliverers {
    private String name, phone, password, sid, address,email,balance,totalearning,totaldelivery,currentpick;

    public Deliverers() {
    }

    public Deliverers(String name, String phone, String password, String sid, String address, String email, String balance, String totalearning, String totaldelivery, String currentpick) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.sid = sid;
        this.address = address;
        this.email = email;
        this.balance = balance;
        this.totalearning = totalearning;
        this.totaldelivery = totaldelivery;
        this.currentpick = currentpick;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTotalearning() {
        return totalearning;
    }

    public void setTotalearning(String totalearning) {
        this.totalearning = totalearning;
    }

    public String getTotaldelivery() {
        return totaldelivery;
    }

    public void setTotaldelivery(String totaldelivery) {
        this.totaldelivery = totaldelivery;
    }

    public String getCurrentpick() {
        return currentpick;
    }

    public void setCurrentpick(String currentpick) {
        this.currentpick = currentpick;
    }
}
