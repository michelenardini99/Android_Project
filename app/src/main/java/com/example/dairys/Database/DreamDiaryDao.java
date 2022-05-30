package com.example.dairys.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DreamDiaryDao {

    @Query("SELECT * FROM dreamdiary")
    List<DreamDiary> getAll();

    @Query("SELECT MAX(dreamId) FROM dreamdiary")
    int getMaxId();

    @Insert
    void insertAll(DreamDiary...dreamDiaries);

    @Query("UPDATE dreamdiary SET `like` = :like WHERE dreamId LIKE :dreamId")
    void setNumberLiked(int like, int dreamId);

    @Query("SELECT * FROM dreamdiary ORDER BY `like` DESC")
    List<DreamDiary> orderByLike();

}
