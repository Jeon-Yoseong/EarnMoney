package com.onespringday.earnmoney.Inquiry;

/**
 * Created by yoseong on 2017-08-21.
 */

public class SalesListDateItem {

    private String date;
    private String count;
    private String price;

    public SalesListDateItem(String date, String count, String price) {
        this.date = date;
        this.count = count;
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
