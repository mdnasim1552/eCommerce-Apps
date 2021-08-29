package com.example.ecommerce.Sellers;

import java.util.Objects;

public class PackingListRow {
    private final String sid;
    private final int amount;
    private final String products;

    public PackingListRow(String sid, int amount, String products) {
        this.sid = sid;
        this.amount = amount;
        this.products=products;
    }

    public String getSid() {
        return sid;
    }

    public int getAmount() {
        return amount;
    }
    public String getProducts() {
        return products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PackingListRow that = (PackingListRow) o;
        return Objects.equals(sid, that.sid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sid);
    }

    @Override
    public String toString() {
        return  sid + " " + amount+" "+products;
    }

    public PackingListRow merge(PackingListRow other) {
        assert (this.equals(other));
        return new PackingListRow(this.sid, this.amount + other.amount,this.products+"@"+other.products);
    }
}
