package com.example.dairys.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ActivityDao {
    @Query("SELECT * FROM activity")
    List<Activity> getAll();

    @Insert
    void insertAll(Activity...activities);

    @Query("SELECT * FROM activity WHERE activityId LIKE :id")
    List<Activity> getActivityFromId(int id);

    @Query("SELECT * FROM activity WHERE activity_name LIKE :name")
    List<Activity> getActivityFromName(String name);
}
