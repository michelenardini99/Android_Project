package com.example.dairys;

public class MyFoodData {

    private String foodName;
    private String foodKcal;
    private Integer foodImage;

    public MyFoodData(String foodName, String foodKcal, Integer foodImage) {
        this.foodName = foodName;
        this.foodKcal = foodKcal;
        this.foodImage = foodImage;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodKcal() {
        return foodKcal;
    }

    public void setFoodKcal(String foodKcal) {
        this.foodKcal = foodKcal;
    }

    public Integer getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(Integer foodImage) {
        this.foodImage = foodImage;
    }
}
