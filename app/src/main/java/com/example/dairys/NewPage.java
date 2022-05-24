package com.example.dairys;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.dairys.Access.LoginActivity;
import com.example.dairys.Database.AppDatabase;
import com.example.dairys.Database.DiaryFood;
import com.example.dairys.Database.DiaryPage;
import com.example.dairys.Database.Food;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class NewPage extends AppCompatActivity {

    private final static int REQUEST_CODE_FOOD = 1;
    private final static int REQUEST_CODE_PHOTO = 2;

    Map<String, List<Food>> selectedFood = new HashMap<>();
    String humorSelected;
    Uri selectedImage;
    List<String> activity = new ArrayList<>();

    private EditText dataPicker;
    private ExtendedFloatingActionButton camera;
    private ImageView imagePhoto;
    private Chip breakfast;
    private Chip lunch;
    private Chip dinner;
    private Chip snack;
    private ChipGroup humorGroup;
    private ChipGroup activityGroup;
    private List<Chip> humorChip;
    private List<Chip> activityChip;
    private TextInputEditText noteEditText;
    private MaterialToolbar topAppBar;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_page_diary);

        selectedImage = null;
        topAppBar = findViewById(R.id.topAppBarPage);
        noteEditText = findViewById(R.id.noteEditText);
        dataPicker = findViewById(R.id.dataPicker);
        camera = findViewById(R.id.camera);
        imagePhoto = findViewById(R.id.image);
        breakfast = findViewById(R.id.breakfast);
        lunch = findViewById(R.id.lunch);
        dinner = findViewById(R.id.dinner);
        snack = findViewById(R.id.snack);
        humorGroup = findViewById(R.id.humorGroup);
        activityGroup = findViewById(R.id.activityGroup);
        humorChip = Arrays.asList(findViewById(R.id.reallyHappy),
                findViewById(R.id.happy),
                findViewById(R.id.so_so),
                findViewById(R.id.bad),
                findViewById(R.id.terrible)
        );
        activityChip = Arrays.asList(findViewById(R.id.walk),
                findViewById(R.id.sport),
                findViewById(R.id.excursion),
                findViewById(R.id.immersion),
                findViewById(R.id.cycling)
        );

        setSupportActionBar(topAppBar);

        setDataPicker(null);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 2);
            }
        });

        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSelectFood(breakfast.getText().toString());
            }
        });

        lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSelectFood(lunch.getText().toString());
            }
        });

        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSelectFood(dinner.getText().toString());
            }
        });

        snack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSelectFood(snack.getText().toString());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_app_bar, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.savePage:
                setHumor();
                setActivity();
                AppDatabase db = AppDatabase.getInstance(NewPage.this);
                int diaryId = db.diaryPageDao().getLastInsert().isEmpty() ? 0 : db.diaryPageDao().getLastInsert().get(0).getDiaryId();
                diaryId++;
                db.diaryPageDao().insertAll(
                        new DiaryPage(diaryId,
                                dataPicker.getText().toString(),
                                noteEditText.getText().toString(),
                                selectedImage == null ? null : selectedImage.toString(),
                                humorSelected
                        )
                );
                int finalDiaryId = diaryId;
                for(String category: selectedFood.keySet()){
                    for(Food food: selectedFood.get(category)){
                        db.diaryFoodDao().insertAll(new DiaryFood(finalDiaryId, food.getFoodId(), category));
                    }
                }
                break;
        }
        Intent intent = new Intent(NewPage.this, MainActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    private void goToSelectFood(String category) {
        Intent intent = new Intent(NewPage.this, FoodActivity.class);
        intent.putExtra("Category", category);
        intent.putExtra("food", new DataWrapper(selectedFood));
        startActivityForResult(intent, 1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == REQUEST_CODE_FOOD){
                selectedFood = (Map<String, List<Food>>) data.getSerializableExtra("food");
            }else if(requestCode == REQUEST_CODE_PHOTO){
                selectedImage = data.getData();
                imagePhoto.setVisibility(View.VISIBLE);
                imagePhoto.setImageURI(selectedImage);
            }
        }
    }

    private void setDataPicker(String date) {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH) + 1;
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        if(date != null){
            dataPicker.setText(date);
        }else{
            dataPicker.setText(day+"/"+month+"/"+year);
        }

        dataPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewPage.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String date = day + "/" + (month+1) + "/" + year;
                        dataPicker.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });
    }

    private void setHumor(){
        List<Integer> ids = humorGroup.getCheckedChipIds();
        for (Integer id:ids){
            Chip chip = humorGroup.findViewById(id);
            humorSelected = chip.getText().toString();
        }
    }

    private void setActivity(){
        List<Integer> ids = activityGroup.getCheckedChipIds();
        activity.clear();
        for (Integer id:ids){
            Chip chip = activityGroup.findViewById(id);
            activity.add(chip.getText().toString());
        }
    }
}
