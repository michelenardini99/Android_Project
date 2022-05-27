package com.example.dairys.Database;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class UserWithDreamDiary {
    @Embedded
    public User user;
    @Relation(
            parentColumn = "id",
            entityColumn = "user_creator_id"
    )
    public List<DreamDiary> dreamDiaryList;
}
