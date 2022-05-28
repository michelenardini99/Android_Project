package com.example.dairys.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DreamFavoriteDao {

    @Insert
    void insertAll(DreamFavorite...dreamFavorites);

    @Query("SELECT * FROM dreamfavorite WHERE dreamId LIKE :dreamId AND userId LIKE :userId")
    List<DreamFavorite> getIfIsFavorite(int dreamId, int userId);

    @Query("DELETE FROM dreamfavorite WHERE dreamId LIKE :dreamId AND userId LIKE :userId")
    void removeFromFavorite(int dreamId, int userId);


}
