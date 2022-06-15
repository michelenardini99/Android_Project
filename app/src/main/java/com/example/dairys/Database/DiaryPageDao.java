package com.example.dairys.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.sql.Date;
import java.util.List;

@Dao
public interface DiaryPageDao {

    @Query("SELECT * FROM diarypage WHERE userId LIKE :userId")
    List<DiaryPage> getAll(int userId);

    @Insert
    void insertAll(DiaryPage...diaryPages);

    @Query("SELECT * FROM diarypage ORDER BY diaryId DESC LIMIT 1;")
    List<DiaryPage> getLastInsert();

    @Query("SELECT * FROM diarypage WHERE date LIKE :date AND userId LIKE :userId")
    List<DiaryPage> getDiaryPageForDate(long date, int userId);

    @Query("SELECT * FROM diarypage WHERE userId LIKE :userId AND date BETWEEN :dateFrom AND :dateTo ORDER BY date ASC")
    List<DiaryPage> getDiaryPageBetweenDate(long dateFrom, long dateTo, int userId);

    @Query("DELETE FROM diarypage WHERE diaryId LIKE :id")
    void deletePage(int id);

}
