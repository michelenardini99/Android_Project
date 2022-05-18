package com.example.dairys;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class NewPageDiaryActivity extends AppCompatActivity {

    private EditText dataPicker;
    private LinearLayout layoutBreakfast;
    private ImageView imgBreakfast;
    private View.OnClickListener clickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_page_diary);

        dataPicker = findViewById(R.id.dataPicker);
        layoutBreakfast = (LinearLayout) findViewById(R.id.breakfastLayout);
        imgBreakfast = findViewById(R.id.imgBreakfast);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH)+1;

        dataPicker.setText(day+"/"+month+"/"+year);

        dataPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewPageDiaryActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String date = day + "/" + month + "/" + year;
                        dataPicker.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });

        layoutBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setBackgroundColor(R.color.green_A400);
            }
        });

    }


}