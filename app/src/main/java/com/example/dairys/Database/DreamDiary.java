package com.example.dairys.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.sql.Date;
import java.text.ParseException;

@Entity
public class DreamDiary {

    @PrimaryKey(autoGenerate = true)
    private int dreamId;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "story")
    private String story;

    @ColumnInfo(name = "like")
    private int like;

    @ColumnInfo(name = "user_creator_id")
    private int userCreatorId;

    @ColumnInfo(name = "date_insert")
    private String dateInsert;

    public DreamDiary(int dreamId, String title, String story, int like, int userCreatorId, String dateInsert) {
        this.dreamId = dreamId;
        this.title = title;
        this.story = story;
        this.like = like;
        this.userCreatorId = userCreatorId;
        this.dateInsert = dateInsert;
    }

    public int getDreamId() {
        return dreamId;
    }

    public void setDreamId(int dreamId) {
        this.dreamId = dreamId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getUserCreatorId() {
        return userCreatorId;
    }

    public void setUserCreatorId(int userCreatorId) {
        this.userCreatorId = userCreatorId;
    }

    public String getDateInsert() {
        return dateInsert;
    }

    public void setDateInsert(String dateInsert) {
        this.dateInsert = dateInsert;
    }

    public static DreamDiary[] populateData(){
        DreamDiary[] dreamDiaries = new DreamDiary[]{
                new DreamDiary(1,"Prova", "Prova", 35, 1, "28 mag 2022")
        };
        return  dreamDiaries;
    }

}
