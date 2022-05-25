package com.example.dairys.Database;

import androidx.room.Entity;

@Entity(primaryKeys = {"diaryId", "activityId"})
public class DiaryActivity {
    public int diaryId;
    public int activityId;

    public DiaryActivity(int diaryId, int activityId) {
        this.diaryId = diaryId;
        this.activityId = activityId;
    }

    public int getDiaryId() {
        return diaryId;
    }

    public void setDiaryId(int diaryId) {
        this.diaryId = diaryId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }
}
