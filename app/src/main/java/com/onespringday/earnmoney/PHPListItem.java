package com.onespringday.earnmoney;

/**
 * Created by yoseong on 2017-08-16.
 */

public class PHPListItem {

    private String[] mData;

    public PHPListItem (String[] mData) {
        this.mData = mData;
    }

    public PHPListItem (String Member_Id) {
        mData = new String[1];

        mData[0] = Member_Id;
    }

    public PHPListItem (String Member_Id, String Member_Password, String Shop_Name, String Shop_Url, String Logo_Photo) {
        mData = new String[5];

        mData[0] = Member_Id;
        mData[1] = Member_Password;
        mData[2] = Shop_Name;
        mData[3] = Shop_Url;
        mData[4] = Logo_Photo;
    }

    public PHPListItem (String Name, String Korea_Name, String China_Unit_Cost, String Korea_Unit_Cost, String Price, String LuckyToday, String Friend, String Bomb, String Color, String Size, String Url, String Photo, String Member_Id) {
        mData = new String[13];

        mData[0] = Name;
        mData[1] = Korea_Name;
        mData[2] = China_Unit_Cost;
        mData[3] = Korea_Unit_Cost;
        mData[4] = Price;
        mData[5] = LuckyToday;
        mData[6] = Friend;
        mData[7] = Bomb;
        mData[8] = Color;
        mData[9] = Size;
        mData[10] = Url;
        mData[11] = Photo;
        mData[12] = Member_Id;
    }

    public PHPListItem (String Korea_Name, String Color, String Size, String Count, String Date, String Member_Id) {
        mData = new String[6];

        mData[0] = Korea_Name;
        mData[1] = Color;
        mData[2] = Size;
        mData[3] = Count;
        mData[4] = Date;
        mData[5] = Member_Id;
    }

    public PHPListItem (String Korea_Name, String Color, String Size, String Count, String Date, String Price, String Member_Id) {
        mData = new String[7];

        mData[0] = Korea_Name;
        mData[1] = Color;
        mData[2] = Size;
        mData[3] = Count;
        mData[4] = Date;
        mData[5] = Price;
        mData[6] = Member_Id;
    }

    public PHPListItem (String Korea_Name, String Color, String Size, String Count, String Date, String Price, String Korea_Unit_Cost, String Member_Id) {
        mData = new String[8];

        mData[0] = Korea_Name;
        mData[1] = Color;
        mData[2] = Size;
        mData[3] = Count;
        mData[4] = Date;
        mData[5] = Price;
        mData[6] = Korea_Unit_Cost;
        mData[7] = Member_Id;
    }

    public PHPListItem (String Korea_Name, String Color, String Size, String Count, String Date, String Price, String Korea_Unit_Cost, String Member_Id, String Product_Photo) {
        mData = new String[9];

        mData[0] = Korea_Name;
        mData[1] = Color;
        mData[2] = Size;
        mData[3] = Count;
        mData[4] = Date;
        mData[5] = Price;
        mData[6] = Korea_Unit_Cost;
        mData[7] = Member_Id;
        mData[8] = Product_Photo;
    }

    public String[] getData() {
        return mData;
    }

    public String getData(int index) {
        return mData[index];
    }

    public void setData(String[] mData) {
        this.mData = mData;
    }
}
