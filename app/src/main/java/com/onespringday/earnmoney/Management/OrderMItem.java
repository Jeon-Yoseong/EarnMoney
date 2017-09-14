package com.onespringday.earnmoney.Management;

/**
 * Created by yoseong on 2017-08-15.
 */

public class OrderMItem {

    private String orderName;
    private String orderColor;
    private String orderSize;
    private String orderCount;
    private String orderDate;

    public OrderMItem(String orderName, String orderColor, String orderSize, String orderCount) {
        this.orderName = orderName;
        this.orderColor = orderColor;
        this.orderSize = orderSize;
        this.orderCount = orderCount;
    }

    public OrderMItem(String orderName, String orderColor, String orderSize, String orderCount, String orderDate) {
        this.orderName = orderName;
        this.orderColor = orderColor;
        this.orderSize = orderSize;
        this.orderCount = orderCount;
        this.orderDate = orderDate;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(String orderCount) {
        this.orderCount = orderCount;
    }

    public String getOrderColor() {
        return orderColor;
    }

    public void setOrderColor(String orderColor) {
        this.orderColor = orderColor;
    }

    public String getOrderSize() {
        return orderSize;
    }

    public void setOrderSize(String orderSize) {
        this.orderSize = orderSize;
    }

    public String getOrderDate() {
        return orderDate;
    }
}
