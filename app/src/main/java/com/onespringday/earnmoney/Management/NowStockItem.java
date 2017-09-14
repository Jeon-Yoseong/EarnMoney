package com.onespringday.earnmoney.Management;

/**
 * Created by yoseong on 2017-08-23.
 */

public class NowStockItem {

    private String stockName;
    private String stockColor;
    private String stockSize;
    private String stockCount;

    public NowStockItem(String stockName, String stockColor, String stockSize, String stockCount) {
        this.stockName = stockName;
        this.stockColor = stockColor;
        this.stockSize = stockSize;
        this.stockCount = stockCount;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockColor() {
        return stockColor;
    }

    public void setStockColor(String stockColor) {
        this.stockColor = stockColor;
    }

    public String getStockSize() {
        return stockSize;
    }

    public void setStockSize(String stockSize) {
        this.stockSize = stockSize;
    }

    public String getStockCount() {
        return stockCount;
    }

    public void setStockCount(String stockCount) {
        this.stockCount = stockCount;
    }
}
