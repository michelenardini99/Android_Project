package com.example.dairys.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;

@Database(entities = {User.class, Food.class, DiaryPage.class, Activity.class, DiaryFood.class, DiaryActivity.class, DreamDiary.class, DreamFavorite.class, DreamTag.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract FoodDao foodDao();
    public abstract ActivityDao activityDao();
    public abstract DiaryPageDao diaryPageDao();
    public abstract DiaryFoodDao diaryFoodDao();
    public abstract DiaryActivityDao diaryActivityDao();
    public abstract DreamFavoriteDao dreamFavoriteDao();
    public abstract DreamDiaryDao dreamDiaryDao();
    public abstract DreamTagDao dreamTagDao();

    public synchronized static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = buildDatabase(context);
        }
        return INSTANCE;
    }

    private static AppDatabase buildDatabase(Context context) {
        return Room.databaseBuilder(context,
                        AppDatabase.class,
                        "diary_database")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                getInstance(context).foodDao().insertAll(Food.populateData());
                                getInstance(context).activityDao().insertAll(Activity.populateData());
                                getInstance(context).dreamDiaryDao().insertAll(DreamDiary.populateData());
                            }
                        });
                    }
                })
                .build();
    }

}
