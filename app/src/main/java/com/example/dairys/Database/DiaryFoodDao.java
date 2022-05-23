package com.example.dairys.Database;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface DiaryFoodDao {

    @Insert
    void insertAll(DiaryFood...diaryFoods);

}
