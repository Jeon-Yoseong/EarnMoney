package com.onespringday.earnmoney.Inquiry;

/**
 * Created by yoseong on 2017-08-09.
 */

public class ProfitItem {

    private String profitDate;
    private String profitCount;
    private String profitTotalMoney;

    public String getProfitDate() {
        return profitDate;
    }

    public void setProfitDate(String profitDate) {
        this.profitDate = profitDate;
    }

    public String getProfitCount() {
        return profitCount;
    }

    public void setProfitCount(String profitCount) {
        this.profitCount = profitCount;
    }

    public String getProfitTotalMoney() {
        return profitTotalMoney;
    }

    public void setProfitTotalMoney(String profitTotalMoney) {
        this.profitTotalMoney = profitTotalMoney;
    }

    public ProfitItem(String profitDate, String profitCount, String profitTotalMoney) {

        this.profitDate = profitDate;
        this.profitCount = profitCount;
        this.profitTotalMoney = profitTotalMoney;
    }
}
