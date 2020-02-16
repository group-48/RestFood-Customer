package com.dash.restfood_customer.models;

import android.content.Intent;

import com.google.firebase.firestore.model.value.BooleanValue;

public class Food {

    private String category;
    private String foodName;
    private String description;
    private Integer price;
    private String image;
    private Integer maxDuration,minDuration;
    private Boolean isVeg;


    public Food(String category, String foodName, String description, String image) {
        this.category = category;
        this.foodName = foodName;
        this.description = description;
        this.price=price;
        this.image = image;
    }

    public Integer getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(Integer maxDuration) {
        this.maxDuration = maxDuration;
    }

    public Integer getMinDuration() {
        return minDuration;
    }

    public void setMinDuration(Integer minDuration) {
        this.minDuration = minDuration;
    }

    public Boolean getVeg() {
        return isVeg;
    }

    public void setVeg(Boolean veg) {
        this.isVeg = veg;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Food(){}


}
