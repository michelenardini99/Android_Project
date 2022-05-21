package com.example.dairys;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyFoodAdapter extends  RecyclerView.Adapter<MyFoodAdapter.ViewHolder>{

    List<MyFoodData> myFoodDataList;
    Context context;

    public MyFoodAdapter(List<MyFoodData> myFoodDataList, FoodActivity activity) {
        this.myFoodDataList = myFoodDataList;
        this.context = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.food_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MyFoodData myFoodData = myFoodDataList.get(position);
        holder.textViewName.setText(myFoodData.getFoodName());
        holder.textViewKcal.setText(myFoodData.getFoodKcal());
        holder.imageView.setImageResource(myFoodData.getFoodImage());

        holder.addFoodAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myFoodData.isSelected()){
                    myFoodData.setSelected(false);
                    holder.addFoodAnimation.setFrame(30);
                }else{
                    holder.addFoodAnimation.playAnimation();
                    myFoodData.setSelected(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return myFoodDataList.size();
    }

    public void setFilteredList(List<MyFoodData> filteredList) {
        this.myFoodDataList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textViewName;
        TextView textViewKcal;
        LottieAnimationView addFoodAnimation;
        boolean isSelect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            isSelect = false;
            imageView = itemView.findViewById(R.id.imageView);
            textViewName = itemView.findViewById(R.id.foodName);
            textViewKcal = itemView.findViewById(R.id.foodKcal);
            addFoodAnimation = itemView.findViewById(R.id.addFoodAnimation);

            addFoodAnimation.setFrame(30);
        }

    }
}
