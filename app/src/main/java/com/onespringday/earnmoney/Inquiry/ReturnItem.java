package com.onespringday.earnmoney.Inquiry;

/**
 * Created by yoseong on 2017-08-10.
 */

public class ReturnItem {

    private String returnDate;
    private String returnCount;
    private String returnTotalMoney;

    public ReturnItem(String returnDate, String returnCount, String returnTotalMoney) {
        this.returnDate = returnDate;
        this.returnCount = returnCount;
        this.returnTotalMoney = returnTotalMoney;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getReturnCount() {
        return returnCount;
    }

    public void setReturnCount(String returnCount) {
        this.returnCount = returnCount;
    }

    public String getReturnTotalMoney() {
        return returnTotalMoney;
    }

    public void setReturnTotalMoney(String returnTotalMoney) {
        this.returnTotalMoney = returnTotalMoney;
    }
}
