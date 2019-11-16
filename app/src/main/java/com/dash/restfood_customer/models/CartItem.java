package com.dash.restfood_customer.models;

import android.util.Log;

public class CartItem {
    private String name,image,shopId,foodId,qty;
    private int price;

    public CartItem(String name, String image, String shopId, String foodId, String qty, int price) {
        this.name = name;
        this.image = image;
        this.shopId = shopId;
        this.foodId = foodId;
        this.qty = qty;
        this.price = price;
    }


    public CartItem() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
