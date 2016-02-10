package com.datainfosys.myshops.data;

/**
 * Created by developer on 9/12/15.
 */
public class InfoShop {
    public int shopId=-1;
    public String shopName="";
    public String shopAddress="";

    public InfoShop() {

    }
    public InfoShop(int shopId, String shopName, String shopAddress) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
    }



    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }


}
