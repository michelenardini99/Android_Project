package com.example.dairys.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.dairys.R;

import java.io.Serializable;

@Entity
public class Food implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int foodId;

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    @ColumnInfo(name = "food_name")
    private String foodName;

    @ColumnInfo(name = "kcal")
    private int kcal;

    @ColumnInfo(name = "photo_food")
    private int photoFood;

    @ColumnInfo(name = "isSelected")
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Food(String foodName, int kcal, int photoFood) {
        this.foodName = foodName;
        this.kcal = kcal;
        this.photoFood = photoFood;
        this.isSelected = false;
    }

    public static Food[] populateData() {
        return new Food[]{
                new Food("Pizza", 256, R.drawable.pizza),
                new Food("Mela", 60, R.drawable.mele),
                new Food("Pasta al ragu", 335, R.drawable.ragu),
                new Food("Hamburger", 302, R.drawable.hamburgher),
                new Food("Insalata greca", 351, R.drawable.insalata_greca),
                new Food("Salsiccia", 208, R.drawable.salsiccia),
                new Food("Crema catalana", 212, R.drawable.crema_catalana)
        };
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public int getPhotoFood() {
        return photoFood;
    }

    public void setPhotoFood(int photoFood) {
        this.photoFood = photoFood;
    }
}
