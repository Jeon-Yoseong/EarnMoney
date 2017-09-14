package com.onespringday.earnmoney.Inquiry;

/**
 * Created by yoseong on 2017-08-07.
 */

public class SalesItem {

    private String salesDate;
    private String salesCount;
    private String salesTotalMoney;

    public SalesItem (String salesDate, String salesCount, String salesTotalMoney) {
        this.salesDate = salesDate;
        this.salesCount = salesCount;
        this.salesTotalMoney = salesTotalMoney;
    }

    public String getSalesDate() {
        return salesDate;
    }

    public String getSalesTotalMoney() {
        return salesTotalMoney;
    }

    public void setSalesTotalMoney(String salesTotalMoney) {
        this.salesTotalMoney = salesTotalMoney;
    }

    public void setSalesDate(String salesDate) {
        this.salesDate = salesDate;
    }

    public String getSalesCount() {
        return salesCount;
    }

    public void setSalesCount(String salesCount) {
        this.salesCount = salesCount;
    }
}
