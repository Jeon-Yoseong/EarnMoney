package com.onespringday.earnmoney.Management;

/**
 * Created by yoseong on 2017-08-27.
 */

public class ReturnMItem {

    private String returnName;
    private String returnColor;
    private String returnSize;
    private String returnCount;
    private String orderDate;
    private String returnDate;

    public ReturnMItem(String returnName, String returnColor, String returnSize, String returnCount, String orderDate, String returnDate) {
        this.returnName = returnName;
        this.returnColor = returnColor;
        this.returnSize = returnSize;
        this.returnCount = returnCount;
        this.orderDate = orderDate;
        this.returnDate = returnDate;
    }

    public String getReturnName() {
        return returnName;
    }

    public String getReturnColor() {
        return returnColor;
    }

    public String getReturnSize() {
        return returnSize;
    }

    public String getReturnCount() {
        return returnCount;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getReturnDate() {
        return returnDate;
    }
}
