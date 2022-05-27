package com.example.dairys.Fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.dairys.Database.Activity;
import com.example.dairys.Database.AppDatabase;
import com.example.dairys.Database.DiaryActivity;
import com.example.dairys.Database.DiaryFood;
import com.example.dairys.Database.DiaryPage;
import com.example.dairys.Database.Food;
import com.example.dairys.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StatisticsFragment extends Fragment {

    LinearLayout linearLayoutLegend;
    LineChart lineChart;
    PieChart pieChart;
    AppDatabase db;
    MaterialTextView date;
    ExtendedFloatingActionButton selectButton;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        linearLayoutLegend = getView().findViewById(R.id.linearLayoutLegenda);
        lineChart = getView().findViewById(R.id.lineChart);
        selectButton = getView().findViewById(R.id.select_data);
        pieChart = getView().findViewById(R.id.pieChart);
        date = getView().findViewById(R.id.dateHome);

        db = AppDatabase.getInstance(getContext());

        setDate(Calendar.getInstance());

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat format = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
                Date dateToChange = null;
                try {
                    dateToChange = format.parse(date.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateToChange);
                switch (view.getId()){
                    case R.id.shiftLeftDate:
                        calendar.add(Calendar.MONTH, -1);
                        break;
                    case R.id.shiftRightDate:
                        calendar.add(Calendar.MONTH, 1);
                        break;
                }
                setDate(calendar);
            }
        };

        getView().findViewById(R.id.shiftRightDate).setOnClickListener(listener);
        getView().findViewById(R.id.shiftLeftDate).setOnClickListener(listener);

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view, R.menu.statistics_menu);
            }
        });

        try {
            createLineChart();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void showMenu(View view, int statistics_menu) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);

        popupMenu.getMenuInflater().inflate(statistics_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.humorStatistics:
                        try {
                            pieChart.setVisibility(View.INVISIBLE);
                            createLineChart();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.activityStatistics:
                        try {
                            lineChart.setVisibility(View.INVISIBLE);
                            linearLayoutLegend.setVisibility(View.INVISIBLE);
                            createPieChart("Activity");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.foodStatistics:
                        try {
                            lineChart.setVisibility(View.INVISIBLE);
                            linearLayoutLegend.setVisibility(View.INVISIBLE);
                            createPieChart("Food");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                return true;
            }
        });

        popupMenu.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createLineChart() throws ParseException {
        List<DiaryPage> diaryPages = getList();

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

        LineData lineData = new LineData(iLineDataSets);
        lineChart.setData(lineData);
        lineChart.invalidate();
        lineChart.animateX(4000, Easing.EaseInOutQuad);

        lineDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        lineDataSet.getValueTextColor(Color.BLACK);

        lineChart.setVisibility(View.VISIBLE);
        linearLayoutLegend.setVisibility(View.VISIBLE);

    }


    private void setDate(Calendar cal){
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(cal.getTime());
        int year = cal.get(Calendar.YEAR);
        date.setText(month_name + " " + year);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createPieChart(String category) throws ParseException {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        Map<String, Integer> list = getMapList(category);

        int i = 1;
        list.forEach((h, v) -> {
            PieEntry pieEntry = new PieEntry(v, h);
            pieEntries.add(pieEntry);
        });
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Activity");

        pieDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter(pieChart));
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(Color.BLACK);

        pieChart.setData(pieData);
        pieChart.invalidate();

        pieChart.animateY(1400, Easing.EaseInOutQuad);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Spending by " + category);
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);

        pieChart.setVisibility(View.VISIBLE);

    }

    private List<DiaryPage> getList() throws ParseException {
        Calendar cal = Calendar.getInstance();
        String dataToFormat = cal.get(Calendar.DAY_OF_MONTH) + " " + date.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        Date dateFormat = new Date(sdf.parse(dataToFormat).getTime());
        cal.setTime(dateFormat);
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dateFrom = new Date(sdf.parse(1+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR)).getTime());
        Date dateTo = new Date(sdf.parse(cal.getActualMaximum(Calendar.DAY_OF_MONTH)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR)).getTime());
        List<DiaryPage> diaryPageList = db.diaryPageDao().getDiaryPageBetweenDate(dateFrom.getTime(), dateTo.getTime());
        return diaryPageList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Map<String, Integer> getMapList(String category) throws ParseException {
        List<DiaryPage> diaryPages = getList();

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
}