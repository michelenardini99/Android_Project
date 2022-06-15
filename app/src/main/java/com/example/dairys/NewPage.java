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
import android.widget.Toast;

import java.sql.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dairys.Database.AppDatabase;
import com.example.dairys.Database.DiaryActivity;
import com.example.dairys.Database.DiaryFood;
import com.example.dairys.Database.DiaryPage;
import com.example.dairys.Database.Food;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewPage extends AppCompatActivity {

    private final static int REQUEST_CODE_FOOD = 1;
    private final static int REQUEST_CODE_PHOTO = 2;

    Map<String, List<Food>> selectedFood = new HashMap<>();
    String humorSelected;
    Uri selectedImage;
    List<String> activity = new ArrayList<>();
    private int idTemp = 0;
    private List<Integer> humorList;
    private List<Integer> actList;
    private List<Integer> categoryList;

    private EditText dataPicker;
    private ExtendedFloatingActionButton camera;
    private ImageView imagePhoto;
    private Chip breakfast;
    private Chip lunch;
    private Chip dinner;
    private Chip snack;
    private ChipGroup humorGroup;
    private ChipGroup activityGroup;
    private ChipGroup eatGroup;
    private List<Chip> humorChip;
    private List<Chip> activityChip;
    private TextInputEditText noteEditText;
    private MaterialToolbar topAppBar;
    private AppDatabase db;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_page_diary);

        db = AppDatabase.getInstance(NewPage.this);


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
        eatGroup = findViewById(R.id.eatGroup);
        humorGroup = findViewById(R.id.humorGroup);
        activityGroup = findViewById(R.id.activityGroup);
        humorChip = Arrays.asList(findViewById(R.id.reallyHappy),
                findViewById(R.id.happy),
                findViewById(R.id.so_so),
                findViewById(R.id.bad),
                findViewById(R.id.terrible)
        );
        humorList = Arrays.asList(R.string.really_happy,
                R.string.happy,
                R.string.so_so,
                R.string.bad,
                R.string.terrible);
        actList = Arrays.asList(R.string.sport,
                R.string.immersion,
                R.string.walk,
                R.string.cycling,
                R.string.excursion);
        activityChip = Arrays.asList(findViewById(R.id.walk),
                findViewById(R.id.sport),
                findViewById(R.id.excursion),
                findViewById(R.id.immersion),
                findViewById(R.id.cycling)
        );
        categoryList = Arrays.asList(R.string.breakfast, R.string.lunch, R.string.dinner, R.string.snack);

        setSupportActionBar(topAppBar);

        Intent intent = getIntent();
        if (intent.hasExtra("isToEdit")){
            try {
                editPage();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            setDataPicker(null);
        }

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
                goToSelectFood(getString(R.string.breakfast_db));
            }
        });

        lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSelectFood(getString(R.string.lunch_db));
            }
        });

        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSelectFood(getString(R.string.dinner_db));
            }
        });

        snack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSelectFood(getString(R.string.snack_db));
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void editPage() throws ParseException {
        List<DiaryPage> page = getPageToEdit();
        idTemp = page.get(0).getDiaryId();
        String humor = page.get(0).getHumor();
        for(int h : humorList){
            if(humor.equals(getString(h))){
                setHumorChipSelected(h);
            }
        }
        db.diaryActivityDao().getFromPageId(page.get(0).getDiaryId()).forEach(a -> {
            setActivityChipSelected(db.activityDao().getActivityFromId(a.getActivityId()).get(0));
        });
        setFoodSelected(page.get(0).getDiaryId());
        if(page.get(0).getNote() != null){
            noteEditText.setText(page.get(0).getNote());
        }
        if(page.get(0).getPhoto() != null){
            imagePhoto.setImageURI(Uri.parse(page.get(0).getPhoto() ));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setFoodSelected(int diaryId) {
        Map<String, List<Food>> foods = new HashMap<>();
        List<String> category = Arrays.asList("Breakfast", "Lunch", "Dinner", "Snack");
        category.forEach(c -> {
            List<Food> temp = new ArrayList<>();
            if(!db.diaryFoodDao().getFromPageId(diaryId, c).isEmpty()){
                checkCategoryFood(c);
                db.diaryFoodDao().getFromPageId(diaryId, c).forEach( f -> {
                    temp.add(db.foodDao().getFoodFromId(f.foodId).get(0));
                });
                foods.put(c, temp);
            }
        });

        selectedFood = foods;
    }

    private void checkCategoryFood(String c) {
        switch (c){
            case "Breakfast":
                eatGroup.check(R.id.breakfast);
                break;
            case "Lunch":
                eatGroup.check(R.id.lunch);
                break;
            case "Dinner":
                eatGroup.check(R.id.dinner);
                break;
            case "Snack":
                eatGroup.check(R.id.snack);
                break;
        }
    }

    private void setActivityChipSelected(com.example.dairys.Database.Activity activity) {
        switch (activity.getActivityName()){
            case "Sport":
                activityGroup.check(R.id.sport);
                break;
            case "Walk":
                activityGroup.check(R.id.walk);
                break;
            case "Excursion":
                activityGroup.check(R.id.excursion);
                break;
            case "Immersion":
                activityGroup.check(R.id.immersion);
                break;
            case "Cycling":
                activityGroup.check(R.id.cycling);
                break;
        }
    }

    private void setHumorChipSelected(int humor) {
        switch (humor){
            case R.string.really_happy:
                humorGroup.check(R.id.reallyHappy);
                break;
            case R.string.happy:
                humorGroup.check(R.id.happy);
                break;
            case R.string.so_so:
                humorGroup.check(R.id.so_so);
                break;
            case R.string.bad:
                humorGroup.check(R.id.bad);
                break;
            case R.string.terrible:
                humorGroup.check(R.id.terrible);
                break;
        }
    }

    private List<DiaryPage> getPageToEdit() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
        java.util.Date dateToParse = format.parse(getIntent().getExtras().getString("date"));
        Calendar c = Calendar.getInstance();
        c.setTime(dateToParse);
        String dateToSend = c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR);
        setDataPicker(dateToSend);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date date = new java.util.Date(sdf.parse(dateToSend).getTime());
        long l = date.getTime();
        return db.diaryPageDao().getDiaryPageForDate(l, db.userDao().userLogged().get(0).getId());
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
        if(humorIsSelected()) {
            switch (item.getItemId()) {
                case R.id.savePage:
                    if (idTemp != 0) {
                        db.diaryPageDao().deletePage(idTemp);
                        db.diaryFoodDao().deleteFoods(idTemp);
                        db.diaryActivityDao().deleteActivity(idTemp);
                    }
                    setHumor();
                    setActivity();
                    int diaryId = db.diaryPageDao().getLastInsert().isEmpty() ? 0 : db.diaryPageDao().getLastInsert().get(0).getDiaryId();
                    diaryId++;
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = null;
                    try {
                        date = new Date(sdf.parse(dataPicker.getText().toString()).getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    db.diaryPageDao().insertAll(
                            new DiaryPage(diaryId, db.userDao().userLogged().get(0).getId(),
                                    date,
                                    noteEditText.getText().toString(),
                                    selectedImage == null ? null : selectedImage.toString(),
                                    humorSelected
                            )
                    );
                    int finalDiaryId = diaryId;
                    for (String category : selectedFood.keySet()) {
                        for (Food food : selectedFood.get(category)) {
                            db.diaryFoodDao().insertAll(new DiaryFood(finalDiaryId, food.getFoodId(), category));
                        }
                    }
                    activity.forEach(a -> {
                        db.diaryActivityDao().insertAll(new DiaryActivity(finalDiaryId, db.activityDao().getActivityFromName(a).get(0).getActivityId()));
                    });
                    break;
            }
            Intent intent = new Intent(NewPage.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(this, "At least select your mood\n", Toast.LENGTH_SHORT).show();
        }
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
                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });
    }

    private void setHumor(){
        List<Integer> ids = humorGroup.getCheckedChipIds();
        for (Integer id:ids){
            Chip chip = humorGroup.findViewById(id);
            for(int h: humorList){
                if(getString(h).equals(chip.getText().toString())){
                    humorSelected = getHumor(h);
                }
            }
        }
    }

    private boolean humorIsSelected(){
        return humorGroup.getCheckedChipIds().size() != 0;
    }

    private void setActivity(){
        List<Integer> ids = activityGroup.getCheckedChipIds();
        activity.clear();
        for (Integer id:ids){
            Chip chip = activityGroup.findViewById(id);
            for(int a: actList){
                if(getString(a).equals(chip.getText().toString())){
                    setActivity(a);
                }
            }
        }
    }

    private void setActivity(int act) {
        switch (act){
            case R.string.sport:
                activity.add("Sport");
                break;
            case R.string.walk:
                activity.add("Walk");
                break;
            case R.string.excursion:
                activity.add("Excursion");
                break;
            case R.string.cycling:
                activity.add("Cycling");
                break;
            case R.string.immersion:
                activity.add("Immersion");
                break;
        }
    }

    private String getHumor(int c){
        switch (c){
            case R.string.really_happy:
                return getString(R.string.really_happy_db);
            case R.string.happy:
                return getString(R.string.happy_db);
            case R.string.so_so:
                return getString(R.string.so_so_db);
            case R.string.bad:
                return getString(R.string.bad_db);
            case R.string.terrible:
                return getString(R.string.terrible_db);
        }
        return "";
    }
}
