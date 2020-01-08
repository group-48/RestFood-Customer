package com.dash.restfood_customer.models;

public class Review {
    private String foodId,foodName,shopId,comments,userName,userId,orderId;
    Float rating;

    public Review(String foodId, String foodName, String shopId, String comments, String userName, String userId, Float rating,String orderId) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.shopId = shopId;
        this.comments = comments;
        this.userName = userName;
        this.userId = userId;
        this.rating = rating;
        this.orderId=orderId;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
