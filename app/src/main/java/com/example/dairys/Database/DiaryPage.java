package com.example.dairys.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.sql.Date;

@Entity
public class DiaryPage {

    @PrimaryKey
    private int diaryId;

    public DiaryPage(int diaryId, Date date, String note, String photo, String humor) {
        this.diaryId = diaryId;
        this.date = date;
        this.note = note;
        this.photo = photo;
        this.humor = humor;
    }

    public int getDiaryId() {
        return diaryId;
    }

    public void setDiaryId(int diaryId) {
        this.diaryId = diaryId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getHumor() {
        return humor;
    }

    public void setHumor(String humor) {
        this.humor = humor;
    }

    @ColumnInfo(name = "date")
    @TypeConverters({DateTypeConvertor.class})
    private Date date;

    @ColumnInfo(name = "note")
    private String note;

    @ColumnInfo(name = "photo")
    private String photo;

    @ColumnInfo(name = "humor")
    private String humor;

}
