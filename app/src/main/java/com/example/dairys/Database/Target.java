package com.example.dairys.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Target {

    @PrimaryKey(autoGenerate = true)
    private int targetId;

    @ColumnInfo(name = "activity_id")
    private int activityId;

    @ColumnInfo(name = "target_num")
    private int targetNum;

    @ColumnInfo(name = "is_completed")
    private boolean isCompleted;

    @ColumnInfo(name = "month")
    private String month;

    @ColumnInfo(name = "year")
    private int year;

    public Target(int activityId, int targetNum, boolean isCompleted, String month, int year) {
        this.activityId = activityId;
        this.targetNum = targetNum;
        this.isCompleted = isCompleted;
        this.month = month;
        this.year = year;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getTargetNum() {
        return targetNum;
    }

    public void setTargetNum(int targetNum) {
        this.targetNum = targetNum;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
