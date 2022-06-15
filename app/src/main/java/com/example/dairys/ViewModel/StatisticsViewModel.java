package com.example.dairys.ViewModel;

import android.content.Context;
import android.os.Build;
import android.text.format.DateFormat;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;

import com.example.dairys.Database.Activity;
import com.example.dairys.Database.AppDatabase;
import com.example.dairys.Database.DiaryActivity;
import com.example.dairys.Database.DiaryFood;
import com.example.dairys.Database.DiaryPage;
import com.example.dairys.Database.Food;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsViewModel extends ViewModel {

    public String date;

    public void setDate(Calendar cal){
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(cal.getTime());
        int year = cal.get(Calendar.YEAR);
        date =  month_name + " " + year;
    }

    public LineData createLineChart(Context context) throws ParseException {
        AppDatabase db = AppDatabase.getInstance(context);
        List<DiaryPage> diaryPages = getList(db);

        ArrayList<Entry> lineEntries = new ArrayList<>();

        int val = 0;
        for (DiaryPage d : diaryPages) {
            String day = (String) DateFormat.format("dd",   d.getDate()); // 20
            switch (d.getHumor()) {
                case "Really happy":
                    val = 4;
                    break;
                case "Happy":
                    val = 3;
                    break;
                case "So so":
                    val = 2;
                    break;
                case "Bad":
                    val = 1;
                    break;
                case "Terrible":
                    val = 0;
                    break;
            }
            lineEntries.add(new Entry(Integer.parseInt(day), val));
        }
        LineDataSet lineDataSet = new LineDataSet(lineEntries, "Humor");
        lineDataSet.setLineWidth(2);
        ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
        iLineDataSets.add(lineDataSet);
        lineDataSet.setDrawValues(false);
        lineDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

        return new LineData(iLineDataSets);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public PieDataSet createPieChart(Context context, String category) throws ParseException {
        AppDatabase db = AppDatabase.getInstance(context);
        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        Map<String, Integer> list = getMapList(db, category);

        int i = 1;
        list.forEach((h, v) -> {
            PieEntry pieEntry = new PieEntry(v, h);
            pieEntries.add(pieEntry);
        });
        return new PieDataSet(pieEntries, "Activity");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Map<String, Integer> getMapList(AppDatabase db, String category) throws ParseException {
        List<DiaryPage> diaryPages = getList(db);

        Map<String, Integer> list = new HashMap<>();

        switch(category){
            case "Activity":
                diaryPages.forEach( d -> {
                    List<DiaryActivity> activityList = db.diaryActivityDao().getFromPageId(d.getDiaryId());
                    activityList.forEach( a -> {
                        List<Activity> ac = db.activityDao().getActivityFromId(a.getActivityId());
                        if(!list.containsKey(ac.get(0).getActivityName())){
                            list.put(ac.get(0).getActivityName(), 1);
                        }else{
                            int count = list.get(ac.get(0).getActivityName());
                            list.remove(ac.get(0).getActivityName());
                            list.put(ac.get(0).getActivityName(), ++count);
                        }
                    });
                });
                break;
            case "Food":
                diaryPages.forEach( d -> {
                    List<DiaryFood> foodList = db.diaryFoodDao().getFromPageIdAll(d.getDiaryId());
                    foodList.forEach( a -> {
                        List<Food> ac = db.foodDao().getFoodFromId(a.getFoodId());
                        if(!list.containsKey(ac.get(0).getFoodName())){
                            list.put(ac.get(0).getFoodName(), 1);
                        }else{
                            int count = list.get(ac.get(0).getFoodName());
                            list.remove(ac.get(0).getFoodName());
                            list.put(ac.get(0).getFoodName(), ++count);
                        }
                    });
                });
                break;
        }
        return list;
    }

    private List<DiaryPage> getList(AppDatabase db) throws ParseException {
        Calendar cal = Calendar.getInstance();
        String dataToFormat = cal.get(Calendar.DAY_OF_MONTH) + " " + date;
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        Date dateFormat = new Date(sdf.parse(dataToFormat).getTime());
        cal.setTime(dateFormat);
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dateFrom = new Date(sdf.parse(1+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR)).getTime());
        Date dateTo = new Date(sdf.parse(cal.getActualMaximum(Calendar.DAY_OF_MONTH)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR)).getTime());
        List<DiaryPage> diaryPageList = db.diaryPageDao().getDiaryPageBetweenDate(dateFrom.getTime(), dateTo.getTime(), db.userDao().userLogged().get(0).getId());
        return diaryPageList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int getAverageKcal(AppDatabase db) throws ParseException {
        List<DiaryPage> diaryPages = getList(db);
        int averageKcal = 0;
        for(DiaryPage d: diaryPages){
            List<DiaryFood> foodList = db.diaryFoodDao().getFromPageIdAll(d.getDiaryId());
            for(DiaryFood f: foodList){
                List<Food> ac = db.foodDao().getFoodFromId(f.getFoodId());
                averageKcal += ac.get(0).getKcal();
            }
        }
        averageKcal /= diaryPages.size();
        return averageKcal;
    }

}
