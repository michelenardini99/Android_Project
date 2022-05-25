package com.example.dairys;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dairys.Database.Activity;
import com.example.dairys.Database.Food;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    List<Activity> myActivityList;
    Context context;

    public ActivityAdapter(List<Activity> myActivityList, Context context) {
        this.myActivityList = myActivityList;
        this.context = context;
    }

    @NonNull
    @Override
    public ActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.activity_page, parent, false);
        ActivityAdapter.ViewHolder viewHolder = new ActivityAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityAdapter.ViewHolder holder, int position) {
        final Activity myActivityData = myActivityList.get(position);

        holder.textViewName.setText(myActivityData.getActivityName());
        holder.imageView.setImageResource(myActivityData.getActivityIcon());
    }

    @Override
    public int getItemCount() {
        return myActivityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textViewName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageActivity);
            textViewName = itemView.findViewById(R.id.nameActivity);
        }
    }
}
