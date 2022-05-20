package com.example.dairys;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

public class FoodActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_selected);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewFood);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<MyFoodData> myFoodDataList = Arrays.asList(
                new MyFoodData("Pizza", "256 kcal", R.drawable.pizza),
                new MyFoodData("Mela", "60 kcal", R.drawable.mele),
                new MyFoodData("Pasta al ragu", "335 kcal", R.drawable.pasta_al_ragu),
                new MyFoodData("Hamburger", "302 kcal", R.drawable.hamburgher),
                new MyFoodData("Insalata greca", "351 kcal", R.drawable.insalata_greca),
                new MyFoodData("Salsiccia", "208 kcal", R.drawable.salsiccia),
                new MyFoodData("Crema catalana", "212 kcal", R.drawable.crema_catalana)
        );

        MyFoodAdapter myFoodAdapter = new MyFoodAdapter(myFoodDataList, this);
        recyclerView.setAdapter(myFoodAdapter);
    }
}