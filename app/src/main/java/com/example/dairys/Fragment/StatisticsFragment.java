package com.example.dairys.Fragment;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
import com.example.dairys.ViewModel.StatisticsViewModel;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
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
    StatisticsViewModel viewModel;
    ExtendedFloatingActionButton selectButton;

    public static enum ColorChart{
        MATERIAL,
        VORDIPLOM,
        PASTEL,
        LIBERTY,
        COLORFUL,
        JOYFUL
    }

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

        viewModel = new ViewModelProvider(this).get(StatisticsViewModel.class);
        linearLayoutLegend = getView().findViewById(R.id.linearLayoutLegenda);
        lineChart = getView().findViewById(R.id.lineChart);
        selectButton = getView().findViewById(R.id.select_data);
        pieChart = getView().findViewById(R.id.pieChart);
        date = getView().findViewById(R.id.dateHome);
        selectButton.setText(R.string.humor);

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
                            selectButton.setText(R.string.humor);
                            createLineChart();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.activityStatistics:
                        try {
                            lineChart.setVisibility(View.INVISIBLE);
                            linearLayoutLegend.setVisibility(View.INVISIBLE);
                            selectButton.setText(R.string.activity);
                            createPieChart("Activity");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.foodStatistics:
                        try {
                            lineChart.setVisibility(View.INVISIBLE);
                            linearLayoutLegend.setVisibility(View.INVISIBLE);
                            selectButton.setText(R.string.food);
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
        LineData lineData = viewModel.createLineChart(getContext());
        lineData.setValueTextColor(setTextColor());
        lineChart.setData(lineData);
        lineChart.invalidate();
        lineChart.animateX(4000, Easing.EaseInOutQuad);

        lineChart.getXAxis().setTextColor(setTextColor());
        lineChart.getAxisLeft().setTextColor(setTextColor());

        lineChart.setVisibility(View.VISIBLE);
        linearLayoutLegend.setVisibility(View.VISIBLE);

        lineChart.setBorderColor(setTextColor());

        lineChart.getDescription().setText("Humor chart");

        lineChart.getDescription().setTextColor(setTextColor());

        lineChart.getLegend().setTextColor(setTextColor());

        lineChart.setBorderColor(setTextColor());

    }


    private void setDate(Calendar cal){
        viewModel.setDate(cal);
        date.setText(viewModel.date);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createPieChart(String category) throws ParseException {
        PieDataSet pieDataSet = viewModel.createPieChart(getContext(), category);

        pieDataSet.setColors(setColor());

        pieDataSet.setValueLineColor(setTextColor());
        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter(pieChart));
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(setTextColor());


        pieChart.setData(pieData);
        pieChart.invalidate();

        pieChart.animateY(1400, Easing.EaseInOutQuad);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(setTextColor());
        if(category.equals("Food")){
            pieChart.setCenterText(category + " Average kcal: " + String.valueOf(viewModel.getAverageKcal(db)));
        }else{
            pieChart.setCenterText(category);
        }
        pieChart.setCenterTextSize(20);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setTextColor(setTextColor());

        pieChart.setVisibility(View.VISIBLE);

    }

    private int setTextColor(){
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                return Color.WHITE;
            case Configuration.UI_MODE_NIGHT_NO:
                return Color.BLACK;
        }
        return 0;
    }

    private int[] setColor(){
        if(SettingsFragment.readColor(getContext()) != null){
            switch (SettingsFragment.readColor(getContext())) {
                case VORDIPLOM:
                    return ColorTemplate.VORDIPLOM_COLORS;
                case MATERIAL:
                    return ColorTemplate.MATERIAL_COLORS;
                case LIBERTY:
                    return ColorTemplate.LIBERTY_COLORS;
                case PASTEL:
                    return ColorTemplate.PASTEL_COLORS;
                case COLORFUL:
                    return ColorTemplate.COLORFUL_COLORS;
                case JOYFUL:
                    return ColorTemplate.JOYFUL_COLORS;
            }
        }
        return ColorTemplate.MATERIAL_COLORS;
    }

}