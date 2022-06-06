package com.example.dairys.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DiaryActivityDao {
    @Insert
    void insertAll(DiaryActivity...diaryActivities);

    @Query("SELECT * FROM diaryactivity WHERE diaryId LIKE :pageId")
    List<DiaryActivity> getFromPageId(int pageId);

    @Query("DELETE FROM diaryactivity WHERE diaryId LIKE :id")
    void deleteActivity(int id);

}
