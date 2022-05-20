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

import java.util.List;

public class MyFoodAdapter extends  RecyclerView.Adapter<MyFoodAdapter.ViewHolder>{

    List<MyFoodData> myFoodDataList;
    Context context;

    public MyFoodAdapter(List<MyFoodData> myFoodDataList, Activity activity) {
        this.myFoodDataList = myFoodDataList;
        this.context = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.food_selected, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final  MyFoodData myFoodData = myFoodDataList.get(position);
        holder.textViewName.setText(myFoodData.getFoodName());
        holder.textViewKcal.setText(myFoodData.getFoodKcal());
        holder.foodImage.setImageResource(myFoodData.getFoodImage());
    }

    @Override
    public int getItemCount() {
        return myFoodDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView foodImage;
        TextView textViewName;
        TextView textViewKcal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.imageView);
            textViewName = itemView.findViewById(R.id.foodName);
            textViewKcal = itemView.findViewById(R.id.foodKcal);
        }

    }
}
