package com.example.dairys.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TargetDao {

    @Insert
    void insertAll(Target...targets);

    @Query("SELECT * FROM target")
    List<Target> getAll();

    @Query("SELECT * FROM target WHERE month LIKE :month AND year LIKE :year")
    List<Target> getTargetCurrentPeriod(String month, int year);

}
