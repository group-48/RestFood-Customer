package com.dash.restfood_customer.models;

public class shop {

private String shop_id;
private String shopName;
private String shopEmail;
private  String shopAddress;
private String shopimage;


    public shop(String shop_id, String shopName, String shopEmail, String shopAddress,String shopimage) {
        this.shop_id = shop_id;
        this.shopName = shopName;
        this.shopEmail = shopEmail;
        this.shopAddress = shopAddress;
        this.shopimage=shopimage;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopEmail() {
        return shopEmail;
    }


    public String getShopimage() {
        return shopimage;
    }

    public void setShopimage(String shopimage) {
        this.shopimage = shopimage;
    }

    public void setShopEmail(String shopEmail) {
        this.shopEmail = shopEmail;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public shop(){}
}
