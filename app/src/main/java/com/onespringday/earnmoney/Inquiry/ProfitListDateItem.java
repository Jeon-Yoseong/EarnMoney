package com.onespringday.earnmoney.Inquiry;

/**
 * Created by yoseong on 2017-08-21.
 */

public class ProfitListDateItem {

    private String Date;
    private String Count;
    private String Profit;

    public ProfitListDateItem(String date, String count, String profit) {
        Date = date;
        Count = count;
        Profit = profit;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    public String getProfit() {
        return Profit;
    }

    public void setProfit(String profit) {
        Profit = profit;
    }
}
