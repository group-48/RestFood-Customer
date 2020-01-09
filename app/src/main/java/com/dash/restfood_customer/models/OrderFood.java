package com.dash.restfood_customer.models;

public class OrderFood {
    private String name,image,shopId,foodId,qty,cust_name,orderId;
    private int price;

    public OrderFood(String name, String image, String shopId, String foodId, String qty, int price,String orderId) {
        this.name = name;
        this.image = image;
        this.shopId = shopId;
        this.foodId = foodId;
        this.qty = qty;
        this.price = price;
        this.orderId=orderId;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
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

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public OrderFood(){}
}





