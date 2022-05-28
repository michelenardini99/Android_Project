package com.example.dairys.Database;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = {"dreamId", "tagName"})
public class DreamTag {

    @NonNull
    private int dreamId;
    @NonNull
    private String tagName;

    public DreamTag(int dreamId, String tagName) {
        this.dreamId = dreamId;
        this.tagName = tagName;
    }

    public int getDreamId() {
        return dreamId;
    }

    public void setDreamId(int dreamId) {
        this.dreamId = dreamId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
