package com.example.ecommerce.Model;

public class Cart {
    private String pid, pname, price, quantity, discount,image,sid,confirmationOfSellers,sellerPhone,sellerAddress,sellerName,usersPhoneNumber;

    public Cart() {
    }

    public Cart(String pid, String pname, String price, String quantity, String discount, String image, String sid, String confirmationOfSellers, String sellerPhone, String sellerAddress, String sellerName, String usersPhoneNumber) {
        this.pid = pid;
        this.pname = pname;
        this.price = price;
        this.quantity = quantity;
        this.discount = discount;
        this.image = image;
        this.sid = sid;
        this.confirmationOfSellers = confirmationOfSellers;
        this.sellerPhone = sellerPhone;
        this.sellerAddress = sellerAddress;
        this.sellerName = sellerName;
        this.usersPhoneNumber = usersPhoneNumber;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getConfirmationOfSellers() {
        return confirmationOfSellers;
    }

    public void setConfirmationOfSellers(String confirmationOfSellers) {
        this.confirmationOfSellers = confirmationOfSellers;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getUsersPhoneNumber() {
        return usersPhoneNumber;
    }

    public void setUsersPhoneNumber(String usersPhoneNumber) {
        this.usersPhoneNumber = usersPhoneNumber;
    }
}
