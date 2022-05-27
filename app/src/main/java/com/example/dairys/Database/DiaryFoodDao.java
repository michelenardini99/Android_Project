package com.example.dairys.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DiaryFoodDao {

    @Insert
    void insertAll(DiaryFood...diaryFoods);

    @Query("SELECT * FROM diaryfood WHERE diaryId LIKE :pageId AND category LIKE :category")
    List<DiaryFood> getFromPageId(int pageId, String category);

    @Query("SELECT * FROM diaryfood WHERE diaryId LIKE :pageId")
    List<DiaryFood> getFromPageIdAll(int pageId);

}
