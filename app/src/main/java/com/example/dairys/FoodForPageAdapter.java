package com.example.dairys;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.dairys.Database.Food;

import java.util.List;

public class FoodForPageAdapter extends RecyclerView.Adapter<FoodForPageAdapter.ViewHolder> {

    List<Food> myFoodDataList;
    Context context;

    public FoodForPageAdapter(List<Food> myFoodDataList, Activity activity) {
        this.myFoodDataList = myFoodDataList;
        this.context = activity;
    }

    @NonNull
    @Override
    public FoodForPageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.food_page_layout, parent, false);
        FoodForPageAdapter.ViewHolder viewHolder = new FoodForPageAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodForPageAdapter.ViewHolder holder, int position) {
        final Food myFoodData = myFoodDataList.get(position);

        holder.textViewName.setText(myFoodData.getFoodName());
        holder.textViewKcal.setText(myFoodData.getKcal() + " kcal");
        holder.imageView.setImageResource(myFoodData.getPhotoFood());

    }

    @Override
    public int getItemCount() {
        return myFoodDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textViewName;
        TextView textViewKcal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageFood);
            textViewName = itemView.findViewById(R.id.nameFoodForPage);
            textViewKcal = itemView.findViewById(R.id.kcaFoodForPage);
        }
    }
}
