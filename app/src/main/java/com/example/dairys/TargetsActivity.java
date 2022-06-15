package com.example.dairys;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.dairys.Database.AppDatabase;
import com.example.dairys.Database.DiaryPage;
import com.example.dairys.Database.Target;
import com.example.dairys.ViewModel.TargetsViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class TargetsActivity extends AppCompatActivity {

    ProgressBar progressBar;
    ProgressBar progressBarYear;
    TextView targetResult;
    TextView targetProgress;
    TextView targetComplete;
    AppDatabase db;
    TargetsViewModel viewModel;

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_targets);

        viewModel = new ViewModelProvider(this).get(TargetsViewModel.class);

        targetComplete = findViewById(R.id.targetComplete);
        progressBar = findViewById(R.id.progressBarMonth);
        targetResult = findViewById(R.id.targetsResult);
        targetProgress = findViewById(R.id.targetProgress);
        progressBarYear = findViewById(R.id.progressBarYear);

        db = AppDatabase.getInstance(this);
        viewModel.setDb(db);

        try {
            viewModel.setList();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        viewModel.setNewTarget();

        progressBar.setMax(viewModel.maxTarget);
        ProgressBarAnimation anim = new ProgressBarAnimation(progressBar, 0, viewModel.getNumActivityTarget());
        anim.setDuration(2000);
        progressBar.startAnimation(anim);
        progressBar.setProgress(viewModel.getNumActivityTarget());
        targetResult.setText(progressBar.getProgress() + "/" + viewModel.maxTarget);
        targetProgress.setText("Remaining " + viewModel.target + " activities: " + (viewModel.maxTarget - progressBar.getProgress()));
        progressBarYear.setMax(1);
        if(progressBar.getProgress() == viewModel.maxTarget){
            progressBarYear.setProgress(progressBarYear.getMax());
            targetComplete.setText("1/1");
        }
    }

    public class ProgressBarAnimation extends Animation {
        private ProgressBar progressBar;
        private float from;
        private float  to;

        public ProgressBarAnimation(ProgressBar progressBar, float from, float to) {
            super();
            this.progressBar = progressBar;
            this.from = from;
            this.to = to;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            float value = from + (to - from) * interpolatedTime;
            progressBar.setProgress((int) value);
        }

    }




}