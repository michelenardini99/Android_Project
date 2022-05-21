package com.example.dairys;

public class MyFoodData {

    private String foodName;
    private String foodKcal;
    private Integer imageView;
    boolean isSelected;

    public MyFoodData(String foodName, String foodKcal, Integer imageView) {
        this.foodName = foodName;
        this.foodKcal = foodKcal;
        this.imageView = imageView;
        isSelected = false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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
        return imageView;
    }

    public void setFoodImage(Integer imageView) {
        this.imageView = imageView;
    }
}
