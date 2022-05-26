package com.example.dairys.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.sql.Date;
import java.util.List;

@Dao
public interface DiaryPageDao {

    @Query("SELECT * FROM diarypage")
    List<DiaryPage> getAll();

    @Insert
    void insertAll(DiaryPage...diaryPages);

    @Query("SELECT * FROM diarypage ORDER BY diaryId DESC LIMIT 1;")
    List<DiaryPage> getLastInsert();

    @Query("SELECT * FROM diarypage WHERE date LIKE :date")
    List<DiaryPage> getDiaryPageForDate(long date);

    @Query("SELECT * FROM diarypage WHERE date BETWEEN :dateFrom AND :dateTo")
    List<DiaryPage> getDiaryPageBetweenDate(long dateFrom, long dateTo);

}
