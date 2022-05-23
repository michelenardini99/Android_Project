package com.example.dairys.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Activity {

    @PrimaryKey(autoGenerate = true)
    private int activityId;

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public static Activity[] populateData() {
        return new Activity[]{
                new Activity("Sport"),
                new Activity("Walk"),
                new Activity("Excursion"),
                new Activity("Immersion"),
                new Activity("Cycling")
        };
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Activity(String activityName) {
        this.activityName = activityName;
    }

    @ColumnInfo(name = "activity_name")
    private String activityName;

}
