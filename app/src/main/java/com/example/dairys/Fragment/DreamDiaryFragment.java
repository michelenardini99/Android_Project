package com.example.dairys.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DreamDiaryFragment extends Fragment {

    ExtendedFloatingActionButton saveDream;
    private RecyclerView recyclerView;
    private FloatingActionButton newDream;
    private AppCompatButton filter;
    private AppCompatButton addTag;
    private EditText tagFilter;
    private ChipGroup sortGroup;
    private ChipGroup filterGroup;

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

        tagFilter = getView().findViewById(R.id.addTagFilter);
        addTag = getView().findViewById(R.id.addTagButton);
        sortGroup = getView().findViewById(R.id.sortDream);
        filterGroup = getView().findViewById(R.id.filterDream);
        filter = getView().findViewById(R.id.filterButton);
        saveDream = getView().findViewById(R.id.saveDream);
        recyclerView = getView().findViewById(R.id.dream_list);
        newDream = getView().findViewById(R.id.new_dream);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dreamDiaryList = db.dreamDiaryDao().getAll();

        setAdapterList(dreamDiaryList);

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
                if(SettingsFragment.readFontStyle(getContext()) != 0){
                    Typeface typeface = ResourcesCompat.getFont(getContext(), SettingsFragment.readFontStyle(getContext()));
                    title.setTypeface(typeface);
                    description.setTypeface(typeface);
                }
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

        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String t = tagFilter.getText().toString().toLowerCase();
                setChip(t, filterGroup);
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                switch (checkCheckedChip()){
                    case "Date":
                        List<DreamDiary> dreamDiaries = db.dreamDiaryDao().getAll();
                        dreamDiaries.forEach(d -> {
                            d.getDateInsert().replaceAll("/"," ");
                        });
                        Collections.sort(dreamDiaries, new Comparator<DreamDiary>() {

                            DateFormat f = new SimpleDateFormat("dd MMM yyyy");
                            @Override
                            public int compare(DreamDiary dreamDiary, DreamDiary t1) {
                                try {
                                    return f.parse(dreamDiary.getDateInsert()).compareTo(f.parse(t1.getDateInsert()));
                                } catch (ParseException e) {
                                    throw new IllegalArgumentException(e);
                                }
                            }
                        });
                        Collections.reverse(dreamDiaries);
                        filterList(dreamDiaries);
                        break;
                    case "Like":
                        filterList(db.dreamDiaryDao().orderByLike());
                        break;
                    default:
                        filterList(db.dreamDiaryDao().getAll());
                        break;
                }
            }
        });
    }

    private void setAdapterList(List<DreamDiary> dreamDiaries) {
        DreamDiaryAdapter dreamDiaryAdapter = new DreamDiaryAdapter(dreamDiaries, getContext());
        recyclerView.setAdapter(dreamDiaryAdapter);
    }

    private String checkCheckedChip() {
        List<Integer> ids = sortGroup.getCheckedChipIds();
        if(!ids.isEmpty()){
            Chip chip = sortGroup.findViewById(ids.get(0));
            return chip.getText().toString();
        }
        return "";
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void filterList(List<DreamDiary> dreamDiaryList){
        List<Integer> ids = filterGroup.getCheckedChipIds();
        List<DreamDiary> dreamDiariesNotSelected = new ArrayList<>();
        for (Integer id:ids){
            Chip chip = filterGroup.findViewById(id);
            switch (chip.getText().toString()){
                case "Favorites":
                    dreamDiaryList.forEach(d -> {
                        if(db.dreamFavoriteDao().getIfIsFavorite(d.getDreamId(), db.userDao().userLogged().get(0).getId()).isEmpty()){
                            dreamDiariesNotSelected.add(d);
                        }
                    });
                    break;
                default:
                    dreamDiaryList.forEach(d -> {
                        if(db.dreamTagDao().getDreamFilterByTag(d.getDreamId(), chip.getText().toString()).isEmpty()){
                            dreamDiariesNotSelected.add(d);
                        }
                    });
                    break;
            }
            dreamDiaryList.removeAll(dreamDiariesNotSelected);
            dreamDiariesNotSelected.clear();
        }
        setAdapterList(dreamDiaryList);
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
        chip.setText(t.toLowerCase());

        chip.isCheckable();
        chip.isFocusable();
        chip.isClickable();

        group.addView(chip);
    }
}