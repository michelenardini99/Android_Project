package com.example.dairys;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FoodActivity extends AppCompatActivity {

    private List<MyFoodData> myFoodDataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MaterialToolbar mActionBarToolbar;
    private SearchView searchView;
    private MyFoodAdapter myFoodAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_selected);

        Intent intent = getIntent();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewFood);
        mActionBarToolbar = findViewById(R.id.topAppBarFood);
        searchView = findViewById(R.id.searchView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setSupportActionBar(mActionBarToolbar);

        getSupportActionBar().setTitle(intent.getStringExtra("Category"));

        myFoodDataList = Arrays.asList(
                new MyFoodData("Pizza", "256 kcal", R.drawable.pizza),
                new MyFoodData("Mela", "60 kcal", R.drawable.mele),
                new MyFoodData("Pasta al ragu", "335 kcal", R.drawable.ragu),
                new MyFoodData("Hamburger", "302 kcal", R.drawable.hamburgher),
                new MyFoodData("Insalata greca", "351 kcal", R.drawable.insalata_greca),
                new MyFoodData("Salsiccia", "208 kcal", R.drawable.salsiccia),
                new MyFoodData("Crema catalana", "212 kcal", R.drawable.crema_catalana)
        );

        myFoodAdapter = new MyFoodAdapter(myFoodDataList, FoodActivity.this);
        recyclerView.setAdapter(myFoodAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_app_bar_food, menu);
        return true;


    }


    public void saveAndReturn(){
        int i = 0;
        for(MyFoodData foodData: myFoodAdapter.myFoodDataList){
            if (foodData.isSelected()){
                i++;
            }
        }
        Toast.makeText(FoodActivity.this, String.valueOf(i), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_food:
                saveAndReturn();
                break;
        }
        return true;
    }

    private void filterList(String newText) {
        List<MyFoodData> filteredList = new ArrayList<>();
        for(MyFoodData foodData: myFoodDataList){
            if(foodData.getFoodName().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(foodData);
            }
        }
        if (!filteredList.isEmpty()){
            myFoodAdapter.setFilteredList(filteredList);
        }
    }
}