package com.example.dairys.ViewModel;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;

import com.example.dairys.Database.AppDatabase;
import com.example.dairys.Database.DreamDiary;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DreamDiaryViewModel extends ViewModel {

    public List<DreamDiary> dreamDiaryList = new ArrayList<>();
    public List<DreamDiary> dreamDiariesNotSelected = new ArrayList<>();
    private AppDatabase db;

    public void setDatabase(Context context){
        db = AppDatabase.getInstance(context);
    }

    public void fillList(){
        dreamDiaryList = db.dreamDiaryDao().getAll();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<DreamDiary> filterListFav(){
        dreamDiaryList.forEach(d -> {
            if(db.dreamFavoriteDao().getIfIsFavorite(d.getDreamId(), db.userDao().userLogged().get(0).getId()).isEmpty()){
                dreamDiariesNotSelected.add(d);
            }
        });
        return  dreamDiariesNotSelected;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<DreamDiary> filterList(String tag){
        dreamDiaryList.forEach(d -> {
            if(db.dreamTagDao().getDreamFilterByTag(d.getDreamId(), tag).isEmpty()){
                dreamDiariesNotSelected.add(d);
            }
        });
        return  dreamDiariesNotSelected;
    }

    public void removeFromList(){
        dreamDiaryList.removeAll(dreamDiariesNotSelected);
    }

    public void clearListNotSelected(){
        dreamDiariesNotSelected.clear();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<DreamDiary> sortDate(){
        List<DreamDiary> dreamDiaries = db.dreamDiaryDao().getAll();
        dreamDiaries.forEach(d -> {
            d.getDateInsert().replaceAll("/"," ");
        });
        Collections.sort(dreamDiaries, new Comparator<DreamDiary>() {

            DateFormat f = new SimpleDateFormat("dd MMM yyyy");
            @Override
            public int compare(DreamDiary dreamDiary, DreamDiary t1) {
                try {
                    return f.parse(dreamDiary.getDateInsert()).compareTo(f.parse(t1.getDateInsert()));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
        Collections.reverse(dreamDiaries);
        return dreamDiaries;
    }

}
