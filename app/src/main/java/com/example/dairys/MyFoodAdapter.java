package com.example.dairys;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.dairys.Database.Food;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.logging.Handler;

public class MyFoodAdapter extends  RecyclerView.Adapter<MyFoodAdapter.ViewHolder>{

    List<Food> myFoodDataList;
    Context context;

    public MyFoodAdapter(List<Food> myFoodDataList, FoodActivity activity) {
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
        final Food myFoodData = myFoodDataList.get(position);
        holder.textViewName.setText(myFoodData.getFoodName());
        holder.textViewKcal.setText(myFoodData.getKcal() + " kcal");
        holder.imageView.setImageResource(myFoodData.getPhotoFood());

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

    public void setFilteredList(List<Food> filteredList) {
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
