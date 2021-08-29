package com.example.ecommerce.Prevalent;

import com.example.ecommerce.Model.Deliverers;
import com.example.ecommerce.Model.Sellers;
import com.example.ecommerce.Model.Users;

public class Prevalent {
    public static Users currentOnlineUser;
    public static Sellers currentOnlineSeller;
    public static Deliverers currentOnlineDeliverer;
    public static final String UserPhoneKey = "UserPhone";
    public static final String UserPasswordKey = "UserPassword";
    public static final String UserParentDbKey = "UserParentDb";

    public static final String SellerEmailKey = "SellerEmail";
    public static final String SellerPasswordKey = "SellerPassword";
    public static final String DelivererEmailKey = "DelivererPhone";
    public static final String DelivererPasswordKey = "DelivererPassword";
}
