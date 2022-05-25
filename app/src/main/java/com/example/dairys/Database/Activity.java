package com.example.dairys.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.dairys.R;

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
                new Activity("Sport", R.drawable.ic_sport),
                new Activity("Walk", R.drawable.ic_walk),
                new Activity("Excursion", R.drawable.ic_excursion),
                new Activity("Immersion", R.drawable.ic_immersion),
                new Activity("Cycling", R.drawable.ic_cycling)
        };
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Activity(String activityName, int activityIcon) {
        this.activityIcon = activityIcon;
        this.activityName = activityName;
    }

    @ColumnInfo(name = "activity_name")
    private String activityName;

    @ColumnInfo(name = "activity_icon")
    private int activityIcon;

    public int getActivityIcon() {
        return activityIcon;
    }

    public void setActivityIcon(int activityIcon) {
        this.activityIcon = activityIcon;
    }
}
