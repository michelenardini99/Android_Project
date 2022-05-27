package com.example.dairys.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dairys.Database.AppDatabase;
import com.example.dairys.Database.DreamDiary;
import com.example.dairys.Database.Food;
import com.example.dairys.DreamDiaryAdapter;
import com.example.dairys.R;

import java.util.ArrayList;
import java.util.List;

public class DreamDiaryFragment extends Fragment {

    private RecyclerView recyclerView;
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

        recyclerView = getView().findViewById(R.id.dream_list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dreamDiaryList = db.dreamDiaryDao().getAll();

        DreamDiaryAdapter dreamDiaryAdapter = new DreamDiaryAdapter(dreamDiaryList, getContext());
        recyclerView.setAdapter(dreamDiaryAdapter);
    }
}