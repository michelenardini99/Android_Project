package com.example.dairys.ViewModel;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;

import com.example.dairys.Database.AppDatabase;
import com.example.dairys.Database.DiaryActivity;
import com.example.dairys.Database.DiaryPage;
import com.example.dairys.Database.Target;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TargetsViewModel extends ViewModel {

    AppDatabase db;
    public String target;
    public List<DiaryPage> list = new ArrayList<>();
    public int maxTarget;

    public void setDb(AppDatabase db){
        this.db = db;
    }

    public void setTarget(String target){
        this.target = target;
    }

    public void setMaxTarget(int max){
        maxTarget = max;
    }


    public void setList() throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dateFrom = new Date(sdf.parse(1+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR)).getTime());
        Date dateTo = new Date(sdf.parse(cal.getActualMaximum(Calendar.DAY_OF_MONTH)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR)).getTime());
        list = db.diaryPageDao().getDiaryPageBetweenDate(dateFrom.getTime(), dateTo.getTime(), db.userDao().userLogged().get(0).getId());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int getNumActivityTarget(){
        List<DiaryActivity> diaryActivityList = new ArrayList<>();
        list.forEach( d -> {
            db.diaryActivityDao().getFromPageId(d.getDiaryId()).forEach( da -> {
                if(db.activityDao().getActivityFromId(da.getActivityId()).get(0).getActivityName().equals(target)){
                    diaryActivityList.add(da);
                }
            });
        });
        return diaryActivityList.size();
    }

    public void setNewTarget(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(c.getTime());
        if(db.targetDao().getTargetCurrentPeriod(month_name, c.get(Calendar.YEAR)).isEmpty()){
            db.targetDao().insertAll(new Target(getRandomActivity(), getMaxTarget(), false, month_name, c.get(Calendar.YEAR)));
        }
        setTarget(db.activityDao().getActivityFromId(db.targetDao().getTargetCurrentPeriod(month_name, c.get(Calendar.YEAR)).get(0).getActivityId()).get(0).getActivityName());
        setMaxTarget(db.targetDao().getTargetCurrentPeriod(month_name, c.get(Calendar.YEAR)).get(0).getTargetNum());
    }

    private int getRandomActivity(){
        Random r = new Random();
        int activityId = r.nextInt((db.activityDao().getAll().size()+1) - 1) + 1;
        return activityId;
    }

    private int getMaxTarget(){
        Random r = new Random();
        int max = r.nextInt(15 - 5) + 5;
        return max;
    }

}
