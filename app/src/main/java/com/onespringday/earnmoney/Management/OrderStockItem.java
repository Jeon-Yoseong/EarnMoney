package com.onespringday.earnmoney.Management;

/**
 * Created by yoseong on 2017-08-25.
 */

public class OrderStockItem {

    private String orderStockName;
    private String orderStockColor;
    private String orderStockSize;
    private String orderStockCount;
    private String orderStockDate;

    public OrderStockItem(String orderStockName, String orderStockColor, String orderStockSize, String orderStockCount, String orderStockDate) {
        this.orderStockName = orderStockName;
        this.orderStockColor = orderStockColor;
        this.orderStockSize = orderStockSize;
        this.orderStockCount = orderStockCount;
        this.orderStockDate = orderStockDate;
    }

    public String getOrderStockName() {
        return orderStockName;
    }

    public void setOrderStockName(String orderStockName) {
        this.orderStockName = orderStockName;
    }

    public String getOrderStockColor() {
        return orderStockColor;
    }

    public void setOrderStockColor(String orderStockColor) {
        this.orderStockColor = orderStockColor;
    }

    public String getOrderStockSize() {
        return orderStockSize;
    }

    public void setOrderStockSize(String orderStockSize) {
        this.orderStockSize = orderStockSize;
    }

    public String getOrderStockCount() {
        return orderStockCount;
    }

    public void setOrderStockCount(String orderStockCount) {
        this.orderStockCount = orderStockCount;
    }

    public String getOrderStockDate() {
        return orderStockDate;
    }

    public void setOrderStockDate(String orderStockDate) {
        this.orderStockDate = orderStockDate;
    }
}

