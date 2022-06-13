package com.example.dairys.Database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"diaryId", "foodId", "category"})
public class DiaryFood {
    public int diaryId;
    public int foodId;
    @NonNull public String category;

    public DiaryFood(int diaryId, int foodId, String category) {
        this.diaryId = diaryId;
        this.foodId = foodId;
        this.category = category;
    }

    public int getDiaryId() {
        return diaryId;
    }

    public void setDiaryId(int diaryId) {
        this.diaryId = diaryId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
