package com.example.dairys.Database;

import androidx.room.Dao;
import androidx.room.Delete;
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

    @Query("SELECT * FROM diarypage WHERE date BETWEEN :dateFrom AND :dateTo ORDER BY date ASC")
    List<DiaryPage> getDiaryPageBetweenDate(long dateFrom, long dateTo);

    @Query("DELETE FROM diarypage WHERE diaryId LIKE :id")
    void deletePage(int id);

}
