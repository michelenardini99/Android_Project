package com.example.dairys.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DreamTagDao {

    @Insert
    void insertAll(DreamTag...dreamTags);

    @Query("SELECT * FROM dreamtag WHERE dreamId LIKE :dreamId")
    List<DreamTag> getDreamTag(int dreamId);

}
