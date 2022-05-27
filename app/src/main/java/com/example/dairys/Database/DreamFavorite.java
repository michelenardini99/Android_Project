package com.example.dairys.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"dreamId", "userId"})
public class DreamFavorite {

    private int dreamId;
    private int userId;

    public DreamFavorite(int dreamId, int userId) {
        this.dreamId = dreamId;
        this.userId = userId;
    }

    public int getDreamId() {
        return dreamId;
    }

    public void setDreamId(int dreamId) {
        this.dreamId = dreamId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
