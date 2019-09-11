package com.dash.restfood_customer;

public class FoodData {

    private String name;
    private String image;

    public FoodData(String name, String image) {
        this.name = name;
        this.image = image;
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

    public FoodData(){

    }
}


