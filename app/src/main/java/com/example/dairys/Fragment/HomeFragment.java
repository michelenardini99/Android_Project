package com.example.dairys.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.dairys.ActivityAdapter;
import com.example.dairys.Database.Activity;
import com.example.dairys.Database.AppDatabase;
import com.example.dairys.Database.DiaryActivity;
import com.example.dairys.Database.DiaryFood;
import com.example.dairys.Database.DiaryPage;
import com.example.dairys.Database.Food;
import com.example.dairys.FoodActivity;
import com.example.dairys.FoodForPageAdapter;
import com.example.dairys.NewPage;
import com.example.dairys.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private ImageView imageHumor;
    private ImageView wallpaperCard;
    private MaterialTextView date;
    private RecyclerView listBreakfast;
    private RecyclerView listLunch;
    private RecyclerView listDinner;
    private RecyclerView listSnack;
    private RecyclerView listActivity;
    private MaterialCardView cardPage;
    private TextView noPage;
    private TextView titlePage;
    private TextView noteForPage;
    private AppDatabase db;
    private LottieAnimationView emptyAnimation;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = AppDatabase.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        noteForPage = getView().findViewById(R.id.noteForPage);
        emptyAnimation = getView().findViewById(R.id.emptyAnimation);
        wallpaperCard = getView().findViewById(R.id.imagePage);
        titlePage = getView().findViewById(R.id.titlePage);
        cardPage = getView().findViewById(R.id.pageCard);
        noPage = getView().findViewById(R.id.notPage);
        imageHumor = getView().findViewById(R.id.humorDay);
        listActivity = getView().findViewById(R.id.list_activity);
        listBreakfast = getView().findViewById(R.id.list_breakfast);
        listLunch = getView().findViewById(R.id.list_lunch);
        listDinner = getView().findViewById(R.id.list_dinner);
        listSnack = getView().findViewById(R.id.list_snack);
        date = getView().findViewById(R.id.dateHome);
        Calendar cal = Calendar.getInstance();
        setDate(cal);
        getView().findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToCreatePageDiary();
            }
        });

        try {
            setPageCard();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
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
                        calendar.add(Calendar.DATE, -1);
                        break;
                    case R.id.shiftRightDate:
                        calendar.add(Calendar.DATE, 1);
                        break;
                }
                setDate(calendar);
                try {
                    setPageCard();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        getView().findViewById(R.id.shiftRightDate).setOnClickListener(listener);
        getView().findViewById(R.id.shiftLeftDate).setOnClickListener(listener);

        listBreakfast.setHasFixedSize(true);
        listBreakfast.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        listDinner.setHasFixedSize(true);
        listDinner.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        listLunch.setHasFixedSize(true);
        listLunch.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        listSnack.setHasFixedSize(true);
        listSnack.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        listActivity.setHasFixedSize(true);
        listActivity.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setPageCard() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
        Date dateToParse = format.parse(date.getText().toString());
        Calendar c = Calendar.getInstance();
        c.setTime(dateToParse);
        String dateToSend = c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR);
        List<DiaryPage> page = db.diaryPageDao().getDiaryPageForDate(dateToSend);
        if(page.size() != 0){
            int id = page.get(0).getDiaryId();
            setHumorEmoji(page.get(0).getHumor());
            setFoodCard(id, "Breakfast", listBreakfast);
            setFoodCard(id, "Dinner", listDinner);
            setFoodCard(id, "Lunch", listLunch);
            setFoodCard(id, "Snack", listSnack);
            setImageCard(page.get(0).getPhoto());
            setNote(page.get(0).getNote());
            setActivity(id);
            cardPage.setVisibility(View.VISIBLE);
            noPage.setVisibility(View.INVISIBLE);
            emptyAnimation.loop(false);
            emptyAnimation.setVisibility(View.INVISIBLE);
        }else{
            cardPage.setVisibility(View.INVISIBLE);
            noPage.setVisibility(View.VISIBLE);
            emptyAnimation.setVisibility(View.VISIBLE);
            emptyAnimation.playAnimation();
            emptyAnimation.loop(true);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setActivity(int id) {
        List<DiaryActivity> diaryActivityList = db.diaryActivityDao().getFromPageId(id);
        if(diaryActivityList.size() != 0){
            List<Activity> activityList = new ArrayList<>();
            diaryActivityList.forEach( p -> {
                activityList.add(db.activityDao().getActivityFromId(p.getActivityId()).get(0));
            });
            listActivity.setAdapter(new ActivityAdapter(activityList, getActivity()));
        }
    }

    private void setNote(String note) {
        noteForPage.setText(note);
    }

    private void setImageCard(String photo) {
        if (photo == null){
            wallpaperCard.setImageResource(R.drawable.default_card_page_wallpaper);
        }else{
            wallpaperCard.setImageURI(Uri.parse(photo));
        }
    }

    private void setHumorEmoji(String humor) {
        switch (humor){
            case "Really happy":
                imageHumor.setImageResource(R.drawable.emotions_really_happy);
                titlePage.setText(R.string.really_happy_phrase);
                break;
            case "Happy":
                imageHumor.setImageResource(R.drawable.happy);
                titlePage.setText(R.string.happy_phrase);
                break;
            case "So so":
                imageHumor.setImageResource(R.drawable.so_so);
                titlePage.setText(R.string.so_so_phrase);
                break;
            case "Bad":
                imageHumor.setImageResource(R.drawable.bad);
                titlePage.setText(R.string.bad_phrase);
                break;
            case "Terrible":
                imageHumor.setImageResource(R.drawable.terrible);
                titlePage.setText(R.string.terrible_phrase);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setFoodCard(int id, String category, RecyclerView recyclerView){
        List<DiaryFood> foodForPage = db.diaryFoodDao().getFromPageId(id, category);
        if(foodForPage.size() != 0){
            List<Food> foodList = new ArrayList<>();
            foodForPage.forEach( p -> {
                foodList.add(db.foodDao().getFoodFromId(p.foodId).get(0));
            });
            recyclerView.setAdapter(new FoodForPageAdapter(foodList, getActivity()));
        }
    }


    private void setDate(Calendar cal){
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(cal.getTime());
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        date.setText(day + " " + month_name + " " + year);
    }

    public void goToCreatePageDiary(){
        Intent intent = new Intent(getContext(), NewPage.class);
        startActivity(intent);
    }
}