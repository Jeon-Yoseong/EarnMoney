package com.onespringday.earnmoney.Inquiry;

/**
 * Created by yoseong on 2017-08-28.
 */

public class ReturnListDateItem {

    private String date;
    private String count;
    private String price;

    public ReturnListDateItem(String date, String count, String price) {
        this.date = date;
        this.count = count;
        this.price = price;
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
}
