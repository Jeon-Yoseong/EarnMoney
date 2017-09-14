package com.onespringday.earnmoney;

/**
 * Created by yoseong on 2017-08-29.
 */

public class MainListItem {

    private String date;
    private String count;
    private String price;
    private String profit;
    private String photo;
    private String name;
    private String color;

    public MainListItem(String date, String count, String price, String profit) {
        this.date = date;
        this.count = count;
        this.price = price;
        this.profit = profit;
    }

    public MainListItem(String name, String color, String count, String price, String photo) {
        this.name = name;
        this.color = color;
        this.count = count;
        this.price = price;
        this.photo = photo;
    }

    public MainListItem(String name, String color, String date, String count, String price, String profit, String photo) {
        this.name = name;
        this.color = color;
        this.date = date;
        this.count = count;
        this.price = price;
        this.profit = profit;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getDate() {
        return date;
    }

    public String getCount() {
        return count;
    }

    public String getPrice() {
        return price;
    }

    public String getProfit() {
        return profit;
    }

    public String getPhoto() {
        return photo;
    }
}
