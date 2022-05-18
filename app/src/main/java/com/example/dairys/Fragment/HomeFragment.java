package com.example.dairys.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dairys.MainActivity;
import com.example.dairys.NewPageDiaryActivity;
import com.example.dairys.R;
import com.example.dairys.databinding.ActivityMainBinding;
import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomeFragment extends Fragment {

    private MaterialTextView date;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        date = getView().findViewById(R.id.dateHome);
        setDate();
        getView().findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToCreatePageDiary();
            }
        });
    }

    private void setDate(){
        Calendar cal=Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(cal.getTime());
        int year = Calendar.getInstance().get(Calendar.YEAR);
        date.setText(month_name + " " + year);
    }

    public void goToCreatePageDiary(){
        Intent intent = new Intent(getContext(), NewPageDiaryActivity.class);
        startActivity(intent);
    }
}