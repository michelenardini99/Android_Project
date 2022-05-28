package com.example.dairys.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dairys.Database.AppDatabase;
import com.example.dairys.Database.DreamDiary;
import com.example.dairys.Database.DreamTag;
import com.example.dairys.Database.Food;
import com.example.dairys.DreamDiaryAdapter;
import com.example.dairys.NewPage;
import com.example.dairys.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DreamDiaryFragment extends Fragment {

    ExtendedFloatingActionButton saveDream;
    private RecyclerView recyclerView;
    private FloatingActionButton newDream;
    private List<DreamDiary> dreamDiaryList = new ArrayList<>();
    AppDatabase db;

    public DreamDiaryFragment() {
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
        return inflater.inflate(R.layout.fragment_dream_diary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = AppDatabase.getInstance(getContext());

        saveDream = getView().findViewById(R.id.saveDream);
        recyclerView = getView().findViewById(R.id.dream_list);
        newDream = getView().findViewById(R.id.new_dream);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dreamDiaryList = db.dreamDiaryDao().getAll();

        DreamDiaryAdapter dreamDiaryAdapter = new DreamDiaryAdapter(dreamDiaryList, getContext());
        recyclerView.setAdapter(dreamDiaryAdapter);

        newDream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext(), R.style.FullscreenDialogTheme);
                View mView = getLayoutInflater().inflate(R.layout.add_dream, null);

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();

                TextView datePicker = mView.findViewById(R.id.addDreamDate);
                EditText title = mView.findViewById(R.id.titleDream);
                EditText description = mView.findViewById(R.id.descriptionDream);
                EditText tag = mView.findViewById(R.id.tagDream);
                ChipGroup chipGroupDream = mView.findViewById(R.id.chipGroupDream);
                AppCompatButton addTag = mView.findViewById(R.id.addTag);
                ExtendedFloatingActionButton saveDream = mView.findViewById(R.id.saveDream);

                setDataPicker(datePicker);

                addTag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String t = tag.getText().toString().toLowerCase();
                        setChip(t, chipGroupDream);
                    }
                });


                saveDream.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int id = (db.dreamDiaryDao().getMaxId())+1;
                        db.dreamDiaryDao().insertAll(new DreamDiary(id, title.getText().toString(),
                                description.getText().toString(),
                                0,
                                db.userDao().userLogged().get(0).getId(),
                                datePicker.getText().toString()));
                        for (int i = 0; i < chipGroupDream.getChildCount(); i++) {
                            String tag = ((Chip) chipGroupDream.getChildAt(i)).getText().toString();
                            db.dreamTagDao().insertAll(new DreamTag(id, tag));
                        }
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void setDataPicker(TextView dataPicker) {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat month_format = new SimpleDateFormat("MMM");
        calendar.set(Calendar.MONTH,month);
        String month_name = month_format.format(calendar.getTime());

        dataPicker.setText(day + " " + month_name + " " + year);

        dataPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(Calendar.MONTH,month);
                        String date = day + " " + month_format.format(calendar.getTime()) + " " + year;
                        dataPicker.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });
    }

    private void setChip(String t, ChipGroup group) {
        final Chip chip = (Chip) this.getLayoutInflater().inflate(R.layout.single_input_chip_layout, null, false);
        chip.setText(t);

        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                group.removeView(chip);
            }
        });

        group.addView(chip);
    }
}