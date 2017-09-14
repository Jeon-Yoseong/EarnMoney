package com.onespringday.earnmoney.Inquiry;

/**
 * Created by yoseong on 2017-08-06.
 */

public class ProductItem {

    private String photo;
    private String name;
    private String sellingPrice;
    private String unitPrice;
    private String profit;
    private String color;
    private String size;
    private String Member_Id;

    public ProductItem(String photo, String name, String sellingPrice, String unitPrice, String profit, String color, String size, String Member_Id) {
        this.photo = photo;
        this.name = name;
        this.sellingPrice = sellingPrice;
        this.unitPrice = unitPrice;
        this.profit = profit;
        this.color = color;
        this.size = size;
        this.Member_Id = Member_Id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMember_Id() {
        return Member_Id;
    }

    public void setMember_Id(String member_Id) {
        Member_Id = member_Id;
    }
}
