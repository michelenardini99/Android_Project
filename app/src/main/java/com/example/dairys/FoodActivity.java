package com.example.dairys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.dairys.Database.AppDatabase;
import com.example.dairys.Database.Food;
import com.google.android.material.appbar.MaterialToolbar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FoodActivity extends AppCompatActivity {

    private List<Food> myFoodDataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MaterialToolbar mActionBarToolbar;
    private SearchView searchView;
    private MyFoodAdapter myFoodAdapter;
    private AppDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_selected);

        db = AppDatabase.getInstance(FoodActivity.this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewFood);
        mActionBarToolbar = findViewById(R.id.topAppBarFood);
        searchView = findViewById(R.id.searchView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setSupportActionBar(mActionBarToolbar);

        getSupportActionBar().setTitle(getIntent().getStringExtra("Category"));

        myFoodDataList = db.foodDao().getAll();

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
        List<Food> foodSelected = new ArrayList<>();
        for(Food foodData: myFoodAdapter.myFoodDataList){
            if (foodData.isSelected()){
                foodSelected.add(foodData);
            }
        }
        DataWrapper dataWrapper = (DataWrapper) getIntent().getSerializableExtra("food");
        Map<String, List<Food>> foodList = dataWrapper.getData();
        if(foodList.containsKey(getIntent().getStringExtra("Category"))){
            foodList.remove(getIntent().getStringExtra("Category"));
        }
        foodList.put(getIntent().getStringExtra("Category"), foodSelected);
        Intent data = new Intent();
        data.putExtra("food", (Serializable) foodList);
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_food:
                saveAndReturn();
                break;
        }
        finish();
        return true;
    }

    private void filterList(String newText) {
        List<Food> filteredList = new ArrayList<>();
        for(Food foodData: myFoodDataList){
            if(foodData.getFoodName().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(foodData);
            }
        }
        if (!filteredList.isEmpty()){
            myFoodAdapter.setFilteredList(filteredList);
        }
    }
}