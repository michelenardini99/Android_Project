package com.example.dairys.Fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dairys.Database.AppDatabase;
import com.example.dairys.Database.DiaryPage;
import com.example.dairys.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
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

    BarChart barChart;
    PieChart pieChart;
    AppDatabase db;
    MaterialTextView date;

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

        try {
            createBarChart();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void setDate(Calendar cal){
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(cal.getTime());
        int year = cal.get(Calendar.YEAR);
        date.setText(month_name + " " + year);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createBarChart() throws ParseException {
        List<DiaryPage> diaryPages = getList();

        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        Map<String, Integer> humor = new HashMap<>();

        diaryPages.forEach( d -> {
            if(!humor.containsKey(d.getHumor())){
                humor.put(d.getHumor(), 1);
                Toast.makeText(getContext(), d.getHumor() + " " + humor.get(d.getHumor()), Toast.LENGTH_SHORT).show();
            }else{
                int count = humor.get(d.getHumor());
                humor.remove(d.getHumor());
                humor.put(d.getHumor(), ++count);
            }
        });

        int i = 1;
        humor.forEach((h, v) -> {
            PieEntry pieEntry = new PieEntry(v, h);
            pieEntries.add(pieEntry);
        });
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Humor");

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
        pieChart.setCenterText("Spending by humor");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);

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
}